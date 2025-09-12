package com.dawn.cs.study.storage.s3.adapter;

import com.dawn.cs.study.storage.s3.port.ReadContentPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class S3ReadContentAdapter implements ReadContentPort {

    private final S3Client s3Client;

    private final ObjectMapper objectMapper;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public String readText(String key) {
        return readAsString(key);
    }

    @Override
    public <T> T readJson(String key, Class<T> type) {
        String json = readAsString(key);
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new RuntimeException("JSON 파싱 실패: " + key, e);
        }
    }

    private String readAsString(String key) {
        try (ResponseInputStream<GetObjectResponse> object = s3Client.getObject(r -> r.bucket(bucket).key(key));
             BufferedReader reader = new BufferedReader(new InputStreamReader(object))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}