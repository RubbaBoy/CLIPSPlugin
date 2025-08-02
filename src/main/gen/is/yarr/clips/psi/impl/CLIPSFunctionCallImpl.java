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

public class CLIPSFunctionCallImpl extends CLIPSPsiElementImpl implements CLIPSFunctionCall {

  public CLIPSFunctionCallImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CLIPSVisitor visitor) {
    visitor.visitFunctionCall(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CLIPSVisitor) accept((CLIPSVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public CLIPSDefName getDefName() {
    return findChildByClass(CLIPSDefName.class);
  }

  @Override
  @NotNull
  public List<CLIPSExpression> getExpressionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSExpression.class);
  }

  @Override
  @Nullable
  public CLIPSGlobalVariableDef getGlobalVariableDef() {
    return findChildByClass(CLIPSGlobalVariableDef.class);
  }

  @Override
  @Nullable
  public CLIPSVariableElement getVariableElement() {
    return findChildByClass(CLIPSVariableElement.class);
  }

  @Override
  @Nullable
  public PsiElement getBuiltinFunction() {
    return findChildByType(BUILTIN_FUNCTION);
  }

  @Override
  @Nullable
  public PsiElement getKeyword() {
    return findChildByType(KEYWORD);
  }

}
