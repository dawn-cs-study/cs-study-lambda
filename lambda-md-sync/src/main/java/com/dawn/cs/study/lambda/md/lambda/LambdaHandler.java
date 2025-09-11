package com.dawn.cs.study.lambda.md.lambda;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.dawn.cs.study.lambda.md.application.ReadContentService;
import com.dawn.cs.study.lambda.md.application.WriteContentService;
import com.dawn.cs.study.lambda.md.application.port.SlugPersistencePort;
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

    private final ReadContentService readContentService;

    private final WriteContentService writeContentService;

    private final SlugPersistencePort slugPersistencePort;

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
                    futures.add(exec.submit(() -> deleteMarkdownHtml(bucket, key, writeContentService)));
                } else if (key.endsWith(".json")) {
                    futures.add(exec.submit(() -> handleJsonDeleted(bucket, key, writeContentService, slugPersistencePort)));
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
                futures.add(exec.submit(() -> renderMarkdownToHtml(bucket, key, readContentService, writeContentService)));
            } else if (key.endsWith(".json")) {
                futures.add(exec.submit(() -> upsertSlugFromJson(bucket, key, readContentService, slugPersistencePort)));
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

    private static void renderMarkdownToHtml(String bucket,
                                             String key,
                                             ReadContentService readContentService,
                                             WriteContentService writeContentService) {
        String html = readContentService.renderHtml(key);
        String htmlKey = toHtmlKey(key);
        writeContentService.upload(bucket, htmlKey, html.getBytes(StandardCharsets.UTF_8), "text/html; charset=UTF-8");
        log.info("Rendering markdown to HTML: {} in {}", htmlKey, bucket);
    }

    private static void upsertSlugFromJson(String bucket,
                                           String key,
                                           ReadContentService readContentService,
                                           SlugPersistencePort slugPersistencePort) {
        Slug slug = readContentService.readJson(key, Slug.class);
        slugPersistencePort.save(slug);
        log.info("Upserting slug from JSON: {} in {}", key, bucket);
    }

    private static void deleteMarkdownHtml(String bucket,
                                           String mdKey,
                                           WriteContentService writeContentService) {
        String htmlKey = toHtmlKey(mdKey);
        writeContentService.delete(bucket, htmlKey);
        log.info("Deleted HTML for removed markdown: {} in {}", htmlKey, bucket);
    }

    private static void handleJsonDeleted(String bucket,
                                          String key,
                                          WriteContentService writeContentService,
                                          SlugPersistencePort slugPersistencePort) {
        writeContentService.delete(bucket, key);
        slugPersistencePort.deleteByTitle(key);
        log.info("JSON removed (deleted from repository): {} in {}", key, bucket);
    }

    private static String toHtmlKey(String mdKey) {
        int idx = mdKey.lastIndexOf('.');
        return (idx > 0 ? mdKey.substring(0, idx) : mdKey) + ".html";
    }


}