package com.dawn.cs.study.lambda.sync.application;

import com.dawn.cs.study.lambda.sync.application.port.VectorStorePort;
import com.dawn.cs.study.storage.s3.port.ReadContentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class IngestMarkdownToVectorStoreUseCase {

    private final ReadContentPort readContentPort;

    private final VectorStorePort vectorStorePort;

    public void markdownEmbedding(String mdKey) {
        String md = readContentPort.readText(mdKey);

        var document = new Document(mdKey, md, Map.of("key", mdKey));

        var splitter = TokenTextSplitter.builder()
                .withChunkSize(512)           // 원하는 청크 크기
                .withMinChunkSizeChars(350)   // 최소 청크 크기
                .withMinChunkLengthToEmbed(5) // 임베딩할 최소 청크 길이
                .withMaxNumChunks(10000)      // 최대 청크 수
                .withKeepSeparator(true)      // 구분자 유지 여부
                .build();

        List<Document> chunks = splitter.apply(List.of(document));
        vectorStorePort.add(chunks);

        log.info("Saved markdown file [{}] with {} chunks into pgvector", mdKey, chunks.size());
    }


}
