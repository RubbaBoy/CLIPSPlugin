package com.example.simplelang.psi.impl;

import com.example.simplelang.SimpleReference;
import com.example.simplelang.psi.SimpleReferenceDeclaration;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class for SimpleReferenceDeclaration that provides the getReference method.
 * This class is used by the generated PSI classes to implement the getReference method.
 */
public class SimpleReferenceDeclarationImplUtil {
    /**
     * Returns a reference to the entity being referenced by this reference declaration.
     * This method is called by the generated PSI classes when getReference() is called on a SimpleReferenceDeclaration.
     *
     * @param element the reference declaration element
     * @return a reference to the entity being referenced
     */
    public static PsiReference getReference(@NotNull SimpleReferenceDeclaration element) {
        // Get the entity name (second child, after the 'ref' keyword)
        PsiElement entityName = element.getFirstChild().getNextSibling();
        if (entityName != null) {
            // Create a reference with a text range that covers the entire entity name
            // Use a relative range for the element
            TextRange range = entityName.getTextRange().shiftRight(-entityName.getTextOffset());
            return new SimpleReference(entityName, range);
        }
        return null;
    }
}