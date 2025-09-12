package com.dawn.cs.study.lambda.sync.application;

import com.dawn.cs.study.lambda.sync.application.port.VectorStorePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteMarkdownEmbeddingUseCase {

    private final VectorStorePort vectorStorePort;

    public void deleteMarkdownEmbedding(String mdKey) {
        try {
            vectorStorePort.delete(List.of(mdKey));
            log.info("Deleted markdown embedding for key={}", mdKey);
        } catch (Exception e) {
            log.error("Failed to delete embedding for key={}", mdKey, e);
            throw new RuntimeException(e);
        }
    }

}
