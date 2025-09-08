package com.dawn.cs_study.content.application;

import com.dawn.cs_study.content.application.port.out.ReadContentPort;
import com.dawn.cs_study.content.application.port.out.ReadMarkdownPort;
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
