// This is a generated file. Not intended for manual editing.
package com.example.simplelang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.example.simplelang.psi.SimpleTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.example.simplelang.psi.*;

public class SimpleReferenceDeclarationImpl extends ASTWrapperPsiElement implements SimpleReferenceDeclaration {

  public SimpleReferenceDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull SimpleVisitor visitor) {
    visitor.visitReferenceDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof SimpleVisitor) accept((SimpleVisitor)visitor);
    else super.accept(visitor);
  }

}
