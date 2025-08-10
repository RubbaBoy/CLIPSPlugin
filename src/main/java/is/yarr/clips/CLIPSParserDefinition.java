package is.yarr.clips;

import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import is.yarr.clips.lexer.CLIPSLexerAdapter;
import is.yarr.clips.parser.CLIPSParser;
import is.yarr.clips.psi.CLIPSFile;
import is.yarr.clips.psi.CLIPSTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Defines how CLIPS files are parsed and how the PSI tree is built.
 */
public class CLIPSParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(CLIPSTypes.COMMENT);
    public static final TokenSet STRING_LITERALS = TokenSet.create(CLIPSTypes.STRING);

    public static final IFileElementType FILE = new IFileElementType(CLIPSLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new CLIPSLexerAdapter();
    }

    @Override
    public @NotNull TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return STRING_LITERALS;
    }

    @NotNull
    @Override
    public PsiParser createParser(Project project) {
        return new CLIPSParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new CLIPSFile(viewProvider);
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        PsiElement element = CLIPSTypes.Factory.createElement(node);
//        System.out.println("[DEBUG_LOG] CLIPSParserDefinition.createElement: Created element of type " + element.getClass().getName() +
//                          " for node type " + node.getElementType() +
//                          ", text: '" + node.getText() + "'");
        return element;
    }
}