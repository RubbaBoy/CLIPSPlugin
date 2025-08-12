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

public class CLIPSDeffunctionConstructImpl extends CLIPSPsiElementImpl implements CLIPSDeffunctionConstruct {

  public CLIPSDeffunctionConstructImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CLIPSVisitor visitor) {
    visitor.visitDeffunctionConstruct(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CLIPSVisitor) accept((CLIPSVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public CLIPSDeffunctionName getDeffunctionName() {
    return findChildByClass(CLIPSDeffunctionName.class);
  }

  @Override
  @NotNull
  public List<CLIPSExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSExpression.class);
  }

  @Override
  @Nullable
  public CLIPSParameterList getParameterList() {
    return findChildByClass(CLIPSParameterList.class);
  }

  @Override
  @Nullable
  public PsiElement getMultifieldVariable() {
    return findChildByType(MULTIFIELD_VARIABLE);
  }

}
