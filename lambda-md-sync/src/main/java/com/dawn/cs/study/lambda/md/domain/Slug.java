package com.dawn.cs.study.lambda.md.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Slug {

    private Long id;
    private String title;

    private Category category;


    private String summary;
    private String author;

    public static Slug of(String title, Category category, String summary, String author) {
        return new Slug(null, title, category, summary, author);
    }

    public static Slug of(Long id, String title, Category category, String summary, String author) {
        return new Slug(id, title, category, summary, author);
    }

}
