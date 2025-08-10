package is.yarr.clips;

import com.intellij.lang.ASTFactory;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.tree.IElementType;
import is.yarr.clips.psi.CLIPSTypes;
import is.yarr.clips.psi.leaf.CLIPSGlobalVariableLeaf;
import is.yarr.clips.psi.leaf.CLIPSIdentifierLeaf;
import is.yarr.clips.psi.leaf.CLIPSVariableLeaf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Minimal AST factory to ensure identifier-like leaves are ContributedReferenceHost,
 * so PsiReferenceContributor providers are queried for CLIPS leaf tokens.
 */
public class CLIPSAstFactory extends ASTFactory {
  @Override
  public @Nullable LeafElement createLeaf(@NotNull IElementType type, @NotNull CharSequence text) {
    if (type == CLIPSTypes.IDENTIFIER) {
      return new CLIPSIdentifierLeaf(type, text);
    }
    if (type == CLIPSTypes.VARIABLE) {
      return new CLIPSVariableLeaf(type, text);
    }
    if (type == CLIPSTypes.GLOBAL_VARIABLE) {
      return new CLIPSGlobalVariableLeaf(type, text);
    }
    return null; // delegate to default for all other token types
  }
}
