package is.yarr.clips.psi;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;

/**
 * Token types for the CLIPS language.
 */
public interface CLIPSTypes {
    // Token types
    IElementType COMMENT = new CLIPSTokenType("COMMENT");
    IElementType KEYWORD = new CLIPSTokenType("KEYWORD");
    IElementType BUILTIN_FUNCTION = new CLIPSTokenType("BUILTIN_FUNCTION");
    IElementType IDENTIFIER = new CLIPSTokenType("IDENTIFIER");
    IElementType STRING = new CLIPSTokenType("STRING");
    IElementType NUMBER = new CLIPSTokenType("NUMBER");
    IElementType VARIABLE = new CLIPSTokenType("VARIABLE");
    IElementType GLOBAL_VARIABLE = new CLIPSTokenType("GLOBAL_VARIABLE");
    IElementType MULTIFIELD_VARIABLE = new CLIPSTokenType("MULTIFIELD_VARIABLE");
    IElementType LPAREN = new CLIPSTokenType("LPAREN");
    IElementType RPAREN = new CLIPSTokenType("RPAREN");
    IElementType LBRACKET = new CLIPSTokenType("LBRACKET");
    IElementType RBRACKET = new CLIPSTokenType("RBRACKET");
    
    /**
     * Factory for creating PSI elements from AST nodes.
     */
    class Factory {
        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();
            // For now, we don't have any custom PSI elements, so we just return null
            // and let the default implementation handle it
            return null;
        }
    }
}
