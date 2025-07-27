package is.yarr.clips.psi;

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import is.yarr.clips.CLIPSLanguage;
import is.yarr.clips.psi.impl.CLIPSVariableElementImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Reference contributor for CLIPS variables.
 * This class attaches references to variable elements in the PSI tree.
 */
public class CLIPSReferenceContributor extends PsiReferenceContributor {

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        // Register reference provider for variable elements
        registrar.registerReferenceProvider(
                PlatformPatterns.psiElement(CLIPSVariableElementImpl.class)
                        .withLanguage(CLIPSLanguage.INSTANCE),
                new PsiReferenceProvider() {
                    @Override
                    public PsiReference @NotNull [] getReferencesByElement(@NotNull PsiElement element,
                                                                          @NotNull ProcessingContext context) {
                        System.out.println("[DEBUG_LOG] Getting references for element: " + element.getText() + " - " + element.getClass().getSimpleName());
                        
                        CLIPSVariableElementImpl variableElement = (CLIPSVariableElementImpl) element;
                        String name = variableElement.getName();
                        if (name == null) {
                            System.out.println("[DEBUG_LOG] Variable name is null, returning empty array");
                            return PsiReference.EMPTY_ARRAY;
                        }
                        
                        System.out.println("[DEBUG_LOG] Creating reference for variable: " + name);

                        // Create a reference for the entire element
                        // The text range is relative to the element's start offset
                        return new PsiReference[]{
                                new CLIPSReference(variableElement, new TextRange(0, element.getTextLength()))
                        };
                    }
                });
    }
}