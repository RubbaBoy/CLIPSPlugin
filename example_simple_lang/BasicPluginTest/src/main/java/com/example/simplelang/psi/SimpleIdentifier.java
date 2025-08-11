package com.example.simplelang.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.ContributedReferenceHost;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

/**
 * Custom leaf element for identifiers in the Simple language.
 * This class provides a more accessible and easier to work with representation of identifiers.
 * Implements ContributedReferenceHost to allow reference contributors to attach references to this element.
 */
public class SimpleIdentifier extends LeafPsiElement implements PsiNamedElement, ContributedReferenceHost {
    /**
     * Creates a new SimpleIdentifier from an IElementType and text.
     *
     * @param type the element type
     * @param text the text
     */
    public SimpleIdentifier(@NotNull IElementType type, @NotNull CharSequence text) {
        super(type, text);
    }
    
    /**
     * Creates a new SimpleIdentifier from an ASTNode.
     * This constructor ensures the element is properly attached to the PSI tree.
     *
     * @param node the AST node
     */
    public SimpleIdentifier(@NotNull ASTNode node) {
        super(node.getElementType(), node.getChars());
    }
    
    @Override
    public String getName() {
        return getText();
    }
    
    @Override
    public PsiNamedElement setName(@NotNull String name) throws IncorrectOperationException {
        // This is a simplified implementation. In a real plugin, you would
        // create a new identifier from the given name and replace the old one.
        throw new UnsupportedOperationException("Renaming is not supported yet");
    }
}