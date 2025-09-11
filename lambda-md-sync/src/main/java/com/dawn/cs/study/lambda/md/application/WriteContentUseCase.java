package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.WriteContentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WriteContentUseCase {

    private final WriteContentPort writeContentPort;

    public void upload(String bucket, String key, byte[] content, String contentType) {
        log.info("upload content {} to bucket {}", key, bucket);
        writeContentPort.upload(bucket, key, content, contentType);
    }

    public void delete(String bucket, String key) {
        log.info("delete content {} from bucket {}", key, bucket);
        writeContentPort.delete(bucket, key);
    }

}
