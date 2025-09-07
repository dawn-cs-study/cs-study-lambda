package com.dawn.cs_study.content.infrastructure.persistence.repository;

import com.dawn.cs_study.content.domain.Slug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlugJpaRepository extends JpaRepository<Slug, Long> {
}
