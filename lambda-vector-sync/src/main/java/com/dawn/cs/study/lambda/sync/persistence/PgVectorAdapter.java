package com.dawn.cs.study.lambda.sync.persistence;


import com.dawn.cs.study.lambda.sync.application.port.VectorStorePort;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class PgVectorAdapter implements VectorStorePort {

    private final PgVectorStore pgVectorStore;


    @Override
    public void add(List<Document> documents) {
        pgVectorStore.add(documents);
    }

    @Override
    public void delete(List<String> ids) {
        pgVectorStore.delete(ids);
    }

}
