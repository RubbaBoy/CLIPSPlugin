package is.yarr.clips;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides detailed validation of template slots through the inspection framework.
 * This inspection checks for issues with template assertions, such as missing required slots,
 * undefined slots, and type mismatches.
 */
public class CLIPSTemplateSlotInspection extends LocalInspectionTool {
    @NotNull
    @Override
    public String getDisplayName() {
        return "CLIPS template slot validation";
    }

    @NotNull
    @Override
    public String getGroupDisplayName() {
        return "CLIPS";
    }

    @NotNull
    @Override
    public String getShortName() {
        return "CLIPSTemplateSlot";
    }

    @Override
    public boolean isEnabledByDefault() {
        return true;
    }

    @NotNull
    @Override
    public PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly) {
        return new CLIPSElementVisitor() {
            @Override
            public void visitFunctionCall(CLIPSFunctionCall functionCall) {
                // Check if this is an assert function call
                PsiElement firstChild = functionCall.getFirstChild();
                if (firstChild != null && "assert".equals(firstChild.getText())) {
                    // This is an assert function call, check for template assertions
                    validateTemplateAssertion(functionCall, holder);
                }
            }
        };
    }
    
    /**
     * Validates a template assertion to ensure it matches the template definition.
     * 
     * @param assertCall The assert function call
     * @param holder The problems holder
     */
    private void validateTemplateAssertion(CLIPSFunctionCall assertCall, @NotNull ProblemsHolder holder) {
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
            holder.registerProblem(templateNameElement, "Template '" + templateName + "' not defined");
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
                holder.registerProblem(templateCall, "Missing required slot '" + slotName + "'");
            }
        }
        
        // Check for undefined slots
        for (Map.Entry<String, PsiElement> entry : assertedSlots.entrySet()) {
            String slotName = entry.getKey();
            PsiElement slotElement = entry.getValue();
            
            if (!definedSlots.containsKey(slotName)) {
                // Slot not defined in the template, report an error
                holder.registerProblem(slotElement, "Undefined slot '" + slotName + "' for template '" + templateName + "'");
            } else {
                // Slot is defined, check the value type
                CLIPSSlotDefinition slotDefinition = definedSlots.get(slotName);
                validateSlotValue(slotElement, slotDefinition, holder);
            }
        }
    }
    
    /**
     * Validates the value of a slot to ensure it matches the slot's type constraint.
     * 
     * @param slotElement The slot element
     * @param slotDefinition The slot definition
     * @param holder The problems holder
     */
    private void validateSlotValue(PsiElement slotElement, CLIPSSlotDefinition slotDefinition, @NotNull ProblemsHolder holder) {
        // This is a simplified implementation; in a real implementation, we would check the value against the slot's type constraint
        // For now, we'll just check if the slot has a type constraint
        var typeConstraints = PsiTreeUtil.findChildrenOfType(slotDefinition, CLIPSTypeConstraint.class);
        if (!typeConstraints.isEmpty()) {
            // The slot has a type constraint, check the value
            CLIPSTypeConstraint typeConstraint = typeConstraints.iterator().next();
            
            // Get the slot value
            PsiElement[] slotChildren = ((CLIPSFunctionCall) slotElement).getChildren();
            if (slotChildren.length < 2) {
                return; // No value
            }
            
            PsiElement slotValue = slotChildren[1];
            
            // Check if the value matches the type constraint
            // This is a simplified implementation; in a real implementation, we would check the value against all type specifiers
            var typeSpecifiers = PsiTreeUtil.findChildrenOfType(typeConstraint, CLIPSTypeSpecifier.class);
            if (!typeSpecifiers.isEmpty()) {
                CLIPSTypeSpecifier typeSpecifier = typeSpecifiers.iterator().next();
                String typeName = typeSpecifier.getText();
                
                // Check if the value matches the type
                if (!valueMatchesType(slotValue, typeName)) {
                    // Value doesn't match the type, report an error
                    holder.registerProblem(slotValue, "Invalid value type for slot. Expected: " + typeName);
                }
            }
        }
    }
    
    /**
     * Checks if a value matches a type.
     * 
     * @param value The value element
     * @param typeName The name of the type
     * @return true if the value matches the type, false otherwise
     */
    private boolean valueMatchesType(PsiElement value, String typeName) {
        // This is a simplified implementation; in a real implementation, we would check the value against the type more thoroughly
        switch (typeName.toUpperCase()) {
            case "STRING":
                return value instanceof CLIPSConstant && value.getText().startsWith("\"");
            case "INTEGER":
                return value instanceof CLIPSConstant && value.getText().matches("-?\\d+");
            case "FLOAT":
                return value instanceof CLIPSConstant && value.getText().matches("-?\\d+\\.\\d+");
            case "SYMBOL":
                return value instanceof CLIPSConstant && !value.getText().startsWith("\"") && !value.getText().matches("-?\\d+(\\.\\d+)?");
            default:
                return true; // Unknown type, assume it matches
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
    
    /**
     * A visitor for CLIPS elements.
     */
    private static class CLIPSElementVisitor extends PsiElementVisitor {
        public void visitFunctionCall(CLIPSFunctionCall functionCall) {
            visitElement(functionCall);
        }
    }
}