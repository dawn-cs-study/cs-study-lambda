package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.WriteContentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriteContentService {

    private final WriteContentPort writeContentPort;

    public void upload(String bucket, String key, byte[] content, String contentType) {
        writeContentPort.upload(bucket, key, content, contentType);
    }

    public void delete(String bucket, String key) {
        writeContentPort.delete(bucket, key);
    }

}
