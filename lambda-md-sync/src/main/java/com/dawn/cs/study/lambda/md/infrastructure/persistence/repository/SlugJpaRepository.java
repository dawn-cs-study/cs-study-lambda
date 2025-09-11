package com.dawn.cs.study.lambda.md.infrastructure.persistence.repository;


import com.dawn.cs.study.lambda.md.infrastructure.persistence.SlugEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlugJpaRepository extends JpaRepository<SlugEntity, Long> {

    void deleteByTitle(String title);

}
