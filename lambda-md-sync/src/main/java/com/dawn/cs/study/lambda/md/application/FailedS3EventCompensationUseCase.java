package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.application.port.FailedS3EventCommandPort;
import com.dawn.cs.study.lambda.md.domain.FailedS3Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@Transactional
@RequiredArgsConstructor
public class FailedS3EventCompensationUseCase {

    private final FailedS3EventCommandPort failedS3EventCommandPort;

    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 5000, multiplier = 2)
    )
    public void insertFailedS3Event(FailedS3Event failedS3Event) {
        log.info("event name : {}, key = {}", failedS3Event.getEventName(), failedS3Event.getKey());
        failedS3EventCommandPort.save(failedS3Event);
    }

    @Recover
    public void recoverFailedS3Event(FailedS3Event failedS3Event) {
        log.error("Compensation record persist failed after retries. eventName={}, key={}",
                failedS3Event.getEventName(), failedS3Event.getKey());
    }

}
