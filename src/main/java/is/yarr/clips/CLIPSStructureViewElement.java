package is.yarr.clips;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement;
import com.intellij.ide.util.treeView.smartTree.TreeElement;
import com.intellij.navigation.ItemPresentation;
import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an element in the structure view for CLIPS files.
 * This defines how each element is displayed, what its children are, and how to navigate to it.
 */
public class CLIPSStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
    private final PsiElement element;

    public CLIPSStructureViewElement(PsiElement element) {
        this.element = element;
    }

    @Override
    public Object getValue() {
        return element;
    }

    @Override
    public void navigate(boolean requestFocus) {
        if (element instanceof NavigatablePsiElement) {
            ((NavigatablePsiElement) element).navigate(requestFocus);
        }
    }

    @Override
    public boolean canNavigate() {
        return element instanceof NavigatablePsiElement && 
               ((NavigatablePsiElement) element).canNavigate();
    }

    @Override
    public boolean canNavigateToSource() {
        return element instanceof NavigatablePsiElement && 
               ((NavigatablePsiElement) element).canNavigateToSource();
    }

    @NotNull
    @Override
    public String getAlphaSortKey() {
        if (element instanceof PsiNamedElement) {
            String name = ((PsiNamedElement) element).getName();
            return name != null ? name : "";
        }
        return "";
    }

    @NotNull
    @Override
    public ItemPresentation getPresentation() {
        if (element instanceof PsiNamedElement) {
            ItemPresentation presentation = ((NavigatablePsiElement) element).getPresentation();
            if (presentation != null) {
                return presentation;
            }
        }
        
        // Create a default presentation
        String name = getElementName();
        return new PresentationData(
            name,
            getElementType(),
            CLIPSIcons.FILE,
            null
        );
    }

    @NotNull
    @Override
    public TreeElement[] getChildren() {
        if (element instanceof CLIPSFile) {
            // For a file, show all top-level constructs
            List<TreeElement> children = new ArrayList<>();
            
            // Add template definitions
            for (CLIPSDeftemplateConstruct template : PsiTreeUtil.findChildrenOfType(element, CLIPSDeftemplateConstruct.class)) {
                children.add(new CLIPSStructureViewElement(template));
            }
            
            // Add rule definitions
            for (CLIPSDefruleConstruct rule : PsiTreeUtil.findChildrenOfType(element, CLIPSDefruleConstruct.class)) {
                children.add(new CLIPSStructureViewElement(rule));
            }
            
            // Add function definitions
            for (CLIPSDeffunctionConstruct function : PsiTreeUtil.findChildrenOfType(element, CLIPSDeffunctionConstruct.class)) {
                children.add(new CLIPSStructureViewElement(function));
            }
            
            // Add global variable definitions
            for (CLIPSDefglobalConstruct global : PsiTreeUtil.findChildrenOfType(element, CLIPSDefglobalConstruct.class)) {
                children.add(new CLIPSStructureViewElement(global));
            }
            
            // Add module definitions
            for (CLIPSDefmoduleConstruct module : PsiTreeUtil.findChildrenOfType(element, CLIPSDefmoduleConstruct.class)) {
                children.add(new CLIPSStructureViewElement(module));
            }
            
            // Add class definitions
            for (CLIPSDefclassConstruct clazz : PsiTreeUtil.findChildrenOfType(element, CLIPSDefclassConstruct.class)) {
                children.add(new CLIPSStructureViewElement(clazz));
            }
            
            // Add deffacts definitions
            for (CLIPSDeffactsConstruct facts : PsiTreeUtil.findChildrenOfType(element, CLIPSDeffactsConstruct.class)) {
                children.add(new CLIPSStructureViewElement(facts));
            }
            
            return children.toArray(new TreeElement[0]);
        } else if (element instanceof CLIPSDeftemplateConstruct) {
            // For a template, show its slots
            List<TreeElement> children = new ArrayList<>();
            for (CLIPSSlotDefinition slot : PsiTreeUtil.findChildrenOfType(element, CLIPSSlotDefinition.class)) {
                children.add(new CLIPSStructureViewElement(slot));
            }
            return children.toArray(new TreeElement[0]);
        } else if (element instanceof CLIPSDeffunctionConstruct) {
            // For a function, show its parameters
            List<TreeElement> children = new ArrayList<>();
            CLIPSParameterList parameterList = PsiTreeUtil.findChildOfType(element, CLIPSParameterList.class);
            if (parameterList != null) {
                for (CLIPSParameter parameter : PsiTreeUtil.findChildrenOfType(parameterList, CLIPSParameter.class)) {
                    children.add(new CLIPSStructureViewElement(parameter));
                }
            }
            return children.toArray(new TreeElement[0]);
        } else if (element instanceof CLIPSDefglobalConstruct) {
            // For a global variable definition, show the global variables
            List<TreeElement> children = new ArrayList<>();
            for (CLIPSGlobalVariableDef global : PsiTreeUtil.findChildrenOfType(element, CLIPSGlobalVariableDef.class)) {
                children.add(new CLIPSStructureViewElement(global));
            }
            return children.toArray(new TreeElement[0]);
        }
        
        // For other elements, no children
        return EMPTY_ARRAY;
    }
    
    /**
     * Gets the name of the element for display in the structure view.
     */
    private String getElementName() {
        if (element instanceof CLIPSFile) {
            return ((CLIPSFile) element).getName();
        } else if (element instanceof CLIPSDeftemplateConstruct) {
            CLIPSTemplateName name = PsiTreeUtil.findChildOfType(element, CLIPSTemplateName.class);
            return name != null ? "deftemplate " + name.getText() : "deftemplate";
        } else if (element instanceof CLIPSDefruleConstruct) {
            CLIPSRuleName name = PsiTreeUtil.findChildOfType(element, CLIPSRuleName.class);
            return name != null ? "defrule " + name.getText() : "defrule";
        } else if (element instanceof CLIPSDeffunctionConstruct) {
            PsiElement name = PsiTreeUtil.findChildOfType(element, CLIPSDefName.class);
            return name != null ? "deffunction " + name.getText() : "deffunction";
        } else if (element instanceof CLIPSDefglobalConstruct) {
            return "defglobal";
        } else if (element instanceof CLIPSDefmoduleConstruct) {
            CLIPSModuleName name = PsiTreeUtil.findChildOfType(element, CLIPSModuleName.class);
            return name != null ? "defmodule " + name.getText() : "defmodule";
        } else if (element instanceof CLIPSDefclassConstruct) {
            CLIPSClassName name = PsiTreeUtil.findChildOfType(element, CLIPSClassName.class);
            return name != null ? "defclass " + name.getText() : "defclass";
        } else if (element instanceof CLIPSDeffactsConstruct) {
            CLIPSDeffactsName name = PsiTreeUtil.findChildOfType(element, CLIPSDeffactsName.class);
            return name != null ? "deffacts " + name.getText() : "deffacts";
        } else if (element instanceof CLIPSSlotDefinition) {
            CLIPSSlotName name = PsiTreeUtil.findChildOfType(element, CLIPSSlotName.class);
            if (element instanceof CLIPSMultislotDefinition) {
                return name != null ? "multislot " + name.getText() : "multislot";
            } else {
                return name != null ? "slot " + name.getText() : "slot";
            }
        } else if (element instanceof CLIPSParameter) {
            return ((CLIPSParameter) element).getText();
        } else if (element instanceof CLIPSGlobalVariableDef) {
            return ((CLIPSGlobalVariableDef) element).getText();
        } else if (element instanceof PsiNamedElement) {
            String name = ((PsiNamedElement) element).getName();
            return name != null ? name : element.getText();
        }
        
        return element.getText();
    }
    
    /**
     * Gets the type of the element for display in the structure view.
     */
    private String getElementType() {
        if (element instanceof CLIPSFile) {
            return "File";
        } else if (element instanceof CLIPSDeftemplateConstruct) {
            return "Template";
        } else if (element instanceof CLIPSDefruleConstruct) {
            return "Rule";
        } else if (element instanceof CLIPSDeffunctionConstruct) {
            return "Function";
        } else if (element instanceof CLIPSDefglobalConstruct) {
            return "Global Variables";
        } else if (element instanceof CLIPSDefmoduleConstruct) {
            return "Module";
        } else if (element instanceof CLIPSDefclassConstruct) {
            return "Class";
        } else if (element instanceof CLIPSDeffactsConstruct) {
            return "Facts";
        } else if (element instanceof CLIPSMultislotDefinition) {
            return "Multislot";
        } else if (element instanceof CLIPSSingleSlotDefinition) {
            return "Slot";
        } else if (element instanceof CLIPSParameter) {
            return "Parameter";
        } else if (element instanceof CLIPSGlobalVariableDef) {
            return "Global Variable";
        }
        
        return "";
    }
}