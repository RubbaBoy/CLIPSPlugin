package is.yarr.clips.lexer;

import com.intellij.lexer.FlexAdapter;

/**
 * Adapter for the CLIPS lexer.
 */
public class CLIPSLexerAdapter extends FlexAdapter {
    public CLIPSLexerAdapter() {
        super(new CLIPSLexerImpl(null));
    }
}