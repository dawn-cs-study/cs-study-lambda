package com.dawn.cs.study.lambda.md.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Slug {

    private final Long id;
    private final String title;

    private final Category category;

    private final String summary;
    private final String author;

    public static Slug of(Long id, String title, Category category, String summary, String author) {
        return new Slug(id, title, category, summary, author);
    }

}
