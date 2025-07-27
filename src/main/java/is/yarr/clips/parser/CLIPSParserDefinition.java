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
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import is.yarr.clips.CLIPSLanguage;
import is.yarr.clips.lexer.CLIPSLexerAdapter;
import is.yarr.clips.psi.CLIPSElementTypes;
import is.yarr.clips.psi.CLIPSFile;
import is.yarr.clips.psi.CLIPSTypes;
import org.jetbrains.annotations.NotNull;

/**
 * Parser definition for CLIPS language.
 * This class is responsible for creating the PSI tree from the lexer tokens.
 */
public class CLIPSParserDefinition implements ParserDefinition {
    public static final TokenSet WHITE_SPACES = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet COMMENTS = TokenSet.create(CLIPSElementTypes.COMMENT);
    public static final TokenSet STRING_LITERALS = TokenSet.create(CLIPSElementTypes.STRING);

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
        System.out.println("Creating element for node: " + node.getElementType().getDebugName());
        return CLIPSElementTypes.Factory.createElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        var clipsFile = new CLIPSFile(viewProvider);
        printPSITree(clipsFile);
        return clipsFile;
    }

    @Override
    public @NotNull SpaceRequirements spaceExistenceTypeBetweenTokens(ASTNode left, ASTNode right) {
        return SpaceRequirements.MAY;
    }

    public static void printPSITree(@NotNull PsiFile file) {
        System.out.println("PSI Tree for file: " + file.getName());
        System.out.println("--------------------------------------------");
        printPSIElement(file, 0);
        System.out.println("--------------------------------------------");
    }

    /**
     * Prints a PSI element and its children recursively.
     *
     * @param element The element to print
     * @param depth   Current depth in the tree (for indentation)
     */
    private static void printPSIElement(@NotNull PsiElement element, int depth) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("  ");
        }

        String elementInfo = getElementInfo(element);
        System.out.println(indent + elementInfo);

        for (PsiElement child : element.getChildren()) {
            printPSIElement(child, depth + 1);
        }
    }

    /**
     * Gets formatted information about a PSI element.
     *
     * @param element The element to get information for
     * @return A string containing element type and text (if a leaf)
     */
    @NotNull
    private static String getElementInfo(@NotNull PsiElement element) {
        String className = element.getClass().getSimpleName();
        String elementType = element.getNode().getElementType().toString();

        if (element instanceof LeafPsiElement) {
            return className + "[" + elementType + "] = '" + element.getText() + "'";
        } else {
            return className + "[" + elementType + "]";
        }
    }

}