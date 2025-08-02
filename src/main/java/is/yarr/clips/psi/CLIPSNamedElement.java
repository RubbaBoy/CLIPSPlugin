package is.yarr.clips.psi;

import com.intellij.psi.PsiNameIdentifierOwner;

/**
 * Interface for named elements in the CLIPS language.
 * This includes variables, templates, functions, etc.
 */
public interface CLIPSNamedElement extends CLIPSPsiElement, PsiNameIdentifierOwner {
}