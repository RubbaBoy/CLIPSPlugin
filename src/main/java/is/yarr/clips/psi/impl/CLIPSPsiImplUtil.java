package is.yarr.clips.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.util.IncorrectOperationException;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;
import static is.yarr.clips.psi.CLIPSTypes.*;

/**
 * Utility methods for working with PSI elements in the CLIPS language.
 * This class implements methods referenced in the BNF file.
 */
public class CLIPSPsiImplUtil {
    
    /**
     * Gets the name of a variable element.
     * Removes the '?' prefix from the variable name.
     */
    public static String getVariableName(PsiElement element) {
        String text = element.getText();
        if (text.startsWith("?")) {
            return text.substring(1); // Remove the '?' prefix
        }
        return text;
    }
    
    /**
     * Gets the name of a global variable.
     * Removes the '?*' prefix and '*' suffix from the global variable name.
     */
    public static String getGlobalVariableName(PsiElement element) {
        String text = element.getText();
        if (text.startsWith("?*") && text.endsWith("*")) {
            return text.substring(2, text.length() - 1); // Remove the '?*' prefix and '*' suffix
        }
        return text;
    }
    
    /**
     * Gets the name of a multifield variable.
     * Removes the '$?' prefix from the multifield variable name.
     */
    public static String getMultifieldVariableName(PsiElement element) {
        String text = element.getText();
        if (text.startsWith("$?")) {
            return text.substring(2); // Remove the '$?' prefix
        }
        return text;
    }
    
    
    /**
     * Gets the name of a variable element.
     * This method is referenced in the BNF file.
     */
    public static String getName(CLIPSVariableElement element) {
        return getVariableName(element);
    }
    
    /**
     * Sets the name of a variable element.
     * This method is referenced in the BNF file.
     */
    public static PsiElement setName(CLIPSVariableElement element, @NotNull String name) throws IncorrectOperationException {
        // This is a simplified implementation
        // In a real implementation, we would create a new variable element with the new name
        throw new IncorrectOperationException("Rename not implemented");
    }
    
    /**
     * Gets the name identifier of a variable element.
     * This method is referenced in the BNF file.
     */
    public static PsiElement getNameIdentifier(CLIPSVariableElement element) {
        return element;
    }
    
    /**
     * Gets the name of a global variable.
     * This method is referenced in the BNF file.
     */
    public static String getName(CLIPSGlobalVariableDef element) {
        ASTNode node = element.getNode();
        PsiElement globalVar = node.getPsi();
        return getGlobalVariableName(globalVar);
    }
    
    /**
     * Sets the name of a global variable.
     * This method is referenced in the BNF file.
     */
    public static PsiElement setName(CLIPSGlobalVariableDef element, @NotNull String name) throws IncorrectOperationException {
        // This is a simplified implementation
        throw new IncorrectOperationException("Rename not implemented");
    }
    
    /**
     * Gets the name identifier of a global variable.
     * This method is referenced in the BNF file.
     */
    public static PsiElement getNameIdentifier(CLIPSGlobalVariableDef element) {
        return element;
    }
    
    /**
     * Gets the name of a template name.
     * This method is referenced in the BNF file.
     */
    public static String getName(CLIPSTemplateName element) {
        return element.getText();
    }
    
    /**
     * Sets the name of a template name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement setName(CLIPSTemplateName element, @NotNull String name) throws IncorrectOperationException {
        // This is a simplified implementation
        throw new IncorrectOperationException("Rename not implemented");
    }
    
    /**
     * Gets the name identifier of a template name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement getNameIdentifier(CLIPSTemplateName element) {
        return element;
    }
    
    /**
     * Gets the name of a slot name.
     * This method is referenced in the BNF file.
     */
    public static String getName(CLIPSSlotName element) {
        return element.getText();
    }
    
    /**
     * Sets the name of a slot name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement setName(CLIPSSlotName element, @NotNull String name) throws IncorrectOperationException {
        // This is a simplified implementation
        throw new IncorrectOperationException("Rename not implemented");
    }
    
    /**
     * Gets the name identifier of a slot name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement getNameIdentifier(CLIPSSlotName element) {
        return element;
    }
    
    /**
     * Gets the name of a parameter.
     * This method is referenced in the BNF file.
     */
    public static String getName(CLIPSParameter element) {
        return getVariableName(element);
    }
    
    /**
     * Sets the name of a parameter.
     * This method is referenced in the BNF file.
     */
    public static PsiElement setName(CLIPSParameter element, @NotNull String name) throws IncorrectOperationException {
        // This is a simplified implementation
        throw new IncorrectOperationException("Rename not implemented");
    }
    
    /**
     * Gets the name identifier of a parameter.
     * This method is referenced in the BNF file.
     */
    public static PsiElement getNameIdentifier(CLIPSParameter element) {
        return element;
    }
    
    /**
     * Gets the name of a class name.
     * This method is referenced in the BNF file.
     */
    public static String getName(CLIPSClassName element) {
        return element.getText();
    }
    
    /**
     * Sets the name of a class name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement setName(CLIPSClassName element, @NotNull String name) throws IncorrectOperationException {
        // This is a simplified implementation
        throw new IncorrectOperationException("Rename not implemented");
    }
    
    /**
     * Gets the name identifier of a class name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement getNameIdentifier(CLIPSClassName element) {
        return element;
    }
    
    /**
     * Gets the name of a deffacts name.
     * This method is referenced in the BNF file.
     */
    public static String getName(CLIPSDeffactsName element) {
        return element.getText();
    }
    
    /**
     * Sets the name of a deffacts name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement setName(CLIPSDeffactsName element, @NotNull String name) throws IncorrectOperationException {
        // This is a simplified implementation
        throw new IncorrectOperationException("Rename not implemented");
    }
    
    /**
     * Gets the name identifier of a deffacts name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement getNameIdentifier(CLIPSDeffactsName element) {
        return element;
    }
    
    /**
     * Gets the name of a module name.
     * This method is referenced in the BNF file.
     */
    public static String getName(CLIPSModuleName element) {
        return element.getText();
    }
    
    /**
     * Sets the name of a module name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement setName(CLIPSModuleName element, @NotNull String name) throws IncorrectOperationException {
        // This is a simplified implementation
        throw new IncorrectOperationException("Rename not implemented");
    }
    
    /**
     * Gets the name identifier of a module name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement getNameIdentifier(CLIPSModuleName element) {
        return element;
    }
    
    /**
     * Gets the name of a rule name.
     * This method is referenced in the BNF file.
     */
    public static String getName(CLIPSRuleName element) {
        return element.getText();
    }
    
    /**
     * Sets the name of a rule name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement setName(CLIPSRuleName element, @NotNull String name) throws IncorrectOperationException {
        // This is a simplified implementation
        throw new IncorrectOperationException("Rename not implemented");
    }
    
    /**
     * Gets the name identifier of a rule name.
     * This method is referenced in the BNF file.
     */
    public static PsiElement getNameIdentifier(CLIPSRuleName element) {
        return element;
    }
    
    /**
     * Gets the name of a multifield variable element.
     * Removes the '$?' prefix from the multifield variable name.
     * This method is referenced in the BNF file.
     */
    public static String getName(CLIPSMultifieldVariableElement element) {
        PsiElement multifieldVar = element.getMultifieldVariable();
        return getMultifieldVariableName(multifieldVar);
    }
    
    /**
     * Sets the name of a multifield variable element.
     * This method is referenced in the BNF file.
     */
    public static PsiElement setName(CLIPSMultifieldVariableElement element, @NotNull String name) throws IncorrectOperationException {
        // This is a simplified implementation
        throw new IncorrectOperationException("Rename not implemented");
    }
    
    /**
     * Gets the name identifier of a multifield variable element.
     * This method is referenced in the BNF file.
     */
    public static PsiElement getNameIdentifier(CLIPSMultifieldVariableElement element) {
        return element.getMultifieldVariable();
    }
    
    // Methods for CLIPSMultifieldVariable and CLIPSMultifieldVar have been removed
    // as they are no longer needed after the regeneration of the parser and lexer classes.
}