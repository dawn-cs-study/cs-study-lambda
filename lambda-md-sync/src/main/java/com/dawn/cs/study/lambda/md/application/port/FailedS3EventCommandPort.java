package com.dawn.cs.study.lambda.md.application.port;

import com.dawn.cs.study.lambda.md.domain.FailedS3Event;

public interface FailedS3EventCommandPort {

    FailedS3Event save(FailedS3Event failedS3Event);

}
