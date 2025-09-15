package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.SlugCommandPort;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@NoArgsConstructor
public class DeleteSlugFromJsonUseCase {

    // todo UseCase final 달기, NoArg -> Required 변경 필요
    // todo Transectional Repo 계층 -> UseCase 계층으로 변경

    private  SlugCommandPort slugCommandPort;

    public void deleteJsonResourceUseCase(String jsonKey) {

        slugCommandPort.deleteByTitle(jsonKey);
        log.info("JSON removed (deleted from repository): {} ", jsonKey);
    }

}
