package com.dawn.cs_study.content.infrastructure.persistence.mapper;

import com.dawn.cs_study.content.domain.Slug;
import com.dawn.cs_study.content.infrastructure.persistence.SlugEntity;
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
