// Java
package is.yarr.clips.highlight;

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceService;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Consumer;
import is.yarr.clips.CLIPSLanguage;
import is.yarr.clips.CLIPSResolveUtils;
import is.yarr.clips.psi.CLIPSDefName;
import is.yarr.clips.psi.CLIPSDeffunctionName;
import is.yarr.clips.psi.CLIPSFunctionCall;
import is.yarr.clips.psi.CLIPSTemplateName;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Highlights usages of a deffunction/template: declaration (write) and all call sites (read).
 */
public final class CLIPSFunctionHighlightUsagesHandler extends HighlightUsagesHandlerBase<PsiElement> {

    private final PsiFile file;
    private final Editor editor;

    public CLIPSFunctionHighlightUsagesHandler(@NotNull Editor editor, @NotNull PsiFile file) {
        super(editor, file);
        this.file = file;
        this.editor = editor;
    }

    @Override
    public @NotNull List<PsiElement> getTargets() {
        if (file.getLanguage() != CLIPSLanguage.INSTANCE) return List.of();
        var caretOffset = editor.getCaretModel().getOffset();
        var leaf = file.findElementAt(caretOffset);
        if (leaf == null) return Collections.emptyList();

        var target = resolveDeclaration(leaf);
        return target == null ? List.of() : List.of(target);
    }

    @Override
    public void selectTargets(@NotNull List<? extends PsiElement> targets, @NotNull Consumer<? super List<? extends PsiElement>> selectionConsumer) {
        selectionConsumer.consume(targets);
    }

    @Override
    public void computeUsages(@NotNull List<? extends PsiElement> targets) {
        if (targets.isEmpty()) return;
        var decl = targets.getFirst();

        // Write usage: highlight the declaration name identifier (or the declaration node)
        PsiElement writeEl;
        if (decl instanceof CLIPSDeffunctionName dfn) {
            var nameId = dfn.getNameIdentifier();
            writeEl = nameId != null ? nameId : dfn;
            getWriteUsages().add(writeEl.getTextRange());
        } else if (decl instanceof CLIPSTemplateName tfn) {
            var nameId = tfn.getNameIdentifier();
            writeEl = nameId != null ? nameId : tfn;
            getWriteUsages().add(writeEl.getTextRange());
        } else {
            return;
        }

        // Read usages: prefer indexed search; fallback to no-index scan in dumb mode or if none found
        var project = file.getProject();
        Collection<PsiReference> refs = Collections.emptyList();

        if (!DumbService.isDumb(project)) {
            var query = ReferencesSearch.search(decl, new LocalSearchScope(file));
            refs = query.findAll();
        }

        if (refs.isEmpty()) {
            // No-index fallback scan within the current file
            refs = CLIPSResolveUtils.findUsagesInFileNoIndexRefs(decl, file);
        }

        for (var ref : refs) {
            var el = ref.getElement();
            var baseRange = el.getTextRange();
            TextRange rIn = ref.getRangeInElement();
            if (baseRange != null) {
                getReadUsages().add(rIn.shiftRight(baseRange.getStartOffset()));
            } else {
                getReadUsages().add(el.getTextRange());
            }
        }
    }

    private PsiElement resolveDeclaration(PsiElement element) {
        if (isValidTarget(element)) return element;
        if (element == null) return null;

        var parent = element.getParent();
        if (isValidTarget(parent)) return parent;

        var inCall = PsiTreeUtil.getParentOfType(element, CLIPSFunctionCall.class, false) != null
                || PsiTreeUtil.getParentOfType(parent, CLIPSFunctionCall.class, false) != null;
        if (!inCall && !(element instanceof CLIPSDefName) && !(parent instanceof CLIPSDefName)) {
            return null;
        }

        var tryRef = tryResolveFromReferences(element);
        if (tryRef != null) return tryRef;

        if (parent != null) {
            var tryRefParent = tryResolveFromReferences(parent);
            if (tryRefParent != null) return tryRefParent;
        }
        return null;
    }

    private PsiElement tryResolveFromReferences(PsiElement e) {
        if (e == null) return null;
        var refs = PsiReferenceService.getService().getReferences(e, PsiReferenceService.Hints.NO_HINTS);
        if (refs.isEmpty()) return null;

        for (PsiReference ref : refs) {
            var resolved = ref.resolve();
            if (isValidTarget(resolved)) return resolved;
        }
        for (PsiReference ref : refs) {
            if (ref instanceof PsiPolyVariantReference poly) {
                var results = poly.multiResolve(false);
                for (var rr : results) {
                    var el = rr.getElement();
                    if (isValidTarget(el)) return el;
                }
            }
        }
        return null;
    }

    private boolean isValidTarget(PsiElement el) {
        return el instanceof CLIPSDeffunctionName || el instanceof CLIPSTemplateName;
    }
}