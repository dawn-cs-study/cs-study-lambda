package com.dawn.cs.study.storage.s3.adapter;

import com.dawn.cs.study.storage.s3.port.WriteContentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@RequiredArgsConstructor
public class S3WriteContentAdapter implements WriteContentPort {

    private final S3Client s3Client;

    @Override
    public void upload(String key, byte[] content, String contentType) {
        PutObjectRequest request = PutObjectRequest.builder()
                .key(key)
                .contentType(contentType)
                .build();

        s3Client.putObject(request, RequestBody.fromBytes(content));
    }

    @Override
    public void delete(String key) {
        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .key(key)
                    .build();
            s3Client.deleteObject(request);
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                // 이미 없는 객체라면 무시 → 멱등성 보장
                return;
            }
            throw e;
        }
    }


}
