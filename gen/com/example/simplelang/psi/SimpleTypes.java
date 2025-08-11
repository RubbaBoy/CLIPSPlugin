// This is a generated file. Not intended for manual editing.
package com.example.simplelang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.example.simplelang.psi.impl.*;

public interface SimpleTypes {

  IElementType ENTITY_DECLARATION = new SimpleElementType("ENTITY_DECLARATION");
  IElementType ENTITY_PROPERTY = new SimpleElementType("ENTITY_PROPERTY");
  IElementType ID = new SimpleElementType("ID");
  IElementType REFERENCE_DECLARATION = new SimpleElementType("REFERENCE_DECLARATION");

  IElementType AS_KEYWORD = new SimpleTokenType("as");
  IElementType COLON = new SimpleTokenType(":");
  IElementType COMMENT = new SimpleTokenType("comment");
  IElementType ENTITY_KEYWORD = new SimpleTokenType("entity");
  IElementType IDENTIFIER = new SimpleTokenType("IDENTIFIER");
  IElementType LBRACE = new SimpleTokenType("{");
  IElementType RBRACE = new SimpleTokenType("}");
  IElementType REF_KEYWORD = new SimpleTokenType("ref");
  IElementType STRING = new SimpleTokenType("STRING");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ENTITY_DECLARATION) {
        return new SimpleEntityDeclarationImpl(node);
      }
      else if (type == ENTITY_PROPERTY) {
        return new SimpleEntityPropertyImpl(node);
      }
      else if (type == ID) {
        return new SimpleIdImpl(node);
      }
      else if (type == REFERENCE_DECLARATION) {
        return new SimpleReferenceDeclarationImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
