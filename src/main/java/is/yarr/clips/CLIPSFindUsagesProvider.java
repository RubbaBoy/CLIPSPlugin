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
        if (element instanceof CLIPSVariableElement) {
            return "variable";
        } else if (element instanceof CLIPSGlobalVariableDef) {
            return "global variable";
        } else if (element instanceof CLIPSTemplateName) {
            return "template";
        } else if (element instanceof CLIPSRuleName) {
            return "rule";
        } else if (element instanceof CLIPSSlotName) {
            return "slot";
        } else if (element instanceof CLIPSParameter) {
            return "parameter";
        } else if (element instanceof CLIPSModuleName) {
            return "module";
        } else if (element instanceof CLIPSClassName) {
            return "class";
        } else if (element instanceof CLIPSDeffactsName) {
            return "deffacts";
        } else {
            return "element";
        }
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