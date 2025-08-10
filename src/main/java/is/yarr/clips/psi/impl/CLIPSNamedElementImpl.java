package is.yarr.clips.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import is.yarr.clips.psi.CLIPSNamedElement;
import org.jetbrains.annotations.NotNull;

/**
 * Base implementation for all named elements in the CLIPS language.
 * The actual implementations of getName, setName, and getNameIdentifier
 * are in CLIPSPsiImplUtil and referenced in the BNF file.
 */
public abstract class CLIPSNamedElementImpl extends CLIPSPsiElementImpl implements CLIPSNamedElement {
    public CLIPSNamedElementImpl(@NotNull ASTNode node) {
        super(node);
    }
    
    // The implementations of getName, setName, and getNameIdentifier
    // are in CLIPSPsiImplUtil and referenced in the BNF file.
}