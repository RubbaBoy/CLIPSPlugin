package is.yarr.clips.parser;

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
import is.yarr.clips.CLIPSLanguage;
import is.yarr.clips.lexer.CLIPSLexerAdapter;
import is.yarr.clips.psi.CLIPSFile;
import is.yarr.clips.psi.CLIPSTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Parser definition for CLIPS language.
 * This class is responsible for creating the PSI tree from the lexer tokens.
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
    public @NotNull PsiParser createParser(Project project) {
        return new CLIPSParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public TokenSet getWhitespaceTokens() {
        return WHITE_SPACES;
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return COMMENTS;
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return STRING_LITERALS;
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        return CLIPSTypes.Factory.createElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new CLIPSFile(viewProvider);
    }
}