package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.ReadContentPort;
import com.dawn.cs.study.lambda.md.application.port.ReadMarkdownPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReadContentUseCase {

    private final ReadContentPort readContentPort;
    private final ReadMarkdownPort readMarkdownPort;

    public String renderHtml(String documentName) {
        log.info("renderHtml start {}", documentName);
        String md = readContentPort.readText(documentName);
        return readMarkdownPort.toHtml(md);
    }

    public <T> T readJson(String key, Class<T> type) {
        log.info("readJson start {}", key);
        return readContentPort.readJson(key, type);
    }
}
