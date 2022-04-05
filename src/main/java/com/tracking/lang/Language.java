package com.tracking.lang;

/**
 * Enumeration of supported languages
 */
public enum Language {
    EN("en"),
    UA("uk");

    private final String value;

    Language(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
