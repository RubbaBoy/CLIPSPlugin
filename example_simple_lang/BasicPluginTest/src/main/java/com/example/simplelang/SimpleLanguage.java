package com.example.simplelang;

import com.intellij.lang.Language;

/**
 * The main class for the Simple language.
 */
public class SimpleLanguage extends Language {
    public static final SimpleLanguage INSTANCE = new SimpleLanguage();

    private SimpleLanguage() {
        super("Simple");
    }
}