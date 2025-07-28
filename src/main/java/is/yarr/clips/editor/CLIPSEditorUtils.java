package is.yarr.clips.editor;

import com.esotericsoftware.kryo.kryo5.util.Null;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import is.yarr.clips.psi.CLIPSElementTypes;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class CLIPSEditorUtils {

    public static @Nullable PsiElement findContainingBlock(PsiElement variable, Function<PsiElement, PsiElement> fallback) {
        if (variable == null) {
            return null;
        }

        // Walk up the tree to find a block-defining construct, skipping null-nodes
        PsiElement current = variable;
        while (current != null) {
            var node = current.getNode();
            if (node != null) {
                IElementType type = node.getElementType();
                if (type == CLIPSElementTypes.DEFFUNCTION_CONSTRUCT ||
                        type == CLIPSElementTypes.DEFRULE_CONSTRUCT ||
                        type == CLIPSElementTypes.DEFTEMPLATE_CONSTRUCT ||
                        type == CLIPSElementTypes.DEFCLASS_CONSTRUCT ||
                        type == CLIPSElementTypes.DEFMODULE_CONSTRUCT) {
                    return current;
                }
            }
            current = current.getParent();
        }
        // If we couldn't find a construct element, try to find a block using the "old" (user-specific) method
        // Should this be merged in from all usages?
        return fallback.apply(variable);
    }

//    public static @Nullable PsiElement findDefinedTemplate(PsiFile file, String name) {
//
//    }

}
