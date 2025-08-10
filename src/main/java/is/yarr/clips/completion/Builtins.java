package is.yarr.clips.completion;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;

import java.util.List;
import java.util.Map;

/**
 * Central registry of CLIPS built-in constructs, keywords, and function signatures.
 * Keep this as the single source of truth for static completion items.
 */
public final class Builtins {
    private Builtins() {}

    public static final Map<String, String> FUNCTION_PARAMS = Map.ofEntries(
        Map.entry("assert", "(<fact-specification>)"),
        Map.entry("retract", "(<fact-index>+)"),
        Map.entry("modify", "(<fact-index> (<slot-name> <value>)+)"),
        Map.entry("duplicate", "(<fact-index> (<slot-name> <value>)*)"),
        Map.entry("printout", "(logical-name <expression>*)"),
        Map.entry("bind", "(?<variable-name> <expression>)"),
        Map.entry("if", "(<test-expression> then <action>* [else <action>*])"),
        Map.entry("while", "(<test-expression> do <action>*)"),
        Map.entry("loop-for-count", "(<count-spec> [do] <action>*)"),
        Map.entry("return", "(<expression>)"),
        Map.entry("gensym", "()"),
        Map.entry("length", "(<multifield-expression>)"),
        Map.entry("length$", "(<multifield-expression>)"),
        Map.entry("member", "(<value> <multifield-expression>)"),
        Map.entry("member$", "(<value> <multifield-expression>)"),
        Map.entry("subseq", "(<multifield-expression> <start> <end>)"),
        Map.entry("subseq$", "(<multifield-expression> <start> <end>)"),
        Map.entry("nth", "(<integer-expression> <multifield-expression>)"),
        Map.entry("nth$", "(<integer-expression> <multifield-expression>)"),
        Map.entry("progn", "(<expression>*)"),
        Map.entry("progn$", "(<multifield-expression> <expression>*)"),
        Map.entry("str-cat", "(<expression>*)"),
        Map.entry("str-length", "(<string-expression>)"),
        Map.entry("str-compare", "(<string-expression> <string-expression>)"),
        Map.entry("str-index", "(<string-expression> <string-expression>)"),
        Map.entry("sub-string", "(<start> <end> <string-expression>)"),
        Map.entry("sym-cat", "(<expression>*)"),
        Map.entry("upcase", "(<string-expression>)"),
        Map.entry("lowcase", "(<string-expression>)")
    );

    // Top-level constructs
    public static final List<LookupElementBuilder> CONSTRUCTS = List.of(
        LookupElementBuilder.create("defrule").bold().withTypeText("Construct").withIcon(AllIcons.Nodes.Class)
            .withTailText(" name [comment] [declaration]* [pattern]* => [action]*", true),
        LookupElementBuilder.create("deftemplate").bold().withTypeText("Construct").withIcon(AllIcons.Nodes.Class)
            .withTailText(" name [comment] [slot-definition]*", true),
        LookupElementBuilder.create("deffacts").bold().withTypeText("Construct").withIcon(AllIcons.Nodes.Class)
            .withTailText(" name [comment] [fact-specification]*", true),
        LookupElementBuilder.create("deffunction").bold().withTypeText("Construct").withIcon(AllIcons.Nodes.Function)
            .withTailText(" name [comment] ([parameter]*) [action]*", true),
        LookupElementBuilder.create("defmodule").bold().withTypeText("Construct").withIcon(AllIcons.Nodes.Module)
            .withTailText(" name [comment] [port-specification]*", true),
        LookupElementBuilder.create("defglobal").bold().withTypeText("Construct").withIcon(AllIcons.Nodes.Variable)
            .withTailText(" [?*name* = value]*", true),
        LookupElementBuilder.create("defclass").bold().withTypeText("Construct").withIcon(AllIcons.Nodes.Class)
            .withTailText(" name [comment] (is-a [class-name]+) [slot-definition]*", true),
        LookupElementBuilder.create("definstances").bold().withTypeText("Construct").withIcon(AllIcons.Nodes.Class)
            .withTailText(" name [comment] [instance-definition]*", true),
        LookupElementBuilder.create("defmessage-handler").bold().withTypeText("Construct").withIcon(AllIcons.Nodes.Method)
            .withTailText(" class-name name [comment] [parameter-specification] [action]*", true),
        LookupElementBuilder.create("defgeneric").bold().withTypeText("Construct").withIcon(AllIcons.Nodes.Method)
            .withTailText(" name [comment]", true),
        LookupElementBuilder.create("defmethod").bold().withTypeText("Construct").withIcon(AllIcons.Nodes.Method)
            .withTailText(" name [index] [comment] [parameter-specification] [action]*", true)
    );

    // Deftemplate slot keywords and attributes
    public static final List<LookupElementBuilder> SLOT_KEYWORDS = List.of(
        LookupElementBuilder.create("slot").bold().withTypeText("Slot").withIcon(AllIcons.Nodes.Field)
            .withTailText(" name [attribute]*", true),
        LookupElementBuilder.create("multislot").bold().withTypeText("Slot").withIcon(AllIcons.Nodes.Field)
            .withTailText(" name [attribute]*", true),
        LookupElementBuilder.create("type").bold().withTypeText("Slot Attribute")
            .withTailText(" SYMBOL | STRING | INTEGER | FLOAT | NUMBER | LEXEME | OBJECT | INSTANCE-NAME | INSTANCE-ADDRESS | EXTERNAL-ADDRESS | FACT-ADDRESS", true),
        LookupElementBuilder.create("default").bold().withTypeText("Slot Attribute").withTailText(" value", true),
        LookupElementBuilder.create("default-dynamic").bold().withTypeText("Slot Attribute").withTailText(" expression", true),
        LookupElementBuilder.create("allowed-symbols").bold().withTypeText("Slot Attribute").withTailText(" symbol+", true),
        LookupElementBuilder.create("allowed-strings").bold().withTypeText("Slot Attribute").withTailText(" string+", true),
        LookupElementBuilder.create("allowed-numbers").bold().withTypeText("Slot Attribute").withTailText(" number+", true),
        LookupElementBuilder.create("allowed-values").bold().withTypeText("Slot Attribute").withTailText(" value+", true),
        LookupElementBuilder.create("range").bold().withTypeText("Slot Attribute").withTailText(" min max", true)
    );

    // LHS logical/declare keywords
    public static final List<LookupElementBuilder> LHS_LOGICAL = List.of(
        LookupElementBuilder.create("and").bold().withTypeText("Logical").withTailText(" expression+", true),
        LookupElementBuilder.create("or").bold().withTypeText("Logical").withTailText(" expression+", true),
        LookupElementBuilder.create("not").bold().withTypeText("Logical").withTailText(" expression", true),
        LookupElementBuilder.create("test").bold().withTypeText("Logical").withTailText(" expression", true),
        LookupElementBuilder.create("exists").bold().withTypeText("Logical").withTailText(" pattern+", true),
        LookupElementBuilder.create("forall").bold().withTypeText("Logical").withTailText(" pattern+ pattern+", true),
        LookupElementBuilder.create("logical").bold().withTypeText("Logical").withTailText(" pattern+", true),
        LookupElementBuilder.create("declare").bold().withTypeText("Rule Attribute").withTailText(" (salience value) | (auto-focus TRUE | FALSE)", true),
        LookupElementBuilder.create("salience").bold().withTypeText("Rule Attribute").withTailText(" value", true),
        LookupElementBuilder.create("auto-focus").bold().withTypeText("Rule Attribute").withTailText(" TRUE | FALSE", true)
    );

    // Control flow (RHS)
    public static final List<String> CONTROL_FLOW_FUNCS = List.of("if", "then", "else", "while", "loop-for-count", "return", "progn", "progn$");

    // IO functions/consts (names that are not in FUNCTION_PARAMS get generic tails)
    public static final List<String> IO_FUNCS = List.of("printout", "open", "close", "read", "readline", "format");

    // Fact manipulation
    public static final List<String> FACT_FUNCS = List.of("assert", "retract", "modify", "duplicate", "facts", "fact-index", "fact-relation", "fact-slot-value");

    // String functions
    public static final List<String> STRING_FUNCS = List.of("str-cat", "str-length", "str-compare", "str-index", "sub-string", "sym-cat", "upcase", "lowcase");

    // Multifield functions
    public static final List<String> MULTIFIELD_FUNCS = List.of("length", "length$", "member", "member$", "subseq", "subseq$", "nth", "nth$", "create$", "delete$", "replace$", "insert$", "explode$", "implode$");

    // Math and comparison symbols/functions
    public static final List<String> MATH_AND_COMPARE = List.of(
        "+", "-", "*", "/", "div", "mod", "max", "min", "abs", "sqrt", "exp", "log", "log10", "round", "integer", "float",
        "=", "<>", "!=", "<", ">", "<=", ">=", "eq", "neq"
    );

    // Constants
    public static final List<String> CONSTANTS = List.of("TRUE", "FALSE", "nil", "crlf", "pi");

    public static LookupElementBuilder function(String name, String category) {
        var builder = LookupElementBuilder.create(name).withTypeText(category).withIcon(AllIcons.Nodes.Function);
        var tail = FUNCTION_PARAMS.get(name);
        if (tail != null) builder = builder.withTailText(" " + tail, true);
        return builder;
    }
}
