package com.example.simplelang.psi;

import com.example.simplelang.SimpleLanguage;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Custom element type for Simple language PSI elements.
 */
public class SimpleElementType extends IElementType {
    public SimpleElementType(@NotNull @NonNls String debugName) {
        super(debugName, SimpleLanguage.INSTANCE);
    }
}