package is.yarr.clips.psi;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

/**
 * Utility methods for CLIPS PSI elements.
 */
public class CLIPSPsiImplUtil {

    public static PsiElement getNameIdentifier(@NotNull CLIPSNamedElement element) {
        return element;
    }

    public static PsiElement setName(@NotNull CLIPSNamedElement element,
                                     @NotNull String newName) {
        PsiElement id = element.getNameIdentifier();
        if (id != null) {
            // replace the leaf with a new identifier
            PsiElement newId =
                    com.intellij.psi.PsiFileFactory.getInstance(element.getProject())
                            .createFileFromText("dummy.clips", element.getContainingFile().getLanguage(),
                                    newName)
                            .getFirstChild();
            id.replace(newId);
        }
        return element;
    }


    /**
     * Gets the name of a variable element.
     * Strips the prefix (?, $?, ?*) and suffix (*) if present.
     *
     * @param element The PSI element
     * @return The variable name without prefixes/suffixes
     */
    public static String getName(CLIPSNamedElement element) {
        String text = element.getText();
        return extractVariableName(text);
    }


    /**
     * Gets the name of a variable element.
     * Strips the prefix (?, $?, ?*) and suffix (*) if present.
     *
     * @param text The text of the variable element
     * @return The variable name without prefixes/suffixes
     */
    public static String extractVariableName(String text) {
        if (text == null) {
            return null;
        }

        // Handle different variable types
        if (text.startsWith("?") && !text.startsWith("?*")) {
            // Regular variable: ?name -> name
            return text.substring(1);
        } else if (text.startsWith("$?")) {
            // Multifield variable: $?name -> name
            return text.substring(2);
        } else if (text.startsWith("?*") && text.endsWith("*")) {
            // Global variable: ?*name* -> name
            return text.substring(2, text.length() - 1);
        }

        return text;
    }
}