package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.SlugCommandPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteJsonResourceUseCase {

    private final SlugCommandPort slugCommandPort;

    public void deleteJsonResourceUseCase(String jsonKey) {

        slugCommandPort.deleteByTitle(jsonKey);
        log.info("JSON removed (deleted from repository): {} ", jsonKey);
    }

}
