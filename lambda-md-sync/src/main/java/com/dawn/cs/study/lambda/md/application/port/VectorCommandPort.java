package com.dawn.cs.study.lambda.md.application.port;

import org.springframework.ai.document.Document;

import java.util.List;

public interface VectorCommandPort {

    public void embedDocuments(List<Document>documents);

    public void delete(String id);

}
