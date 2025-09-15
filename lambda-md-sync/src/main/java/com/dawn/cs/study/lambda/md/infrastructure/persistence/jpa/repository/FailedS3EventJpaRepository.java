package com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.repository;

import com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.FailedS3EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FailedS3EventJpaRepository extends JpaRepository<FailedS3EventEntity, Long> {
}
