// This is a generated file. Not intended for manual editing.
package com.example.simplelang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.example.simplelang.psi.SimpleTypes.*;
import com.example.simplelang.psi.*;

public class SimpleEntityDeclarationImpl extends SimpleEntityDeclarationMixin implements SimpleEntityDeclaration {

  public SimpleEntityDeclarationImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SimpleVisitor visitor) {
    visitor.visitEntityDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SimpleVisitor) accept((SimpleVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<SimpleEntityProperty> getEntityPropertyList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, SimpleEntityProperty.class);
  }

  @Override
  @Nullable
  public SimpleId getId() {
    return findChildByClass(SimpleId.class);
  }

}
