package is.yarr.clips.editor;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import is.yarr.clips.CLIPSFileType;
import is.yarr.clips.CLIPSLanguage;
import is.yarr.clips.psi.CLIPSElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Custom handler for selecting CLIPS variables when double-clicked.
 * Overrides the default selection behavior to provide language-specific selection.
 */
public class CLIPSSelectWordHandler extends EditorActionHandler {
    private final EditorActionHandler originalHandler;

    public CLIPSSelectWordHandler(EditorActionHandler originalHandler) {
        this.originalHandler = originalHandler;
    }

    @Override
    protected boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
        Project project = editor.getProject();
        if (project == null) {
            return false;
        }

        PsiFile file = PsiManager.getInstance(project).findFile(editor.getVirtualFile());
        if (file == null || !(file.getLanguage().is(CLIPSLanguage.INSTANCE))) {
            return false;
        }

        // Check if the element at caret position is a variable
        int offset = caret.getOffset();
        PsiElement element = file.findElementAt(offset);
        if (element == null) {
            return false;
        }

        // This handler is interested in leaf elements that are variable tokens
        IElementType elementType = element.getNode().getElementType();
        return elementType == CLIPSElementTypes.VARIABLE_ELEMENT ||
                elementType == CLIPSElementTypes.GLOBAL_VARIABLE;
    }

    @Override
    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        if (caret == null) {
            caret = editor.getCaretModel().getCurrentCaret();
        }

        PsiFile file = getPsiFile(editor, dataContext);
        if (file == null || !file.getFileType().equals(CLIPSFileType.INSTANCE)) {
            // Fall back to the original handler for non-CLIPS files
            originalHandler.execute(editor, caret, dataContext);
            return;
        }

        int caretOffset = caret.getOffset();
        PsiElement element = ReadAction.compute(() -> file.findElementAt(caretOffset));

        if (element == null) {
            originalHandler.execute(editor, caret, dataContext);
            return;
        }

        // Check if the element is a CLIPS variable
        if (element instanceof LeafPsiElement &&
                (element.getNode().getElementType() == CLIPSElementTypes.VARIABLE_ELEMENT ||
                        element.getNode().getElementType() == CLIPSElementTypes.VARIABLE ||
                        element.getNode().getElementType() == CLIPSElementTypes.MULTIFIELD_VARIABLE ||
                        element.getNode().getElementType() == CLIPSElementTypes.GLOBAL_VARIABLE)) {

            // Get the text range of the variable
            TextRange range = element.getTextRange();

            // Select the variable
            caret.setSelection(range.getStartOffset(), range.getEndOffset());
        } else {
            // If not a variable, fall back to default behavior
            originalHandler.execute(editor, caret, dataContext);
        }
    }

    @Nullable
    private PsiFile getPsiFile(@NotNull Editor editor, DataContext dataContext) {
        Project project = editor.getProject();
        if (project == null) {
            return null;
        }

        return PsiManager.getInstance(project).findFile(editor.getVirtualFile());
    }
}