package com.dawn.cs.study.lambda.md.handler;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.dawn.cs.study.lambda.md.application.*;
import com.dawn.cs.study.lambda.md.domain.Slug;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
        ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor();
        List<Future<?>> futures = new ArrayList<>();

        log.info("S3ContentEventHandler start");

        for (S3EventNotification.S3EventNotificationRecord r : event.getRecords()) {
            String eventName = r.getEventName();
            String bucket = r.getS3().getBucket().getName();
            String key = URLDecoder.decode(r.getS3().getObject().getKey(), StandardCharsets.UTF_8);

            Runnable task = switch (eventName.split(":")[0]) { // "ObjectCreated" or "ObjectRemoved"
                case "ObjectRemoved" -> switch (getExtension(key)) {
                    case "md" -> () -> deleteMarkdownHtmlUseCase.deleteMarkdownHtml(bucket, key);
                    case "json" -> () -> deleteJsonResourceUseCase.deleteJsonResourceUseCase(bucket, key);
                    default -> throw new RuntimeException("잘못된 파일 확장자입니다. 확장자는 반드시 'md' 또는 'json' 이어야 합니다.");
                };

                case "ObjectCreated" -> switch (getExtension(key)) {
                    case "md" -> () -> renderMarkdownToHtmlUseCase.renderHtml(bucket, key);
                    case "json" -> () -> upsertSlugFromJsonUseCase.upsertSlugFromJson(key, Slug.class);
                    default -> throw new RuntimeException("잘못된 파일 확장자입니다. 확장자는 반드시 'md' 또는 'json' 이어야 합니다.");
                };

                default ->
                        throw new RuntimeException("잘못된 S3 이벤트입니다. 이벤트는 반드시 ObjectRemoved 또는 ObjectCreated 이어야 합니다.");
            };

            futures.add(exec.submit(task));
        }

        try {
            for (Future<?> f : futures) f.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } finally {
            exec.close();
        }

        return "OK";
    }

    private static String getExtension(String key) {
        int idx = key.lastIndexOf('.');
        return (idx != -1) ? key.substring(idx + 1).toLowerCase() : "";
    }

}