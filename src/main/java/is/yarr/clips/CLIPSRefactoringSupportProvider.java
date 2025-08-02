package is.yarr.clips;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides support for refactoring operations in CLIPS files.
 * This enables features like renaming variables, templates, rules, etc.
 */
public class CLIPSRefactoringSupportProvider extends RefactoringSupportProvider {
    /**
     * Determines if an element can be renamed.
     * In CLIPS, named elements like variables, templates, rules, etc. can be renamed.
     *
     * @param element The element to check
     * @return true if the element can be renamed, false otherwise
     */
    @Override
    public boolean isMemberInplaceRenameAvailable(@NotNull PsiElement element, @Nullable PsiElement context) {
        return element instanceof CLIPSNamedElement;
    }
    
    /**
     * Determines if an element supports inline refactoring.
     * This is used for operations like "Inline Variable" or "Inline Method".
     * Currently, CLIPS does not support inline refactoring.
     *
     * @param element The element to check
     * @return true if the element supports inline refactoring, false otherwise
     */
    @Override
    public boolean isInplaceRenameAvailable(@NotNull PsiElement element, @Nullable PsiElement context) {
        // Check if the element is a named element
        if (element instanceof CLIPSNamedElement) {
            // Allow renaming for specific types of named elements
            return element instanceof CLIPSVariableElement ||
                   element instanceof CLIPSGlobalVariableDef ||
                   element instanceof CLIPSTemplateName ||
                   element instanceof CLIPSRuleName ||
                   element instanceof CLIPSSlotName ||
                   element instanceof CLIPSParameter ||
                   element instanceof CLIPSModuleName ||
                   element instanceof CLIPSClassName ||
                   element instanceof CLIPSDeffactsName;
        }
        return false;
    }
}