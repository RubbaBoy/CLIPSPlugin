package is.yarr.clips.psi.leaf;

import com.intellij.psi.ContributedReferenceHost;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

/**
 * Variable leaf (e.g., ?x) that participates in reference provider queries.
 */
public class CLIPSVariableLeaf extends LeafPsiElement implements ContributedReferenceHost {
  public CLIPSVariableLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
    super(type, text);
  }
}
