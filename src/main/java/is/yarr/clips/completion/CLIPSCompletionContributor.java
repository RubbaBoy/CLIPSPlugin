package is.yarr.clips.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import is.yarr.clips.CLIPSLanguage;
import is.yarr.clips.CLIPSReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static is.yarr.clips.completion.CLIPSCompletionSupport.*;

/**
 * Completion contributor for the CLIPS language.
 * Provides contextual suggestions using PSI and built-ins.
 */
public class CLIPSCompletionContributor extends CompletionContributor {

    public CLIPSCompletionContributor() {
        extend(CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(CLIPSLanguage.INSTANCE),
            new CompletionProvider<>() {
                @Override
                protected void addCompletions(@NotNull CompletionParameters parameters,
                                              @NotNull ProcessingContext context,
                                              @NotNull CompletionResultSet result) {
                    var position = parameters.getPosition();
                    if (position instanceof com.intellij.psi.PsiComment || position.getParent() instanceof com.intellij.psi.PsiComment) {
                        return; // suppress in comments
                    }
                    var items = new ArrayList<LookupElement>();

                    // Collect project-defined function names
                    var projectFunctionItems = collectDynamicVariants(position, CLIPSReference.ReferenceType.FUNCTION);
                    var projectFunctionNames = new HashSet<String>();
                    for (var le : projectFunctionItems) projectFunctionNames.add(le.getLookupString());

                    // 1) Top-level or inside top-level parentheses: constructs only
                    if (isTopLevelOrInTopLevelParens(position)) {
                        // Attach special insert handler for constructs to optionally wrap with () at true top-level
                        for (var b : Builtins.CONSTRUCTS) {
                            result.addElement(b.withInsertHandler(constructInsertHandler()));
                        }
                        return;
                    }

                    // 2) Deftemplate context: slot keywords
                    if (insideDeftemplate(position)) {
                        items.addAll(Builtins.SLOT_KEYWORDS);
                    }

                    // 3) Deffacts: suggest template names at pattern heads
                    if (insideDeffacts(position)) {
                        items.addAll(collectDynamicVariants(position, CLIPSReference.ReferenceType.TEMPLATE));
                    }

                    // 4) Rule RHS or deffunction body: functions + constants
                    if (inRhs(position) || insideDeffunction(position)) {
                        items.addAll(CLIPSCompletionSupport.builtinsFunctionsForRhs());
                    }

                    // 5) LHS keywords and template heads
                    if (inRuleLhs(position)) {
                        items.addAll(Builtins.LHS_LOGICAL);
                        items.addAll(collectDynamicVariants(position, CLIPSReference.ReferenceType.TEMPLATE));
                        // In (test ...) allow expression functions but avoid RHS-only fact ops
                        if (inLhsTestExpression(position)) {
                            var exprFuncs = new ArrayList<LookupElement>(CLIPSCompletionSupport.builtinsFunctionsForRhs());
                            exprFuncs.removeIf(le -> Builtins.FACT_FUNCS.contains(le.getLookupString()));
                            items.addAll(exprFuncs);
                        }
                    }

                    // 6) Dynamic PSI-backed suggestions
                    var doc = parameters.getEditor().getDocument();
                    var off = parameters.getOffset();
                    var justTypedQuestion = off > 0 && doc.getCharsSequence().charAt(off - 1) == '?';
                    if (justTypedQuestion) {
                        items.addAll(collectDynamicVariants(position, CLIPSReference.ReferenceType.VARIABLE));
                    } else {
                        items.addAll(collectDynamicVariants(position, CLIPSReference.ReferenceType.VARIABLE));
                        items.addAll(collectDynamicVariants(position, CLIPSReference.ReferenceType.GLOBAL_VARIABLE));
                        items.addAll(projectFunctionItems);
                        items.addAll(collectDynamicVariants(position, CLIPSReference.ReferenceType.RULE));
                        items.addAll(collectDynamicVariants(position, CLIPSReference.ReferenceType.SLOT));
                    }

                    removeDuplicates(items);

                    // Present
                    addAll(result, items, projectFunctionNames);

                    result.runRemainingContributors(parameters, completionResult -> result.passResult(completionResult));
                }
            }
        );
    }

    private void removeDuplicates(List<LookupElement> items) {
        var seen = new java.util.HashSet<String>();
        items.removeIf(le -> {
            var name = le.getLookupString();
            if (seen.contains(name)) return true;
            seen.add(name);
            return false;
        });
    }

    private static void addAll(CompletionResultSet result, List<LookupElement> items, java.util.Set<String> projectFunctionNames) {
        for (var le : items) {
            if (le instanceof LookupElementBuilder b) {
                var name = b.getLookupString();
                if (isFunctionLikeName(name) || projectFunctionNames.contains(name)) {
                    result.addElement(b.withInsertHandler(spaceAfterNameHandler()));
                    continue;
                }
            }
            result.addElement(le);
        }
    }

    private static boolean isFunctionLikeName(String name) {
        return Builtins.FUNCTION_PARAMS.containsKey(name)
            || Builtins.CONTROL_FLOW_FUNCS.contains(name)
            || Builtins.IO_FUNCS.contains(name)
            || Builtins.FACT_FUNCS.contains(name)
            || Builtins.STRING_FUNCS.contains(name)
            || Builtins.MULTIFIELD_FUNCS.contains(name)
            || Builtins.MATH_AND_COMPARE.contains(name);
    }

    private static InsertHandler<LookupElement> spaceAfterNameHandler() {
        return (context, item) -> {
            var doc = context.getDocument();
            var tail = context.getTailOffset();
            doc.insertString(tail, " ");
            context.getEditor().getCaretModel().moveToOffset(tail + 1);
        };
    }

    private static InsertHandler<LookupElement> constructInsertHandler() {
        return (context, item) -> {
            var editor = context.getEditor();
            var doc = context.getDocument();
            var file = context.getFile();
            var name = item.getLookupString();

            var start = context.getStartOffset();
            var tail = context.getTailOffset();
            var chars = doc.getCharsSequence();

            // Determine if we're already inside a top-level paren: previous non-space is '('
            int prev = start - 1;
            while (prev >= 0 && Character.isWhitespace(chars.charAt(prev))) prev--;
            var insideParen = prev >= 0 && chars.charAt(prev) == '(';

            if (insideParen) {
                // Already inside parentheses: ensure a space after the name
                if (tail < chars.length() && chars.charAt(tail) != ' ') {
                    doc.insertString(tail, " ");
                }
                editor.getCaretModel().moveToOffset(tail + (tail < chars.length() && chars.charAt(tail) == ' ' ? 1 : 1));
                return;
            }

            // Not inside parens: wrap in (name ) and place caret before the ')'. Avoid duplicating an existing ')'.
            int next = tail;
            while (next < chars.length() && Character.isWhitespace(chars.charAt(next))) next++;
            var nextIsClosing = next < chars.length() && chars.charAt(next) == ')';

            var prefix = "(" + name + " ";
            var suffix = nextIsClosing ? "" : ")";

            doc.replaceString(start, tail, prefix + suffix);
            // Caret just after the space: before optional ')'
            editor.getCaretModel().moveToOffset(start + prefix.length());
        };
    }
}
