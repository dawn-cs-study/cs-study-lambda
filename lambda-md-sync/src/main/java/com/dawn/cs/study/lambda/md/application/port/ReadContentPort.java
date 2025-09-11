package com.dawn.cs.study.lambda.md.application.port;

public interface ReadContentPort {

    String readText(String key);

    <T> T readJson(String key, Class<T> type);

}
