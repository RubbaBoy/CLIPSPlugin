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

public class CLIPSConstructImpl extends CLIPSPsiElementImpl implements CLIPSConstruct {

  public CLIPSConstructImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull CLIPSVisitor visitor) {
    visitor.visitConstruct(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof CLIPSVisitor) accept((CLIPSVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public CLIPSDefclassConstruct getDefclassConstruct() {
    return findChildByClass(CLIPSDefclassConstruct.class);
  }

  @Override
  @Nullable
  public CLIPSDeffactsConstruct getDeffactsConstruct() {
    return findChildByClass(CLIPSDeffactsConstruct.class);
  }

  @Override
  @Nullable
  public CLIPSDeffunctionConstruct getDeffunctionConstruct() {
    return findChildByClass(CLIPSDeffunctionConstruct.class);
  }

  @Override
  @Nullable
  public CLIPSDefglobalConstruct getDefglobalConstruct() {
    return findChildByClass(CLIPSDefglobalConstruct.class);
  }

  @Override
  @Nullable
  public CLIPSDefmoduleConstruct getDefmoduleConstruct() {
    return findChildByClass(CLIPSDefmoduleConstruct.class);
  }

  @Override
  @Nullable
  public CLIPSDefruleConstruct getDefruleConstruct() {
    return findChildByClass(CLIPSDefruleConstruct.class);
  }

  @Override
  @Nullable
  public CLIPSDeftemplateConstruct getDeftemplateConstruct() {
    return findChildByClass(CLIPSDeftemplateConstruct.class);
  }

}
