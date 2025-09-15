package com.dawn.cs.study.lambda.md.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FailedS3Event {

    private final Long id;
    private final String eventName;
    private final String key;

    public static FailedS3Event of(String eventName, String key) {
        return new FailedS3Event(null, eventName, key);
    }

    public static FailedS3Event of(Long id, String eventName, String key) {
        return new FailedS3Event(id, eventName, key);
    }

}