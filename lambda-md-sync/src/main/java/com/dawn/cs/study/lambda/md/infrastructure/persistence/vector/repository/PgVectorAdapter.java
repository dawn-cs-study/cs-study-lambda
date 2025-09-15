package com.dawn.cs.study.lambda.md.infrastructure.persistence.vector.repository;

import com.dawn.cs.study.lambda.md.application.port.VectorCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PgVectorAdapter implements VectorCommandPort {

    private final PgVectorStore pgVectorStore;

    @Override
    public void embedDocuments(List<Document> documents) {
        pgVectorStore.add(documents);
    }

    @Override
    public void delete(String id) {
        pgVectorStore.delete("key =='" + id + "'");
    }

}
