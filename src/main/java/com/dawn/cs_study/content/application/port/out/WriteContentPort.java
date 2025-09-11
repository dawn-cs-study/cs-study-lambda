package com.dawn.cs_study.content.application.port.out;

public interface WriteContentPort {

    void upload(String bucket, String key, byte[] content, String contentType);

    void delete(String bucket, String key);

}
