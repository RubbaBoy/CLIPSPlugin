package com.example.simplelang;

import com.example.simplelang.parser.SimpleParser;
import com.example.simplelang.psi.SimpleTypes;
import com.intellij.lang.ASTFactory;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;

/**
 * Parser definition for the Simple language.
 * This class tells the IntelliJ Platform how to create a lexer, parser, and PSI file for our language.
 */
public class SimpleParserDefinition implements ParserDefinition {
    public static final IFileElementType FILE = new IFileElementType(SimpleLanguage.INSTANCE);

    @NotNull
    @Override
    public Lexer createLexer(Project project) {
        return new SimpleLexerAdapter();
    }

    @NotNull
    @Override
    public TokenSet getCommentTokens() {
        return TokenSet.create(SimpleTypes.COMMENT);
    }

    @NotNull
    @Override
    public TokenSet getStringLiteralElements() {
        return TokenSet.create(SimpleTypes.STRING);
    }

    @NotNull
    @Override
    public PsiParser createParser(final Project project) {
        return new SimpleParser();
    }

    @NotNull
    @Override
    public IFileElementType getFileNodeType() {
        return FILE;
    }

    @NotNull
    @Override
    public PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new SimpleFile(viewProvider);
    }

    @NotNull
    @Override
    public PsiElement createElement(ASTNode node) {
        var element = SimpleTypes.Factory.createElement(node);
        System.out.println("[SimpleParserDefinition] createElement: type=" + node.getElementType() + ", psiClass=" + element.getClass().getName());
        return element;
    }
}