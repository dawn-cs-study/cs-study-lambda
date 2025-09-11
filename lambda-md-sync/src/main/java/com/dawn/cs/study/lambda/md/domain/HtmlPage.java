package com.dawn.cs.study.lambda.md.domain;

public final class HtmlPage {
    private static final String DEFAULT_LANG = "ko";
    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String DEFAULT_VIEWPORT = "width=device-width, initial-scale=1";
    private static final String CSS_MARKDOWN = "/styles/article-style.css";
    private static final String CSS_PRISM = "/styles/prism.css";
    private static final String JS_PRISM = "/scripts/prism.js";

    private final String body;

    private HtmlPage(String body) {
        this.body = java.util.Objects.requireNonNullElse(body, "");
    }

    public static HtmlPage of(String body) {
        return new HtmlPage(body);
    }

    public String render() {
        return """
                <!DOCTYPE html>
                <html lang="%s">
                <head>
                  <meta charset="%s">
                  <meta name="viewport" content="%s">
                  <link rel="stylesheet" href="%s">
                  <link rel="stylesheet" href="%s">
                  <script defer src="%s"></script>
                </head>
                <body>
                  <article class="markdown-body">
                  %s
                  </article>
                </body>
                </html>
                """.formatted(DEFAULT_LANG, DEFAULT_CHARSET, DEFAULT_VIEWPORT,
                CSS_MARKDOWN, CSS_PRISM, JS_PRISM, body);
    }
}