package com.dawn.cs.study.lambda.md.infrastructure.markdown;

import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MarkdownConfig {

    @Bean
    Parser markdownParser() {
        return Parser.builder()
                .extensions(List.of(
                        TablesExtension.create(),
                        StrikethroughExtension.create(),
                        TaskListExtension.create(),
                        AutolinkExtension.create()
                ))
                .build();
    }

    @Bean
    HtmlRenderer htmlRenderer() {
        // 보안: 원시 HTML 차단(필요 시 설정 토글)
        return HtmlRenderer.builder()
                .escapeHtml(true)      // Markdown 내 raw HTML 무효화(기본 보안)
                .softBreak("<br/>")    // 줄바꿈 렌더링 정책(선호에 따라)
                .build();
    }

}
