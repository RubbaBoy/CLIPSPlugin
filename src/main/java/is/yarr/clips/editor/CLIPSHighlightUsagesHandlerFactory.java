package is.yarr.clips.editor;

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerFactory;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import is.yarr.clips.psi.CLIPSElementTypes;
import is.yarr.clips.psi.CLIPSNamedElement;
import is.yarr.clips.psi.CLIPSTypes;
import is.yarr.clips.psi.impl.CLIPSVariableElementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Factory for creating highlight usage handlers for CLIPS variables.
 * This is responsible for creating handlers that highlight all occurrences of a variable when clicked.
 */
public class CLIPSHighlightUsagesHandlerFactory implements HighlightUsagesHandlerFactory {

    @Override
    public @Nullable HighlightUsagesHandlerBase createHighlightUsagesHandler(@NotNull Editor editor, @NotNull PsiFile file) {
        // Get the element at the caret position
        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);
        
        // Check if the element is a CLIPS variable
        if (element instanceof CLIPSVariableElementImpl variableElement) {
            return new CLIPSHighlightUsagesHandler(editor, file, variableElement);
        }
        
        // If the element is a leaf element, check if it's a variable token
        if (element instanceof LeafPsiElement leafElement) {
            ASTNode node = leafElement.getNode();
            
            // Check if the node has a variable token type
            if (node.getElementType() == CLIPSElementTypes.VARIABLE || 
                node.getElementType() == CLIPSElementTypes.MULTIFIELD_VARIABLE || 
                node.getElementType() == CLIPSElementTypes.GLOBAL_VARIABLE) {
                
                // Create a CLIPSVariableElementImpl with the node
                CLIPSVariableElementImpl variableElement = new CLIPSVariableElementImpl(node);
                return new CLIPSHighlightUsagesHandler(editor, file, variableElement);
            }
        }
        
        return null;
    }
}