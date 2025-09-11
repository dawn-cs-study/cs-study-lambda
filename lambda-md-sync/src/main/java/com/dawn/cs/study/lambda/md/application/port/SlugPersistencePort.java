package com.dawn.cs.study.lambda.md.application.port;


import com.dawn.cs.study.lambda.md.domain.Slug;

public interface SlugPersistencePort {

    Slug findById(Long id);

    Slug save(Slug slug);

    void deleteById(Long id);

    void deleteByTitle(String title);


}
