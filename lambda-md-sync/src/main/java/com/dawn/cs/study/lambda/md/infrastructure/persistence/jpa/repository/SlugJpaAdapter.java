package com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.repository;

import com.dawn.cs.study.lambda.md.application.port.SlugCommandPort;
import com.dawn.cs.study.lambda.md.domain.Slug;
import com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.SlugEntity;
import com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.mapper.SlugEntityDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class SlugJpaAdapter implements SlugCommandPort {

    private final SlugJpaRepository repository;

    private final SlugEntityDomainMapper mapper;

    @Override
    public Slug save(Slug slug) {
        SlugEntity slugEntity = mapper.toEntity(slug);
        return mapper.toDomain(repository.save(slugEntity));
    }

    @Override
    public void deleteByTitle(String title) {
        repository.deleteByTitle(title);
    }
}
