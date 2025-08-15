package is.yarr.clips;

import com.intellij.model.psi.PsiSymbolReferenceService;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceService;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.psi.CLIPSDefName;
import is.yarr.clips.psi.CLIPSDefclassConstruct;
import is.yarr.clips.psi.CLIPSDeffactsConstruct;
import is.yarr.clips.psi.CLIPSDeffunctionConstruct;
import is.yarr.clips.psi.CLIPSDeffunctionName;
import is.yarr.clips.psi.CLIPSDefglobalConstruct;
import is.yarr.clips.psi.CLIPSDefmoduleConstruct;
import is.yarr.clips.psi.CLIPSDefruleConstruct;
import is.yarr.clips.psi.CLIPSDeftemplateConstruct;
import is.yarr.clips.psi.CLIPSFunctionCall;
import is.yarr.clips.psi.CLIPSMultifieldVariableElement;
import is.yarr.clips.psi.CLIPSMultislotDefinition;
import is.yarr.clips.psi.CLIPSParameter;
import is.yarr.clips.psi.CLIPSRuleName;
import is.yarr.clips.psi.CLIPSSingleSlotDefinition;
import is.yarr.clips.psi.CLIPSSlotName;
import is.yarr.clips.psi.CLIPSTemplateName;
import is.yarr.clips.psi.CLIPSTypes;
import is.yarr.clips.psi.CLIPSVariableElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class CLIPSResolveUtils {

    private static final Predicate<PsiElement> DEFAULT_REF_CARRIER = el -> {
        if (el == null || !el.isValid()) return false;
        var node = el.getNode();
        if (node == null) return false;
        return el instanceof CLIPSVariableElement
                || el instanceof CLIPSMultifieldVariableElement
                || el instanceof CLIPSParameter
                || el instanceof CLIPSSlotName
                || el instanceof CLIPSDefName
                || node.getElementType() == CLIPSTypes.IDENTIFIER
                || node.getElementType() == CLIPSTypes.VARIABLE
                || node.getElementType() == CLIPSTypes.GLOBAL_VARIABLE;
    };

    public static Set<PsiReference> findUsagesInFileNoIndexRefs(PsiElement declaration, PsiFile file) {
        return findUsagesInFileNoIndexRefs(declaration, file, DEFAULT_REF_CARRIER);
    }

    public static Set<PsiReference> findUsagesInFileNoIndexRefs(
            PsiElement declaration,
            PsiFile file,
            Predicate<PsiElement> refCarrierPredicate
    ) {
        if (declaration == null || file == null || !file.isValid()) return Collections.emptySet();

        String targetName = (declaration instanceof PsiNamedElement pne) ? pne.getName() : null;
        Set<PsiReference> out = new LinkedHashSet<>();

        PsiTreeUtil.processElements(file, el -> {
            ProgressManager.checkCanceled();
            if (!refCarrierPredicate.test(el)) return true;

            if (targetName != null) {
                String text = el.getText();
                if (text == null || !text.contains(targetName)) return true;
            }

            var refs = PsiReferenceService.getService().getReferences(el, PsiReferenceService.Hints.NO_HINTS);
            if (refs.isEmpty()) return true;

            for (PsiReference ref : refs) {
                PsiElement resolved = ref.resolve();
                if (resolved == declaration) {
                    out.add(ref);
                    continue;
                }
                if (ref instanceof PsiPolyVariantReference poly) {
                    for (ResolveResult rr : poly.multiResolve(false)) {
                        if (rr.getElement() == declaration) {
                            out.add(ref);
                            break;
                        }
                    }
                }
            }
            return true;
        });

        return out;
    }

    // Optional: wrappers for different consumers

    public static List<PsiReference> findUsagesInFileNoIndexList(PsiElement declaration, PsiFile file) {
        return new ArrayList<>(findUsagesInFileNoIndexRefs(declaration, file));
    }

    public static List<PsiElement> findUsageElementsInFileNoIndex(PsiElement declaration, PsiFile file) {
        return findUsagesInFileNoIndexRefs(declaration, file).stream()
                .map(PsiReference::getElement)
                .filter(Objects::nonNull)
                .filter(PsiElement::isValid)
                .distinct()
                .toList();
    }

    public static List<TextRange> findUsageRangesInFileNoIndex(PsiElement declaration, PsiFile file) {
        return findUsagesInFileNoIndexRefs(declaration, file).stream()
                .map(ref -> {
                    PsiElement el = ref.getElement();
                    TextRange base = el.getTextRange();
                    TextRange rin = ref.getRangeInElement();
                    return base != null ? rin.shiftRight(base.getStartOffset()) : el.getTextRange();
                })
                .toList();
    }



    public static List<PsiElement> resolveFromReferences(PsiElement e) {
        if (e == null) return Collections.emptyList();
        var elements = new ArrayList<PsiElement>();

        var refs = PsiReferenceService.getService().getReferences(e, PsiReferenceService.Hints.NO_HINTS);
        if (refs.isEmpty()) {
            return Collections.emptyList();
        }

        for (PsiReference ref : refs) {
            if (ref instanceof PsiPolyVariantReference poly) {
                var results = poly.multiResolve(false);
                for (var rr : results) {
                    var el = rr.getElement();
                    if (el != null) {
                        elements.add(el);
                    }
                }
            } else {
                var resolved = ref.resolve();
                if (resolved != null) {
                    elements.add(resolved);
                }
            }

        }

        System.out.println("elements = " + elements);

        return elements;
    }

    public static boolean isDeclarationSite(PsiElement e) {
        if (e == null) return false;
        var node = e.getNode();
        if (node == null) return false;
        var type = node.getElementType();
        // IDENTIFIER within CLIPSDefName under a construct = declaration
        if (type == CLIPSTypes.IDENTIFIER && e.getParent() instanceof CLIPSDefName) {
            var inFunctionCall = PsiTreeUtil.getParentOfType(e, CLIPSFunctionCall.class, false) != null;
            if (inFunctionCall) return false; // this is a usage (call), not a declaration
            // Declaration if inside one of the construct nodes
            var inRule = PsiTreeUtil.getParentOfType(e, CLIPSDefruleConstruct.class, false) != null;
            var inFunc = PsiTreeUtil.getParentOfType(e, CLIPSDeffunctionConstruct.class, false) != null;
            var inTemplate = PsiTreeUtil.getParentOfType(e, CLIPSDeftemplateConstruct.class, false) != null;
            return inRule || inFunc || inTemplate;
        }
        // Slot name inside slot definition is a declaration
        if (e.getParent() instanceof CLIPSSlotName) {
            var inSingle = PsiTreeUtil.getParentOfType(e, CLIPSSingleSlotDefinition.class, false) != null;
            var inMulti = PsiTreeUtil.getParentOfType(e, CLIPSMultislotDefinition.class, false) != null;
            if (inSingle || inMulti) return true;
        }

        return e instanceof CLIPSParameter;
    }

}
