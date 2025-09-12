package com.dawn.cs.study.lambda.sync;

import com.dawn.cs.study.storage.s3.PropertyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({
        PropertyConfig.class
})
public class VectorSyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(VectorSyncApplication.class, args);
    }

}
