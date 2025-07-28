package is.yarr.clips.editor;

import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Factory for creating CLIPS-specific word selection handlers.
 * Wraps the original handler with our custom implementation.
 */
public class CLIPSSelectWordHandlerFactory extends EditorActionHandler {
    private final EditorActionHandler originalHandler;
    private final CLIPSSelectWordHandler clipsHandler;

    public CLIPSSelectWordHandlerFactory(EditorActionHandler originalHandler) {
        this.originalHandler = originalHandler;
        this.clipsHandler = new CLIPSSelectWordHandler(originalHandler);
    }

    @Override
    protected void doExecute(@NotNull Editor editor, @Nullable Caret caret, DataContext dataContext) {
        clipsHandler.execute(editor, caret, dataContext);
    }

    @Override
    public boolean isEnabledForCaret(@NotNull Editor editor, @NotNull Caret caret, DataContext dataContext) {
        return originalHandler.isEnabled(editor, caret, dataContext);
    }
}
