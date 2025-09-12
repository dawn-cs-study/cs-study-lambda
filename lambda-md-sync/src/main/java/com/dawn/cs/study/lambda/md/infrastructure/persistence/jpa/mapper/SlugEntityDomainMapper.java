package com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.mapper;

import com.dawn.cs.study.lambda.md.domain.Slug;
import com.dawn.cs.study.lambda.md.infrastructure.persistence.jpa.SlugEntity;
import org.springframework.stereotype.Component;

@Component
public class SlugEntityDomainMapper {

    public Slug toDomain(SlugEntity entity) {
        return Slug.of(entity.getId(), entity.getTitle(), entity.getCategory(), entity.getSummary(), entity.getAuthor());
    }

    public SlugEntity toEntity(Slug domain) {
        return new SlugEntity(domain.getId(), domain.getTitle(), domain.getCategory(), domain.getSummary(), domain.getAuthor());
    }


}
