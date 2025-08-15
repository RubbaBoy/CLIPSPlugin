package is.yarr.clips.highlight;

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.CLIPSLanguage;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Factory that creates a HighlightUsages handler for CLIPS deffunction names and their usages.
 */
public final class CLIPSHighlightUsagesHandlerFactory implements HighlightUsagesHandlerFactory {

    @Override
    public @Nullable HighlightUsagesHandlerBase<?> createHighlightUsagesHandler(@NotNull Editor editor, @NotNull PsiFile psiFile) {
        if (psiFile.getLanguage() != CLIPSLanguage.INSTANCE) return null;
        var offset = editor.getCaretModel().getOffset();
        var element = psiFile.findElementAt(offset);
        if (element != null && isApplicable(element)) {
            System.out.println("here");
            return new CLIPSFunctionHighlightUsagesHandler(editor, psiFile);
        }

        var parent = element != null ? element.getParent() : null;
        if (parent != null && isApplicable(parent)) {
            System.out.println("here2");
            return new CLIPSFunctionHighlightUsagesHandler(editor, psiFile);
        }
        return null;
    }

    private static boolean isApplicable(@NotNull PsiElement target) {
        // If caret is on the declaration name itself
        if (target instanceof CLIPSDeffunctionName) return true;
        // If caret is on IDENTIFIER leaf under CLIPSDefName inside a CLIPSFunctionCall and not in declaration
        var node = target.getNode();
        if (node != null && node.getElementType() == CLIPSTypes.IDENTIFIER) {
            var defName = target.getParent();
            if (defName instanceof CLIPSDefName) {
                var inFuncCall = PsiTreeUtil.getParentOfType(defName, CLIPSFunctionCall.class, false) != null;
                var inFunctionDecl = PsiTreeUtil.getParentOfType(defName, CLIPSDeffunctionConstruct.class, false) != null;
                if (inFuncCall && !inFunctionDecl) return true;
            }
        }
        if (target instanceof CLIPSDefName) {
            var inFuncCall = PsiTreeUtil.getParentOfType(target, CLIPSFunctionCall.class, false) != null;
            var inFunctionDecl = PsiTreeUtil.getParentOfType(target, CLIPSDeffunctionConstruct.class, false) != null;
            return inFuncCall && !inFunctionDecl;
        }
        return false;
    }
}
