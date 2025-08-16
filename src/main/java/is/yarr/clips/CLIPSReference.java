package is.yarr.clips;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import is.yarr.clips.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Provides reference resolution for CLIPS elements.
 * This enables features like "Go to Declaration" and "Find Usages".
 */
public class CLIPSReference extends PsiPolyVariantReferenceBase<PsiElement> {
    private final String name;
    private final ReferenceType type;
    private final boolean isAmbiguous;

    /**
     * Enum to distinguish between different types of references.
     */
    public enum ReferenceType {
        VARIABLE,
        GLOBAL_VARIABLE,
        TEMPLATE,
        RULE,
        FUNCTION,
        SLOT
    }

    /**
     * Creates a new CLIPS reference.
     *
     * @param element The element that contains the reference
     * @param textRange The text range within the element that represents the reference
     * @param name The name of the referenced element
     * @param type The type of the reference
     */
    public CLIPSReference(@NotNull PsiElement element, TextRange textRange, String name, ReferenceType type) {
        this(element, textRange, name, type, false);
    }

    /**
     * Creates a new CLIPS reference.
     *
     * @param element The element that contains the reference
     * @param textRange The text range within the element that represents the reference
     * @param name The name of the referenced element
     * @param type The type of the reference
     */
    public CLIPSReference(@NotNull PsiElement element, TextRange textRange, String name, ReferenceType type, boolean isAmbiguous) {
        super(element, textRange);
        this.name = name;
        this.type = type;
        this.isAmbiguous = isAmbiguous;
    }

    /**
     * Resolves the reference to its target elements.
     * This is used for "Go to Declaration" and other navigation features.
     *
     * @param incompleteCode Whether the code is incomplete
     * @return An array of resolve results
     */
    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        Project project = myElement.getProject();
        List<ResolveResult> results = new ArrayList<>();
        
        switch (type) {
            case VARIABLE -> findVariableDeclarations(project, results);
            case GLOBAL_VARIABLE -> findGlobalVariableDeclarations(project, results);
            case TEMPLATE -> findTemplateDeclarations(project, results);
            case RULE -> findRuleDeclarations(project, results);
            case FUNCTION -> findFunctionDeclarations(project, results);
            case SLOT -> findSlotDeclarations(project, results);
        }

        return results.toArray(ResolveResult[]::new);
    }

    /**
     * Provides variants for code completion.
     *
     * @return An array of lookup elements
     */
    @Override
    public Object @NotNull [] getVariants() {
        Project project = myElement.getProject();
        List<LookupElement> variants = new ArrayList<>();
        
        switch (type) {
            case VARIABLE -> addVariableVariants(project, variants);
            case GLOBAL_VARIABLE -> addGlobalVariableVariants(project, variants);
            case TEMPLATE -> addTemplateVariants(project, variants);
            case RULE -> addRuleVariants(project, variants);
            case FUNCTION -> addFunctionVariants(project, variants);
            case SLOT -> addSlotVariants(project, variants);
        }
        
        return variants.toArray();
    }

    public String getName() {
        return name;
    }

    public ReferenceType getType() {
        return type;
    }

    public boolean isAmbiguous() {
        return isAmbiguous;
    }

    /**
     * Finds variable declarations in the current scope.
     */
    private void findVariableDeclarations(Project project, List<ResolveResult> results) {

        // Find the containing construct (rule, function, etc.)
        PsiElement scope = findContainingScope(myElement);
        if (scope == null) {
            return;
        }
        
        // Find all variable declarations in the scope
        var variables = PsiTreeUtil.findChildrenOfType(scope, CLIPSVariableElement.class);

        for (CLIPSVariableElement variable : variables) {
            String varName = variable.getName();
            boolean isDecl = isDeclaration(variable);

            if (name.equals(varName) && isDecl) {
                results.add(new PsiElementResolveResult(variable));
            }
        }
        
        // Also find multifield variable declarations in the scope
        var multifieldVariables = PsiTreeUtil.findChildrenOfType(scope, CLIPSMultifieldVariableElement.class);

        for (CLIPSMultifieldVariableElement variable : multifieldVariables) {
            String varName = variable.getName();
            boolean isDecl = isMultifieldDeclaration(variable);

            if (name.equals(varName) && isDecl) {
                results.add(new PsiElementResolveResult(variable));
            }
        }

        var paramDefs = PsiTreeUtil.findChildrenOfType(scope, CLIPSParameter.class);

        for (CLIPSParameter param : paramDefs) {
            String varName = param.getName();

            if (name.equals(varName)) {
                results.add(new PsiElementResolveResult(param));
            }
        }
    }

    /**
     * Finds global variable declarations in the file.
     */
    private void findGlobalVariableDeclarations(Project project, List<ResolveResult> results) {
        // Find all global variable declarations in the file
        PsiFile file = myElement.getContainingFile();
        var globals = PsiTreeUtil.findChildrenOfType(file, CLIPSGlobalVariableDef.class);
        for (CLIPSGlobalVariableDef global : globals) {
            if (name.equals(global.getName())) {
                results.add(new PsiElementResolveResult(global));
            }
        }
    }

    /**
     * Finds template declarations in the file.
     */
    private void findTemplateDeclarations(Project project, List<ResolveResult> results) {
        // Find all template declarations in the file
        PsiFile file = myElement.getContainingFile();
        var templates = PsiTreeUtil.findChildrenOfType(file, CLIPSTemplateName.class);
        for (CLIPSTemplateName template : templates) {
            if (name.equals(template.getName())) {
//                PsiElement idLeaf = template.getNameIdentifier() != null ? template.getNameIdentifier() : template;
//                results.add(new PsiElementResolveResult(idLeaf));

                results.add(new PsiElementResolveResult(template));
            }
        }
    }

    /**
     * Finds rule declarations in the file.
     */
    private void findRuleDeclarations(Project project, List<ResolveResult> results) {
        // Find all rule declarations in the file
        PsiFile file = myElement.getContainingFile();
        var rules = PsiTreeUtil.findChildrenOfType(file, CLIPSRuleName.class);
        for (CLIPSRuleName rule : rules) {
            if (name.equals(rule.getName())) {
//                PsiElement idLeaf = rule.getNameIdentifier() != null ? rule.getNameIdentifier() : rule;
//                results.add(new PsiElementResolveResult(idLeaf));
                results.add(new PsiElementResolveResult(rule));
            }
        }
    }

    /**
     * Finds function declarations in the file.
     */
    private void findFunctionDeclarations(Project project, List<ResolveResult> results) {
        System.out.println("CLIPSReference.findFunctionDeclarations");
        // Find all function declarations in the file
        PsiFile file = myElement.getContainingFile();
        var functions = PsiTreeUtil.findChildrenOfType(file, CLIPSDeffunctionConstruct.class);
        for (CLIPSDeffunctionConstruct function : functions) {
            PsiElement nameElement = PsiTreeUtil.findChildOfType(function, CLIPSDeffunctionName.class);
            System.out.println("CLIPSReference.findFunctionDeclarations: function=" + function + ", nameElement=" + nameElement + ", name=" + name);
            if (nameElement != null && name.equals(nameElement.getText())) {
                results.add(new PsiElementResolveResult(nameElement));
            }
        }
    }

    /**
     * Finds slot declarations in the template.
     */
    private void findSlotDeclarations(Project project, List<ResolveResult> results) {
        // Find the containing template pattern
        PsiElement templatePattern = findContainingTemplatePattern(myElement);
        if (templatePattern == null) return;
        
        // Find the template name
        PsiElement templateNameElement = templatePattern.getFirstChild();
        if (templateNameElement == null) return;
        String templateName = templateNameElement.getText();
        
        // Find the template declaration
        PsiFile file = myElement.getContainingFile();
        var templates = PsiTreeUtil.findChildrenOfType(file, CLIPSDeftemplateConstruct.class);
        for (CLIPSDeftemplateConstruct template : templates) {
            CLIPSTemplateName nameElement = PsiTreeUtil.findChildOfType(template, CLIPSTemplateName.class);
            if (nameElement != null && templateName.equals(nameElement.getText())) {
                // Find the slot declaration
                var slots = PsiTreeUtil.findChildrenOfType(template, CLIPSSlotName.class);
                for (CLIPSSlotName slot : slots) {
                    if (name.equals(slot.getName())) {
//                        PsiElement idLeaf = slot.getNameIdentifier() != null ? slot.getNameIdentifier() : slot;
//                        results.add(new PsiElementResolveResult(idLeaf));
                        results.add(new PsiElementResolveResult(slot));
                    }
                }
            }
        }
    }

    /**
     * Adds variable variants for code completion.
     */
    private void addVariableVariants(Project project, List<LookupElement> variants) {
        // Find the containing construct (rule, function, etc.)
        PsiElement scope = findContainingScope(myElement);
        if (scope == null) return;
        
        // Find all variable declarations in the scope
        var variables = PsiTreeUtil.findChildrenOfType(scope, CLIPSVariableElement.class);
        for (CLIPSVariableElement variable : variables) {
            if (isDeclaration(variable)) {
                variants.add(LookupElementBuilder
                    .create(variable)
                    .withIcon(CLIPSIcons.FILE)
                    .withTypeText("Variable")
                );
            }
        }
        
        // Also find multifield variable declarations in the scope
        var multifieldVariables = PsiTreeUtil.findChildrenOfType(scope, CLIPSMultifieldVariableElement.class);
        for (CLIPSMultifieldVariableElement variable : multifieldVariables) {
            if (isMultifieldDeclaration(variable)) {
                variants.add(LookupElementBuilder
                    .create(variable)
                    .withIcon(CLIPSIcons.FILE)
                    .withTypeText("Multifield Variable")
                );
            }
        }
    }

    /**
     * Adds global variable variants for code completion.
     */
    private void addGlobalVariableVariants(Project project, List<LookupElement> variants) {
        // Find all global variable declarations in the file
        PsiFile file = myElement.getContainingFile();
        var globals = PsiTreeUtil.findChildrenOfType(file, CLIPSGlobalVariableDef.class);
        for (CLIPSGlobalVariableDef global : globals) {
            variants.add(LookupElementBuilder
                .create(global)
                .withIcon(CLIPSIcons.FILE)
                .withTypeText("Global Variable")
            );
        }
    }

    /**
     * Adds template variants for code completion.
     */
    private void addTemplateVariants(Project project, List<LookupElement> variants) {
        // Find all template declarations in the file
        PsiFile file = myElement.getContainingFile();
        var templates = PsiTreeUtil.findChildrenOfType(file, CLIPSTemplateName.class);
        for (CLIPSTemplateName template : templates) {
            variants.add(LookupElementBuilder
                .create(template)
                .withIcon(CLIPSIcons.FILE)
                .withTypeText("Template")
            );
        }
    }

    /**
     * Adds rule variants for code completion.
     */
    private void addRuleVariants(Project project, List<LookupElement> variants) {
        // Find all rule declarations in the file
        PsiFile file = myElement.getContainingFile();
        var rules = PsiTreeUtil.findChildrenOfType(file, CLIPSRuleName.class);
        for (CLIPSRuleName rule : rules) {
            variants.add(LookupElementBuilder
                .create(rule)
                .withIcon(CLIPSIcons.FILE)
                .withTypeText("Rule")
            );
        }
    }

    /**
     * Adds function variants for code completion.
     */
    private void addFunctionVariants(Project project, List<LookupElement> variants) {
        // Find all function declarations in the file
        PsiFile file = myElement.getContainingFile();
        var functions = PsiTreeUtil.findChildrenOfType(file, CLIPSDeffunctionConstruct.class);
        for (CLIPSDeffunctionConstruct function : functions) {
            PsiElement nameElement = PsiTreeUtil.findChildOfType(function, CLIPSDeffunctionName.class);
            if (nameElement != null) {
                variants.add(LookupElementBuilder
                    .create(nameElement.getText())
                    .withIcon(CLIPSIcons.FILE)
                    .withTypeText("Function")
                );
            }
        }
    }

    /**
     * Adds slot variants for code completion.
     */
    private void addSlotVariants(Project project, List<LookupElement> variants) {
        // Find the containing template pattern
        PsiElement templatePattern = findContainingTemplatePattern(myElement);
        if (templatePattern == null) return;
        
        // Find the template name
        PsiElement templateNameElement = templatePattern.getFirstChild();
        if (templateNameElement == null) return;
        String templateName = templateNameElement.getText();
        
        // Find the template declaration
        PsiFile file = myElement.getContainingFile();
        var templates = PsiTreeUtil.findChildrenOfType(file, CLIPSDeftemplateConstruct.class);
        for (CLIPSDeftemplateConstruct template : templates) {
            CLIPSTemplateName nameElement = PsiTreeUtil.findChildOfType(template, CLIPSTemplateName.class);
            if (nameElement != null && templateName.equals(nameElement.getText())) {
                // Find the slot declarations
                var slots = PsiTreeUtil.findChildrenOfType(template, CLIPSSlotName.class);
                for (CLIPSSlotName slot : slots) {
                    variants.add(LookupElementBuilder
                        .create(slot)
                        .withIcon(CLIPSIcons.FILE)
                        .withTypeText("Slot")
                    );
                }
            }
        }
    }

    /**
     * Finds the containing scope (rule, function, etc.) for a variable.
     * This method identifies the appropriate scope for variable declarations and references.
     */
    private PsiElement findContainingScope(PsiElement element) {
        System.out.println("[DEBUG_LOG] findContainingScope called for element: " + element);
        
        PsiElement scope = PsiTreeUtil.getParentOfType(element, 
            CLIPSDefruleConstruct.class, 
            CLIPSDeffunctionConstruct.class,
            CLIPSDeftemplateConstruct.class,
            CLIPSDefglobalConstruct.class,
            CLIPSDeffactsConstruct.class,
            CLIPSDefmoduleConstruct.class,
            CLIPSDefclassConstruct.class
        );
        
        System.out.println("[DEBUG_LOG] findContainingScope found scope: " + scope + 
                          (scope != null ? " (class: " + scope.getClass().getSimpleName() + ")" : ""));
        return scope;
    }

    /**
     * Finds the containing template pattern for a slot reference.
     */
    private PsiElement findContainingTemplatePattern(PsiElement element) {
        // This is a simplified implementation
        // In a real implementation, we would need to traverse the PSI tree
        // to find the template pattern that contains the slot reference
        return null;
    }

    /**
     * Checks if a variable element is a declaration.
     * In CLIPS, variables are often declared implicitly by their first use.
     * This method determines if the given variable is the first occurrence in its scope.
     */
    private boolean isDeclaration(CLIPSVariableElement variable) {
        System.out.println("[DEBUG_LOG] isDeclaration called for variable: " + variable);
        
        // Find the containing scope (rule, function, etc.)
        PsiElement scope = findContainingScope(variable);
        System.out.println("[DEBUG_LOG] isDeclaration scope: " + scope);
        if (scope == null) {
            System.out.println("[DEBUG_LOG] isDeclaration: No scope found, returning false");
            return false;
        }
        
        // Get the name of the variable (without the '?' prefix)
        String variableName = variable.getName();
        System.out.println("[DEBUG_LOG] isDeclaration variableName: " + variableName);
        if (variableName == null) {
            System.out.println("[DEBUG_LOG] isDeclaration: No variable name, returning false");
            return false;
        }
        
        // Find all variable elements in the scope
        Collection<CLIPSVariableElement> variables = PsiTreeUtil.findChildrenOfType(scope, CLIPSVariableElement.class);
        System.out.println("[DEBUG_LOG] isDeclaration found variables: count=" + variables.size());
        
        // Sort the variables by their text offset to find the first occurrence
        List<CLIPSVariableElement> sortedVariables = new ArrayList<>(variables);
        sortedVariables.sort(Comparator.comparingInt(PsiElement::getTextOffset));
        
        // Check if the given variable is the first occurrence of its name in the scope
        for (CLIPSVariableElement v : sortedVariables) {
            String vName = v.getName();
            int offset = v.getTextOffset();
            System.out.println("[DEBUG_LOG] isDeclaration checking variable: " + v + ", name=" + vName + ", offset=" + offset);

            if (!variableName.equals(vName)) {
                System.out.println("cont, variableName=" + variableName + ", vName=" + vName);
                continue;
            }

            // Special-case: (bind ?name <expr>) — the ?name right after 'bind' is a declaration
            var parentExpr = PsiTreeUtil.getParentOfType(v, CLIPSFunctionCall.class);
            if (parentExpr != null) {
                var firstChild = parentExpr.getFirstChild(); // LPAREN
                var secondChild = firstChild != null ? firstChild.getNextSibling() : null; // function name token
                var thirdChild = secondChild != null ? secondChild.getNextSibling() : null; // first argument PSI
                if (secondChild != null && "bind".equals(secondChild.getText())) {
                    // The first argument should be this variable leaf or its VariableElement wrapper
                    if (thirdChild != null && thirdChild.getText().equals("?" + variableName)) {
                        System.out.println("[DEBUG_LOG] isDeclaration: detected bind declaration for " + variableName);
                        return v.equals(variable);
                    }
                }
            }

            boolean isFirst = v.equals(variable);
            if (isFirst) {
                System.out.println("[DEBUG_LOG] isDeclaration found matching name: " + v + ", isFirst=" + isFirst);
                return true;
            }
//            return isFirst;
        }
        
        System.out.println("[DEBUG_LOG] isDeclaration: No matching variable found, returning false");
        return false;
    }
    
    /**
     * Checks if a multifield variable element is a declaration.
     * In CLIPS, variables are often declared implicitly by their first use.
     * This method determines if the given multifield variable is the first occurrence in its scope.
     */
    private boolean isMultifieldDeclaration(CLIPSMultifieldVariableElement variable) {
        System.out.println("[DEBUG_LOG] isMultifieldDeclaration called for variable: " + variable);
        
        // Find the containing scope (rule, function, etc.)
        PsiElement scope = findContainingScope(variable);
        System.out.println("[DEBUG_LOG] isMultifieldDeclaration scope: " + scope);
        if (scope == null) {
            System.out.println("[DEBUG_LOG] isMultifieldDeclaration: No scope found, returning false");
            return false;
        }
        
        // Get the name of the variable (without the '$?' prefix)
        String variableName = variable.getName();
        System.out.println("[DEBUG_LOG] isMultifieldDeclaration variableName: " + variableName);
        if (variableName == null) {
            System.out.println("[DEBUG_LOG] isMultifieldDeclaration: No variable name, returning false");
            return false;
        }
        
        // Find all multifield variable elements in the scope
        Collection<CLIPSMultifieldVariableElement> variables = PsiTreeUtil.findChildrenOfType(scope, CLIPSMultifieldVariableElement.class);
        System.out.println("[DEBUG_LOG] isMultifieldDeclaration found variables: count=" + variables.size());
        
        // Sort the variables by their text offset to find the first occurrence
        List<CLIPSMultifieldVariableElement> sortedVariables = new ArrayList<>(variables);
        sortedVariables.sort((v1, v2) -> v1.getTextOffset() - v2.getTextOffset());
        
        // Check if the given variable is the first occurrence of its name in the scope
        for (CLIPSMultifieldVariableElement v : sortedVariables) {
            String vName = v.getName();
            int offset = v.getTextOffset();
            System.out.println("[DEBUG_LOG] isMultifieldDeclaration checking variable: " + v + ", name=" + vName + ", offset=" + offset);
            
            if (variableName.equals(vName)) {
                boolean isFirst = v.equals(variable);
                System.out.println("[DEBUG_LOG] isMultifieldDeclaration found matching name: " + v + ", isFirst=" + isFirst);
                return isFirst;
            }
        }
        
        System.out.println("[DEBUG_LOG] isMultifieldDeclaration: No matching variable found, returning false");
        return false;
    }
}