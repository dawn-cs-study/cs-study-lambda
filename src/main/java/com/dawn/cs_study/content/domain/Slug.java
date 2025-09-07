package com.dawn.cs_study.content.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Slug {

    private Long id;
    private String title;

    private Category category;

    private List<String> tags;

    private String summary;
    private String author;

    public static Slug of(String title, Category category, List<String> tags, String summary, String author) {
        return new Slug(null, title, category, tags, summary, author);
    }

    public static Slug of(Long id, String title, Category category, List<String> tags, String summary, String author) {
        return new Slug(id, title, category, tags, summary, author);
    }

}
