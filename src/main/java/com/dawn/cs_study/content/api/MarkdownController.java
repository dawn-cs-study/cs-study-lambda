package com.dawn.cs_study.content.api;

import com.dawn.cs_study.content.application.port.out.SlugPersistencePort;
import com.dawn.cs_study.content.domain.Category;
import com.dawn.cs_study.content.domain.Slug;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MarkdownController {

    private final SlugPersistencePort slugPersistencePort;

    @GetMapping("/{title}")
    public void dd(@PathVariable(name = "title") String title) throws Exception {

        // 1-1. CDN 체크 후 없으면 html 렌더
        // 1-2. CDN 체크 있으면 바로 제공
        // 2. 결과물 CDN 으로 업로드 후 제공

        // 현재는 S3에서 가져오는 중
    }


    @GetMapping("dd")
    public void slug() {

        slugPersistencePort.save(Slug.of("title", Category.ALGORITHM, List.of("tea", "223"), "summary", "me"));

    }

}

