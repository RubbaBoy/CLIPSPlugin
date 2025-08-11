package com.example.simplelang.psi.impl;

import com.example.simplelang.psi.SimpleId;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom leaf implementation for SimpleId.
 * This class extends LeafPsiElement to ensure that SimpleId is a leaf node in the PSI tree.
 */
public class SimpleIdLeafImpl extends LeafPsiElement implements SimpleId {
    /**
     * Creates a new SimpleIdLeafImpl from an IElementType and text.
     *
     * @param type the element type
     * @param text the text
     */
    public SimpleIdLeafImpl(@NotNull IElementType type, @NotNull CharSequence text) {
        super(type, text);
    }
    
    /**
     * Creates a new SimpleIdLeafImpl from an ASTNode.
     * This constructor ensures the element is properly attached to the PSI tree.
     *
     * @param node the AST node
     */
    public SimpleIdLeafImpl(@NotNull ASTNode node) {
        super(node.getElementType(), node.getChars());
    }

    @Override
    @NotNull
    public PsiElement getIdentifier() {
        // Since this is a leaf element, it is its own identifier
        return this;
    }

    /**
     * Returns the name identifier of this element.
     * This is used by the SimpleEntityDeclarationMixin to get the name identifier.
     */
    @Nullable
    public PsiElement getNameIdentifier() {
        return this;
    }
    
    /**
     * Static utility method for getting the name identifier of a SimpleId.
     * This is used by the generated code to implement the getNameIdentifier method.
     */
    public static PsiElement getNameIdentifier(SimpleId element) {
        return element.getIdentifier();
    }
}