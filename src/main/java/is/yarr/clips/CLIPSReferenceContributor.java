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
        // Diagnostic: broad language-scoped provider to verify contributor is firing
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withLanguage(CLIPSLanguage.INSTANCE),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                       @NotNull ProcessingContext context) {
                    var node = element.getNode();
                    var elemType = node != null ? node.getElementType() : null;
                    var parent = element.getParent();
                    System.out.println("[DEBUG_LOG] [Diag] ReferenceContributor queried: text='" + element.getText() + "', type=" + elemType +
                            ", elemClass=" + element.getClass().getName() + ", parentClass=" + (parent != null ? parent.getClass().getName() : "null"));
                    return PsiReference.EMPTY_ARRAY; // no-op; specific providers below will still run
                }
            }
        );
        
        // Register reference provider for variables (composite PSI element)
        System.out.println("[DEBUG_LOG] Registering reference provider for CLIPSVariableElement");
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
                    
                    if (element instanceof CLIPSVariableElement variable) {
                        var name = variable.getName();
                        System.out.println("[DEBUG_LOG] Variable name: " + name);
                        if (name == null) return PsiReference.EMPTY_ARRAY;
                        var range = TextRange.from(0, element.getTextLength());
                        return new PsiReference[]{ new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.VARIABLE) };
                    }
                    return PsiReference.EMPTY_ARRAY;
                }
            }
        );
        
        // Register reference provider for multifield variables (composite PSI element)
        System.out.println("[DEBUG_LOG] Registering reference provider for CLIPSMultifieldVariableElement");
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSMultifieldVariableElement.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    System.out.println("[DEBUG_LOG] Multifield variable reference provider's getReferencesByElement called for: " + element);
                    if (element instanceof CLIPSMultifieldVariableElement variable) {
                        var name = variable.getName();
                        if (name == null) return PsiReference.EMPTY_ARRAY;
                        var text = element.getText();
                        var startOffset = text.startsWith("$?") ? 2 : 0;
                        var range = new TextRange(startOffset, element.getTextLength());
                        return new PsiReference[]{ new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.VARIABLE) };
                    }
                    return PsiReference.EMPTY_ARRAY;
                }
            }
        );
        
        // Register reference provider for VARIABLE leaf token
        System.out.println("[DEBUG_LOG] Registering reference provider for VARIABLE token");
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(CLIPSTypes.VARIABLE),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    System.out.println("[DEBUG_LOG] VARIABLE token reference provider's getReferencesByElement called for: " + element);
                    var text = element.getText();
                    var name = text.startsWith("?") ? text.substring(1) : text;
                    if (name.isEmpty()) return PsiReference.EMPTY_ARRAY;
                    var range = TextRange.from(0, element.getTextLength());
                    return new PsiReference[]{ new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.VARIABLE) };
                }
            }
        );
        
        // Register reference provider for GLOBAL_VARIABLE leaf token
        System.out.println("[DEBUG_LOG] Registering reference provider for GLOBAL_VARIABLE token");
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(CLIPSTypes.GLOBAL_VARIABLE),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    System.out.println("[DEBUG_LOG] GLOBAL_VARIABLE token provider called for: " + element);
                    var text = element.getText();
                    var core = (text.startsWith("?*") && text.endsWith("*")) ? text.substring(2, text.length() - 1) : text;
                    if (core.isEmpty()) return PsiReference.EMPTY_ARRAY;
                    var startOffset = text.startsWith("?*") ? 2 : 0;
                    var end = text.endsWith("*") ? element.getTextLength() - 1 : element.getTextLength();
                    var range = new TextRange(startOffset, end);
                    return new PsiReference[]{ new CLIPSReference(element, range, core, CLIPSReference.ReferenceType.GLOBAL_VARIABLE) };
                }
            }
        );
        
        // Register reference provider for IDENTIFIER leaves used as names (def_name)
        System.out.println("[DEBUG_LOG] Registering reference provider for IDENTIFIER within CLIPSDefName");
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(CLIPSTypes.IDENTIFIER).withParent(CLIPSDefName.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    System.out.println("[DEBUG_LOG] IDENTIFIER-in-DefName provider called for: " + element + " text='" + element.getText() + "'");
                    // Determine context: function call vs pattern head
                    var inFunction = element.getParent() != null &&
                                     com.intellij.psi.util.PsiTreeUtil.getParentOfType(element, CLIPSFunctionCall.class, false) != null;
                    var type = inFunction ? CLIPSReference.ReferenceType.FUNCTION : CLIPSReference.ReferenceType.TEMPLATE;
                    var range = TextRange.from(0, element.getTextLength());
                    return new PsiReference[]{ new CLIPSReference(element, range, element.getText(), type) };
                }
            }
        );
        
        // Register reference provider for IDENTIFIER leaves used as slot names
        System.out.println("[DEBUG_LOG] Registering reference provider for IDENTIFIER within CLIPSSlotName");
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(CLIPSTypes.IDENTIFIER).withParent(CLIPSSlotName.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    System.out.println("[DEBUG_LOG] IDENTIFIER-in-SlotName provider called for: " + element + " text='" + element.getText() + "'");
                    var range = TextRange.from(0, element.getTextLength());
                    return new PsiReference[]{ new CLIPSReference(element, range, element.getText(), CLIPSReference.ReferenceType.SLOT) };
                }
            }
        );
        
        // Register reference provider for parameters (composite PSI element)
        System.out.println("[DEBUG_LOG] Registering reference provider for CLIPSParameter");
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSParameter.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    System.out.println("[DEBUG_LOG] Parameter reference provider's getReferencesByElement called for: " + element);
                    if (element instanceof CLIPSParameter parameter) {
                        var name = parameter.getName();
                        if (name == null) return PsiReference.EMPTY_ARRAY;
                        var text = element.getText();
                        var startOffset = text.startsWith("?") ? 1 : 0;
                        var range = new TextRange(startOffset, element.getTextLength());
                        return new PsiReference[]{ new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.VARIABLE) };
                    }
                    return PsiReference.EMPTY_ARRAY;
                }
            }
        );
    }
}