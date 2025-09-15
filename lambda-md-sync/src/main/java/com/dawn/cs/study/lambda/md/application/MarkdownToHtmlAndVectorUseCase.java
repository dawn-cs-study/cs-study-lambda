package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.ReadContentPort;
import com.dawn.cs.study.lambda.md.application.port.ReadMarkdownPort;
import com.dawn.cs.study.lambda.md.application.port.VectorCommandPort;
import com.dawn.cs.study.lambda.md.application.port.WriteContentPort;
import com.dawn.cs.study.lambda.md.domain.support.KeyNamingPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class MarkdownToHtmlAndVectorUseCase {

    private final ReadContentPort readContentPort;
    private final ReadMarkdownPort readMarkdownPort;
    private final WriteContentPort writeContentPort;
    private final VectorCommandPort vectorCommandPort;

    public void markdownToHtmlAndVector(String mdKey) {
        log.info("renderHtml start {}", mdKey);
        String markdown = readContentPort.readText(mdKey);

        embedMarkdown(markdown, mdKey);
        renderMarkdownToHtmlAndStore(markdown, KeyNamingPolicy.toHtmlKey(mdKey));
    }

    private void embedMarkdown(String md, String mdKey) {
        log.info("Saved markdown file [{}] into pgvector", mdKey);

        var document = new Document(md, Map.of("key", mdKey));

        var splitter = TokenTextSplitter.builder()
                .withChunkSize(512)           // 원하는 청크 크기
                .withMinChunkSizeChars(350)   // 최소 청크 크기
                .withMinChunkLengthToEmbed(5) // 임베딩할 최소 청크 길이
                .withMaxNumChunks(10000)      // 최대 청크 수
                .withKeepSeparator(true)      // 구분자 유지 여부
                .build();

        List<Document> chunks = splitter.apply(List.of(document));
        vectorCommandPort.embedDocuments(chunks);
    }

    private void renderMarkdownToHtmlAndStore(String markdown, String htmlKey) {

        log.info("renderMarkdownToHtmlAndStore start {}", htmlKey);
        String html = readMarkdownPort.toHtml(markdown);

        writeContentPort.upload(
                htmlKey,
                html.getBytes(StandardCharsets.UTF_8),
                "text/html; charset=UTF-8"
        );
    }


}
