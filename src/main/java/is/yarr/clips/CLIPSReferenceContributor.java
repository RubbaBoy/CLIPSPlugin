package is.yarr.clips;

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceContributor;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.PsiReferenceRegistrar;
import com.intellij.util.ProcessingContext;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;

/**
 * Registers reference providers for CLIPS elements.
 * This enables features like "Go to Declaration" and "Find Usages".
 */
public class CLIPSReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        System.out.println("[DEBUG_LOG] CLIPSReferenceContributor.registerReferenceProviders called");
        
        // Register reference provider for variables
        System.out.println("[DEBUG_LOG] Registering reference provider for CLIPSVariableElement");
        // Register reference provider for specific PSI element types
        System.out.println("[DEBUG_LOG] Registering reference provider with specific patterns");
        
        // Create patterns for each element type we want to provide references for
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSVariableElement.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    System.out.println("[DEBUG_LOG] Variable reference provider's getReferencesByElement called for: " + element);
                    System.out.println("[DEBUG_LOG] Element class: " + element.getClass().getName());
                    System.out.println("[DEBUG_LOG] Element text: " + element.getText());
                    System.out.println("[DEBUG_LOG] Element language: " + element.getLanguage());
                    System.out.println("[DEBUG_LOG] Element parent: " + element.getParent());
                    System.out.println("[DEBUG_LOG] Element parent class: " + (element.getParent() != null ? element.getParent().getClass().getName() : "null"));
                    
                    if (element instanceof CLIPSVariableElement) {
                        CLIPSVariableElement variable = (CLIPSVariableElement) element;
                        String name = variable.getName();
                        System.out.println("[DEBUG_LOG] Variable name: " + name);
                        
                        if (name == null) {
                            System.out.println("[DEBUG_LOG] Variable name is null, returning empty array");
                            return PsiReference.EMPTY_ARRAY;
                        }
                        
                        // For variables, we need to exclude the '?' prefix from the range
                        String text = element.getText();
                        int startOffset = text.startsWith("?") ? 1 : 0;
                        TextRange range = new TextRange(startOffset, element.getTextLength());
                        System.out.println("[DEBUG_LOG] Creating reference for variable: " + element + ", range: " + range + ", name: " + name);
                        
                        CLIPSReference reference = new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.VARIABLE);
                        System.out.println("[DEBUG_LOG] Created variable reference: " + reference);
                        
                        return new PsiReference[]{reference};
                    }
                    
                    System.out.println("[DEBUG_LOG] Not a variable, returning empty array");
                    return PsiReference.EMPTY_ARRAY;
                }
            }
        );
        
        // Register reference provider for multifield variables
        System.out.println("[DEBUG_LOG] Registering reference provider for CLIPSMultifieldVariableElement");
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSMultifieldVariableElement.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    System.out.println("[DEBUG_LOG] Multifield variable reference provider's getReferencesByElement called for: " + element);
                    System.out.println("[DEBUG_LOG] Element class: " + element.getClass().getName());
                    System.out.println("[DEBUG_LOG] Element text: " + element.getText());
                    
                    if (element instanceof CLIPSMultifieldVariableElement) {
                        CLIPSMultifieldVariableElement variable = (CLIPSMultifieldVariableElement) element;
                        String name = variable.getName();
                        System.out.println("[DEBUG_LOG] Multifield variable name: " + name);
                        
                        if (name == null) {
                            System.out.println("[DEBUG_LOG] Multifield variable name is null, returning empty array");
                            return PsiReference.EMPTY_ARRAY;
                        }
                        
                        // For multifield variables, we need to exclude the '$?' prefix from the range
                        String text = element.getText();
                        int startOffset = text.startsWith("$?") ? 2 : 0;
                        TextRange range = new TextRange(startOffset, element.getTextLength());
                        System.out.println("[DEBUG_LOG] Creating reference for multifield variable: " + element + ", range: " + range + ", name: " + name);
                        
                        CLIPSReference reference = new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.VARIABLE);
                        System.out.println("[DEBUG_LOG] Created multifield reference: " + reference);
                        
                        return new PsiReference[]{reference};
                    }
                    
                    System.out.println("[DEBUG_LOG] Not a multifield variable, returning empty array");
                    return PsiReference.EMPTY_ARRAY;
                }
            }
        );
        
        // Register reference provider for VARIABLE token
        System.out.println("[DEBUG_LOG] Registering reference provider for VARIABLE token");
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(CLIPSTypes.VARIABLE),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    System.out.println("[DEBUG_LOG] VARIABLE token reference provider's getReferencesByElement called for: " + element);
                    System.out.println("[DEBUG_LOG] Element class: " + element.getClass().getName());
                    System.out.println("[DEBUG_LOG] Element text: " + element.getText());
                    System.out.println("[DEBUG_LOG] Element language: " + element.getLanguage());
                    System.out.println("[DEBUG_LOG] Element parent: " + element.getParent());
                    System.out.println("[DEBUG_LOG] Element parent class: " + (element.getParent() != null ? element.getParent().getClass().getName() : "null"));
                    
                    // For VARIABLE token, we need to extract the name (without the '?' prefix)
                    String text = element.getText();
                    String name = text.startsWith("?") ? text.substring(1) : text;
                    System.out.println("[DEBUG_LOG] VARIABLE token name: " + name);
                    
                    if (name.isEmpty()) {
                        System.out.println("[DEBUG_LOG] VARIABLE token name is empty, returning empty array");
                        return PsiReference.EMPTY_ARRAY;
                    }
                    
                    // For variables, we need to exclude the '?' prefix from the range
                    int startOffset = text.startsWith("?") ? 1 : 0;
                    TextRange range = new TextRange(startOffset, element.getTextLength());
                    System.out.println("[DEBUG_LOG] Creating reference for VARIABLE token: " + element + ", range: " + range + ", name: " + name);
                    
                    CLIPSReference reference = new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.VARIABLE);
                    System.out.println("[DEBUG_LOG] Created VARIABLE token reference: " + reference);
                    
                    return new PsiReference[]{reference};
                }
            }
        );
        
        // Register reference provider for parameters
        System.out.println("[DEBUG_LOG] Registering reference provider for CLIPSParameter");
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSParameter.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    System.out.println("[DEBUG_LOG] Parameter reference provider's getReferencesByElement called for: " + element);
                    System.out.println("[DEBUG_LOG] Element class: " + element.getClass().getName());
                    System.out.println("[DEBUG_LOG] Element text: " + element.getText());
                    
                    if (element instanceof CLIPSParameter) {
                        CLIPSParameter parameter = (CLIPSParameter) element;
                        String name = parameter.getName();
                        System.out.println("[DEBUG_LOG] Parameter name: " + name);
                        
                        if (name == null) {
                            System.out.println("[DEBUG_LOG] Parameter name is null, returning empty array");
                            return PsiReference.EMPTY_ARRAY;
                        }
                        
                        // For parameters, we need to exclude the '?' prefix from the range
                        String text = element.getText();
                        int startOffset = text.startsWith("?") ? 1 : 0;
                        TextRange range = new TextRange(startOffset, element.getTextLength());
                        System.out.println("[DEBUG_LOG] Creating reference for parameter: " + element + ", range: " + range + ", name: " + name);
                        
                        CLIPSReference reference = new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.VARIABLE);
                        System.out.println("[DEBUG_LOG] Created parameter reference: " + reference);
                        
                        return new PsiReference[]{reference};
                    }
                    
                    System.out.println("[DEBUG_LOG] Not a parameter, returning empty array");
                    return PsiReference.EMPTY_ARRAY;
                }
            }
        );
    }
}