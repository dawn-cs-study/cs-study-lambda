package com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.repository;

import com.dawn.cs.study.lambda.md.application.port.SlugCommandPort;
import com.dawn.cs.study.lambda.md.domain.Slug;
import com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.SlugEntity;
import com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.mapper.SlugEntityDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class SlugJpaAdapter implements SlugCommandPort {

    private final SlugJpaRepository slugJpaRepository;

    private final SlugEntityDomainMapper slugEntityDomainMapper;

    @Override
    public Slug save(Slug slug) {
        SlugEntity slugEntity = slugEntityDomainMapper.toEntity(slug);
        return slugEntityDomainMapper.toDomain(slugJpaRepository.save(slugEntity));
    }

    @Override
    public void deleteByTitle(String title) {
        slugJpaRepository.deleteByTitle(title);
    }
}
