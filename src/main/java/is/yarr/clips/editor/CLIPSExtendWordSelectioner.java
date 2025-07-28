package is.yarr.clips.editor;

import com.intellij.codeInsight.editorActions.wordSelection.AbstractWordSelectioner;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import is.yarr.clips.CLIPSLanguage;
import is.yarr.clips.psi.CLIPSElementTypes;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * Provides custom word selection behavior for CLIPS variables.
 * When a variable is double-clicked, this ensures the entire variable is selected.
 */
public class CLIPSExtendWordSelectioner extends AbstractWordSelectioner {

    @Override
    public boolean canSelect(@NotNull PsiElement e) {
        var file = e.getContainingFile();
        if (file == null || !(file.getLanguage() instanceof CLIPSLanguage)) {
            return false;
        }

        // This selectioner is interested in leaf elements that are variable tokens.
        IElementType elementType = e.getNode().getElementType();
        return elementType == CLIPSElementTypes.VARIABLE_ELEMENT ||
                elementType == CLIPSElementTypes.VARIABLE ||
                elementType == CLIPSElementTypes.MULTIFIELD_VARIABLE ||
                elementType == CLIPSElementTypes.GLOBAL_VARIABLE;
    }

    @Override
    public List<TextRange> select(@NotNull PsiElement e, @NotNull CharSequence editorText, int cursorOffset, @NotNull Editor editor) {
        return Collections.singletonList(e.getTextRange());
    }
}