package is.yarr.clips;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides real-time validation of CLIPS code.
 * This annotator focuses on validating template assertions to ensure they match template definitions.
 */
public class CLIPSAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        // Check for template assertions
        if (element instanceof CLIPSFunctionCall) {
            CLIPSFunctionCall functionCall = (CLIPSFunctionCall) element;
            
            // Check if this is an assert function call
            PsiElement firstChild = functionCall.getFirstChild();
            if (firstChild != null && "assert".equals(firstChild.getText())) {
                // This is an assert function call, check for template assertions
                validateTemplateAssertion(functionCall, holder);
            }
        }
    }
    
    /**
     * Validates a template assertion to ensure it matches the template definition.
     * 
     * @param assertCall The assert function call
     * @param holder The annotation holder
     */
    private void validateTemplateAssertion(CLIPSFunctionCall assertCall, @NotNull AnnotationHolder holder) {
        // Find the template assertion inside the assert call
        // The structure should be: (assert (template-name (slot1 value1) (slot2 value2) ...))
        PsiElement[] children = assertCall.getChildren();
        if (children.length < 2) {
            return; // Not enough children for a template assertion
        }
        
        // The second child should be the template assertion
        PsiElement templateAssertion = children[1];
        if (!(templateAssertion instanceof CLIPSFunctionCall)) {
            return; // Not a function call, so not a template assertion
        }
        
        // Get the template name
        CLIPSFunctionCall templateCall = (CLIPSFunctionCall) templateAssertion;
        PsiElement templateNameElement = templateCall.getFirstChild();
        if (templateNameElement == null) {
            return; // No template name
        }
        
        String templateName = templateNameElement.getText();
        
        // Find the template definition
        CLIPSDeftemplateConstruct templateDefinition = findTemplateDefinition(assertCall, templateName);
        if (templateDefinition == null) {
            // Template not found, report an error
            holder.newAnnotation(HighlightSeverity.ERROR, "Template '" + templateName + "' not defined")
                  .range(templateNameElement)
                  .create();
            return;
        }
        
        // Get the slots from the template definition
        Map<String, CLIPSSlotDefinition> definedSlots = getDefinedSlots(templateDefinition);
        
        // Get the slots from the template assertion
        Map<String, PsiElement> assertedSlots = getAssertedSlots(templateCall);
        
        // Check for missing required slots
        for (Map.Entry<String, CLIPSSlotDefinition> entry : definedSlots.entrySet()) {
            String slotName = entry.getKey();
            CLIPSSlotDefinition slotDefinition = entry.getValue();
            
            if (!assertedSlots.containsKey(slotName) && isRequiredSlot(slotDefinition)) {
                // Required slot is missing, report an error
                holder.newAnnotation(HighlightSeverity.ERROR, "Missing required slot '" + slotName + "'")
                      .range(templateCall)
                      .create();
            }
        }
        
        // Check for undefined slots
        for (Map.Entry<String, PsiElement> entry : assertedSlots.entrySet()) {
            String slotName = entry.getKey();
            PsiElement slotElement = entry.getValue();
            
            if (!definedSlots.containsKey(slotName)) {
                // Slot not defined in the template, report an error
                holder.newAnnotation(HighlightSeverity.ERROR, "Undefined slot '" + slotName + "' for template '" + templateName + "'")
                      .range(slotElement)
                      .create();
            }
        }
    }
    
    /**
     * Finds the template definition with the given name.
     * 
     * @param context The context element
     * @param templateName The name of the template to find
     * @return The template definition, or null if not found
     */
    private CLIPSDeftemplateConstruct findTemplateDefinition(PsiElement context, String templateName) {
        // Find all template definitions in the file
        var file = context.getContainingFile();
        var templates = PsiTreeUtil.findChildrenOfType(file, CLIPSDeftemplateConstruct.class);
        
        // Find the template with the matching name
        for (CLIPSDeftemplateConstruct template : templates) {
            CLIPSTemplateName name = PsiTreeUtil.findChildOfType(template, CLIPSTemplateName.class);
            if (name != null && templateName.equals(name.getText())) {
                return template;
            }
        }
        
        return null;
    }
    
    /**
     * Gets the slots defined in a template.
     * 
     * @param template The template definition
     * @return A map of slot names to slot definitions
     */
    private Map<String, CLIPSSlotDefinition> getDefinedSlots(CLIPSDeftemplateConstruct template) {
        Map<String, CLIPSSlotDefinition> slots = new HashMap<>();
        
        // Find all slot definitions in the template
        var slotDefinitions = PsiTreeUtil.findChildrenOfType(template, CLIPSSlotDefinition.class);
        
        // Add each slot to the map
        for (CLIPSSlotDefinition slot : slotDefinitions) {
            CLIPSSlotName name = PsiTreeUtil.findChildOfType(slot, CLIPSSlotName.class);
            if (name != null) {
                slots.put(name.getText(), slot);
            }
        }
        
        return slots;
    }
    
    /**
     * Gets the slots asserted in a template assertion.
     * 
     * @param templateCall The template assertion
     * @return A map of slot names to slot elements
     */
    private Map<String, PsiElement> getAssertedSlots(CLIPSFunctionCall templateCall) {
        Map<String, PsiElement> slots = new HashMap<>();
        
        // The children of the template call should be the template name followed by slot assertions
        PsiElement[] children = templateCall.getChildren();
        
        // Skip the first child (the template name)
        for (int i = 1; i < children.length; i++) {
            PsiElement child = children[i];
            
            // Each slot assertion should be a function call: (slot-name value)
            if (child instanceof CLIPSFunctionCall) {
                CLIPSFunctionCall slotCall = (CLIPSFunctionCall) child;
                PsiElement slotNameElement = slotCall.getFirstChild();
                
                if (slotNameElement != null) {
                    slots.put(slotNameElement.getText(), slotCall);
                }
            }
        }
        
        return slots;
    }
    
    /**
     * Checks if a slot is required (i.e., it has no default value).
     * 
     * @param slotDefinition The slot definition
     * @return true if the slot is required, false otherwise
     */
    private boolean isRequiredSlot(CLIPSSlotDefinition slotDefinition) {
        // A slot is required if it doesn't have a default value
        // This is a simplified implementation; in a real implementation, we would check for default attributes
        return PsiTreeUtil.findChildrenOfType(slotDefinition, CLIPSDefaultAttribute.class).isEmpty();
    }
}