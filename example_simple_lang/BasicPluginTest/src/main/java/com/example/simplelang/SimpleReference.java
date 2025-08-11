package com.example.simplelang;

import com.example.simplelang.psi.SimpleEntityDeclaration;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Reference implementation for the Simple language.
 * This class handles resolving references to entity declarations.
 */
public class SimpleReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private final String entityName;

    public SimpleReference(@NotNull PsiElement element, TextRange rangeInElement) {
        super(element, rangeInElement);
        entityName = element.getText().substring(rangeInElement.getStartOffset(), rangeInElement.getEndOffset());
    }

    /**
     * Resolves the reference to multiple possible targets.
     *
     * @return an array of resolve results
     */
    @NotNull
    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        var project = myElement.getProject();
        var entities = SimpleUtil.findEntities(project, entityName);
        var results = new ArrayList<ResolveResult>(entities.size());
        
        for (var entity : entities) {
            results.add(new PsiElementResolveResult(entity));
        }
        
        return results.toArray(new ResolveResult[0]);
    }

    /**
     * Resolves the reference to a single target.
     *
     * @return the resolved element, or null if the reference could not be resolved
     */
    @Nullable
    @Override
    public PsiElement resolve() {
        // Try resolving in the same file first (works even if indices are incomplete)
        var file = myElement.getContainingFile();
        if (file != null) {
            var local = com.intellij.psi.util.PsiTreeUtil.findChildrenOfType(file, SimpleEntityDeclaration.class);
            for (var entity : local) {
                if (entityName.equals(entity.getName())) {
                    System.out.println("[SimpleReference] resolve: found in same file -> " + entityName);
                    return entity;
                }
            }
        }
        // Fallback to project-wide search via index
        var result = SimpleUtil.findEntity(myElement.getProject(), entityName);
        System.out.println("[SimpleReference] resolve: project-wide -> " + entityName + " => " + (result != null));
        return result;
    }

    /**
     * Returns the possible variants for code completion.
     *
     * @return an array of lookup elements
     */
    @NotNull
    @Override
    public Object @NotNull [] getVariants() {
        var project = myElement.getProject();
        var entities = SimpleUtil.findEntities(project);
        var variants = new ArrayList<LookupElement>(entities.size());
        
        for (var entity : entities) {
            if (entity.getName() != null && !entity.getName().isEmpty()) {
                variants.add(LookupElementBuilder.create(entity)
                        .withIcon(SimpleIcons.FILE)
                        .withTypeText(entity.getContainingFile().getName()));
            }
        }
        
        return variants.toArray();
    }
}