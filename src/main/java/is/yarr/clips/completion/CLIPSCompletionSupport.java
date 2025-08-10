package is.yarr.clips.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.CLIPSReference;
import is.yarr.clips.psi.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Helper methods to build context-aware completion items.
 * This class tries to reuse CLIPSReference.getVariants() where possible to avoid duplication.
 */
public final class CLIPSCompletionSupport {
    private CLIPSCompletionSupport() {}

    /**
     * Collect dynamic variants using CLIPSReference for the given type.
     */
    public static List<LookupElement> collectDynamicVariants(PsiElement position, CLIPSReference.ReferenceType type) {
        var list = new ArrayList<LookupElement>();
        // CLIPSReference#getVariants returns Object[] of LookupElement
        var textLen = Math.max(1, position.getTextLength());
        var ref = new CLIPSReference(position, TextRange.from(0, textLen), "", type);
        for (var obj : ref.getVariants()) {
            if (obj instanceof LookupElement le) list.add(le);
        }
        return list;
    }

    /**
     * Is the caret at top-level (direct child of file)?
     */
    public static boolean isTopLevel(PsiElement position) {
        var parent = position.getParent();
        return parent instanceof CLIPSFile || (parent != null && parent.getParent() instanceof CLIPSFile);
    }

    /**
     * True if caret is at file top-level or inside a top-level parenthesized form, e.g. "( ... )" directly under file.
     * This enables showing constructs when typing inside parentheses at the file root.
     */
    public static boolean isTopLevelOrInTopLevelParens(PsiElement position) {
        if (isTopLevel(position)) return true;
        var call = PsiTreeUtil.getParentOfType(position, CLIPSFunctionCall.class, false);
        return call != null && call.getParent() instanceof CLIPSFile;
    }

    public static boolean insideDeftemplate(PsiElement position) {
        return PsiTreeUtil.getParentOfType(position, CLIPSDeftemplateConstruct.class, false) != null;
       }

    public static boolean insideDeffunction(PsiElement position) {
        return PsiTreeUtil.getParentOfType(position, CLIPSDeffunctionConstruct.class, false) != null;
    }

    public static boolean insideDeffacts(PsiElement position) {
        return PsiTreeUtil.getParentOfType(position, CLIPSDeffactsConstruct.class, false) != null;
    }

    public static boolean inFunctionCall(PsiElement position) {
        return PsiTreeUtil.getParentOfType(position, CLIPSFunctionCall.class, false) != null;
    }

    /**
     * PSI-aware LHS detection: inside a defrule and before the '=>' separator.
     */
    public static boolean inRuleLhs(PsiElement position) {
        var rule = PsiTreeUtil.getParentOfType(position, CLIPSDefruleConstruct.class, false);
        if (rule == null) return false;
        var sep = findRuleSeparator(rule);
        if (sep == null) return true; // incomplete code: assume still typing LHS before creating '=>'
        var caret = position.getTextOffset();
        return caret < sep.getTextRange().getStartOffset();
    }

    /**
     * PSI-aware RHS detection: inside a defrule and after the '=>' separator.
     */
    public static boolean inRhs(PsiElement position) {
        var rule = PsiTreeUtil.getParentOfType(position, CLIPSDefruleConstruct.class, false);
        if (rule == null) return false;
        var sep = findRuleSeparator(rule);
        if (sep == null) return false; // no separator yet
        var caret = position.getTextOffset();
        return caret > sep.getTextRange().getEndOffset();
    }

    /**
     * True when caret is on LHS within a (test ...) form. Allows expression functions but not RHS-only actions.
     */
    public static boolean inLhsTestExpression(PsiElement position) {
        if (!inRuleLhs(position)) return false;
        var call = PsiTreeUtil.getParentOfType(position, CLIPSFunctionCall.class, false);
        if (call == null) return false;
        var text = call.getText();
        return text != null && text.startsWith("(test");
    }

    /**
     * Find the '=>' separator leaf inside a defrule.
     */
    private static PsiElement findRuleSeparator(CLIPSDefruleConstruct rule) {
        for (var child = rule.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof PsiWhiteSpace) continue;
            var t = child.getText();
            if ("=>".equals(t)) return child;
            var found = findSeparatorDeep(child);
            if (found != null) return found;
        }
        return null;
    }

    private static PsiElement findSeparatorDeep(PsiElement node) {
        for (var child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof PsiWhiteSpace) continue;
            if ("=>".equals(child.getText())) return child;
            var found = findSeparatorDeep(child);
            if (found != null) return found;
        }
        return null;
    }

    public static LookupElementBuilder withVarPresentation(LookupElementBuilder base, boolean global) {
        return base.withTypeText(global ? "Global" : "Local", true);
    }

    public static Collection<LookupElement> builtinsFunctionsForRhs() {
        var items = new ArrayList<LookupElement>();
        for (var name : Builtins.CONTROL_FLOW_FUNCS) items.add(Builtins.function(name, "Control Flow"));
        for (var name : Builtins.IO_FUNCS) items.add(Builtins.function(name, "I/O"));
        for (var name : Builtins.FACT_FUNCS) items.add(Builtins.function(name, "Fact"));
        for (var name : Builtins.STRING_FUNCS) items.add(Builtins.function(name, "String"));
        for (var name : Builtins.MULTIFIELD_FUNCS) items.add(Builtins.function(name, "Multifield"));
        for (var name : Builtins.MATH_AND_COMPARE) items.add(Builtins.function(name, "Math/Compare"));
        for (var name : Builtins.CONSTANTS) items.add(LookupElementBuilder.create(name).withTypeText("Constant", true));
        return items;
    }
}
