package com.dawn.cs_study.content.infrastructure.persistence.repository;

import com.dawn.cs_study.content.infrastructure.persistence.SlugEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlugJpaRepository extends JpaRepository<SlugEntity, Long> {

    void deleteByTitle(String title);

}
