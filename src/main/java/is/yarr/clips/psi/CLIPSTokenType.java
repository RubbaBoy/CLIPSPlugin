package is.yarr.clips.psi;

import com.intellij.psi.tree.IElementType;
import is.yarr.clips.CLIPSLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for CLIPS token types.
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