package com.example.simplelang.psi;

import com.example.simplelang.psi.impl.SimpleIdLeafImpl;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Factory for creating SimpleIdentifier instances.
 * This class is used by the parser to create the appropriate PSI elements for tokens.
 */
public class SimpleIdentifierFactory {
    /**
     * Creates a SimpleIdentifier for an IDENTIFIER token.
     *
     * @param type the token type
     * @param text the token text
     * @return a SimpleIdentifier instance
     */
    public static LeafElement createIdentifier(IElementType type, CharSequence text) {
        return new SimpleIdentifier(type, text);
    }
    
    /**
     * Creates a PsiElement for a token.
     * If the token is an IDENTIFIER, a SimpleIdentifier is created.
     * If the token type is ID, a SimpleIdLeafImpl is created.
     * Otherwise, the default element creation is used.
     *
     * @param node the AST node
     * @return a PsiElement for the token
     */
    public static PsiElement createElement(@NotNull ASTNode node) {
        IElementType type = node.getElementType();
        if (type == SimpleTypes.IDENTIFIER) {
            // Use the ASTNode directly to create the leaf element
            return new SimpleIdentifier(node);
        } else if (type == SimpleTypes.ID) {
            // Use the ASTNode directly to create the leaf element
            return new SimpleIdLeafImpl(node);
        }
        return SimpleTypes.Factory.createElement(node);
    }
}