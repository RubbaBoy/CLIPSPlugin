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

public class CLIPSConstraintImpl extends CLIPSPsiElementImpl implements CLIPSConstraint {

  public CLIPSConstraintImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CLIPSVisitor visitor) {
    visitor.visitConstraint(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CLIPSVisitor) accept((CLIPSVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<CLIPSConstant> getConstantList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSConstant.class);
  }

  @Override
  @NotNull
  public List<CLIPSFunctionCall> getFunctionCallList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSFunctionCall.class);
  }

  @Override
  @NotNull
  public List<CLIPSGlobalVariableDef> getGlobalVariableDefList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSGlobalVariableDef.class);
  }

  @Override
  @NotNull
  public List<CLIPSMultifieldVariableElement> getMultifieldVariableElementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSMultifieldVariableElement.class);
  }

  @Override
  @NotNull
  public List<CLIPSPredicateConstraint> getPredicateConstraintList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSPredicateConstraint.class);
  }

  @Override
  @NotNull
  public List<CLIPSVariableElement> getVariableElementList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSVariableElement.class);
  }

}
