package com.dawn.cs_study.content.infrastructure.persistence.repository;

import com.dawn.cs_study.content.application.port.out.SlugRepository;
import com.dawn.cs_study.content.domain.Slug;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class SlugRepositoryImpl implements SlugRepository {

    private final SlugJpaRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Optional<Slug> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Slug save(Slug slug) {
        return repository.save(slug);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
