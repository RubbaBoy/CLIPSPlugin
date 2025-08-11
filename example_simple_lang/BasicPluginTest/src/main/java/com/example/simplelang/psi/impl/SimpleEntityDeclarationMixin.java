package com.example.simplelang.psi.impl;

import com.example.simplelang.psi.SimpleEntityDeclaration;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom implementation of SimpleEntityDeclaration that provides the getNameIdentifier method.
 * This class is used as a mixin in the .bnf file to add functionality to the generated SimpleEntityDeclarationImpl class.
 */
public abstract class SimpleEntityDeclarationMixin extends SimpleNamedElementImpl implements SimpleEntityDeclaration {
    public SimpleEntityDeclarationMixin(@NotNull ASTNode node) {
        super(node);
    }

    /**
     * Returns the element that represents the name identifier of this entity declaration.
     * This is the identifier after the 'entity' keyword.
     */
    @Nullable
    @Override
    public PsiElement getNameIdentifier() {
        var id = getId();
        return id != null ? id.getIdentifier() : null;
    }
}