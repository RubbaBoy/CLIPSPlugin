// This is a generated file. Not intended for manual editing.
package is.yarr.clips.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static is.yarr.clips.psi.CLIPSTypes.*;
import is.yarr.clips.psi.*;

public class CLIPSExportSpecImpl extends CLIPSPsiElementImpl implements CLIPSExportSpec {

  public CLIPSExportSpecImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CLIPSVisitor visitor) {
    visitor.visitExportSpec(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CLIPSVisitor) accept((CLIPSVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<CLIPSDefName> getDefNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSDefName.class);
  }

  @Override
  @Nullable
  public CLIPSVariableElement getVariableElement() {
    return findChildByClass(CLIPSVariableElement.class);
  }

}
