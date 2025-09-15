package com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.repository;

import com.dawn.cs.study.lambda.md.application.port.FailedS3EventCommandPort;
import com.dawn.cs.study.lambda.md.domain.FailedS3Event;
import com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.FailedS3EventEntity;
import com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.mapper.FailedS3EventEntityDomainMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FailedS3EventJpaAdapter implements FailedS3EventCommandPort {

    private final FailedS3EventEntityDomainMapper mapper;

    private final FailedS3EventJpaRepository repository;

    @Override
    public FailedS3Event save(FailedS3Event failedS3Event) {
        FailedS3EventEntity entity = repository.save(mapper.toEntity(failedS3Event));
        return mapper.toDomain(entity);
    }
}
