package is.yarr.clips;

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

/**
 * Provides a settings page for customizing CLIPS syntax highlighting colors.
 */
public class CLIPSColorSettingsPage implements ColorSettingsPage {
    private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
            new AttributesDescriptor("Keyword", CLIPSSyntaxHighlighter.KEYWORD),
            new AttributesDescriptor("Built-in function", CLIPSSyntaxHighlighter.BUILTIN_FUNCTION),
            new AttributesDescriptor("User function call", CLIPSSyntaxHighlighter.USER_FUNCTION_CALL),
            new AttributesDescriptor("Template call", CLIPSSyntaxHighlighter.TEMPLATE_CALL),
            new AttributesDescriptor("Variable", CLIPSSyntaxHighlighter.VARIABLE),
            new AttributesDescriptor("Global variable", CLIPSSyntaxHighlighter.GLOBAL_VARIABLE),
            new AttributesDescriptor("Multifield variable", CLIPSSyntaxHighlighter.MULTIFIELD_VARIABLE),
            new AttributesDescriptor("String", CLIPSSyntaxHighlighter.STRING),
            new AttributesDescriptor("Number", CLIPSSyntaxHighlighter.NUMBER),
            new AttributesDescriptor("Comment", CLIPSSyntaxHighlighter.COMMENT),
            new AttributesDescriptor("Identifier", CLIPSSyntaxHighlighter.IDENTIFIER),
            new AttributesDescriptor("Parentheses", CLIPSSyntaxHighlighter.PARENTHESES),
            new AttributesDescriptor("Operator", CLIPSSyntaxHighlighter.OPERATOR),
            new AttributesDescriptor("Bad character", CLIPSSyntaxHighlighter.BAD_CHARACTER),
    };

    @Nullable
    @Override
    public Icon getIcon() {
        return CLIPSIcons.FILE;
    }

    @NotNull
    @Override
    public SyntaxHighlighter getHighlighter() {
        return new CLIPSSyntaxHighlighter();
    }

    @NotNull
    @Override
    public String getDemoText() {
        return """
               ; This is a comment
               
               (deftemplate person
                 (slot name (type STRING))
                 (slot age (type INTEGER))
                 (multislot hobbies)
               )
               
               (deffunction foo (?x) (return ?x))
               
               (defglobal
                 ?*max-age* = 100
                 ?*min-age* = 0
               )
               
               (defrule adult-person
                 ?p <- (person (name ?n) (age ?a&:(>= ?a 18)))
                 =>
                 (printout t ?n " is an adult" crlf)
                 (assert (adult ?n))
               )
               
               (defrule child-person
                 ?p <- (person (name ?n) (age ?a&:(< ?a 18)))
                 =>
                 (printout t ?n " is a child" crlf)
                 (assert (child ?n))
               )
               
               (assert (person (name "John") (age 30) (hobbies "reading" "swimming" "hiking")))
               (assert (person (name "Jane") (age 25) (hobbies "painting" "music")))
               (assert (person (name "Bob") (age 10) (hobbies "games" "sports")))
               
               (printout t (foo 1) crlf)
               (assert (person (name "Zed") (age 42)))
               (run)
               """;
    }

    @Nullable
    @Override
    public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
        return null;
    }

    @Override
    public AttributesDescriptor @NotNull [] getAttributeDescriptors() {
        return DESCRIPTORS;
    }

    @Override
    public ColorDescriptor @NotNull [] getColorDescriptors() {
        return ColorDescriptor.EMPTY_ARRAY;
    }

    @NotNull
    @Override
    public String getDisplayName() {
        return "CLIPS";
    }
}