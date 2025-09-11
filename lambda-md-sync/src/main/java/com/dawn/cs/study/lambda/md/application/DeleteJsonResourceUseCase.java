package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.SlugCommandPort;
import com.dawn.cs.study.lambda.md.application.port.WriteContentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteJsonResourceUseCase {

    private final SlugCommandPort slugCommandPort;

    private final WriteContentPort writeContentPort;

    public void deleteJsonResourceUseCase(String bucket,
                                          String jsonKey) {

        writeContentPort.delete(bucket, jsonKey);
        log.info("delete bucket:{}, key:{}", bucket, jsonKey);

        slugCommandPort.deleteByTitle(jsonKey);
        log.info("JSON removed (deleted from repository): {} ", jsonKey);
    }

}
