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

public class CLIPSDefglobalConstructImpl extends CLIPSPsiElementImpl implements CLIPSDefglobalConstruct {

  public CLIPSDefglobalConstructImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CLIPSVisitor visitor) {
    visitor.visitDefglobalConstruct(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CLIPSVisitor) accept((CLIPSVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<CLIPSExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSExpression.class);
  }

  @Override
  @NotNull
  public List<CLIPSGlobalVariableDef> getGlobalVariableDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSGlobalVariableDef.class);
  }

}
