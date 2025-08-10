package is.yarr.clips.completion;

import com.intellij.codeInsight.completion.CompletionLocation;
import com.intellij.codeInsight.completion.CompletionWeigher;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiElement;
import is.yarr.clips.psi.CLIPSGlobalVariableDef;
import org.jetbrains.annotations.NotNull;

/**
 * Orders completion items: locals > globals > built-ins.
 * Approximation: LookupElements backed by PSI elements are considered project symbols.
 */
public class CLIPSCompletionWeigher extends CompletionWeigher {
    @Override
    public Comparable<?> weigh(@NotNull LookupElement element, @NotNull CompletionLocation location) {
        PsiElement psi = element.getPsiElement();
        if (psi != null) {
            // Project-defined PSI-backed element
            if (psi instanceof CLIPSGlobalVariableDef) {
                return 1; // globals after locals
            }
            return 0; // locals/templates/functions/rules first
        }
        return 2; // built-ins last
    }
}
