package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.ReadContentPort;
import com.dawn.cs.study.lambda.md.application.port.ReadMarkdownPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReadContentService {

    private final ReadContentPort readContentPort;
    private final ReadMarkdownPort readMarkdownPort;

    public String renderHtml(String documentName) {
        String md = readContentPort.readText(documentName);
        return readMarkdownPort.toHtml(md);
    }

    public <T> T readJson(String key, Class<T> type) {
        return readContentPort.readJson(key, type);
    }
}
