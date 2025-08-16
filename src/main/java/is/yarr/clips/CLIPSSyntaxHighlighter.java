package is.yarr.clips;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import is.yarr.clips.lexer.CLIPSLexerAdapter;
import is.yarr.clips.psi.CLIPSTypes;
import org.jetbrains.annotations.NotNull;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

/**
 * Defines syntax highlighting for CLIPS files.
 */
public class CLIPSSyntaxHighlighter extends SyntaxHighlighterBase {
    // Define text attribute keys for different token types
    public static final TextAttributesKey KEYWORD = 
            createTextAttributesKey("CLIPS_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey BUILTIN_FUNCTION = 
            createTextAttributesKey("CLIPS_BUILTIN_FUNCTION");
    public static final TextAttributesKey VARIABLE = 
            createTextAttributesKey("CLIPS_VARIABLE", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
    public static final TextAttributesKey GLOBAL_VARIABLE = 
            createTextAttributesKey("CLIPS_GLOBAL_VARIABLE", DefaultLanguageHighlighterColors.STATIC_FIELD);
    public static final TextAttributesKey MULTIFIELD_VARIABLE = 
            createTextAttributesKey("CLIPS_MULTIFIELD_VARIABLE", DefaultLanguageHighlighterColors.INSTANCE_FIELD);
    public static final TextAttributesKey STRING = 
            createTextAttributesKey("CLIPS_STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER = 
            createTextAttributesKey("CLIPS_NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey COMMENT = 
            createTextAttributesKey("CLIPS_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey IDENTIFIER = 
            createTextAttributesKey("CLIPS_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER);
    public static final TextAttributesKey PARENTHESES = 
            createTextAttributesKey("CLIPS_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey OPERATOR = 
            createTextAttributesKey("CLIPS_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN);

    // Semantic (Annotator-driven) keys
    public static final TextAttributesKey USER_FUNCTION_CALL =
            createTextAttributesKey("CLIPS_USER_FUNCTION_CALL");
    public static final TextAttributesKey TEMPLATE_CALL =
            createTextAttributesKey("CLIPS_TEMPLATE_CALL");

    public static final TextAttributesKey BAD_CHARACTER = 
            createTextAttributesKey("CLIPS_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER);

    // Define arrays of text attributes for each token type
    private static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    private static final TextAttributesKey[] BUILTIN_FUNCTION_KEYS = new TextAttributesKey[]{BUILTIN_FUNCTION};
    private static final TextAttributesKey[] VARIABLE_KEYS = new TextAttributesKey[]{VARIABLE};
    private static final TextAttributesKey[] GLOBAL_VARIABLE_KEYS = new TextAttributesKey[]{GLOBAL_VARIABLE};
    private static final TextAttributesKey[] MULTIFIELD_VARIABLE_KEYS = new TextAttributesKey[]{MULTIFIELD_VARIABLE};
    private static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    private static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
    private static final TextAttributesKey[] IDENTIFIER_KEYS = new TextAttributesKey[]{IDENTIFIER};
    private static final TextAttributesKey[] PARENTHESES_KEYS = new TextAttributesKey[]{PARENTHESES};
    private static final TextAttributesKey[] OPERATOR_KEYS = new TextAttributesKey[]{OPERATOR};
    private static final TextAttributesKey[] BAD_CHARACTER_KEYS = new TextAttributesKey[]{BAD_CHARACTER};
    private static final TextAttributesKey[] EMPTY_KEYS = new TextAttributesKey[0];

    @NotNull
    @Override
    public Lexer getHighlightingLexer() {
        return new CLIPSLexerAdapter();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(CLIPSTypes.KEYWORD)) {
            return KEYWORD_KEYS;
        } else if (tokenType.equals(CLIPSTypes.BUILTIN_FUNCTION)) {
            return BUILTIN_FUNCTION_KEYS;
        } else if (tokenType.equals(CLIPSTypes.VARIABLE)) {
            return VARIABLE_KEYS;
        } else if (tokenType.equals(CLIPSTypes.GLOBAL_VARIABLE)) {
            return GLOBAL_VARIABLE_KEYS;
        } else if (tokenType.equals(CLIPSTypes.MULTIFIELD_VARIABLE)) {
            return MULTIFIELD_VARIABLE_KEYS;
        } else if (tokenType.equals(CLIPSTypes.STRING)) {
            return STRING_KEYS;
        } else if (tokenType.equals(CLIPSTypes.NUMBER)) {
            return NUMBER_KEYS;
        } else if (tokenType.equals(CLIPSTypes.COMMENT)) {
            return COMMENT_KEYS;
        } else if (tokenType.equals(CLIPSTypes.IDENTIFIER)) {
            return IDENTIFIER_KEYS;
        } else if (tokenType.equals(CLIPSTypes.LPAREN) || tokenType.equals(CLIPSTypes.RPAREN)) {
            return PARENTHESES_KEYS;
        } else if (tokenType.equals(CLIPSTypes.AMPERSAND) || tokenType.equals(CLIPSTypes.PIPE) || 
                  tokenType.equals(CLIPSTypes.TILDE) || tokenType.equals(CLIPSTypes.COLON)) {
            return OPERATOR_KEYS;
        } else if (tokenType.equals(TokenType.BAD_CHARACTER)) {
            return BAD_CHARACTER_KEYS;
        } else {
            return EMPTY_KEYS;
        }
    }
}