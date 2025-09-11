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
public class LambdaHandler implements Function<S3Event, String> {

    private final DeleteJsonResourceUseCase deleteJsonResourceUseCase;
    private final DeleteMarkdownHtmlUseCase deleteMarkdownHtmlUseCase;
    private final RenderMarkdownToHtmlUseCase renderMarkdownToHtmlUseCase;
    private final UpsertSlugFromJsonUseCase upsertSlugFromJsonUseCase;

    @Override
    public String apply(S3Event event) {
        ExecutorService exec = Executors.newVirtualThreadPerTaskExecutor();
        List<Future<?>> futures = new ArrayList<>();

        log.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        for (S3EventNotification.S3EventNotificationRecord r : event.getRecords()) {
            final String eventName = r.getEventName(); // e.g., ObjectCreated:Put
            final String bucket = r.getS3().getBucket().getName();
            final String key = URLDecoder.decode(r.getS3().getObject().getKey(), StandardCharsets.UTF_8);

            if (eventName == null) {
                log.warn("skip: eventName is null for key: {}", key);
                continue;
            }

            if (eventName.startsWith("ObjectRemoved:")) {
                if (key.endsWith(".md")) {
                    futures.add(exec.submit(() -> deleteMarkdownHtmlUseCase.deleteMarkdownHtml(bucket, key)));
                } else if (key.endsWith(".json")) {
                    futures.add(exec.submit(() -> deleteJsonResourceUseCase.deleteJsonResourceUseCase(bucket, key)));
                } else {
                    log.info("skip: ObjectRemoved for unsupported key: {}", key);
                }
                continue;
            }

            if (!eventName.startsWith("ObjectCreated:")) {
                log.info("skip: unsupported eventName: {} for key: {}", eventName, key);
                continue;
            }

            if (key.endsWith(".md")) {
                futures.add(exec.submit(() -> renderMarkdownToHtmlUseCase.renderHtml(bucket, key)));
            } else if (key.endsWith(".json")) {
                futures.add(exec.submit(() -> upsertSlugFromJsonUseCase.upsertSlugFromJson(key, Slug.class)));
            } else {
                log.info("skip: ObjectCreated for unsupported key: {}", key);
            }
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


}