package is.yarr.clips.navigation;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.CLIPSLanguage;
import is.yarr.clips.CLIPSReference;
import is.yarr.clips.CLIPSResolveUtils;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Go To Declaration handler for CLIPS language.
 * Prefers existing PsiReferences on the leaf/parent; falls back to building a CLIPSReference
 * using the same trimming and context heuristics as the reference contributor.
 */
public final class CLIPSGotoDeclarationHandler implements GotoDeclarationHandler {

    @Override
    public @Nullable PsiElement[] getGotoDeclarationTargets(PsiElement sourceElement, int offset, Editor editor) {
        if (sourceElement == null) return PsiElement.EMPTY_ARRAY;
        PsiFile file = sourceElement.getContainingFile();
        if (file == null || file.getLanguage() != CLIPSLanguage.INSTANCE) return PsiElement.EMPTY_ARRAY;

        PsiElement leaf = file.findElementAt(offset);
        if (leaf == null) leaf = sourceElement;

        // If caret is on a declaration, optionally navigate to usages (unconventional for Go To Declaration)
        if (CLIPSResolveUtils.isDeclarationSite(leaf)) {

            // No-index scan (keep scope small; here we use the current file)
            var usageRefs = CLIPSResolveUtils.findUsagesInFileNoIndexRefs(leaf, file); // returns List<PsiReference>
            if (usageRefs.isEmpty()) return PsiElement.EMPTY_ARRAY;

            // Convert references to their PsiElements so the IDE can navigate to them
            List<PsiElement> usageElements = usageRefs.stream()
                    .map(ref -> {
                        PsiElement el = ref.getElement();
                        return el.isValid() ? el : null;
                    })
                    .filter(Objects::nonNull)
                    .distinct()
                    .toList();

            return usageElements.isEmpty() ? PsiElement.EMPTY_ARRAY : usageElements.toArray(PsiElement[]::new);
        }

        // Normal path: caret on a reference → resolve to declaration targets
        var targets = CLIPSResolveUtils.resolveFromReferences(leaf);
        if (!targets.isEmpty()) {
            return targets.toArray(PsiElement[]::new);
        }

        // Try parent as a fallback
        var parent = leaf.getParent();
        if (parent != null) {
            targets = CLIPSResolveUtils.resolveFromReferences(parent);
            if (!targets.isEmpty()) {
                return targets.toArray(PsiElement[]::new);
            }
        }

        return PsiElement.EMPTY_ARRAY;
    }
}
