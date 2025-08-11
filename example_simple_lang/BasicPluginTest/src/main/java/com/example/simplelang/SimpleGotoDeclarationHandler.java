package com.example.simplelang;

import com.example.simplelang.psi.SimpleReferenceDeclaration;
import com.example.simplelang.psi.SimpleTypes;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.Nullable;

/**
 * Fallback Go To Declaration handler for the Simple language.
 * Uses the same resolution logic as our contributed reference to provide navigation
 * even if the platform doesn’t pick up the contributed reference automatically.
 */
public class SimpleGotoDeclarationHandler implements GotoDeclarationHandler {
    @Override
    public @Nullable PsiElement[] getGotoDeclarationTargets(PsiElement element, int offset, Editor editor) {
        if (element == null) return PsiElement.EMPTY_ARRAY;
        PsiFile file = element.getContainingFile();
        if (file == null || file.getLanguage() != SimpleLanguage.INSTANCE) return PsiElement.EMPTY_ARRAY;

        // Handle identifiers inside reference declarations
        var node = element.getNode();
        var parent = element.getParent();
        if (node != null && node.getElementType() == SimpleTypes.IDENTIFIER && parent instanceof SimpleReferenceDeclaration) {
            var range = TextRange.from(0, element.getTextLength());
            var ref = new SimpleReference(element, range);
            var resolved = ref.resolve();
            if (resolved != null) {
                return new PsiElement[]{resolved};
            }
            var variants = ref.multiResolve(false);
            if (variants.length > 0) {
                return java.util.Arrays.stream(variants)
                        .map(r -> r.getElement())
                        .filter(java.util.Objects::nonNull)
                        .toArray(PsiElement[]::new);
            }
        }
        return PsiElement.EMPTY_ARRAY;
    }
}
