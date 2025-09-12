package com.dawn.cs.study.storage.s3.port;

public interface WriteContentPort {

    void upload(String key, byte[] content, String contentType);

    void delete(String key);

}
