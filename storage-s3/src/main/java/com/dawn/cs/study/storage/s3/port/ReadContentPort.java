package com.dawn.cs.study.storage.s3.port;

public interface ReadContentPort {

    String readText(String key);

    <T> T readJson(String key, Class<T> type);

}
