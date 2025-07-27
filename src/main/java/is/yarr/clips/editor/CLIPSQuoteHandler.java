package is.yarr.clips.editor;

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.highlighter.HighlighterIterator;
import com.intellij.psi.tree.IElementType;
import is.yarr.clips.psi.CLIPSElementTypes;
import is.yarr.clips.psi.CLIPSTypes;

/**
 * Quote handler for CLIPS language.
 * Handles automatic insertion of closing quotes when typing opening quotes
 * and properly handles escaped quotes within strings.
 */
public class CLIPSQuoteHandler extends SimpleTokenSetQuoteHandler {
    public CLIPSQuoteHandler() {
        super(CLIPSElementTypes.STRING);
    }

    /**
     * Checks if the quote at the given offset is an opening quote.
     * This method is overridden to enable automatic insertion of matching quotes.
     */
    @Override
    public boolean isOpeningQuote(HighlighterIterator iterator, int offset) {
        // Get the character at the current offset
        CharSequence text = iterator.getDocument().getCharsSequence();
        if (offset < text.length() && text.charAt(offset) == '"') {
            // Check if we're not inside a string token
            IElementType tokenType = iterator.getTokenType();
            if (tokenType != CLIPSElementTypes.STRING) {
                return true;
            }

            // If we're at the start of a string token, it's an opening quote
            return offset == iterator.getStart();
        }

        return false;
    }

    /**
     * Checks if the quote at the given offset is a closing quote.
     * This method is overridden to properly handle escaped quotes.
     */
    @Override
    public boolean isClosingQuote(HighlighterIterator iterator, int offset) {
        final IElementType tokenType = iterator.getTokenType();

        if (tokenType == CLIPSElementTypes.STRING) {
            int start = iterator.getStart();
            int end = iterator.getEnd();

            // It's a closing quote if we're at the end of the string token
            return offset == end - 1;
        }

        return false;
    }

    /**
     * Checks if there's a non-closed literal at the given offset.
     * This method is overridden to properly handle escaped quotes.
     */
    @Override
    public boolean hasNonClosedLiteral(Editor editor, HighlighterIterator iterator, int offset) {
        Document document = editor.getDocument();
        CharSequence text = document.getCharsSequence();
        
        if (iterator.getTokenType() == CLIPSElementTypes.STRING) {
            // Check if the current character is a quote
            if (offset < text.length() && text.charAt(offset) == '"') {
                // Check if it's escaped
                if (offset > 0 && text.charAt(offset - 1) == '\\') {
                    // Count the number of consecutive backslashes before this quote
                    int backslashCount = 0;
                    int i = offset - 1;
                    while (i >= 0 && text.charAt(i) == '\\') {
                        backslashCount++;
                        i--;
                    }
                    
                    // If there's an odd number of backslashes, the quote is escaped
                    if (backslashCount % 2 == 1) {
                        return true;
                    }
                }
            }
        }
        
        // Fall back to the default implementation
        return super.hasNonClosedLiteral(editor, iterator, offset);
    }
}