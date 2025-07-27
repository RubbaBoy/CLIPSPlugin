package is.yarr.clips.psi;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import is.yarr.clips.psi.impl.CLIPSDefclassConstructImpl;
import is.yarr.clips.psi.impl.CLIPSDeffunctionConstructImpl;
import is.yarr.clips.psi.impl.CLIPSDefmoduleConstructImpl;
import is.yarr.clips.psi.impl.CLIPSDefruleConstructImpl;
import is.yarr.clips.psi.impl.CLIPSDeftemplateConstructImpl;
import is.yarr.clips.psi.impl.CLIPSPsiElementImpl;

/**
 * Token types for the CLIPS language.
 */
public interface CLIPSTypes {
    // Token types
//    IElementType COMMENT = new CLIPSTokenType("COMMENT_clipsTypes");
//    IElementType KEYWORD = new CLIPSTokenType("KEYWORD_clipsTypes");
//    IElementType BUILTIN_FUNCTION = new CLIPSTokenType("BUILTIN_FUNCTION_clipsTypes");
//    IElementType IDENTIFIER = new CLIPSTokenType("IDENTIFIER_clipsTypes");
//    IElementType STRING = new CLIPSTokenType("STRING_clipsTypes");
//    IElementType NUMBER = new CLIPSTokenType("NUMBER_clipsTypes");
//    IElementType VARIABLE = new CLIPSTokenType("VARIABLE_clipsTypes");
//    IElementType GLOBAL_VARIABLE = new CLIPSTokenType("GLOBAL_VARIABLE_clipsTypes");
//    IElementType MULTIFIELD_VARIABLE = new CLIPSTokenType("MULTIFIELD_VARIABLE_clipsTypes");
//    IElementType LPAREN = new CLIPSTokenType("LPAREN_clipsTypes");
//    IElementType RPAREN = new CLIPSTokenType("RPAREN_clipsTypes");
//    IElementType LBRACKET = new CLIPSTokenType("LBRACKET_clipsTypes");
//    IElementType RBRACKET = new CLIPSTokenType("RBRACKET_clipsTypes");

//    static boolean isSearchable(PsiElement element) {
//        if (element instanceof LeafPsiElement leaf && !(element instanceof PsiWhiteSpace)) {
//            // Ignore parentheses and brackets
//            return leaf.getElementType() != CLIPSElementTypes.LPAREN &&
//                    leaf.getElementType() != CLIPSElementTypes.RPAREN &&
//                    leaf.getElementType() != CLIPSElementTypes.LBRACKET &&
//                    leaf.getElementType() != CLIPSElementTypes.RBRACKET;
//        }
//
//        return false;
//    }

    /**
     * Factory for creating PSI elements from AST nodes.
     */
    class Factory {
        private static final Logger LOG = Logger.getInstance(Factory.class);

        public static PsiElement createElement(ASTNode node) {
            IElementType type = node.getElementType();

            LOG.warn("Creating element for type: " + type);

            if (type == CLIPSElementTypes.VARIABLE) {
                LOG.warn("Found a VARIABLE token. You can create your custom PSI element here.");
                // Here you would return your custom PSI element for variables, for example:
                // return new CLIPSVariablePsiImpl(node);
            }


            System.out.println("type.getDebugName() = " + type.getDebugName());

            // Create custom PSI elements for variables
            if (type == CLIPSElementTypes.VARIABLE || type == CLIPSElementTypes.MULTIFIELD_VARIABLE || type == CLIPSElementTypes.GLOBAL_VARIABLE) {
                System.out.println("Making var!");
                return new is.yarr.clips.psi.impl.CLIPSVariableElementImpl(node);
            }

            // Create custom PSI elements for constructs
            if (type == CLIPSElementTypes.DEFMODULE_CONSTRUCT) {
                return new CLIPSDefmoduleConstructImpl(node);
            } else if (type == CLIPSElementTypes.DEFCLASS_CONSTRUCT) {
                return new CLIPSDefclassConstructImpl(node);
            } else if (type == CLIPSElementTypes.DEFTEMPLATE_CONSTRUCT) {
                return new CLIPSDeftemplateConstructImpl(node);
            } else if (type == CLIPSElementTypes.DEFFUNCTION_CONSTRUCT) {
                return new CLIPSDeffunctionConstructImpl(node);
            } else if (type == CLIPSElementTypes.DEFRULE_CONSTRUCT) {
                return new CLIPSDefruleConstructImpl(node);
            }

            // For other element types, return null and let the default implementation handle it
//            return new CLIPSPsiElementImpl(node);
            return new CLIPSPsiElementImpl(node);
        }
    }
}
