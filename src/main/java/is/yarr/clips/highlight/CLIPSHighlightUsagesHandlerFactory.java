package is.yarr.clips.highlight;

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerFactory;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import is.yarr.clips.psi.CLIPSDefName;
import is.yarr.clips.psi.CLIPSFunctionCall;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Factory that creates a HighlightUsagesHandler for CLIPS identifiers so that
 * caret-based highlighting works for deffunction declarations and function call heads.
 */
public class CLIPSHighlightUsagesHandlerFactory implements HighlightUsagesHandlerFactory {
    @Override
    public @Nullable HighlightUsagesHandlerBase<?> createHighlightUsagesHandler(@NotNull Editor editor, @NotNull PsiFile file) {
//        var offset = editor.getCaretModel().getOffset();
//        PsiElement target = file.findElementAt(offset);
//        if (target == null || target.getTextLength() == 0 || target.getNode() == null) {
//            if (offset > 0) target = file.findElementAt(offset - 1);
//        }
//        if (target == null) return null;
//        var handler = new CLIPSHighlightUsagesHandler(editor, file, target);
//        return handler.applicable() ? handler : null;
        return null;
    }
}
