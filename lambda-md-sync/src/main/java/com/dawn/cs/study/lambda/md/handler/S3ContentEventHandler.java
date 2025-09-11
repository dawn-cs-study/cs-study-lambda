package com.dawn.cs.study.lambda.md.handler;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.dawn.cs.study.lambda.md.application.DeleteJsonResourceUseCase;
import com.dawn.cs.study.lambda.md.application.DeleteMarkdownHtmlUseCase;
import com.dawn.cs.study.lambda.md.application.RenderMarkdownToHtmlUseCase;
import com.dawn.cs.study.lambda.md.application.UpsertSlugFromJsonUseCase;
import com.dawn.cs.study.lambda.md.domain.Slug;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3ContentEventHandler implements Function<S3Event, String> {

    private final DeleteJsonResourceUseCase deleteJsonResourceUseCase;
    private final DeleteMarkdownHtmlUseCase deleteMarkdownHtmlUseCase;
    private final RenderMarkdownToHtmlUseCase renderMarkdownToHtmlUseCase;
    private final UpsertSlugFromJsonUseCase upsertSlugFromJsonUseCase;

    @Override
    public String apply(S3Event event) {
        log.info("S3ContentEventHandler start");

        // 가상 스레드 풀 생성 (Java 21)
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // 각 이벤트를 CompletableFuture로 비동기 실행
            List<CompletableFuture<Void>> tasks = event.getRecords().stream()
                    .map(r -> {
                        String eventName = r.getEventName();
                        String bucket = r.getS3().getBucket().getName();
                        String key = URLDecoder.decode(r.getS3().getObject().getKey(), StandardCharsets.UTF_8);

                        Runnable task = switch (eventName.split(":")[0]) { // "ObjectCreated" or "ObjectRemoved"
                            case "ObjectRemoved" -> switch (getExtension(key)) {
                                case "md" -> () -> deleteMarkdownHtmlUseCase.deleteMarkdownHtml(bucket, key);
                                case "json" -> () -> deleteJsonResourceUseCase.deleteJsonResourceUseCase(bucket, key);
                                default ->
                                        throw new RuntimeException("잘못된 파일 확장자입니다. 확장자는 반드시 'md' 또는 'json' 이어야 합니다.");
                            };
                            case "ObjectCreated" -> switch (getExtension(key)) {
                                case "md" -> () -> renderMarkdownToHtmlUseCase.renderHtml(bucket, key);
                                case "json" -> () -> upsertSlugFromJsonUseCase.upsertSlugFromJson(key, Slug.class);
                                default ->
                                        throw new RuntimeException("잘못된 파일 확장자입니다. 확장자는 반드시 'md' 또는 'json' 이어야 합니다.");
                            };
                            default ->
                                    throw new RuntimeException("잘못된 S3 이벤트입니다. 이벤트는 반드시 ObjectRemoved 또는 ObjectCreated 이어야 합니다.");
                        };

                        return CompletableFuture.runAsync(task, executor);
                    })
                    .toList();

            // allOf -> 여러 Future를 병렬로 처리, 내부적으로는 배열 구간을 분할해 이분탐색후 재귀적으로 처리
            // 모든 task 를 기다리는 새로운 CompletableFuture<Void> 만들어 반환
            // join -> 새로운 CompletableFuture<Void> 끝날 때까지 블로킹
            CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0]))
                    .join();

        } catch (Exception e) {
            log.error("S3ContentEventHandler error", e);
            throw new RuntimeException(e);
        }

        return "OK";
    }

    private static String getExtension(String key) {
        int idx = key.lastIndexOf('.');
        return (idx != -1) ? key.substring(idx + 1).toLowerCase() : "";
    }
}