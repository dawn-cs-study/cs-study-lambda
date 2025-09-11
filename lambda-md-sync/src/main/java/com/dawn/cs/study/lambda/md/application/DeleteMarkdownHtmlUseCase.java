package com.dawn.cs.study.lambda.md.application;

import com.dawn.cs.study.lambda.md.Utils;
import com.dawn.cs.study.lambda.md.application.port.WriteContentPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteMarkdownHtmlUseCase {

    private final WriteContentPort writeContentPort;

    public void deleteMarkdownHtml(String bucket,
                                   String mdKey) {
        writeContentPort.delete(bucket, mdKey);
        String htmlKey = Utils.toHtmlKey(mdKey);
        writeContentPort.delete(bucket, htmlKey);
        log.info("Deleted HTML for removed markdown: {} in {}", htmlKey, bucket);
    }


}
