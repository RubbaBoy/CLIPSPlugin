// This is a generated file. Not intended for manual editing.
package com.example.simplelang.psi;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class SimpleVisitor extends PsiElementVisitor {

  public void visitEntityDeclaration(@NotNull SimpleEntityDeclaration o) {
    visitNamedElement(o);
  }

  public void visitEntityProperty(@NotNull SimpleEntityProperty o) {
    visitPsiElement(o);
  }

  public void visitId(@NotNull SimpleId o) {
    visitPsiElement(o);
  }

  public void visitReferenceDeclaration(@NotNull SimpleReferenceDeclaration o) {
    visitPsiElement(o);
  }

  public void visitNamedElement(@NotNull SimpleNamedElement o) {
    visitPsiElement(o);
  }

  public void visitPsiElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
