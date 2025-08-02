package is.yarr.clips;

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

/**
 * Provides support for commenting and uncommenting code in CLIPS files.
 * CLIPS uses semicolon (;) for line comments and does not support block comments.
 */
public class CLIPSCommenter implements Commenter {
    @Nullable
    @Override
    public String getLineCommentPrefix() {
        return ";";
    }

    @Nullable
    @Override
    public String getBlockCommentPrefix() {
        return null; // CLIPS does not support block comments
    }

    @Nullable
    @Override
    public String getBlockCommentSuffix() {
        return null; // CLIPS does not support block comments
    }

    @Nullable
    @Override
    public String getCommentedBlockCommentPrefix() {
        return null; // CLIPS does not support block comments
    }

    @Nullable
    @Override
    public String getCommentedBlockCommentSuffix() {
        return null; // CLIPS does not support block comments
    }
}