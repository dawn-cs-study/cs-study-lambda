package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.VectorCommandPort;
import com.dawn.cs.study.lambda.md.application.port.WriteContentPort;
import com.dawn.cs.study.lambda.md.domain.support.KeyNamingPolicy;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@NoArgsConstructor
public class DeleteMarkdownAndVectorUseCase {

    private  WriteContentPort writeContentPort;

    private VectorCommandPort vectorCommandPort;

    public void deleteMarkdownArtifacts(String mdKey) {
        deleteMarkdownEmbedding(mdKey);
        deleteMarkdown(mdKey);
    }

    private void deleteMarkdown(String mdKey) {
        String htmlKey = KeyNamingPolicy.toHtmlKey(mdKey);
        writeContentPort.delete(htmlKey);
        log.info("Deleted HTML for removed markdown: {}", htmlKey);
    }

    private void deleteMarkdownEmbedding(String mdKey) {
        try {
            vectorCommandPort.delete(mdKey);
            log.info("Deleted markdown embedding for key={}", mdKey);
        } catch (Exception e) {
            log.error("Failed to delete embedding for key={}", mdKey, e);
            throw new RuntimeException(e);
        }
    }


}
