package com.dawn.cs.study.lambda.md.application.port;

import org.springframework.ai.document.Document;

import java.util.List;

public interface VectorStorePort {

    public void add(List<Document> documents);

    public void delete(String id);

}
