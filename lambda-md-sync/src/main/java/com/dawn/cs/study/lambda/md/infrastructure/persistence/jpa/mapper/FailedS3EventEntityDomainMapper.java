package com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.mapper;

import com.dawn.cs.study.lambda.md.domain.FailedS3Event;
import com.dawn.cs.study.lambda.md.domain.Slug;
import com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.FailedS3EventEntity;
import com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.SlugEntity;
import org.springframework.stereotype.Component;

@Component
public class FailedS3EventEntityDomainMapper {

    public FailedS3Event toDomain(FailedS3EventEntity entity) {
        return FailedS3Event.of(entity.getId(), entity.getEventName(), entity.getKey());
    }

    public FailedS3EventEntity toEntity(FailedS3Event domain) {
        return new FailedS3EventEntity(domain.getId(), domain.getEventName(), domain.getKey());
    }

}
