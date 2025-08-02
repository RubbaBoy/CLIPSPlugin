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

public class CLIPSDefclassConstructImpl extends CLIPSPsiElementImpl implements CLIPSDefclassConstruct {

  public CLIPSDefclassConstructImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CLIPSVisitor visitor) {
    visitor.visitDefclassConstruct(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CLIPSVisitor) accept((CLIPSVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public CLIPSClassName getClassName() {
    return findChildByClass(CLIPSClassName.class);
  }

  @Override
  @NotNull
  public List<CLIPSDefName> getDefNameList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSDefName.class);
  }

  @Override
  @NotNull
  public List<CLIPSSlotDefinition> getSlotDefinitionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSSlotDefinition.class);
  }

}
