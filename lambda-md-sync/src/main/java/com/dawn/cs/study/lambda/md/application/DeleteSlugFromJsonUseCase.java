package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.SlugCommandPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class DeleteSlugFromJsonUseCase {

    private  final SlugCommandPort slugCommandPort;

    public void deleteJsonResourceUseCase(String jsonKey) {

        slugCommandPort.deleteByTitle(jsonKey);
        log.info("JSON removed (deleted from repository): {} ", jsonKey);
    }

}
