package is.yarr.clips.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import is.yarr.clips.psi.CLIPSNamedElement;
import is.yarr.clips.psi.CLIPSPsiElement;
import is.yarr.clips.psi.CLIPSPsiImplUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Base implementation class for CLIPS PSI elements.
 * All CLIPS PSI element implementations will extend this class.
 */
public class CLIPSPsiElementImpl extends ASTWrapperPsiElement implements CLIPSPsiElement {
    public CLIPSPsiElementImpl(@NotNull ASTNode node) {
        super(node);
    }
    
    /**
     * Gets the text of this element without leading or trailing whitespace.
     * 
     * @return The trimmed text
     */
    public String getTrimmedText() {
        return getText().trim();
    }
    
    /**
     * Gets the text of this element without comments.
     * 
     * @return The text without comments
     */
    public String getTextWithoutComments() {
        // This is a simple implementation that removes semicolon comments
        // A more sophisticated implementation would use the lexer to identify comments
        String text = getText();
        StringBuilder result = new StringBuilder();
        
        boolean inComment = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            
            if (c == ';') {
                inComment = true;
            } else if (c == '\n') {
                inComment = false;
                result.append(c);
            } else if (!inComment) {
                result.append(c);
            }
        }
        
        return result.toString();
    }
    
    /**
     * Gets the parent CLIPS element, if any.
     * 
     * @return The parent CLIPS element, or null if none exists
     */
    public CLIPSPsiElement getParentCLIPSElement() {
        if (getParent() instanceof CLIPSPsiElement) {
            return (CLIPSPsiElement) getParent();
        }
        return null;
    }

    /**
     * For named PSI elements, returns the identifier node.
     */
    public PsiElement getNameIdentifier() {
        return CLIPSPsiImplUtil.getNameIdentifier((CLIPSNamedElement) this);
    }

    /**
     * For named PSI elements, returns the name.
     */
    public String getName() {
        return CLIPSPsiImplUtil.getName((CLIPSNamedElement) this);
    }

    /**
     * For named PSI elements, renames the identifier.
     */
    public PsiElement setName(@NotNull String newName) {
        return CLIPSPsiImplUtil.setName((CLIPSNamedElement) this, newName);
    }

}