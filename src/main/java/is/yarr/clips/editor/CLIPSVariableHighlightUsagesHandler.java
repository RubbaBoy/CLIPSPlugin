package is.yarr.clips.editor;

import com.intellij.codeInsight.highlighting.HighlightUsagesHandlerBase;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.Consumer;
import is.yarr.clips.psi.CLIPSElementTypes;
import is.yarr.clips.psi.CLIPSNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Handler for highlighting all occurrences of a CLIPS variable when clicked.
 */
public class CLIPSVariableHighlightUsagesHandler extends HighlightUsagesHandlerBase<PsiElement> {
    private final CLIPSNamedElement myVariable;
    private final List<PsiElement> myTargets = new ArrayList<>();

    public CLIPSVariableHighlightUsagesHandler(@NotNull Editor editor, @NotNull PsiFile file, @NotNull CLIPSNamedElement variable) {
        super(editor, file);
        myVariable = variable;
        myTargets.add(variable);
    }

    @Override
    public @NotNull List<PsiElement> getTargets() {
        return myTargets;
    }

    @Override
    protected void selectTargets(@NotNull List<? extends PsiElement> targets, @NotNull Consumer<? super List<? extends PsiElement>> selectionConsumer) {
        selectionConsumer.consume(targets);
    }

    @Override
    public void computeUsages(@NotNull List<? extends PsiElement> targets) {
        // Add the variable itself to the list of usages
        addOccurrence(myVariable);

        // Get the variable name without the prefix (?, $?, ?*)
        String variableName = myVariable.getName();
        if (variableName == null || variableName.isEmpty()) {
            return;
        }

        // Check if the variable is global
        boolean isGlobal = isGlobalVariable(myVariable);
//        System.out.println("[DEBUG_LOG] Is global variable: " + isGlobal);

        // Find all occurrences of the variable in the file
        PsiFile file = myVariable.getContainingFile();

        if (isGlobal) {
            // For global variables, highlight all occurrences in the file
            findVariableOccurrences(file, variableName, null);
        } else {
            // For local variables, find the containing block and only highlight occurrences within that block
            PsiElement containingBlock = findContainingBlock(myVariable);
//            System.out.println("[DEBUG_LOG] Containing block: " + (containingBlock != null ? containingBlock.getText() : "null"));

            if (containingBlock != null) {
                // Only highlight occurrences within the containing block
                findVariableOccurrences(containingBlock, variableName, containingBlock);
            } else {
                // If we couldn't find a containing block, fall back to highlighting all occurrences
//                System.out.println("[DEBUG_LOG] Couldn't find containing block, highlighting all occurrences");
                findVariableOccurrences(file, variableName, null);
            }
        }
    }

    /**
     * Checks if a variable is a global variable.
     *
     * @param variable The variable element to check
     * @return True if the variable is global, false otherwise
     */
    private boolean isGlobalVariable(PsiElement variable) {
        if (variable instanceof LeafPsiElement leafElement) {
            return leafElement.getElementType() == CLIPSElementTypes.GLOBAL_VARIABLE;
        }

        // If it's a CLIPSNamedElement, check its text
        String text = variable.getText();
        return text != null && text.startsWith("?*") && text.endsWith("*");
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

        // Find all opening parentheses that are before the variable in the document
        List<PsiElement> openParens = new ArrayList<>();
        PsiElement element = file.getFirstChild();

        // Iterate through all elements in the file
        while (element != null) {
            if (element instanceof LeafPsiElement leafElement) {

                // Check if it's an opening parenthesis
                if (leafElement.getElementType() == CLIPSElementTypes.LPAREN) {
                    int parenOffset = leafElement.getTextRange().getStartOffset();

                    // Check if it's before the variable
                    if (parenOffset < variableOffset) {
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
                                    int blockEndOffset = blockEnd.getTextRange().getEndOffset();

                                    // Check if the variable is within this block
                                    if (blockEndOffset > variableOffset) {
                                        openParens.add(leafElement);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            element = element.getNextSibling();
        }

        // Find the opening parenthesis that is closest to the variable
        PsiElement closestParen = null;
        int closestDistance = Integer.MAX_VALUE;

        for (PsiElement paren : openParens) {
            int parenOffset = paren.getTextRange().getStartOffset();
            int distance = variableOffset - parenOffset;

            if (distance < closestDistance) {
                closestDistance = distance;
                closestParen = paren;
            }
        }

        // If we found a closest opening parenthesis, return the block
        if (closestParen != null) {
            PsiElement blockEnd = findMatchingClosingParenthesis(closestParen);
            if (blockEnd != null) {
                return findBlockElement(closestParen, blockEnd);
            }
        }

        return null;
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
     * Finds all occurrences of a variable with the given name in the element.
     * Recursively traverses the PSI tree to find usages.
     *
     * @param element The element to start searching from
     * @param variableName The variable name to look for (without prefix)
     * @param containingBlock The containing block to restrict the search to, or null to search everywhere
     */
    private void findVariableOccurrences(PsiElement element, String variableName, @Nullable PsiElement containingBlock) {
        // If a containing block is specified, don't search outside of it.
        if (containingBlock != null && !element.equals(containingBlock) && !com.intellij.psi.util.PsiTreeUtil.isAncestor(containingBlock, element, false)) {
            return;
        }

        // Check if the current element is a variable we are looking for.
        if (element instanceof CLIPSNamedElement named) {
            String currentVariableName = named.getName();
            if (variableName.equals(currentVariableName)) {
                addOccurrence(element);
            }
        }

//        System.out.println(element.getNode().getElementType() + " Going to recurse into children: " + Arrays.stream(element.getChildren()).map(a -> a.getNode().getElementType().toString()).collect(Collectors.joining(", ")));
        // Recurse into children.
        for (PsiElement child : element.getChildren()) {
            if (child instanceof PsiWhiteSpace) continue;

//            System.out.println("Recursing into child (" + child.getNode().getElementType() + "): " + child.getText());
            findVariableOccurrences(child, variableName, containingBlock);
        }
    }
}