package is.yarr.clips.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import is.yarr.clips.psi.CLIPSPsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Base implementation for all PSI elements in the CLIPS language.
 */
public class CLIPSPsiElementImpl extends ASTWrapperPsiElement implements CLIPSPsiElement {
    public CLIPSPsiElementImpl(@NotNull ASTNode node) {
        super(node);
    }
}