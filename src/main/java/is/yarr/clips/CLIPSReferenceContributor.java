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
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSVariableElement.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    System.out.println("[DEBUG_LOG] Variable reference provider's getReferencesByElement called for: " + element);
                    
                    CLIPSVariableElement variable = (CLIPSVariableElement) element;
                    String name = variable.getName();
                    System.out.println("[DEBUG_LOG] Variable name: " + name);
                    
                    if (name == null) {
                        System.out.println("[DEBUG_LOG] Variable name is null, returning empty array");
                        return PsiReference.EMPTY_ARRAY;
                    }
                    
                    // Create a reference for the variable
                    // The text range should be relative to the element (0-based)
                    TextRange range = new TextRange(0, element.getTextLength());
                    System.out.println("[DEBUG_LOG] Creating reference for variable: " + element + ", range: " + range + ", name: " + name);
                    
                    return new PsiReference[]{
                        new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.VARIABLE)
                    };
                }
            }
        );
        
        // Register reference provider for global variables
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSGlobalVariableDef.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    CLIPSGlobalVariableDef global = (CLIPSGlobalVariableDef) element;
                    String name = global.getName();
                    if (name == null) {
                        return PsiReference.EMPTY_ARRAY;
                    }
                    
                    // Create a reference for the global variable
                    TextRange range = new TextRange(0, element.getTextLength());
                    return new PsiReference[]{
                        new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.GLOBAL_VARIABLE)
                    };
                }
            }
        );
        
        // Register reference provider for template names
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSTemplateName.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    CLIPSTemplateName template = (CLIPSTemplateName) element;
                    String name = template.getName();
                    if (name == null) {
                        return PsiReference.EMPTY_ARRAY;
                    }
                    
                    // Create a reference for the template name
                    TextRange range = new TextRange(0, element.getTextLength());
                    return new PsiReference[]{
                        new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.TEMPLATE)
                    };
                }
            }
        );
        
        // Register reference provider for rule names
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSRuleName.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    CLIPSRuleName rule = (CLIPSRuleName) element;
                    String name = rule.getName();
                    if (name == null) {
                        return PsiReference.EMPTY_ARRAY;
                    }
                    
                    // Create a reference for the rule name
                    TextRange range = new TextRange(0, element.getTextLength());
                    return new PsiReference[]{
                        new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.RULE)
                    };
                }
            }
        );
        
        // Register reference provider for slot names
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSSlotName.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    CLIPSSlotName slot = (CLIPSSlotName) element;
                    String name = slot.getName();
                    if (name == null) {
                        return PsiReference.EMPTY_ARRAY;
                    }
                    
                    // Create a reference for the slot name
                    TextRange range = new TextRange(0, element.getTextLength());
                    return new PsiReference[]{
                        new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.SLOT)
                    };
                }
            }
        );
    }
}