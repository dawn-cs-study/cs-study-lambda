package com.dawn.cs.study.lambda.md.handler;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import com.dawn.cs.study.lambda.md.application.DeleteSlugFromJsonUseCase;
import com.dawn.cs.study.lambda.md.application.DeleteMarkdownAndVectorUseCase;
import com.dawn.cs.study.lambda.md.application.MarkdownToHtmlAndVectorUseCase;
import com.dawn.cs.study.lambda.md.application.UpsertSlugFromJsonUseCase;
import com.dawn.cs.study.lambda.md.domain.Slug;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3EventHandler implements Function<S3Event, String> {

    private final DeleteSlugFromJsonUseCase deleteSlugFromJsonUseCase;
    private final DeleteMarkdownAndVectorUseCase deleteMarkdownAndVectorUseCase;
    private final MarkdownToHtmlAndVectorUseCase markdownToHtmlAndVectorUseCase;
    private final UpsertSlugFromJsonUseCase upsertSlugFromJsonUseCase;

    @Override
    public String apply(S3Event event) {

        long start = System.currentTimeMillis();
        log.info("S3ContentEventHandler start at {}", start);

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // 각 이벤트를 CompletableFuture로 비동기 실행
            // allOf -> 모든 task 를 기다리는 새로운 CompletableFuture<Void> 만들어 반환
            // join -> 반환된 CompletableFuture<Void> 끝날 때까지 블로킹 , 즉 모든 task 실행이 끝날 때까지 blocking
            // runAsync 에서 바로 task 실행
            CompletableFuture.allOf(
                    event.getRecords().stream()
                            .map(r -> CompletableFuture.runAsync(buildTask(r), executor))
                            .toArray(CompletableFuture[]::new)
            ).join();

        } catch (Exception e) {
            log.error("S3ContentEventHandler error", e);
            throw new RuntimeException(e);
        }

        long end = System.currentTimeMillis();
        log.info("S3ContentEventHandler end at {}, elapsed={} ms", end, (end - start));


        return "OK";
    }

    private Runnable buildTask(S3EventNotificationRecord r) {

        String eventName = r.getEventName();
        String key = URLDecoder.decode(r.getS3().getObject().getKey(), StandardCharsets.UTF_8);

        return switch (eventName.split(":")[0]) { // "ObjectCreated" or "ObjectRemoved"
            case "ObjectRemoved" -> switch (getExtension(key)) {
                case "md" -> () -> deleteMarkdownAndVectorUseCase.deleteMarkdownArtifacts(key);
                case "json" -> () -> deleteSlugFromJsonUseCase.deleteJsonResourceUseCase(key);
                default ->
                        throw new RuntimeException(String.format("잘못된 파일 확장자입니다. 확장자는 반드시 'md' 또는 'json' 이어야 합니다. %s", key));
            };
            case "ObjectCreated" -> switch (getExtension(key)) {
                case "md" -> () -> markdownToHtmlAndVectorUseCase.markdownToHtmlAndVector(key);
                case "json" -> () -> upsertSlugFromJsonUseCase.upsertSlugFromJson(key, Slug.class);
                default ->
                        throw new RuntimeException(String.format("잘못된 파일 확장자입니다. 확장자는 반드시 'md' 또는 'json' 이어야 합니다. %s", key));
            };
            default -> throw new RuntimeException("잘못된 S3 이벤트입니다. 이벤트는 반드시 ObjectRemoved 또는 ObjectCreated 이어야 합니다.");
        };
    }

    private String getExtension(String key) {
        int idx = key.lastIndexOf('.');
        return (idx != -1) ? key.substring(idx + 1).toLowerCase() : "";
    }
}