package is.yarr.clips.editor;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Query;
import is.yarr.clips.psi.impl.CLIPSVariableElementImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Annotator for CLIPS language.
 * This class highlights variables and their references.
 */
public class CLIPSAnnotator implements Annotator {

    // Define text attributes for variable highlighting
    public static final TextAttributesKey VARIABLE_DECLARATION = 
            TextAttributesKey.createTextAttributesKey("CLIPS.VARIABLE_DECLARATION", 
                    DefaultLanguageHighlighterColors.INSTANCE_FIELD);
    
    public static final TextAttributesKey VARIABLE_REFERENCE = 
            TextAttributesKey.createTextAttributesKey("CLIPS.VARIABLE_REFERENCE", 
                    DefaultLanguageHighlighterColors.IDENTIFIER);

    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        // Only process variable elements
        if (!(element instanceof CLIPSVariableElementImpl variableElement)) {
            return;
        }

        String variableName = variableElement.getName();
        if (variableName == null || variableName.isEmpty()) {
            return;
        }

        // Highlight the variable declaration
        TextRange range = element.getTextRange();
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(range)
                .textAttributes(VARIABLE_DECLARATION)
                .create();

        // Find and highlight all references to this variable
        Query<PsiReference> query = ReferencesSearch.search(variableElement);
        Collection<PsiReference> references = query.findAll();

        for (PsiReference reference : references) {
            PsiElement refElement = reference.getElement();
            if (refElement != element) {
                TextRange refRange = refElement.getTextRange();
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(refRange)
                        .textAttributes(VARIABLE_REFERENCE)
                        .create();
            }
        }
    }
}