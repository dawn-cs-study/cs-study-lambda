package com.dawn.cs_study.content.infrastructure.persistence;

import com.dawn.cs_study.content.domain.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SlugEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ElementCollection
    private List<String> tags;

    private String summary;

    private String author;

}