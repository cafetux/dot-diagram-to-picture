package com.dot.renderder.chart;

public enum LinkType {
    SIMPLE("--"),
    ARROW("->");

    private final String text;

    LinkType(String text) {
        this.text = text;
    }

    public static LinkType from(String text) {
        for (LinkType value : values()) {
            if(value.getText().equals(text.trim())){
                return value;
            }
        }
        throw new RuntimeException(text+" is not valid link");
    }

    public String getText() {
        return text;
    }
}
