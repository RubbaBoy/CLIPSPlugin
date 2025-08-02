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

public class CLIPSDefmoduleConstructImpl extends CLIPSPsiElementImpl implements CLIPSDefmoduleConstruct {

  public CLIPSDefmoduleConstructImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CLIPSVisitor visitor) {
    visitor.visitDefmoduleConstruct(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CLIPSVisitor) accept((CLIPSVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<CLIPSExportSpec> getExportSpecList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSExportSpec.class);
  }

  @Override
  @NotNull
  public List<CLIPSImportSpec> getImportSpecList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, CLIPSImportSpec.class);
  }

  @Override
  @Nullable
  public CLIPSModuleName getModuleName() {
    return findChildByClass(CLIPSModuleName.class);
  }

}
