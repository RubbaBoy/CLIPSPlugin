package is.yarr.clips;

import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingBuilderEx;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides code folding support for CLIPS files.
 * This allows users to collapse and expand sections of code.
 */
public class CLIPSFoldingBuilder extends FoldingBuilderEx implements DumbAware {
    @Override
    public FoldingDescriptor @NotNull [] buildFoldRegions(@NotNull PsiElement root, @NotNull Document document, boolean quick) {
        List<FoldingDescriptor> descriptors = new ArrayList<>();
        
        // Find all constructs that can be folded
        collectFoldRegions(root, descriptors);
        
        return descriptors.toArray(new FoldingDescriptor[0]);
    }

    private void collectFoldRegions(@NotNull PsiElement element, List<FoldingDescriptor> descriptors) {
        // Add folding for defrule constructs
        if (element instanceof CLIPSDefruleConstruct) {
            ASTNode node = element.getNode();
            TextRange range = element.getTextRange();
            
            // Only fold if the construct is large enough
            if (range.getLength() > 30) {
                descriptors.add(new FoldingDescriptor(node, range));
            }
        }
        
        // Add folding for deftemplate constructs
        if (element instanceof CLIPSDeftemplateConstruct) {
            ASTNode node = element.getNode();
            TextRange range = element.getTextRange();
            
            // Only fold if the construct is large enough
            if (range.getLength() > 30) {
                descriptors.add(new FoldingDescriptor(node, range));
            }
        }
        
        // Add folding for deffunction constructs
        if (element instanceof CLIPSDeffunctionConstruct) {
            ASTNode node = element.getNode();
            TextRange range = element.getTextRange();
            
            // Only fold if the construct is large enough
            if (range.getLength() > 30) {
                descriptors.add(new FoldingDescriptor(node, range));
            }
        }
        
        // Add folding for defglobal constructs
        if (element instanceof CLIPSDefglobalConstruct) {
            ASTNode node = element.getNode();
            TextRange range = element.getTextRange();
            
            // Only fold if the construct is large enough
            if (range.getLength() > 30) {
                descriptors.add(new FoldingDescriptor(node, range));
            }
        }
        
        // Recursively process child elements
        for (PsiElement child : element.getChildren()) {
            collectFoldRegions(child, descriptors);
        }
    }

    @Nullable
    @Override
    public String getPlaceholderText(@NotNull ASTNode node) {
        PsiElement element = node.getPsi();
        
        if (element instanceof CLIPSDefruleConstruct) {
            CLIPSRuleName ruleName = PsiTreeUtil.findChildOfType(element, CLIPSRuleName.class);
            if (ruleName != null) {
                return "(defrule " + ruleName.getText() + " ...)";
            }
            return "(defrule ...)";
        }
        
        if (element instanceof CLIPSDeftemplateConstruct) {
            CLIPSTemplateName templateName = PsiTreeUtil.findChildOfType(element, CLIPSTemplateName.class);
            if (templateName != null) {
                return "(deftemplate " + templateName.getText() + " ...)";
            }
            return "(deftemplate ...)";
        }
        
        if (element instanceof CLIPSDeffunctionConstruct) {
            // Find the function name
            PsiElement functionName = PsiTreeUtil.findChildOfType(element, CLIPSDefName.class);
            if (functionName != null) {
                return "(deffunction " + functionName.getText() + " ...)";
            }
            return "(deffunction ...)";
        }
        
        if (element instanceof CLIPSDefglobalConstruct) {
            return "(defglobal ...)";
        }
        
        return "...";
    }

    @Override
    public boolean isCollapsedByDefault(@NotNull ASTNode node) {
        return false; // Don't collapse by default
    }
}