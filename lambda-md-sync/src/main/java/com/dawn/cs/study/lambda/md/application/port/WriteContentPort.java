package com.dawn.cs.study.lambda.md.application.port;

public interface WriteContentPort {

    void upload(String bucket, String key, byte[] content, String contentType);

    void delete(String bucket, String key);

}
