package is.yarr.clips.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import is.yarr.clips.psi.impl.CLIPSVariableElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Reference implementation for CLIPS variables.
 * This class handles resolving references to variable declarations.
 */
public class CLIPSReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
    private final String variableName;

    public CLIPSReference(@NotNull CLIPSVariableElementImpl element, TextRange rangeInElement) {
        super(element, rangeInElement);
        variableName = element.getName();
    }

    /**
     * Resolves the reference to all matching variable declarations.
     * 
     * @return ResolveResult array with all matching declarations
     */
    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        // Get the file containing this reference
        PsiFile file = myElement.getContainingFile();
        System.out.println("[DEBUG_LOG] Resolving references for variable: " + variableName + " in file: " + file.getName());
        
        // Find all variable declarations in the file
        List<ResolveResult> results = new ArrayList<>();
        findVariableDeclarations(file, results);
        
        System.out.println("[DEBUG_LOG] Found " + results.size() + " declarations for variable: " + variableName);
        return results.toArray(new ResolveResult[0]);
    }

    /**
     * Resolves the reference to a single variable declaration.
     * Returns the first matching declaration found.
     * 
     * @return The first matching variable declaration, or null if none found
     */
    @Nullable
    @Override
    public PsiElement resolve() {
        ResolveResult[] resolveResults = multiResolve(false);
        return resolveResults.length > 0 ? resolveResults[0].getElement() : null;
    }

    /**
     * Gets all possible variable declarations that could match this reference.
     * 
     * @return Array of variable names that could match
     */
    @Override
    public Object @NotNull [] getVariants() {
        // For now, we don't provide completion variants
        return new Object[0];
    }

    /**
     * Handles renaming the variable.
     * 
     * @param newElementName The new name for the variable
     * @return The element with the updated name
     * @throws IncorrectOperationException If renaming is not supported
     */
    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        // For now, we don't support renaming
        return myElement;
    }

    /**
     * Recursively finds all variable declarations in the file that match this reference.
     * 
     * @param element The current element to check
     * @param results List to collect matching declarations
     */
    private void findVariableDeclarations(PsiElement element, List<ResolveResult> results) {
        // Check if this element is a variable declaration
        if (isVariableDeclaration(element)) {
            CLIPSNamedElement namedElement = (CLIPSNamedElement) element;
            String elementName = namedElement.getName();
            System.out.println("[DEBUG_LOG] Checking variable declaration: " + element.getText() + " with name: " + elementName);
            
            if (variableName.equals(elementName)) {
                results.add(new PsiElementResolveResult(element));
                System.out.println("[DEBUG_LOG] Added variable declaration to results: " + element.getText());
            }
        }
        
        // Recursively check all children
        for (PsiElement child : element.getChildren()) {
            findVariableDeclarations(child, results);
        }
    }

    /**
     * Checks if the given element is a variable declaration.
     * 
     * @param element The element to check
     * @return True if the element is a variable declaration, false otherwise
     */
    private boolean isVariableDeclaration(PsiElement element) {
        // In CLIPS, variables can be declared in several contexts:
        // 1. Function parameters: (deffunction calculate-grade (?score) ...)
        // 2. Pattern binding: ?s <- (student (id ?id) ...)
        // 3. Explicit binding: (bind ?x 10)
        
        // For now, we'll consider any variable as both a declaration and a reference
        // This simplifies the implementation but means we'll highlight all occurrences
        boolean isNamedElement = element instanceof CLIPSNamedElement;
        
        if (isNamedElement) {
            System.out.println("[DEBUG_LOG] Found variable declaration: " + element.getText() + " - " + element.getClass().getSimpleName());
        }
        
        return isNamedElement;
    }
}