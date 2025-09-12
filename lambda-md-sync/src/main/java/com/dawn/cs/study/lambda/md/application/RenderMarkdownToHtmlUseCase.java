package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.domain.KeyNamingPolicy;
import com.dawn.cs.study.lambda.md.application.port.ReadContentPort;
import com.dawn.cs.study.lambda.md.application.port.ReadMarkdownPort;
import com.dawn.cs.study.lambda.md.application.port.WriteContentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class RenderMarkdownToHtmlUseCase {

    private final ReadContentPort readContentPort;
    private final ReadMarkdownPort readMarkdownPort;
    private final WriteContentPort writeContentPort;

    public void renderHtml(String bucket, String mdKey) {
        log.info("renderHtml start {}", mdKey);
        String md = readContentPort.readText(mdKey);

        log.info("renderHtml end {}", md);
        String html = readMarkdownPort.toHtml(md);

        String htmlKey = KeyNamingPolicy.toHtmlKey(mdKey);

        log.info("renderHtml key {}", htmlKey);
        writeContentPort.upload(
                bucket, htmlKey, html.getBytes(StandardCharsets.UTF_8), "text/html; charset=UTF-8");

        log.info("renderHtml end {}", htmlKey);
    }


}
