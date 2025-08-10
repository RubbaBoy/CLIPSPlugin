package is.yarr.clips.completion;

import com.intellij.codeInsight.AutoPopupController;
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import is.yarr.clips.CLIPSLanguage;
import org.jetbrains.annotations.NotNull;

/**
 * Triggers completion popup when '?' is typed in CLIPS files to assist variable completion.
 */
public class CLIPSTypedHandler extends TypedHandlerDelegate {
    @Override
    public @NotNull Result charTyped(char c, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (c == '?' && file.getLanguage().isKindOf(CLIPSLanguage.INSTANCE)) {
            AutoPopupController.getInstance(project).scheduleAutoPopup(editor);
            return Result.STOP;
        }
        return Result.CONTINUE;
    }
}
