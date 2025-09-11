package com.dawn.cs.study.lambda.md.infrastructure.markdown;


import com.dawn.cs.study.lambda.md.application.port.ReadMarkdownPort;
import com.dawn.cs.study.lambda.md.domain.HtmlPage;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReadMarkdownAdapter implements ReadMarkdownPort {

    private final Parser parser;

    private final HtmlRenderer renderer;


    @Override
    public String toHtml(String md) {
        return HtmlPage.of(renderer.render(parser.parse(md))).render();
    }

}
