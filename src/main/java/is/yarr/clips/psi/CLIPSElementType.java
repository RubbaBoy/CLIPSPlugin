package is.yarr.clips.psi;

import com.intellij.psi.tree.IElementType;
import is.yarr.clips.CLIPSLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Base class for CLIPS element types.
 * This is used for composite elements in the PSI tree.
 */
public class CLIPSElementType extends IElementType {
    public CLIPSElementType(@NotNull @NonNls String debugName) {
        super(debugName, CLIPSLanguage.INSTANCE);
    }
}