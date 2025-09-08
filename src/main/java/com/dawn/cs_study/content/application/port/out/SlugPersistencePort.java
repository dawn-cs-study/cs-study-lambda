package com.dawn.cs_study.content.application.port.out;

import com.dawn.cs_study.content.domain.Slug;

public interface SlugPersistencePort {

    Slug findById(Long id);

    Slug save(Slug slug);

    void deleteById(Long id);

    void deleteByTitle(String title);


}
