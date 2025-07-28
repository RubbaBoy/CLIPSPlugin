package is.yarr.clips.editor;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import is.yarr.clips.psi.CLIPSElementTypes;
import is.yarr.clips.psi.CLIPSGlobalVariableDef;
import is.yarr.clips.psi.CLIPSNamedElement;
import is.yarr.clips.psi.CLIPSParameter;
import is.yarr.clips.psi.CLIPSPsiImplUtil;
import is.yarr.clips.psi.CLIPSTypes;
import is.yarr.clips.psi.CLIPSVariableElement;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Handler for Ctrl+click navigation to variable definitions in CLIPS files.
 */
public class CLIPSGotoDeclarationHandler implements GotoDeclarationHandler {

    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement sourceElement, int offset, Editor editor) {
        if (sourceElement == null) {
            return null;
        }

        // Check if the element is a variable
        if (sourceElement instanceof LeafPsiElement leafElement) {
            IElementType type = leafElement.getElementType();
            
            if (type == CLIPSElementTypes.VARIABLE ||
                type == CLIPSElementTypes.MULTIFIELD_VARIABLE || 
                type == CLIPSElementTypes.GLOBAL_VARIABLE) {
                
                // Extract the variable name
                String variableName = CLIPSPsiImplUtil.extractVariableName(leafElement.getText());
//                String variableName = leafElement.getText();
                System.out.println("[DEBUG_LOG] Looking for declaration of variable: " + variableName);
                
                // Check if it's a global variable
                boolean isGlobal = type == CLIPSElementTypes.GLOBAL_VARIABLE;

                PsiFile file = sourceElement.getContainingFile();

                if (isGlobal) {
                    // For global variables, highlight all occurrences in the file
                    return findGlobalVariableDeclaration(file, variableName);
                } else {
                    // For local variables, find the containing block and only highlight occurrences within that block
                    PsiElement containingBlock = findContainingBlock(sourceElement);
                    System.out.println("[DEBUG_LOG] Containing block: " + (containingBlock != null ? containingBlock.getText() : "null"));

                    if (containingBlock != null) {
                        // Only highlight occurrences within the containing block
                        return findLocalVariableDeclaration(containingBlock, variableName, containingBlock);
                    } else {
                        // If we couldn't find a containing block, fall back to highlighting all occurrences
                        System.out.println("[DEBUG_LOG] Couldn't find containing block, highlighting all occurrences");
                        return findLocalVariableDeclaration(file, variableName, null);
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * Finds the declaration of a global variable in a defglobal block.
     * 
     * @param sourceElement The source element (the variable being clicked)
     * @param variableName The variable name (without prefix/suffix)
     * @return An array of PsiElements representing the declaration, or null if not found
     */
    private PsiElement[] findGlobalVariableDeclaration(PsiElement sourceElement, String variableName) {
        // Find all defglobal blocks in the file
        PsiFile file = sourceElement.getContainingFile();
        List<PsiElement> declarations = new ArrayList<>();
        
        // Find all defglobal elements in the file
        for (PsiElement child : file.getChildren()) {
            System.out.println("Looking at child: " + child.getNode().getElementType() + " - " + child.getText());
            if (child.getNode().getElementType() == CLIPSElementTypes.CONSTRUCT) {
                var firstChild = child.getFirstChild();
                System.out.println("First child: " + (firstChild != null ? firstChild.getNode().getElementType() : "null"));
                if (firstChild != null && firstChild.getNode().getElementType() == CLIPSElementTypes.DEFGLOBAL_CONSTRUCT) {
                    System.out.println("Found defglobal construct: " + child.getText());
                    // Found a defglobal block, now search for the variable declaration
                    findGlobalVariableInDefglobal(firstChild, variableName, declarations);
                }
            }
        }
        
        // If we couldn't find any declarations using the hierarchical tree, try the old method
        if (declarations.isEmpty()) {
            findDefglobalBlocksFallback(file, variableName, declarations);
        }
        
        return declarations.isEmpty() ? null : declarations.toArray(new PsiElement[0]);
    }
    
    /**
     * Searches for a global variable declaration in a defglobal block.
     * 
     * @param defglobal The defglobal element
     * @param variableName The variable name to look for
     * @param declarations The list to add declarations to
     */
    private void findGlobalVariableInDefglobal(PsiElement defglobal, String variableName, List<PsiElement> declarations) {
        // Iterate through all children of the defglobal element
        for (PsiElement child : defglobal.getChildren()) {
            System.out.println("\tchild: " + child.getNode().getElementType() + " - " + child.getText());
            if (child instanceof CLIPSGlobalVariableDef globalVariableDef) {
                String currentVarName = globalVariableDef.getName();
                System.out.println("\t\tChecking variable: " + currentVarName + " == " + variableName);
                if (variableName.equals(currentVarName)) {
                    // Found the variable declaration
                    declarations.add(child);
                }
            }
        }
    }
    
    /**
     * Fallback method to recursively search for defglobal blocks and find global variable declarations.
     * This is used when the hierarchical tree structure doesn't provide a clear defglobal element.
     * 
     * @param element The element to search in
     * @param variableName The variable name to look for
     * @param declarations The list to add declarations to
     */
    private void findDefglobalBlocksFallback(PsiElement element, String variableName, List<PsiElement> declarations) {
        // Check if this element is an opening parenthesis
        if (element instanceof LeafPsiElement leafElement) {
            if (leafElement.getElementType() == CLIPSElementTypes.LPAREN) {
                // Check if the next token is the defglobal keyword
                PsiElement next = leafElement.getNextSibling();
                while (next != null && !(next instanceof LeafPsiElement)) {
                    next = next.getNextSibling();
                }
                
                if (next != null) {
                    LeafPsiElement nextLeaf = (LeafPsiElement) next;
                    // TODO: Really test this
                    if (nextLeaf.getElementType() == CLIPSElementTypes.KEYWORD && nextLeaf.getText().equals("defglobal")) {
                        // Found a defglobal block, now search for the variable declaration
                        PsiElement current = nextLeaf.getNextSibling();
                        while (current != null) {
                            if (current instanceof LeafPsiElement currentLeaf) {
                                if (currentLeaf.getElementType() == CLIPSElementTypes.GLOBAL_VARIABLE) {
                                    String currentVarName = CLIPSPsiImplUtil.extractVariableName(currentLeaf.getText());
                                    if (variableName.equals(currentVarName)) {
                                        // Found the variable declaration
                                        declarations.add(currentLeaf);
                                    }
                                } else if (currentLeaf.getElementType() == CLIPSElementTypes.RPAREN) {
                                    // End of defglobal block
                                    break;
                                }
                            }
                            current = current.getNextSibling();
                        }
                    }
                }
            }
        }
        
        // Recursively check all children
        for (PsiElement child : element.getChildren()) {
            findDefglobalBlocksFallback(child, variableName, declarations);
        }
    }
    
    /**
     * Finds the declaration of a local variable in its containing block.
     * 
     * @param sourceElement The source element (the variable being clicked)
     * @param variableName The variable name (without prefix)
     * @return An array of PsiElements representing the declaration, or null if not found
     */
    private PsiElement[] findLocalVariableDeclaration(PsiElement sourceElement, String variableName, @Nullable PsiElement containingBlock) {
        // Find the first occurrence of the variable in the block
        List<PsiElement> declarations = new ArrayList<>();
        findFirstVariableOccurrence(containingBlock, variableName, declarations);

        return declarations.isEmpty() ? null : declarations.toArray(new PsiElement[0]);
    }
    
    /**
     * Finds the containing block for a variable.
     * A block is a top-level construct like deffunction, defrule, etc.
     * 
     * @param variable The variable element to find the containing block for
     * @return The containing block element, or null if not found
     */
    @Nullable
    private PsiElement findContainingBlock(PsiElement variable) {
        return CLIPSEditorUtils.findContainingBlock(variable, this::findContainingBlockFallback);
    }
    
    /**
     * Fallback method to find the containing block for a variable.
     * This is used when the hierarchical tree structure doesn't provide a clear containing block.
     * 
     * @param variable The variable element to find the containing block for
     * @return The containing block element, or null if not found
     */
    @Nullable
    private PsiElement findContainingBlockFallback(PsiElement variable) {
        if (variable == null) {
            return null;
        }
        
        // Get the file containing the variable
        PsiFile file = variable.getContainingFile();
        if (file == null) {
            return null;
        }
        
        // Get the text range of the variable
        int variableOffset = variable.getTextRange().getStartOffset();
        
        // Find all potential containing blocks
        List<PsiElement> potentialBlocks = new ArrayList<>();
        findPotentialContainingBlocks(file, variableOffset, potentialBlocks);
        
        // Find the block that is closest to the variable
        PsiElement closestBlock = null;
        int closestDistance = Integer.MAX_VALUE;
        
        for (PsiElement block : potentialBlocks) {
            // Get the start offset of the block
            int blockOffset = block.getTextRange().getStartOffset();
            int distance = variableOffset - blockOffset;
            
            // Update the closest block if this one is closer
            if (distance > 0 && distance < closestDistance) {
                closestDistance = distance;
                closestBlock = block;
            }
        }
        
        return closestBlock;
    }
    
    /**
     * Recursively finds all potential containing blocks for a variable.
     * 
     * @param element The element to check
     * @param variableOffset The offset of the variable in the document
     * @param potentialBlocks The list to add potential blocks to
     */
    private void findPotentialContainingBlocks(PsiElement element, int variableOffset, List<PsiElement> potentialBlocks) {
        // Check if this element is an opening parenthesis
        if (element instanceof LeafPsiElement leafElement) {
            if (leafElement.getElementType() == CLIPSElementTypes.LPAREN) {
                // Check if the next token is a block-defining keyword
                PsiElement next = leafElement.getNextSibling();
                while (next != null && !(next instanceof LeafPsiElement)) {
                    next = next.getNextSibling();
                }
                
                if (next instanceof LeafPsiElement nextLeaf) {
                    if (nextLeaf.getElementType() == CLIPSElementTypes.KEYWORD && isBlockKeyword(nextLeaf.getText())) {
                        // Find the matching closing parenthesis
                        PsiElement blockEnd = findMatchingClosingParenthesis(leafElement);
                        if (blockEnd != null) {
                            // Get the text range of the block
                            TextRange blockRange = new TextRange(
                                leafElement.getTextRange().getStartOffset(),
                                blockEnd.getTextRange().getEndOffset()
                            );
                            
                            // Check if the variable is within this block
                            if (blockRange.getStartOffset() < variableOffset && blockRange.getEndOffset() > variableOffset) {
                                // Found a potential containing block
                                PsiElement block = findBlockElement(leafElement, blockEnd);
                                if (block != null) {
                                    potentialBlocks.add(block);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Recursively check all children
        for (PsiElement child : element.getChildren()) {
            findPotentialContainingBlocks(child, variableOffset, potentialBlocks);
        }
    }
    
    /**
     * Checks if a keyword is a block-defining keyword.
     * 
     * @param keyword The keyword to check
     * @return True if the keyword defines a block, false otherwise
     */
    private boolean isBlockKeyword(String keyword) {
        return keyword.equals("deffunction") || 
               keyword.equals("defrule") || 
               keyword.equals("deftemplate") || 
               keyword.equals("defglobal") || 
               keyword.equals("defclass") || 
               keyword.equals("definstances") || 
               keyword.equals("defmessage-handler") || 
               keyword.equals("defgeneric") || 
               keyword.equals("defmethod");
    }
    
    /**
     * Finds the matching closing parenthesis for an opening parenthesis.
     * 
     * @param openParen The opening parenthesis element
     * @return The matching closing parenthesis element, or null if not found
     */
    @Nullable
    private PsiElement findMatchingClosingParenthesis(PsiElement openParen) {
        Stack<PsiElement> stack = new Stack<>();
        stack.push(openParen);
        
        PsiElement current = openParen.getNextSibling();
        while (current != null && !stack.isEmpty()) {
            if (current instanceof LeafPsiElement) {
                LeafPsiElement leafElement = (LeafPsiElement) current;
                if (leafElement.getElementType() == CLIPSElementTypes.LPAREN) {
                    stack.push(current);
                } else if (leafElement.getElementType() == CLIPSElementTypes.RPAREN) {
                    stack.pop();
                    if (stack.isEmpty()) {
                        return current;
                    }
                }
            }
            
            current = current.getNextSibling();
        }
        
        return null;
    }
    
    /**
     * Finds the block element that spans from the opening parenthesis to the closing parenthesis.
     * 
     * @param start The opening parenthesis element
     * @param end The closing parenthesis element
     * @return The block element, or null if not found
     */
    @Nullable
    private PsiElement findBlockElement(PsiElement start, PsiElement end) {
        // Since we don't have a structured PSI tree, we'll use the parent element
        // that contains both the start and end elements
        PsiElement parent = start.getParent();
        if (parent != null && parent.getTextRange().contains(end.getTextRange())) {
            return parent;
        }
        
        return null;
    }
    
    /**
     * Finds the first occurrence of a variable with the given name in the element.
     * Uses a recursive approach to traverse the hierarchical PSI tree.
     * 
     * @param element The element to check and its children
     * @param variableName The variable name to look for (without prefix)
     * @param declarations The list to add the declaration to
     */
    private void findFirstVariableOccurrence(PsiElement element, String variableName, List<PsiElement> declarations) {
        // If we already found a declaration, stop searching
        if (!declarations.isEmpty()) {
            return;
        }

        // If a containing block is specified, don't search outside of it.
//        if (containingBlock != null && !element.equals(containingBlock) && !com.intellij.psi.util.PsiTreeUtil.isAncestor(containingBlock, element, false)) {
//            return;
//        }

        // Check if the current element is a variable we are looking for.
        if (element instanceof CLIPSNamedElement named) {
            String currentVariableName = named.getName();
            System.out.println("\tNames? " + variableName + " == " + currentVariableName);
            if (variableName.equals(currentVariableName)) {
                declarations.add(element);
                return;
            }
        }
//        } else if (element instanceof CLIPSParameter) {
//            if (element.getText().equals(variableName)) {
//                declarations.add(element);
//                return;
//            }
//        }
/*
        // For function parameters, check the parameter list first
        if (element.getNode().getElementType() == CLIPSElementTypes.DEFFUNCTION_CONSTRUCT) {
            PsiElement paramList = null;
            for (PsiElement child : element.getChildren()) {
                if (child.getNode().getElementType() == CLIPSElementTypes.PARAMETER_LIST) {
                    paramList = child;
                    break;
                }
            }

            if (paramList != null) {
                // Check all variables in the parameter list
                for (PsiElement param : paramList.getChildren()) {
                    if (param instanceof LeafPsiElement) {
                        LeafPsiElement leafElement = (LeafPsiElement) param;
                        IElementType type = leafElement.getElementType();

                        if (type == CLIPSElementTypes.VARIABLE || type == CLIPSElementTypes.MULTIFIELD_VARIABLE) {
                            String elementText = leafElement.getText();
                            String elementName = extractVariableName(elementText);

                            if (variableName.equals(elementName)) {
                                declarations.add(leafElement);
                                System.out.println("[DEBUG_LOG] Found parameter declaration: " + elementText);
                                return;
                            }
                        }
                    }
                }
            }
        }

        // For rule patterns, check the left-hand side first
//        if (element.getNode().getElementType() == CLIPSElementTypes.DEFRULE_CONSTRUCT) {
//            PsiElement lhs = null;
//            for (PsiElement child : element.getChildren()) {
//                if (child.getNode().getElementType() == CLIPSElementTypes.LHS) {
//                    lhs = child;
//                    break;
//                }
//            }
//
//            if (lhs != null) {
//                // Check all pattern bindings in the left-hand side
//                for (PsiElement pattern : lhs.getChildren()) {
//                    if (pattern instanceof LeafPsiElement) {
//                        LeafPsiElement leafElement = (LeafPsiElement) pattern;
//                        IElementType type = leafElement.getElementType();
//
//                        if (type == CLIPSElementTypes.VARIABLE || type == CLIPSElementTypes.MULTIFIELD_VARIABLE) {
//                            String elementText = leafElement.getText();
//                            String elementName = extractVariableName(elementText);
//
//                            if (variableName.equals(elementName)) {
//                                declarations.add(leafElement);
//                                System.out.println("[DEBUG_LOG] Found pattern binding: " + elementText);
//                                return;
//                            }
//                        }
//                    }
//
//                    // Recursively check the pattern for variables
//                    findFirstVariableOccurrence(pattern, variableName, declarations);
//                    if (!declarations.isEmpty()) {
//                        return;
//                    }
//                }
//            }
//        }

        // Check if this element is a variable
        if (element instanceof LeafPsiElement) {
            LeafPsiElement leafElement = (LeafPsiElement) element;
            IElementType type = leafElement.getElementType();

            // Check if it's a variable token
            if (type == CLIPSElementTypes.VARIABLE ||
                type == CLIPSElementTypes.MULTIFIELD_VARIABLE) {

                // Extract the variable name from the text
                String elementText = leafElement.getText();
                String elementName = extractVariableName(elementText);

                // If the names match, add this occurrence as the declaration
                if (variableName.equals(elementName)) {
                    declarations.add(leafElement);
                    System.out.println("[DEBUG_LOG] Found local variable declaration: " + elementText);
                    return;
                }
            }
        }
*/

        System.out.println(element.getNode().getElementType() + " Going to recurse into children: " + Arrays.stream(element.getChildren()).map(a -> a.getNode().getElementType().toString()).collect(Collectors.joining(", ")));
        // Recursively check all children
        for (PsiElement child : element.getChildren()) {
            System.out.println("Recursing into child (" + child.getNode().getElementType() + "): " + child.getText());
            findFirstVariableOccurrence(child, variableName, declarations);
            // If we found a declaration, stop searching
            if (!declarations.isEmpty()) {
                return;
            }
        }
    }
}