package com.example.simplelang.psi.impl;

import com.example.simplelang.psi.SimpleNamedElement;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base implementation for Simple language named elements.
 * This class provides common functionality for elements that can be referenced.
 */
public abstract class SimpleNamedElementImpl extends ASTWrapperPsiElement implements SimpleNamedElement {
    public SimpleNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        var nameIdentifier = getNameIdentifier();
        return nameIdentifier != null ? nameIdentifier.getText() : null;
    }

    @Override
    public PsiElement setName(@NotNull String name) throws IncorrectOperationException {
        var nameIdentifier = getNameIdentifier();
        if (nameIdentifier != null) {
            // This is a simplified implementation. In a real plugin, you would
            // create a new identifier from the given name and replace the old one.
            throw new UnsupportedOperationException("Renaming is not supported yet");
        }
        return this;
    }

    /**
     * Returns the element that represents the name identifier of this named element.
     * For entity declarations, this would be the identifier after the 'entity' keyword.
     */
    @Nullable
    @Override
    public abstract PsiElement getNameIdentifier();
}