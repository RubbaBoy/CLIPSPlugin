package is.yarr.clips.highlight;

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.CommonProcessors;
import com.intellij.util.Consumer;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Highlights usages for CLIPS function declarations and function call heads.
 */
public class CLIPSHighlightUsagesHandler extends HighlightUsagesHandlerBase<PsiElement> {

    private final PsiElement myTargetElement;
    private final PsiFile myFileRef;
    private boolean myApplicable;

    public CLIPSHighlightUsagesHandler(@NotNull Editor editor, @NotNull PsiFile file, @NotNull PsiElement target) {
        super(editor, file);
        this.myTargetElement = target;
        this.myFileRef = file;
        this.myApplicable = computeApplicability(target);
    }

    public boolean applicable() {
        return myApplicable;
    }

    private static boolean isFunctionCallHeadIdentifier(PsiElement element) {
        if (element == null) return false;
        var parent = element.getParent();
        if (parent instanceof CLIPSDeffunctionName) return true; // clicking inside declaration name should be applicable
        if (!(parent instanceof CLIPSDefName)) return false;
        return com.intellij.psi.util.PsiTreeUtil.getParentOfType(element, CLIPSFunctionCall.class, false) != null;
    }

    private static boolean computeApplicability(PsiElement element) {
        System.out.println("computeApplicability: " + element);
        if (element instanceof CLIPSDeffunctionName) {
            System.out.println("true");
            return true;
        }
        var functionCallHeadIdentifier = isFunctionCallHeadIdentifier(element);
        System.out.println("app = " + functionCallHeadIdentifier);
        return functionCallHeadIdentifier;
    }

    @Override
    public @NotNull List<PsiElement> getTargets() {
        var targets = new ArrayList<PsiElement>();
        if (!myApplicable) return targets;
        if (myTargetElement instanceof CLIPSDeffunctionName defName) {
            targets.add(defName);
        } else if (isFunctionCallHeadIdentifier(myTargetElement)) {
            System.out.println("here! " + myTargetElement.getReferences().length);
            // Resolve call head to function declaration(s)
            for (PsiReference ref : myTargetElement.getReferences()) {
                var resolved = ref.resolve();
                System.out.println("resolved = " + resolved);
                if (resolved != null) targets.add(resolved);
            }
        }
        System.out.println("targets = " + targets);
        return targets;
    }

    @Override
    protected void selectTargets(@NotNull List<? extends PsiElement> targets, @NotNull Consumer<? super List<? extends PsiElement>> consumer) {
        // No chooser needed; pass all targets through
        consumer.consume(targets);
    }

    @Override
    public void computeUsages(@NotNull List<? extends PsiElement> targets) {
        if (targets.isEmpty()) return;
        var fileScope = GlobalSearchScope.fileScope(myFileRef.getProject(), myFileRef.getVirtualFile());
        for (var target : targets) {
            System.out.println("usg target = " + target);
            // Highlight declaration name
            var nameId = target instanceof CLIPSNamedElement ne ? ne.getNameIdentifier() : target;
            if (nameId != null) addOccurrence(nameId);
            // Highlight usages within this file
            var query = ReferencesSearch.search(target, fileScope);
            var results = new ArrayList<PsiReference>();
            query.forEach(new CommonProcessors.CollectProcessor<>(results));
            for (var ref : results) {
                var element = ref.getElement();
                addOccurrence(element);
            }
        }
    }
}
