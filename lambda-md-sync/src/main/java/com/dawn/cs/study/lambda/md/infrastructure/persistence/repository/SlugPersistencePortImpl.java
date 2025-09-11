package com.dawn.cs.study.lambda.md.infrastructure.persistence.repository;

import com.dawn.cs.study.lambda.md.application.port.SlugPersistencePort;
import com.dawn.cs.study.lambda.md.domain.Slug;
import com.dawn.cs.study.lambda.md.infrastructure.persistence.SlugEntity;
import com.dawn.cs.study.lambda.md.infrastructure.persistence.mapper.SlugEntityDomainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class SlugPersistencePortImpl implements SlugPersistencePort {

    private final SlugJpaRepository repository;

    private final SlugEntityDomainMapper mapper;



    @Override
    @Transactional(readOnly = true)
    public Slug findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDomain)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public Slug save(Slug slug) {
        SlugEntity slugEntity = mapper.toEntity(slug);
        return mapper.toDomain(repository.save(slugEntity));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteByTitle(String title) {
        repository.deleteByTitle(title);
    }
}
