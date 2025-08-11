package is.yarr.clips.psi.leaf;

import com.intellij.psi.ContributedReferenceHost;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Global variable leaf (e.g., ?*x*) that participates in reference provider queries.
 */
public class CLIPSGlobalVariableLeaf extends LeafPsiElement implements ContributedReferenceHost {
  public CLIPSGlobalVariableLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
    super(type, text);
  }
}
