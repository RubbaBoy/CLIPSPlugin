package com.example.simplelang;

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

/**
 * Adapter for the SimpleLexer that provides the interface expected by the IntelliJ Platform.
 */
public class SimpleLexerAdapter extends FlexAdapter {
    public SimpleLexerAdapter() {
        super(new SimpleLexer((Reader) null));
    }
}