package is.yarr.clips;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.tree.TokenSet;
import is.yarr.clips.lexer.CLIPSLexerAdapter;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides support for finding usages of CLIPS elements.
 * This enables the "Find Usages" feature (Alt+F7) in the IDE.
 */
public class CLIPSFindUsagesProvider implements FindUsagesProvider {
    @Nullable
    @Override
    public WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(
            new CLIPSLexerAdapter(),
            TokenSet.create(CLIPSTypes.IDENTIFIER),
            TokenSet.create(CLIPSTypes.COMMENT),
            TokenSet.create(CLIPSTypes.STRING, CLIPSTypes.NUMBER)
        );
    }

    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiNamedElement;
    }

    @Nullable
    @Override
    public String getHelpId(@NotNull PsiElement psiElement) {
        return null; // Use default help
    }

    @NotNull
    @Override
    public String getType(@NotNull PsiElement element) {
        return switch (element) {
            case CLIPSVariableElement clipsVariableElement -> "variable";
            case CLIPSGlobalVariableDef clipsGlobalVariableDef -> "global variable";
            case CLIPSTemplateName clipsTemplateName -> "template";
            case CLIPSRuleName clipsRuleName -> "rule";
            case CLIPSSlotName clipsSlotName -> "slot";
            case CLIPSParameter clipsParameter -> "parameter";
            case CLIPSModuleName clipsModuleName -> "module";
            case CLIPSClassName clipsClassName -> "class";
            case CLIPSDeffactsName clipsDeffactsName -> "deffacts";
            case CLIPSDeffunctionName clipsDeffunctionName -> "function";
            default -> "element";
        };
    }

    @NotNull
    @Override
    public String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof PsiNamedElement) {
            String name = ((PsiNamedElement) element).getName();
            return name != null ? name : "";
        }
        return "";
    }

    @NotNull
    @Override
    public String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        if (element instanceof PsiNamedElement) {
            String name = ((PsiNamedElement) element).getName();
            if (name != null) {
                if (element instanceof CLIPSVariableElement) {
                    return "?" + name; // Add the '?' prefix for variables
                } else if (element instanceof CLIPSGlobalVariableDef) {
                    return "?*" + name + "*"; // Add the '?*' prefix and '*' suffix for global variables
                } else {
                    return name;
                }
            }
        }
        return element.getText();
    }
}