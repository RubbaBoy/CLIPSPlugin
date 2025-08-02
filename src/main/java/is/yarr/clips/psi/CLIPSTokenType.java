package is.yarr.clips.psi;

import com.intellij.psi.tree.IElementType;
import is.yarr.clips.CLIPSLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a token type in the CLIPS language.
 */
public class CLIPSTokenType extends IElementType {
    public CLIPSTokenType(@NotNull @NonNls String debugName) {
        super(debugName, CLIPSLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "CLIPSTokenType." + super.toString();
    }
}