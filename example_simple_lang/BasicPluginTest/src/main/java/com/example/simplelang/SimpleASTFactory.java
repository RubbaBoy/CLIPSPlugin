package com.example.simplelang;

import com.example.simplelang.psi.SimpleIdentifier;
import com.example.simplelang.psi.SimpleTypes;
import com.example.simplelang.psi.impl.SimpleIdLeafImpl;
import com.intellij.lang.ASTFactory;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom ASTFactory implementation for the Simple language.
 * This class is responsible for creating leaf elements in the PSI tree.
 */
public class SimpleASTFactory extends ASTFactory {
    /**
     * Creates a leaf element for the given token type and text.
     * This method is called by the IntelliJ Platform when creating leaf elements in the PSI tree.
     *
     * @param type the token type
     * @param text the token text
     * @return a leaf element for the token, or null to use the default implementation
     */
    @Override
    public @Nullable LeafElement createLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
        if (type == SimpleTypes.IDENTIFIER) {
            System.out.println("[SimpleASTFactory] createLeaf IDENTIFIER: '" + text + "'");
            return new SimpleIdentifier(type, text);
        } else if (type == SimpleTypes.ID) {
            System.out.println("[SimpleASTFactory] createLeaf ID: '" + text + "'");
            return new SimpleIdLeafImpl(type, text);
        }
        return null; // delegate to default implementation for other token types
    }
}