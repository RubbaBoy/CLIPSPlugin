package is.yarr.clips.editor;

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.Consumer;
import is.yarr.clips.psi.CLIPSDefName;
import is.yarr.clips.psi.CLIPSElementTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Handler for highlighting all occurrences of a CLIPS variable when clicked.
 */
public class CLIPSDefNameHighlightUsagesHandler extends HighlightUsagesHandlerBase<PsiElement> {
    private final CLIPSDefName myVariable;
    private final List<PsiElement> myTargets = new ArrayList<>();

    public CLIPSDefNameHighlightUsagesHandler(@NotNull Editor editor, @NotNull PsiFile file, @NotNull CLIPSDefName defName) {
        super(editor, file);
        myVariable = defName;
        myTargets.add(defName);
    }

    @Override
    public @NotNull List<PsiElement> getTargets() {
        return myTargets;
    }

    @Override
    protected void selectTargets(@NotNull List<? extends PsiElement> targets, @NotNull Consumer<? super List<? extends PsiElement>> selectionConsumer) {
        selectionConsumer.consume(targets);
    }

    @Override
    public void computeUsages(@NotNull List<? extends PsiElement> targets) {
        // Add the variable itself to the list of usages
        addOccurrence(myVariable);

        // Get the variable name without the prefix (?, $?, ?*)
        String variableName = myVariable.getName();
        if (variableName == null || variableName.isEmpty()) {
            return;
        }


        // Check if the variable is global
        // Find all occurrences of the variable in the file
        PsiFile file = myVariable.getContainingFile();

        findVariableOccurrences(file, variableName);
    }

    /**
     * Finds all occurrences of a variable with the given name in the element.
     * Recursively traverses the PSI tree to find usages.
     *
     * @param element The element to start searching from
     * @param variableName The variable name to look for (without prefix)
     */
    private void findVariableOccurrences(PsiElement element, String variableName) {
        if (element instanceof CLIPSDefName defName) {
            if (defName.getText().equals(variableName)) {
                addOccurrence(defName);
                return; // Don't recurse further into this element (there *should* only be one DefName per element)
            }
        }

        for (PsiElement child : element.getChildren()) {
            if (child instanceof PsiWhiteSpace) continue;

            findVariableOccurrences(child, variableName);
        }
    }
}