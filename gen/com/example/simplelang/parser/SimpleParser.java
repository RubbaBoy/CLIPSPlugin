// This is a generated file. Not intended for manual editing.
package com.example.simplelang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.example.simplelang.psi.SimpleTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class SimpleParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, null);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return simpleFile(b, l + 1);
  }

  /* ********************************************************** */
  // ENTITY_KEYWORD id LBRACE entityProperty* RBRACE
  public static boolean entityDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "entityDeclaration")) return false;
    if (!nextTokenIs(b, ENTITY_KEYWORD)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ENTITY_DECLARATION, null);
    r = consumeToken(b, ENTITY_KEYWORD);
    p = r; // pin = 1
    r = r && report_error_(b, id(b, l + 1));
    r = p && report_error_(b, consumeToken(b, LBRACE)) && r;
    r = p && report_error_(b, entityDeclaration_3(b, l + 1)) && r;
    r = p && consumeToken(b, RBRACE) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // entityProperty*
  private static boolean entityDeclaration_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "entityDeclaration_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!entityProperty(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "entityDeclaration_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER COLON IDENTIFIER
  public static boolean entityProperty(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "entityProperty")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, IDENTIFIER, COLON, IDENTIFIER);
    exit_section_(b, m, ENTITY_PROPERTY, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean id(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "id")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, ID, r);
    return r;
  }

  /* ********************************************************** */
  // entityDeclaration | referenceDeclaration | COMMENT
  static boolean item_(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item_")) return false;
    boolean r;
    r = entityDeclaration(b, l + 1);
    if (!r) r = referenceDeclaration(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    return r;
  }

  /* ********************************************************** */
  // REF_KEYWORD IDENTIFIER AS_KEYWORD IDENTIFIER {
  // }
  public static boolean referenceDeclaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "referenceDeclaration")) return false;
    if (!nextTokenIs(b, REF_KEYWORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, REF_KEYWORD, IDENTIFIER, AS_KEYWORD, IDENTIFIER);
    r = r && referenceDeclaration_4(b, l + 1);
    exit_section_(b, m, REFERENCE_DECLARATION, r);
    return r;
  }

  // {
  // }
  private static boolean referenceDeclaration_4(PsiBuilder b, int l) {
    return true;
  }

  /* ********************************************************** */
  // item_*
  static boolean simpleFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simpleFile")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item_(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "simpleFile", c)) break;
    }
    return true;
  }

}
