package is.yarr.clips.annotators;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.CLIPSSyntaxHighlighter;
import is.yarr.clips.cache.CLIPSTemplateCache;
import is.yarr.clips.psi.CLIPSDefName;
import is.yarr.clips.psi.CLIPSDeffunctionName;
import is.yarr.clips.psi.CLIPSExpression;
import is.yarr.clips.psi.CLIPSFunctionCall;
import is.yarr.clips.psi.CLIPSSlotName;
import is.yarr.clips.psi.CLIPSTemplateName;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Annotates call-head identifiers in CLIPS s-expressions with semantic colors and validates
 * template calls against a cached template model (name -> slots).
 *
 * - Built-in functions are lexically colored and skipped.
 * - User-defined function calls get CLIPS_USER_FUNCTION_CALL.
 * - Template calls get CLIPS_TEMPLATE_CALL and their slots are validated using a cache.
 */
public class CLIPSCallHeadAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        // Color actual declaration names directly (definition site)
        var definition = classifyDefinition(element);
        if (definition != null) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(element.getTextRange())
                    .textAttributes(definition)
                    .create();
            return;
        }

        if (!(element instanceof CLIPSFunctionCall call)) return;

        // Skip builtin calls; lexer already colors them
        if (call.getBuiltinFunction() != null) return;

        var defName = call.getDefName();
        if (defName == null) return;

        var head = findHeadIdentifier(defName);
        if (head == null) return;

        // Is this a template call?
        boolean isTemplate = isTemplateCall(head);
        if (isTemplate) {
            // Color the head as a template call
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(head.getTextRange())
                    .textAttributes(CLIPSSyntaxHighlighter.TEMPLATE_CALL)
                    .create();

            // Validate slots using a cached model
            validateTemplateCallSlots(call, head.getText(), holder);
            return;
        }

        // Otherwise, treat as user function call (color only)
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(head.getTextRange())
                .textAttributes(CLIPSSyntaxHighlighter.USER_FUNCTION_CALL)
                .create();
    }

    private static PsiElement findHeadIdentifier(@NotNull CLIPSDefName defName) {
        return defName.getFirstChild();
    }

    private static @Nullable TextAttributesKey classifyDefinition(@NotNull PsiElement headIdentifier) {
        if (headIdentifier instanceof CLIPSDeffunctionName) {
            return CLIPSSyntaxHighlighter.USER_FUNCTION_CALL;
        } else if (headIdentifier instanceof CLIPSTemplateName) {
            return CLIPSSyntaxHighlighter.TEMPLATE_CALL;
        }
        return null;
    }

    private static boolean isTemplateCall(@NotNull PsiElement headIdentifier) {
        // If head resolves to a template name, treat as template call.
        // Keep this lightweight (the cache lookup below will do the heavy lifting).
        return headIdentifier instanceof CLIPSTemplateName
                || looksLikeTemplateByName(headIdentifier.getText(), headIdentifier);
    }

    private static boolean looksLikeTemplateByName(@NotNull String name, @NotNull PsiElement ctx) {
        // Fast path: consult cached map to see if a template of this name exists in the file.
        var file = ctx.getContainingFile();
        if (file == null) return false;
        Map<String, CLIPSTemplateCache.TemplateInfo> map = CLIPSTemplateCache.getTemplates(file);
        return map.containsKey(name);
    }

    private static void validateTemplateCallSlots(@NotNull CLIPSFunctionCall call,
                                                  @NotNull String templateName,
                                                  @NotNull AnnotationHolder holder) {
        var file = call.getContainingFile();
        if (file == null) return;

        Map<String, CLIPSTemplateCache.TemplateInfo> templates = CLIPSTemplateCache.getTemplates(file);
        var template = templates.get(templateName);
        if (template == null) {
            // Unknown template; optionally mark the head elsewhere
            return;
        }

        // Walk children s-expressions like: (templateName (slot1 ...) (slot2 ...))
        for (CLIPSExpression expr : call.getExpressionList()) {
            // The first expression after head often is the first slot call: (slotName value)
            var slotCall = PsiTreeUtil.getChildOfType(expr, CLIPSFunctionCall.class);
            if (slotCall == null) continue;

            var slotDefName = slotCall.getDefName();
            if (slotDefName == null) continue;

            var slotHead = slotDefName.getFirstChild();
            if (!(slotHead instanceof CLIPSSlotName) && slotHead != null) {
                // If grammar wraps differently, still get text
                highlightUnknownSlotIfAny(slotHead, template, holder);
                continue;
            }

            if (slotHead == null) continue;
            String slotName = slotHead.getText();
            var info = template.slots().get(slotName);
            if (info == null) {
                // Undefined slot for this template
                holder.newAnnotation(HighlightSeverity.ERROR,
                                "Undefined slot '" + slotName + "' for template '" + templateName + "'")
                        .range(slotHead.getTextRange())
                        .create();
                continue;
            }

            // Let user know what the type is?
        }

        // Check for missing required slots?
    }

    private static void highlightUnknownSlotIfAny(@NotNull PsiElement slotHead,
                                                  @NotNull CLIPSTemplateCache.TemplateInfo template,
                                                  @NotNull AnnotationHolder holder) {
        String name = slotHead.getText();
        if (!template.slots().containsKey(name)) {
            holder.newAnnotation(HighlightSeverity.ERROR,
                            "Undefined slot '" + name + "' for template '" + template.name() + "'")
                    .range(slotHead.getTextRange())
                    .create();
        }
    }
}