package com.dawn.cs.study.lambda.md.application.port;

public interface WriteContentPort {

    void upload(String key, byte[] content, String contentType);

    void delete(String key);

}
