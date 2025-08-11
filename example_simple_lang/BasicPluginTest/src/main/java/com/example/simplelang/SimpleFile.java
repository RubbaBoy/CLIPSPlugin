package com.example.simplelang;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a Simple language file.
 * This is the root of the PSI tree for Simple language files.
 */
public class SimpleFile extends PsiFileBase {
    public SimpleFile(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, SimpleLanguage.INSTANCE);
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return SimpleFileType.INSTANCE;
    }

    @Override
    public String toString() {
        return "Simple File";
    }
}