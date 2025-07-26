package is.yarr.clips.editor;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import is.yarr.clips.psi.CLIPSTypes;

/**
 * Quote handler for CLIPS language.
 * Handles automatic insertion of closing quotes when typing opening quotes.
 */
public class CLIPSQuoteHandler extends SimpleTokenSetQuoteHandler {
    public CLIPSQuoteHandler() {
        super(CLIPSTypes.STRING);
    }
}