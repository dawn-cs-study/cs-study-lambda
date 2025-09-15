package com.dawn.cs.study.lambda.md.handler;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification.S3EventNotificationRecord;
import com.dawn.cs.study.lambda.md.application.*;
import com.dawn.cs.study.lambda.md.domain.FailedS3Event;
import com.dawn.cs.study.lambda.md.domain.Slug;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3EventHandler implements Function<S3Event, String> {

    private final DeleteSlugFromJsonUseCase deleteSlugFromJsonUseCase;
    private final DeleteMarkdownAndVectorUseCase deleteMarkdownAndVectorUseCase;
    private final MarkdownToHtmlAndVectorUseCase markdownToHtmlAndVectorUseCase;
    private final UpsertSlugFromJsonUseCase upsertSlugFromJsonUseCase;
    private static final Class<Slug> SLUG_TYPE = Slug.class;
     private final FailedS3EventCompensationUseCase failedS3EventCompensationUseCase;

    @Override
    public String apply(S3Event event) {
        long start = System.currentTimeMillis();
        log.info("S3ContentEventHandler start at {}", start);

        // runAsync = Runnable 결과값 반환이 없어서 예외 발생시 전체 실패
        // supplyAsync = Supplier 결과값 반환 있어 예외 발생시 집계 후 보상 처리 가능

        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        List<CompletableFuture<Optional<FailedS3Event>>> futures = event.getRecords().stream()
                .map(r -> CompletableFuture
                        .supplyAsync(() -> {
                            buildTask(r).run();
                            return Optional.<FailedS3Event>empty();
                        }, executor)
                        .exceptionally(ex -> {
                            // 실패 경로: 로그 + 실패 키 반환
                            log.error("Task 실패 eventName= {}, eventKey= {}"
                                    , r.getEventName(), r.getS3().getObject().getKey(), ex);
                            return Optional.of(FailedS3Event.of(r.getEventName(), r.getS3().getObject().getKey()));
                        }))
                .toList();

        // 전체 완료를 알려주는 하나의 CompletableFuture<Void> 생성
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 이미 위에서 전체 완료됐기 때문에, 기다리지 않고 값 반환
        List<FailedS3Event> failedRecords = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Optional::stream)
                .toList();

        if (!failedRecords.isEmpty()) {
            log.error("실패 {}건", failedRecords.size());
             failedRecords.forEach(failedS3EventCompensationUseCase::insertFailedS3Event);
        }

        executor.close();
        long end = System.currentTimeMillis();
        log.info("S3ContentEventHandler end at {}, elapsed={} ms", end, (end - start));
        return "OK";
    }

    private Runnable buildTask(S3EventNotificationRecord r) {

        String eventName = r.getEventName();

        String key = r.getS3().getObject().getKey();

        return switch (eventName.split(":")[0]) { // "ObjectCreated" or "ObjectRemoved"
            case "ObjectRemoved" -> switch (getExtension(key)) {
                case "md" -> () -> deleteMarkdownAndVectorUseCase.deleteMarkdownArtifacts(key);
                case "json" -> () -> deleteSlugFromJsonUseCase.deleteJsonResourceUseCase(key);
                default ->
                        throw new RuntimeException(String.format("잘못된 파일 확장자입니다. 확장자는 반드시 'md' 또는 'json' 이어야 합니다. %s", key));
            };
            case "ObjectCreated" -> switch (getExtension(key)) {
                case "md" -> () -> markdownToHtmlAndVectorUseCase.markdownToHtmlAndVector(key);
                case "json" -> () -> upsertSlugFromJsonUseCase.upsertSlugFromJson(key, SLUG_TYPE);
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
