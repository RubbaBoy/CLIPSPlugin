package is.yarr.clips.psi.manipulators;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import is.yarr.clips.psi.leaf.CLIPSIdentifierLeaf;
import org.jetbrains.annotations.NotNull;

public class CLIPSIdentifierManipulator implements ElementManipulator<CLIPSIdentifierLeaf> {

    @Override
    public @NotNull TextRange getRangeInElement(@NotNull CLIPSIdentifierLeaf element) {
        return TextRange.from(0, element.getTextLength());
    }

    @Override
    public CLIPSIdentifierLeaf handleContentChange(@NotNull CLIPSIdentifierLeaf element,
                                                   @NotNull TextRange range,
                                                   @NotNull String newContent) {
        var oldText = element.getText();
        var updated = oldText.substring(0, range.getStartOffset())
                + newContent
                + oldText.substring(range.getEndOffset());

        return (CLIPSIdentifierLeaf) element.replaceWithText(updated);
    }

    @Override
    public CLIPSIdentifierLeaf handleContentChange(@NotNull CLIPSIdentifierLeaf element,
                                                   @NotNull String newContent) {
        return handleContentChange(element, getRangeInElement(element), newContent);
    }
}
