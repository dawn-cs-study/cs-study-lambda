package com.dawn.cs.study.lambda.md.application.port;

import com.dawn.cs.study.lambda.md.domain.Slug;

public interface SlugCommandPort {

    Slug save(Slug slug);

    void deleteByTitle(String title);

}
