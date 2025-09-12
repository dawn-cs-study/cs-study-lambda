package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.ReadContentPort;
import com.dawn.cs.study.lambda.md.application.port.SlugCommandPort;
import com.dawn.cs.study.lambda.md.domain.Slug;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpsertSlugFromJsonUseCase {

    private final ReadContentPort readContentPort;

    private final SlugCommandPort slugCommandPort;


    public void upsertSlugFromJson(String jsonKey, Class<Slug> type) {
        log.info("readJson start {}", jsonKey);
        Slug slug = readContentPort.readJson(jsonKey, type);
        log.info("readJson end {}", jsonKey);
        slugCommandPort.save(slug);
        log.info(slug.toString());
    }

}
