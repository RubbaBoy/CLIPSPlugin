package is.yarr.clips.parser;

import com.intellij.lang.ASTNode;
import com.intellij.lang.LightPsiParser;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.lang.PsiParser;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Parser for CLIPS language.
 * This is a simple parser that just returns a dummy AST node for now.
 */
public class CLIPSParser implements PsiParser, LightPsiParser {
    @NotNull
    @Override
    public ASTNode parse(@NotNull IElementType root, @NotNull PsiBuilder builder) {
        parseLight(root, builder);
        return builder.getTreeBuilt();
    }

    @Override
    public void parseLight(IElementType root, PsiBuilder builder) {
        builder.setDebugMode(false);
        Marker rootMarker = builder.mark();
        
        // Parse all tokens
        while (!builder.eof()) {
            builder.advanceLexer();
        }
        
        rootMarker.done(root);
    }
}