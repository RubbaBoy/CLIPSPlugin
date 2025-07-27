package is.yarr.clips.editor;

import com.intellij.lang.BracePair;
import com.intellij.lang.PairedBraceMatcher;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import is.yarr.clips.psi.CLIPSElementTypes;
import is.yarr.clips.psi.CLIPSTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Brace matcher for CLIPS language.
 * Handles matching of parentheses and brackets in CLIPS code.
 */
public class CLIPSBraceMatcher implements PairedBraceMatcher {
    private static final BracePair[] PAIRS = new BracePair[]{
            new BracePair(CLIPSElementTypes.LPAREN, CLIPSElementTypes.RPAREN, true)
//            new BracePair(CLIPSElementTypes.LBRACKET, CLIPSElementTypes.RBRACKET, true)
    };

    @Override
    public BracePair @NotNull [] getPairs() {
        return PAIRS;
    }

    @Override
    public boolean isPairedBracesAllowedBeforeType(@NotNull IElementType lbraceType, @Nullable IElementType contextType) {
        return true;
    }

    @Override
    public int getCodeConstructStart(PsiFile file, int openingBraceOffset) {
        return openingBraceOffset;
    }
}