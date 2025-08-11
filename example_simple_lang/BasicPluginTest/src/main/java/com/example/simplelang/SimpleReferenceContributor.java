package com.example.simplelang;

import com.example.simplelang.psi.SimpleReferenceDeclaration;
import com.example.simplelang.psi.SimpleTypes;
import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

/**
 * Reference contributor for the Simple language.
 * Temporarily broad pattern to detect what elements are queried; still returns
 * references only for IDENTIFIER leaves under SimpleReferenceDeclaration.
 */
public class SimpleReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        System.out.println("[SimpleReferenceContributor] Registering provider: ANY psiElement with language=Simple (diagnostic mode)");
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement().withLanguage(SimpleLanguage.INSTANCE),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                          @NotNull ProcessingContext context) {
                        var parent = element.getParent();
                        var elemClass = element.getClass().getName();
                        var parentClass = parent != null ? parent.getClass().getName() : "null";
                        var elemType = element.getNode() != null ? element.getNode().getElementType() : null;
                        System.out.println("[SimpleReferenceContributor] getReferencesByElement: text='" + element.getText() + "', type=" + elemType + ", elemClass=" + elemClass + ", parentClass=" + parentClass);

                        // Only return references for the intended case
                        if (elemType == SimpleTypes.IDENTIFIER && parent instanceof SimpleReferenceDeclaration) {
                            var range = TextRange.from(0, element.getTextLength());
                            System.out.println("[SimpleReferenceContributor] -> returning SimpleReference for IDENTIFIER inside SimpleReferenceDeclaration");
                            return new PsiReference[]{new SimpleReference(element, range)};
                        }
                        return PsiReference.EMPTY_ARRAY;
                    }
                });
    }
}