package com.dawn.cs.study.lambda.md.domain;


public class KeyNamingPolicy {

    public static String toHtmlKey(String mdKey) {
        int idx = mdKey.lastIndexOf('.');
        return (idx > 0 ? mdKey.substring(0, idx) : mdKey) + ".html";
    }
}
