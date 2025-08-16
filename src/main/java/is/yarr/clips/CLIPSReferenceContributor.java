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
import is.yarr.clips.psi.impl.CLIPSDeffunctionNameImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Registers reference providers for CLIPS elements.
 * This enables features like "Go to Declaration" and "Find Usages".
 */
public class CLIPSReferenceContributor extends PsiReferenceContributor {
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSVariableElement.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    if (element instanceof CLIPSVariableElement variable) {
                        var name = variable.getName();
                        if (name == null) return PsiReference.EMPTY_ARRAY;
                        var range = TextRange.from(0, element.getTextLength());
                        return new PsiReference[]{ new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.VARIABLE) };
                    }
                    return PsiReference.EMPTY_ARRAY;
                }
            }
        );
        
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSMultifieldVariableElement.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
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

        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement()
                        .withElementType(CLIPSTypes.IDENTIFIER)
                        .withParent(CLIPSDefName.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                       @NotNull ProcessingContext context) {

                    var inFuncCall = com.intellij.psi.util.PsiTreeUtil.getParentOfType(element, CLIPSFunctionCall.class, false) != null;
                    var inDefFunction = com.intellij.psi.util.PsiTreeUtil.getParentOfType(element, CLIPSDeffunctionConstruct.class, false) != null;
                    var parent = element.getParent();
                    if (!inFuncCall || inDefFunction || !(parent instanceof CLIPSDefName)) return PsiReference.EMPTY_ARRAY;

                    var name = element.getText();
                    var file = element.getContainingFile();

                    boolean hasFunction = false;
                    for (var def : com.intellij.psi.util.PsiTreeUtil.findChildrenOfType(file, CLIPSDeffunctionConstruct.class)) {
                        var deffuncName = com.intellij.psi.util.PsiTreeUtil.findChildOfType(def, CLIPSDeffunctionName.class);
                        if (deffuncName != null && name.equals(deffuncName.getText())) { hasFunction = true; break; }
                    }

                    boolean hasTemplate = false;
                    for (var tn : com.intellij.psi.util.PsiTreeUtil.findChildrenOfType(file, CLIPSTemplateName.class)) {
                        if (name.equals(tn.getText())) { hasTemplate = true; break; }
                    }

                    var range = TextRange.from(0, element.getTextLength());
                    if (hasFunction && hasTemplate) {
                        System.out.println("[DEBUG_LOG] IDENTIFIER call head has both function and template definitions: '" + name + "'");
                        return new PsiReference[]{
                            new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.FUNCTION, true),
                            new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.TEMPLATE, true)
                        };
                    } else if (hasFunction) {
                        System.out.println("[DEBUG_LOG] IDENTIFIER call head resolved as FUNCTION: '" + name + "'");
                        return new PsiReference[]{ new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.FUNCTION) };
                    } else if (hasTemplate) {
                        System.out.println("[DEBUG_LOG] IDENTIFIER call head resolved as TEMPLATE (pattern-like usage inside call): '" + name + "'");
                        return new PsiReference[]{ new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.TEMPLATE) };
                    }

                    System.out.println("[DEBUG_LOG] IDENTIFIER call head defaulting to FUNCTION: '" + name + "'");
                    return new PsiReference[]{ new CLIPSReference(element, range, name, CLIPSReference.ReferenceType.FUNCTION) };
                }
            }
        );

        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement().withElementType(CLIPSTypes.IDENTIFIER).withParent(CLIPSSlotName.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
                    var range = TextRange.from(0, element.getTextLength());
                    return new PsiReference[]{ new CLIPSReference(element, range, element.getText(), CLIPSReference.ReferenceType.SLOT) };
                }
            }
        );
        
        registrar.registerReferenceProvider(
            PlatformPatterns.psiElement(CLIPSParameter.class),
            new PsiReferenceProvider() {
                @Override
                public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                     @NotNull ProcessingContext context) {
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