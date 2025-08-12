package is.yarr.clips.navigation;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.CLIPSLanguage;
import is.yarr.clips.CLIPSReference;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Go To Declaration handler for CLIPS language.
 * Prefers existing PsiReferences on the leaf/parent; falls back to building a CLIPSReference
 * using the same trimming and context heuristics as the reference contributor.
 */
public final class CLIPSGotoDeclarationHandler implements GotoDeclarationHandler {

    @Override
    public @Nullable PsiElement[] getGotoDeclarationTargets(PsiElement sourceElement, int offset, Editor editor) {
        if (sourceElement == null) return PsiElement.EMPTY_ARRAY;
        var file = sourceElement.getContainingFile();
        if (file == null || file.getLanguage() != CLIPSLanguage.INSTANCE) return PsiElement.EMPTY_ARRAY;

        // Minimal debug: useful when diagnosing handler invocation.
        // System.out.println("[DEBUG_LOG] CLIPSGotoDeclarationHandler invoked at offset=" + offset + ", element=" + sourceElement);

        // Try leaf at caret
        var leaf = file.findElementAt(offset);
        if (leaf == null) leaf = sourceElement;

        // Avoid fallback if caret is on a declaration site (to prevent self-navigation)
        if (isDeclarationSite(leaf)) {
            System.out.println("faz EMPTY ---------------------------------------------------------------- 111");
            return PsiElement.EMPTY_ARRAY;
        }

        var targets = resolveFromReferences(leaf);
        if (targets.length > 0) {
            System.out.println("faz targets.length = " + targets.length);
            return filterPreferDeclarations(targets);
        }

        // Try parent
        var parent = leaf.getParent();
        if (parent != null) {
            targets = resolveFromReferences(parent);

            if (isDeclarationSite(parent)) {
                System.out.println("faz EMPTY ---------------------------------------------------------------- 222");
                return PsiElement.EMPTY_ARRAY;
            }

            if (targets.length > 0) return filterPreferDeclarations(targets);
        }

        System.out.println("faz NOT ---------------------------------------------------------------- here");

        // Fallback: synthesize a CLIPSReference from token/context
        var fallback = buildFallbackReference(leaf);
        if (fallback == null && parent != null) {
            fallback = buildFallbackReference(parent);
        }

        if (fallback != null) {
            var resolved = fallback.resolve();
            System.out.println("faz resolved = " + resolved);
            if (resolved != null) return new PsiElement[]{resolved};
            var extracted = extractElements(fallback.multiResolve(false));
            System.out.println("faz  EXTRACTED ---------------------------------------------------------------- " + extracted.length);
            return filterPreferDeclarations(extracted);
        }
        return PsiElement.EMPTY_ARRAY;
    }

    private static PsiElement[] resolveFromReferences(PsiElement element) {
        if (element == null) return PsiElement.EMPTY_ARRAY;
        var refs = element.getReferences();
        if (refs == null || refs.length == 0) return PsiElement.EMPTY_ARRAY;

        // First, try direct resolve for any reference
        for (var ref : refs) {
            var resolved = ref.resolve();
            if (resolved != null) return new PsiElement[]{resolved};
        }
        // Then, collect poly-variant results
        List<PsiElement> collected = new ArrayList<>();
        for (var ref : refs) {
            if (ref instanceof PsiPolyVariantReference poly) {
                for (var rr : poly.multiResolve(false)) {
                    var el = rr.getElement();
                    if (el != null) collected.add(el);
                }
            }
        }
        return collected.isEmpty() ? PsiElement.EMPTY_ARRAY : collected.toArray(PsiElement[]::new);
    }

    private static CLIPSReference buildFallbackReference(PsiElement e) {
        if (e == null) return null;
        var node = e.getNode();
        if (node == null) return null;
        var type = node.getElementType();
        var text = e.getText();

        // VARIABLE token
        if (type == CLIPSTypes.VARIABLE) {
            var name = text.startsWith("?") ? text.substring(1) : text;
            if (name.isEmpty()) return null;
            return new CLIPSReference(e, TextRange.from(0, e.getTextLength()), name, CLIPSReference.ReferenceType.VARIABLE);
        }
        // GLOBAL_VARIABLE token
        if (type == CLIPSTypes.GLOBAL_VARIABLE) {
            var start = text.startsWith("?*") ? 2 : 0;
            var end = text.endsWith("*") ? e.getTextLength() - 1 : e.getTextLength();
            var core = (start == 2 && end > start) ? text.substring(2, end) : text;
            if (core.isEmpty()) return null;
            return new CLIPSReference(e, new TextRange(start, end), core, CLIPSReference.ReferenceType.GLOBAL_VARIABLE);
        }
        // IDENTIFIER within contexts we care about
        if (type == CLIPSTypes.IDENTIFIER) {
            var parent = e.getParent();
            if (parent instanceof CLIPSDefName) {
                // Determine if this def name belongs to a rule definition (then we use RULE)
                var inRuleDef = PsiTreeUtil.getParentOfType(parent, CLIPSRuleName.class, false) != null;
                if (inRuleDef) {
                    return new CLIPSReference(e, TextRange.from(0, e.getTextLength()), text, CLIPSReference.ReferenceType.RULE);
                }
                // Otherwise distinguish between function name used in call vs template
                var inFunctionCall = PsiTreeUtil.getParentOfType(e, CLIPSFunctionCall.class, false) != null;
                var refType = inFunctionCall ? CLIPSReference.ReferenceType.FUNCTION : CLIPSReference.ReferenceType.TEMPLATE;
                return new CLIPSReference(e, TextRange.from(0, e.getTextLength()), text, refType);
            }
            if (parent instanceof CLIPSSlotName) {
                return new CLIPSReference(e, TextRange.from(0, e.getTextLength()), text, CLIPSReference.ReferenceType.SLOT);
            }
            // Some grammars may place multifield markers at leaf level; guard below handles composite too
        }
        // Composite elements for variables
        if (e instanceof CLIPSMultifieldVariableElement mv) {
            var name = mv.getName();
            if (name == null || name.isEmpty()) return null;
            var t = e.getText();
            var start = t.startsWith("$?") ? 2 : 0;
            return new CLIPSReference(e, new TextRange(start, e.getTextLength()), name, CLIPSReference.ReferenceType.VARIABLE);
        }
        if (e instanceof CLIPSVariableElement v) {
            var name = v.getName();
            if (name == null || name.isEmpty()) return null;
            return new CLIPSReference(e, TextRange.from(0, e.getTextLength()), name, CLIPSReference.ReferenceType.VARIABLE);
        }
        return null;
    }

    private static boolean isDeclarationSite(PsiElement e) {
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

    private static PsiElement[] filterPreferDeclarations(PsiElement[] targets) {
        if (targets == null || targets.length == 0) return PsiElement.EMPTY_ARRAY;
        List<PsiElement> variableDecls = new ArrayList<>();
        boolean hasVariableTargets = false;
        for (var t : targets) {
            if (t instanceof CLIPSParameter param) {
                return new PsiElement[]{param}; // Prefer parameters as they are always declarations
            } else if (t instanceof CLIPSVariableElement ve) {
                hasVariableTargets = true;
                if (isVariableDeclaration(ve)) variableDecls.add(t);
            } else if (t instanceof CLIPSMultifieldVariableElement mve) {
                hasVariableTargets = true;
                if (isMultifieldVariableDeclaration(mve)) variableDecls.add(t);
            }
        }
        if (hasVariableTargets && !variableDecls.isEmpty()) {
            return variableDecls.toArray(PsiElement[]::new);
        }
        return targets;
    }

    private static boolean isVariableDeclaration(CLIPSVariableElement variable) {
        var scope = findScope(variable);
        if (scope == null) return false;
        var variableName = variable.getName();
        if (variableName == null) return false;
        var variables = PsiTreeUtil.findChildrenOfType(scope, CLIPSVariableElement.class);
        var sorted = new ArrayList<>(variables);
        sorted.sort(java.util.Comparator.comparingInt(PsiElement::getTextOffset));
        for (var v : sorted) {
            var vName = v.getName();
            if (!variableName.equals(vName)) continue;
            // Special-case: (bind ?name ...)
            var parentCall = PsiTreeUtil.getParentOfType(v, CLIPSFunctionCall.class);
            if (parentCall != null) {
                var first = parentCall.getFirstChild();
                var second = first != null ? first.getNextSibling() : null;
                var third = second != null ? second.getNextSibling() : null;
                if (second != null && "bind".equals(second.getText())) {
                    if (third != null && third.getText().equals("?" + variableName)) {
                        return v.equals(variable);
                    }
                }
            }
            return v.equals(variable);
        }
        return false;
    }

    private static boolean isMultifieldVariableDeclaration(CLIPSMultifieldVariableElement variable) {
        var scope = findScope(variable);
        if (scope == null) return false;
        var name = variable.getName();
        if (name == null) return false;
        var variables = PsiTreeUtil.findChildrenOfType(scope, CLIPSMultifieldVariableElement.class);
        var sorted = new ArrayList<>(variables);
        sorted.sort(java.util.Comparator.comparingInt(PsiElement::getTextOffset));
        for (var v : sorted) {
            var vName = v.getName();
            if (!name.equals(vName)) continue;
            return v.equals(variable);
        }
        return false;
    }

    private static PsiElement findScope(PsiElement element) {
        return PsiTreeUtil.getParentOfType(element,
                CLIPSDefruleConstruct.class,
                CLIPSDeffunctionConstruct.class,
                CLIPSDeftemplateConstruct.class,
                CLIPSDefglobalConstruct.class,
                CLIPSDeffactsConstruct.class,
                CLIPSDefmoduleConstruct.class,
                CLIPSDefclassConstruct.class
        );
    }

    private static PsiElement[] extractElements(ResolveResult[] results) {
        if (results == null || results.length == 0) return PsiElement.EMPTY_ARRAY;
        List<PsiElement> list = new ArrayList<>(results.length);
        for (var rr : results) {
            var el = rr.getElement();
            if (el != null) list.add(el);
        }
        return list.isEmpty() ? PsiElement.EMPTY_ARRAY : list.toArray(PsiElement[]::new);
    }
}
