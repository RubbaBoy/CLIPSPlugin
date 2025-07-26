package is.yarr.clips.completion;

import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.util.ProcessingContext;
import is.yarr.clips.CLIPSLanguage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Completion contributor for CLIPS language.
 * Provides code completion for CLIPS keywords and built-in functions.
 */
public class CLIPSCompletionContributor extends CompletionContributor {
    
    // Maps for storing function parameter info
    private static final Map<String, String> FUNCTION_PARAMS = new HashMap<>();
    
    static {
        // Initialize function parameter info
        FUNCTION_PARAMS.put("assert", "(<fact-specification>)");
        FUNCTION_PARAMS.put("retract", "(<fact-index>+)");
        FUNCTION_PARAMS.put("modify", "(<fact-index> (<slot-name> <value>)+)");
        FUNCTION_PARAMS.put("duplicate", "(<fact-index> (<slot-name> <value>)*)");
        FUNCTION_PARAMS.put("printout", "(logical-name <expression>*)");
        FUNCTION_PARAMS.put("bind", "(?<variable-name> <expression>)");
        FUNCTION_PARAMS.put("if", "(<test-expression> then <action>* [else <action>*])");
        FUNCTION_PARAMS.put("while", "(<test-expression> do <action>*)");
        FUNCTION_PARAMS.put("loop-for-count", "(<count-spec> [do] <action>*)");
        FUNCTION_PARAMS.put("return", "(<expression>)");
        FUNCTION_PARAMS.put("gensym", "()");
        FUNCTION_PARAMS.put("length", "(<multifield-expression>)");
        FUNCTION_PARAMS.put("length$", "(<multifield-expression>)");
        FUNCTION_PARAMS.put("member", "(<value> <multifield-expression>)");
        FUNCTION_PARAMS.put("member$", "(<value> <multifield-expression>)");
        FUNCTION_PARAMS.put("subseq", "(<multifield-expression> <start> <end>)");
        FUNCTION_PARAMS.put("subseq$", "(<multifield-expression> <start> <end>)");
        FUNCTION_PARAMS.put("nth", "(<integer-expression> <multifield-expression>)");
        FUNCTION_PARAMS.put("nth$", "(<integer-expression> <multifield-expression>)");
        FUNCTION_PARAMS.put("progn", "(<expression>*)");
        FUNCTION_PARAMS.put("progn$", "(<multifield-expression> <expression>*)");
        FUNCTION_PARAMS.put("str-cat", "(<expression>*)");
        FUNCTION_PARAMS.put("str-length", "(<string-expression>)");
        FUNCTION_PARAMS.put("str-compare", "(<string-expression> <string-expression>)");
        FUNCTION_PARAMS.put("str-index", "(<string-expression> <string-expression>)");
        FUNCTION_PARAMS.put("sub-string", "(<start> <end> <string-expression>)");
        FUNCTION_PARAMS.put("sym-cat", "(<expression>*)");
        FUNCTION_PARAMS.put("upcase", "(<string-expression>)");
        FUNCTION_PARAMS.put("lowcase", "(<string-expression>)");
    }
    
    public CLIPSCompletionContributor() {
        // Add completion for keywords and functions
        extend(CompletionType.BASIC,
                PlatformPatterns.psiElement().withLanguage(CLIPSLanguage.INSTANCE),
                new CompletionProvider<>() {
                    @Override
                    protected void addCompletions(@NotNull CompletionParameters parameters,
                                                 @NotNull ProcessingContext context,
                                                 @NotNull CompletionResultSet result) {
                        // Add CLIPS construct keywords
                        addConstructKeywords(result);
                        
                        // Add CLIPS slot keywords
                        addSlotKeywords(result);
                        
                        // Add CLIPS logical keywords
                        addLogicalKeywords(result);
                        
                        // Add CLIPS control flow functions
                        addControlFlowFunctions(result);
                        
                        // Add CLIPS I/O functions
                        addIOFunctions(result);
                        
                        // Add CLIPS fact manipulation functions
                        addFactFunctions(result);
                        
                        // Add CLIPS string functions
                        addStringFunctions(result);
                        
                        // Add CLIPS list/multifield functions
                        addMultifieldFunctions(result);
                        
                        // Add CLIPS math functions
                        addMathFunctions(result);
                        
                        // Add CLIPS predefined constants
                        addConstants(result);
                    }
                });
    }
    
    /**
     * Adds CLIPS construct keywords to the completion result set.
     */
    private void addConstructKeywords(CompletionResultSet result) {
        result.addElement(LookupElementBuilder.create("defrule").bold()
                .withTypeText("Construct")
                .withIcon(AllIcons.Nodes.Class)
                .withTailText(" name [comment] [declaration]* [pattern]* => [action]*", true));
                
        result.addElement(LookupElementBuilder.create("deftemplate").bold()
                .withTypeText("Construct")
                .withIcon(AllIcons.Nodes.Class)
                .withTailText(" name [comment] [slot-definition]*", true));
                
        result.addElement(LookupElementBuilder.create("deffacts").bold()
                .withTypeText("Construct")
                .withIcon(AllIcons.Nodes.Class)
                .withTailText(" name [comment] [fact-specification]*", true));
                
        result.addElement(LookupElementBuilder.create("deffunction").bold()
                .withTypeText("Construct")
                .withIcon(AllIcons.Nodes.Function)
                .withTailText(" name [comment] ([parameter]*) [action]*", true));
                
        result.addElement(LookupElementBuilder.create("defmodule").bold()
                .withTypeText("Construct")
                .withIcon(AllIcons.Nodes.Module)
                .withTailText(" name [comment] [port-specification]*", true));
                
        result.addElement(LookupElementBuilder.create("defglobal").bold()
                .withTypeText("Construct")
                .withIcon(AllIcons.Nodes.Variable)
                .withTailText(" [?*name* = value]*", true));
                
        result.addElement(LookupElementBuilder.create("defclass").bold()
                .withTypeText("Construct")
                .withIcon(AllIcons.Nodes.Class)
                .withTailText(" name [comment] (is-a [class-name]+) [slot-definition]*", true));
                
        result.addElement(LookupElementBuilder.create("definstances").bold()
                .withTypeText("Construct")
                .withIcon(AllIcons.Nodes.Class)
                .withTailText(" name [comment] [instance-definition]*", true));
                
        result.addElement(LookupElementBuilder.create("defmessage-handler").bold()
                .withTypeText("Construct")
                .withIcon(AllIcons.Nodes.Method)
                .withTailText(" class-name name [comment] [parameter-specification] [action]*", true));
                
        result.addElement(LookupElementBuilder.create("defgeneric").bold()
                .withTypeText("Construct")
                .withIcon(AllIcons.Nodes.Method)
                .withTailText(" name [comment]", true));
                
        result.addElement(LookupElementBuilder.create("defmethod").bold()
                .withTypeText("Construct")
                .withIcon(AllIcons.Nodes.Method)
                .withTailText(" name [index] [comment] [parameter-specification] [action]*", true));
    }
    
    /**
     * Adds CLIPS slot keywords to the completion result set.
     */
    private void addSlotKeywords(CompletionResultSet result) {
        result.addElement(LookupElementBuilder.create("slot").bold()
                .withTypeText("Slot")
                .withIcon(AllIcons.Nodes.Field)
                .withTailText(" name [attribute]*", true));
                
        result.addElement(LookupElementBuilder.create("multislot").bold()
                .withTypeText("Slot")
                .withIcon(AllIcons.Nodes.Field)
                .withTailText(" name [attribute]*", true));
                
        result.addElement(LookupElementBuilder.create("type").bold()
                .withTypeText("Slot Attribute")
                .withTailText(" SYMBOL | STRING | INTEGER | FLOAT | NUMBER | LEXEME | OBJECT | INSTANCE-NAME | INSTANCE-ADDRESS | EXTERNAL-ADDRESS | FACT-ADDRESS", true));
                
        result.addElement(LookupElementBuilder.create("default").bold()
                .withTypeText("Slot Attribute")
                .withTailText(" value", true));
                
        result.addElement(LookupElementBuilder.create("default-dynamic").bold()
                .withTypeText("Slot Attribute")
                .withTailText(" expression", true));
                
        result.addElement(LookupElementBuilder.create("allowed-symbols").bold()
                .withTypeText("Slot Attribute")
                .withTailText(" symbol+", true));
                
        result.addElement(LookupElementBuilder.create("allowed-strings").bold()
                .withTypeText("Slot Attribute")
                .withTailText(" string+", true));
                
        result.addElement(LookupElementBuilder.create("allowed-numbers").bold()
                .withTypeText("Slot Attribute")
                .withTailText(" number+", true));
                
        result.addElement(LookupElementBuilder.create("allowed-values").bold()
                .withTypeText("Slot Attribute")
                .withTailText(" value+", true));
                
        result.addElement(LookupElementBuilder.create("range").bold()
                .withTypeText("Slot Attribute")
                .withTailText(" min max", true));
    }
    
    /**
     * Adds CLIPS logical keywords to the completion result set.
     */
    private void addLogicalKeywords(CompletionResultSet result) {
        result.addElement(LookupElementBuilder.create("and").bold()
                .withTypeText("Logical")
                .withTailText(" expression+", true));
                
        result.addElement(LookupElementBuilder.create("or").bold()
                .withTypeText("Logical")
                .withTailText(" expression+", true));
                
        result.addElement(LookupElementBuilder.create("not").bold()
                .withTypeText("Logical")
                .withTailText(" expression", true));
                
        result.addElement(LookupElementBuilder.create("test").bold()
                .withTypeText("Logical")
                .withTailText(" expression", true));
                
        result.addElement(LookupElementBuilder.create("exists").bold()
                .withTypeText("Logical")
                .withTailText(" pattern+", true));
                
        result.addElement(LookupElementBuilder.create("forall").bold()
                .withTypeText("Logical")
                .withTailText(" pattern+ pattern+", true));
                
        result.addElement(LookupElementBuilder.create("logical").bold()
                .withTypeText("Logical")
                .withTailText(" pattern+", true));
                
        result.addElement(LookupElementBuilder.create("declare").bold()
                .withTypeText("Rule Attribute")
                .withTailText(" (salience value) | (auto-focus TRUE | FALSE)", true));
                
        result.addElement(LookupElementBuilder.create("salience").bold()
                .withTypeText("Rule Attribute")
                .withTailText(" value", true));
                
        result.addElement(LookupElementBuilder.create("auto-focus").bold()
                .withTypeText("Rule Attribute")
                .withTailText(" TRUE | FALSE", true));
    }
    
    /**
     * Adds CLIPS control flow functions to the completion result set.
     */
    private void addControlFlowFunctions(CompletionResultSet result) {
        addFunctionWithParams(result, "if", "Control Flow");
        addFunctionWithParams(result, "then", "Control Flow");
        addFunctionWithParams(result, "else", "Control Flow");
        addFunctionWithParams(result, "while", "Control Flow");
        addFunctionWithParams(result, "loop-for-count", "Control Flow");
        addFunctionWithParams(result, "return", "Control Flow");
        addFunctionWithParams(result, "progn", "Control Flow");
        addFunctionWithParams(result, "progn$", "Control Flow");
        
        result.addElement(LookupElementBuilder.create("do").withTypeText("Control Flow"));
        result.addElement(LookupElementBuilder.create("break").withTypeText("Control Flow"));
        result.addElement(LookupElementBuilder.create("switch").withTypeText("Control Flow")
                .withTailText(" expression case-action+", true));
        result.addElement(LookupElementBuilder.create("case").withTypeText("Control Flow")
                .withTailText(" expression then action+", true));
        result.addElement(LookupElementBuilder.create("default").withTypeText("Control Flow")
                .withTailText(" action+", true));
    }
    
    /**
     * Adds CLIPS I/O functions to the completion result set.
     */
    private void addIOFunctions(CompletionResultSet result) {
        addFunctionWithParams(result, "printout", "I/O");
        
        result.addElement(LookupElementBuilder.create("open").withTypeText("I/O")
                .withTailText(" file-name mode logical-name", true));
        result.addElement(LookupElementBuilder.create("close").withTypeText("I/O")
                .withTailText(" logical-name", true));
        result.addElement(LookupElementBuilder.create("read").withTypeText("I/O")
                .withTailText(" [logical-name]", true));
        result.addElement(LookupElementBuilder.create("readline").withTypeText("I/O")
                .withTailText(" [logical-name]", true));
        result.addElement(LookupElementBuilder.create("format").withTypeText("I/O")
                .withTailText(" logical-name format-string expression*", true));
        
        result.addElement(LookupElementBuilder.create("crlf").withTypeText("I/O Constant"));
        result.addElement(LookupElementBuilder.create("tab").withTypeText("I/O Constant"));
        result.addElement(LookupElementBuilder.create("t").withTypeText("I/O Constant")
                .withTailText(" (standard output)", true));
    }
    
    /**
     * Adds CLIPS fact manipulation functions to the completion result set.
     */
    private void addFactFunctions(CompletionResultSet result) {
        addFunctionWithParams(result, "assert", "Fact");
        addFunctionWithParams(result, "retract", "Fact");
        addFunctionWithParams(result, "modify", "Fact");
        addFunctionWithParams(result, "duplicate", "Fact");
        
        result.addElement(LookupElementBuilder.create("facts").withTypeText("Fact")
                .withTailText(" [logical-name] [module-name]", true));
        result.addElement(LookupElementBuilder.create("fact-index").withTypeText("Fact")
                .withTailText(" <fact-address>", true));
        result.addElement(LookupElementBuilder.create("fact-relation").withTypeText("Fact")
                .withTailText(" <fact-address>", true));
        result.addElement(LookupElementBuilder.create("fact-slot-value").withTypeText("Fact")
                .withTailText(" <fact-address> <slot-name>", true));
    }
    
    /**
     * Adds CLIPS string functions to the completion result set.
     */
    private void addStringFunctions(CompletionResultSet result) {
        addFunctionWithParams(result, "str-cat", "String");
        addFunctionWithParams(result, "str-length", "String");
        addFunctionWithParams(result, "str-compare", "String");
        addFunctionWithParams(result, "str-index", "String");
        addFunctionWithParams(result, "sub-string", "String");
        addFunctionWithParams(result, "sym-cat", "String");
        addFunctionWithParams(result, "upcase", "String");
        addFunctionWithParams(result, "lowcase", "String");
    }
    
    /**
     * Adds CLIPS list/multifield functions to the completion result set.
     */
    private void addMultifieldFunctions(CompletionResultSet result) {
        addFunctionWithParams(result, "length", "Multifield");
        addFunctionWithParams(result, "length$", "Multifield");
        addFunctionWithParams(result, "member", "Multifield");
        addFunctionWithParams(result, "member$", "Multifield");
        addFunctionWithParams(result, "subseq", "Multifield");
        addFunctionWithParams(result, "subseq$", "Multifield");
        addFunctionWithParams(result, "nth", "Multifield");
        addFunctionWithParams(result, "nth$", "Multifield");
        
        result.addElement(LookupElementBuilder.create("create$").withTypeText("Multifield")
                .withTailText(" value*", true));
        result.addElement(LookupElementBuilder.create("delete$").withTypeText("Multifield")
                .withTailText(" <multifield-expression> <begin> <end>", true));
        result.addElement(LookupElementBuilder.create("replace$").withTypeText("Multifield")
                .withTailText(" <multifield-expression> <begin> <end> value*", true));
        result.addElement(LookupElementBuilder.create("insert$").withTypeText("Multifield")
                .withTailText(" <multifield-expression> <index> value*", true));
        result.addElement(LookupElementBuilder.create("explode$").withTypeText("Multifield")
                .withTailText(" <string-expression>", true));
        result.addElement(LookupElementBuilder.create("implode$").withTypeText("Multifield")
                .withTailText(" <multifield-expression>", true));
    }
    
    /**
     * Adds CLIPS math functions to the completion result set.
     */
    private void addMathFunctions(CompletionResultSet result) {
        result.addElement(LookupElementBuilder.create("+").withTypeText("Math")
                .withTailText(" number+", true));
        result.addElement(LookupElementBuilder.create("-").withTypeText("Math")
                .withTailText(" number+", true));
        result.addElement(LookupElementBuilder.create("*").withTypeText("Math")
                .withTailText(" number+", true));
        result.addElement(LookupElementBuilder.create("/").withTypeText("Math")
                .withTailText(" number+", true));
        result.addElement(LookupElementBuilder.create("div").withTypeText("Math")
                .withTailText(" number number", true));
        result.addElement(LookupElementBuilder.create("mod").withTypeText("Math")
                .withTailText(" number number", true));
        result.addElement(LookupElementBuilder.create("max").withTypeText("Math")
                .withTailText(" number+", true));
        result.addElement(LookupElementBuilder.create("min").withTypeText("Math")
                .withTailText(" number+", true));
        result.addElement(LookupElementBuilder.create("abs").withTypeText("Math")
                .withTailText(" number", true));
        result.addElement(LookupElementBuilder.create("sqrt").withTypeText("Math")
                .withTailText(" number", true));
        result.addElement(LookupElementBuilder.create("exp").withTypeText("Math")
                .withTailText(" number", true));
        result.addElement(LookupElementBuilder.create("log").withTypeText("Math")
                .withTailText(" number", true));
        result.addElement(LookupElementBuilder.create("log10").withTypeText("Math")
                .withTailText(" number", true));
        result.addElement(LookupElementBuilder.create("round").withTypeText("Math")
                .withTailText(" number", true));
        result.addElement(LookupElementBuilder.create("integer").withTypeText("Math")
                .withTailText(" expression", true));
        result.addElement(LookupElementBuilder.create("float").withTypeText("Math")
                .withTailText(" expression", true));
        
        result.addElement(LookupElementBuilder.create("=").withTypeText("Comparison")
                .withTailText(" number number", true));
        result.addElement(LookupElementBuilder.create("<>").withTypeText("Comparison")
                .withTailText(" number number", true));
        result.addElement(LookupElementBuilder.create("!=").withTypeText("Comparison")
                .withTailText(" number number", true));
        result.addElement(LookupElementBuilder.create("<").withTypeText("Comparison")
                .withTailText(" number number", true));
        result.addElement(LookupElementBuilder.create(">").withTypeText("Comparison")
                .withTailText(" number number", true));
        result.addElement(LookupElementBuilder.create("<=").withTypeText("Comparison")
                .withTailText(" number number", true));
        result.addElement(LookupElementBuilder.create(">=").withTypeText("Comparison")
                .withTailText(" number number", true));
        result.addElement(LookupElementBuilder.create("eq").withTypeText("Comparison")
                .withTailText(" value value", true));
        result.addElement(LookupElementBuilder.create("neq").withTypeText("Comparison")
                .withTailText(" value value", true));
    }
    
    /**
     * Adds CLIPS predefined constants to the completion result set.
     */
    private void addConstants(CompletionResultSet result) {
        result.addElement(LookupElementBuilder.create("TRUE").withTypeText("Constant"));
        result.addElement(LookupElementBuilder.create("FALSE").withTypeText("Constant"));
        result.addElement(LookupElementBuilder.create("nil").withTypeText("Constant"));
        result.addElement(LookupElementBuilder.create("crlf").withTypeText("Constant"));
        result.addElement(LookupElementBuilder.create("pi").withTypeText("Constant"));
    }
    
    /**
     * Helper method to add a function with its parameters to the completion result set.
     */
    private void addFunctionWithParams(CompletionResultSet result, String functionName, String category) {
        LookupElementBuilder builder = LookupElementBuilder.create(functionName)
                .withTypeText(category)
                .withIcon(AllIcons.Nodes.Function);
        
        String params = FUNCTION_PARAMS.get(functionName);
        if (params != null) {
            builder = builder.withTailText(" " + params, true);
        }
        
        result.addElement(builder);
    }
}