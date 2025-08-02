package is.yarr.clips.lexer;

import com.intellij.lexer.FlexAdapter;

/**
 * Adapter for the CLIPS lexer to make it compatible with IntelliJ's lexer framework.
 * This class wraps the generated CLIPSLexerImpl class from the flex file.
 */
public class CLIPSLexerAdapter extends FlexAdapter {
    public CLIPSLexerAdapter() {
        super(new CLIPSLexerImpl(null));
    }
}