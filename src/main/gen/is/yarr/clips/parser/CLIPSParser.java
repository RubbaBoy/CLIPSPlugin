// This is a generated file. Not intended for manual editing.
package is.yarr.clips.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static is.yarr.clips.psi.CLIPSTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class CLIPSParser implements PsiParser, LightPsiParser {

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
    return File(b, l + 1);
  }

  /* ********************************************************** */
  // item*
  static boolean File(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "File")) return false;
    while (true) {
      int c = current_position_(b);
      if (!item(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "File", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "and" conditional_element+ RPAREN
  static boolean and_ce(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_ce")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "and");
    p = r; // pin = 2
    r = r && report_error_(b, and_ce_2(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // conditional_element+
  private static boolean and_ce_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_ce_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = conditional_element(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!conditional_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "and_ce_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // not_constraint (AMPERSAND not_constraint)*
  static boolean and_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_constraint")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = not_constraint(b, l + 1);
    r = r && and_constraint_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (AMPERSAND not_constraint)*
  private static boolean and_constraint_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_constraint_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!and_constraint_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "and_constraint_1", c)) break;
    }
    return true;
  }

  // AMPERSAND not_constraint
  private static boolean and_constraint_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_constraint_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, AMPERSAND);
    r = r && not_constraint(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // variable_element "<-" pattern_ce
  static boolean assigned_pattern_CE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assigned_pattern_CE")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = variable_element(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, "<-"));
    r = p && pattern_ce(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // LPAREN "cardinality" cardinality_specification cardinality_specification RPAREN
  public static boolean cardinality_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cardinality_constraint")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CARDINALITY_CONSTRAINT, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "cardinality");
    p = r; // pin = 2
    r = r && report_error_(b, cardinality_specification(b, l + 1));
    r = p && report_error_(b, cardinality_specification(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // NUMBER | VARIABLE
  public static boolean cardinality_specification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "cardinality_specification")) return false;
    if (!nextTokenIs(b, "<cardinality specification>", NUMBER, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CARDINALITY_SPECIFICATION, "<cardinality specification>");
    r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, VARIABLE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean class_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "class_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, CLASS_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // assigned_pattern_CE | pattern_ce | test_ce | and_ce | or_ce | not_ce | exists_ce | forall_ce | logical_ce
  static boolean conditional_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditional_element")) return false;
    if (!nextTokenIs(b, "", LPAREN, VARIABLE)) return false;
    boolean r;
    r = assigned_pattern_CE(b, l + 1);
    if (!r) r = pattern_ce(b, l + 1);
    if (!r) r = test_ce(b, l + 1);
    if (!r) r = and_ce(b, l + 1);
    if (!r) r = or_ce(b, l + 1);
    if (!r) r = not_ce(b, l + 1);
    if (!r) r = exists_ce(b, l + 1);
    if (!r) r = forall_ce(b, l + 1);
    if (!r) r = logical_ce(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // STRING | NUMBER | IDENTIFIER | "crlf" | "t" | "TRUE" | "FALSE" | "nil"
  public static boolean constant(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONSTANT, "<constant>");
    r = consumeToken(b, STRING);
    if (!r) r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, "crlf");
    if (!r) r = consumeToken(b, "t");
    if (!r) r = consumeToken(b, "TRUE");
    if (!r) r = consumeToken(b, "FALSE");
    if (!r) r = consumeToken(b, "nil");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // or_constraint
  public static boolean constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constraint")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONSTRAINT, "<constraint>");
    r = or_constraint(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // type_constraint
  //                        | range_constraint
  //                        | cardinality_constraint
  public static boolean constraint_attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constraint_attribute")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_constraint(b, l + 1);
    if (!r) r = range_constraint(b, l + 1);
    if (!r) r = cardinality_constraint(b, l + 1);
    exit_section_(b, m, CONSTRAINT_ATTRIBUTE, r);
    return r;
  }

  /* ********************************************************** */
  // defrule_construct
  //             | deftemplate_construct
  //             | deffacts_construct
  //             | defglobal_construct
  //             | deffunction_construct
  //             | defmodule_construct
  //             | defclass_construct
  public static boolean construct(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "construct")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = defrule_construct(b, l + 1);
    if (!r) r = deftemplate_construct(b, l + 1);
    if (!r) r = deffacts_construct(b, l + 1);
    if (!r) r = defglobal_construct(b, l + 1);
    if (!r) r = deffunction_construct(b, l + 1);
    if (!r) r = defmodule_construct(b, l + 1);
    if (!r) r = defclass_construct(b, l + 1);
    exit_section_(b, m, CONSTRUCT, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN "declare" rule_property+ RPAREN
  static boolean declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declaration")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "declare");
    r = r && declaration_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // rule_property+
  private static boolean declaration_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declaration_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = rule_property(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!rule_property(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "declaration_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean def_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "def_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, DEF_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN "default" expression* RPAREN
  //                     | LPAREN "default-dynamic" expression* RPAREN
  public static boolean default_attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "default_attribute")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = default_attribute_0(b, l + 1);
    if (!r) r = default_attribute_1(b, l + 1);
    exit_section_(b, m, DEFAULT_ATTRIBUTE, r);
    return r;
  }

  // LPAREN "default" expression* RPAREN
  private static boolean default_attribute_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "default_attribute_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "default");
    r = r && default_attribute_0_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // expression*
  private static boolean default_attribute_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "default_attribute_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "default_attribute_0_2", c)) break;
    }
    return true;
  }

  // LPAREN "default-dynamic" expression* RPAREN
  private static boolean default_attribute_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "default_attribute_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "default-dynamic");
    r = r && default_attribute_1_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // expression*
  private static boolean default_attribute_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "default_attribute_1_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "default_attribute_1_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "defclass" class_name [STRING]
  //                          ["is-a" def_name+]
  //                          slot_definition*
  //                        RPAREN
  public static boolean defclass_construct(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defclass_construct")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEFCLASS_CONSTRUCT, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "defclass");
    p = r; // pin = 2
    r = r && report_error_(b, class_name(b, l + 1));
    r = p && report_error_(b, defclass_construct_3(b, l + 1)) && r;
    r = p && report_error_(b, defclass_construct_4(b, l + 1)) && r;
    r = p && report_error_(b, defclass_construct_5(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [STRING]
  private static boolean defclass_construct_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defclass_construct_3")) return false;
    consumeToken(b, STRING);
    return true;
  }

  // ["is-a" def_name+]
  private static boolean defclass_construct_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defclass_construct_4")) return false;
    defclass_construct_4_0(b, l + 1);
    return true;
  }

  // "is-a" def_name+
  private static boolean defclass_construct_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defclass_construct_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "is-a");
    r = r && defclass_construct_4_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // def_name+
  private static boolean defclass_construct_4_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defclass_construct_4_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = def_name(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!def_name(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "defclass_construct_4_0_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // slot_definition*
  private static boolean defclass_construct_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defclass_construct_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!slot_definition(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "defclass_construct_5", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "deffacts" deffacts_name [STRING] deffacts_rhs_pattern* RPAREN
  public static boolean deffacts_construct(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deffacts_construct")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEFFACTS_CONSTRUCT, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "deffacts");
    p = r; // pin = 2
    r = r && report_error_(b, deffacts_name(b, l + 1));
    r = p && report_error_(b, deffacts_construct_3(b, l + 1)) && r;
    r = p && report_error_(b, deffacts_construct_4(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [STRING]
  private static boolean deffacts_construct_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deffacts_construct_3")) return false;
    consumeToken(b, STRING);
    return true;
  }

  // deffacts_rhs_pattern*
  private static boolean deffacts_construct_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deffacts_construct_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!deffacts_rhs_pattern(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "deffacts_construct_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean deffacts_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deffacts_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, DEFFACTS_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN def_name expression* RPAREN
  public static boolean deffacts_rhs_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deffacts_rhs_pattern")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && def_name(b, l + 1);
    r = r && deffacts_rhs_pattern_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, DEFFACTS_RHS_PATTERN, r);
    return r;
  }

  // expression*
  private static boolean deffacts_rhs_pattern_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deffacts_rhs_pattern_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "deffacts_rhs_pattern_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "deffunction" function_name [STRING]
  //                             LPAREN parameter_list [MULTIFIELD_VARIABLE] RPAREN
  //                             rhs_action*
  //                           RPAREN
  public static boolean deffunction_construct(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deffunction_construct")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEFFUNCTION_CONSTRUCT, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "deffunction");
    p = r; // pin = 2
    r = r && report_error_(b, function_name(b, l + 1));
    r = p && report_error_(b, deffunction_construct_3(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, LPAREN)) && r;
    r = p && report_error_(b, parameter_list(b, l + 1)) && r;
    r = p && report_error_(b, deffunction_construct_6(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, RPAREN)) && r;
    r = p && report_error_(b, deffunction_construct_8(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [STRING]
  private static boolean deffunction_construct_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deffunction_construct_3")) return false;
    consumeToken(b, STRING);
    return true;
  }

  // [MULTIFIELD_VARIABLE]
  private static boolean deffunction_construct_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deffunction_construct_6")) return false;
    consumeToken(b, MULTIFIELD_VARIABLE);
    return true;
  }

  // rhs_action*
  private static boolean deffunction_construct_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deffunction_construct_8")) return false;
    while (true) {
      int c = current_position_(b);
      if (!rhs_action(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "deffunction_construct_8", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // global_variable_def "=" expression
  static boolean defglobal_assignment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defglobal_assignment")) return false;
    if (!nextTokenIs(b, GLOBAL_VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = global_variable_def(b, l + 1);
    r = r && consumeToken(b, "=");
    r = r && expression(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN "defglobal" [IDENTIFIER] defglobal_assignment* RPAREN
  public static boolean defglobal_construct(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defglobal_construct")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEFGLOBAL_CONSTRUCT, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "defglobal");
    p = r; // pin = 2
    r = r && report_error_(b, defglobal_construct_2(b, l + 1));
    r = p && report_error_(b, defglobal_construct_3(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [IDENTIFIER]
  private static boolean defglobal_construct_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defglobal_construct_2")) return false;
    consumeToken(b, IDENTIFIER);
    return true;
  }

  // defglobal_assignment*
  private static boolean defglobal_construct_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defglobal_construct_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!defglobal_assignment(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "defglobal_construct_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "defmodule" module_name comment? port_specification* RPAREN
  public static boolean defmodule_construct(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defmodule_construct")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEFMODULE_CONSTRUCT, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "defmodule");
    p = r; // pin = 2
    r = r && report_error_(b, module_name(b, l + 1));
    r = p && report_error_(b, defmodule_construct_3(b, l + 1)) && r;
    r = p && report_error_(b, defmodule_construct_4(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // comment?
  private static boolean defmodule_construct_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defmodule_construct_3")) return false;
    consumeToken(b, COMMENT);
    return true;
  }

  // port_specification*
  private static boolean defmodule_construct_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defmodule_construct_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!port_specification(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "defmodule_construct_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "defrule" rule_name [STRING]
  //                         [declaration]
  //                         conditional_element*
  //                         "=>"
  //                         rhs_action*
  //                       RPAREN
  public static boolean defrule_construct(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defrule_construct")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEFRULE_CONSTRUCT, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "defrule");
    p = r; // pin = 2
    r = r && report_error_(b, rule_name(b, l + 1));
    r = p && report_error_(b, defrule_construct_3(b, l + 1)) && r;
    r = p && report_error_(b, defrule_construct_4(b, l + 1)) && r;
    r = p && report_error_(b, defrule_construct_5(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, "=>")) && r;
    r = p && report_error_(b, defrule_construct_7(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [STRING]
  private static boolean defrule_construct_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defrule_construct_3")) return false;
    consumeToken(b, STRING);
    return true;
  }

  // [declaration]
  private static boolean defrule_construct_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defrule_construct_4")) return false;
    declaration(b, l + 1);
    return true;
  }

  // conditional_element*
  private static boolean defrule_construct_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defrule_construct_5")) return false;
    while (true) {
      int c = current_position_(b);
      if (!conditional_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "defrule_construct_5", c)) break;
    }
    return true;
  }

  // rhs_action*
  private static boolean defrule_construct_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "defrule_construct_7")) return false;
    while (true) {
      int c = current_position_(b);
      if (!rhs_action(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "defrule_construct_7", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "deftemplate" template_name (COMMENT)? slot_definition* RPAREN
  public static boolean deftemplate_construct(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deftemplate_construct")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DEFTEMPLATE_CONSTRUCT, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "deftemplate");
    p = r; // pin = 2
    r = r && report_error_(b, template_name(b, l + 1));
    r = p && report_error_(b, deftemplate_construct_3(b, l + 1)) && r;
    r = p && report_error_(b, deftemplate_construct_4(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (COMMENT)?
  private static boolean deftemplate_construct_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deftemplate_construct_3")) return false;
    consumeToken(b, COMMENT);
    return true;
  }

  // slot_definition*
  private static boolean deftemplate_construct_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "deftemplate_construct_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!slot_definition(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "deftemplate_construct_4", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "exists" conditional_element+ RPAREN
  static boolean exists_ce(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exists_ce")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "exists");
    p = r; // pin = 2
    r = r && report_error_(b, exists_ce_2(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // conditional_element+
  private static boolean exists_ce_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "exists_ce_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = conditional_element(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!conditional_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "exists_ce_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // "export" port_item
  public static boolean export_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "export_spec")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPORT_SPEC, "<export spec>");
    r = consumeToken(b, "export");
    r = r && port_item(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // constant | variable_element | global_variable_def | multifield_variable_element | function_call
  public static boolean expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPRESSION, "<expression>");
    r = constant(b, l + 1);
    if (!r) r = variable_element(b, l + 1);
    if (!r) r = global_variable_def(b, l + 1);
    if (!r) r = multifield_variable_element(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // LPAREN "forall" conditional_element conditional_element+ RPAREN
  static boolean forall_ce(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forall_ce")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "forall");
    p = r; // pin = 2
    r = r && report_error_(b, conditional_element(b, l + 1));
    r = p && report_error_(b, forall_ce_3(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // conditional_element+
  private static boolean forall_ce_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forall_ce_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = conditional_element(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!conditional_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "forall_ce_3", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN (if_guts | regular_function_guts) RPAREN
  public static boolean function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && function_call_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, FUNCTION_CALL, r);
    return r;
  }

  // if_guts | regular_function_guts
  private static boolean function_call_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_call_1")) return false;
    boolean r;
    r = if_guts(b, l + 1);
    if (!r) r = regular_function_guts(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // def_name | BUILTIN_FUNCTION | variable_element | global_variable_def | KEYWORD
  static boolean function_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_name")) return false;
    boolean r;
    r = def_name(b, l + 1);
    if (!r) r = consumeToken(b, BUILTIN_FUNCTION);
    if (!r) r = variable_element(b, l + 1);
    if (!r) r = global_variable_def(b, l + 1);
    if (!r) r = consumeToken(b, KEYWORD);
    return r;
  }

  /* ********************************************************** */
  // GLOBAL_VARIABLE
  public static boolean global_variable_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "global_variable_def")) return false;
    if (!nextTokenIs(b, GLOBAL_VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, GLOBAL_VARIABLE);
    exit_section_(b, m, GLOBAL_VARIABLE_DEF, r);
    return r;
  }

  /* ********************************************************** */
  // "if" expression "then" expression* ("else" expression*)?
  static boolean if_guts(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_guts")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, "if");
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1));
    r = p && report_error_(b, consumeToken(b, "then")) && r;
    r = p && report_error_(b, if_guts_3(b, l + 1)) && r;
    r = p && if_guts_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // expression*
  private static boolean if_guts_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_guts_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "if_guts_3", c)) break;
    }
    return true;
  }

  // ("else" expression*)?
  private static boolean if_guts_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_guts_4")) return false;
    if_guts_4_0(b, l + 1);
    return true;
  }

  // "else" expression*
  private static boolean if_guts_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_guts_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, "else");
    r = r && if_guts_4_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // expression*
  private static boolean if_guts_4_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_guts_4_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "if_guts_4_0_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // "import" def_name port_item
  public static boolean import_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_spec")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_SPEC, "<import spec>");
    r = consumeToken(b, "import");
    r = r && def_name(b, l + 1);
    r = r && port_item(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // construct | function_call | COMMENT
  static boolean item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "item")) return false;
    if (!nextTokenIs(b, "", COMMENT, LPAREN)) return false;
    boolean r;
    r = construct(b, l + 1);
    if (!r) r = function_call(b, l + 1);
    if (!r) r = consumeToken(b, COMMENT);
    return r;
  }

  /* ********************************************************** */
  // LPAREN "logical" conditional_element+ RPAREN
  static boolean logical_ce(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_ce")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "logical");
    p = r; // pin = 2
    r = r && report_error_(b, logical_ce_2(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // conditional_element+
  private static boolean logical_ce_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "logical_ce_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = conditional_element(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!conditional_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "logical_ce_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean module_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, MODULE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // MULTIFIELD_VARIABLE
  public static boolean multifield_variable_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multifield_variable_element")) return false;
    if (!nextTokenIs(b, MULTIFIELD_VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, MULTIFIELD_VARIABLE);
    exit_section_(b, m, MULTIFIELD_VARIABLE_ELEMENT, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN "multislot" slot_name template_attribute* RPAREN
  public static boolean multislot_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multislot_definition")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MULTISLOT_DEFINITION, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "multislot");
    p = r; // pin = 2
    r = r && report_error_(b, slot_name(b, l + 1));
    r = p && report_error_(b, multislot_definition_3(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // template_attribute*
  private static boolean multislot_definition_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "multislot_definition_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!template_attribute(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "multislot_definition_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "not" conditional_element RPAREN
  static boolean not_ce(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_ce")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "not");
    p = r; // pin = 2
    r = r && report_error_(b, conditional_element(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // TILDE? single_field_constraint
  static boolean not_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_constraint")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = not_constraint_0(b, l + 1);
    r = r && single_field_constraint(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // TILDE?
  private static boolean not_constraint_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_constraint_0")) return false;
    consumeToken(b, TILDE);
    return true;
  }

  /* ********************************************************** */
  // LPAREN "is-a" constraint RPAREN
  //   | LPAREN "name" constraint RPAREN
  //   | LPAREN slot_name constraint* RPAREN
  static boolean object_attribute_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_attribute_constraint")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = object_attribute_constraint_0(b, l + 1);
    if (!r) r = object_attribute_constraint_1(b, l + 1);
    if (!r) r = object_attribute_constraint_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN "is-a" constraint RPAREN
  private static boolean object_attribute_constraint_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_attribute_constraint_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "is-a");
    r = r && constraint(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN "name" constraint RPAREN
  private static boolean object_attribute_constraint_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_attribute_constraint_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "name");
    r = r && constraint(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN slot_name constraint* RPAREN
  private static boolean object_attribute_constraint_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_attribute_constraint_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && slot_name(b, l + 1);
    r = r && object_attribute_constraint_2_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // constraint*
  private static boolean object_attribute_constraint_2_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_attribute_constraint_2_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!constraint(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "object_attribute_constraint_2_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "object" object_attribute_constraint* RPAREN
  static boolean object_pattern_CE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_pattern_CE")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "object");
    p = r; // pin = 2
    r = r && report_error_(b, object_pattern_CE_2(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // object_attribute_constraint*
  private static boolean object_pattern_CE_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "object_pattern_CE_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!object_attribute_constraint(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "object_pattern_CE_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "or" conditional_element+ RPAREN
  static boolean or_ce(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_ce")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "or");
    p = r; // pin = 2
    r = r && report_error_(b, or_ce_2(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // conditional_element+
  private static boolean or_ce_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_ce_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = conditional_element(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!conditional_element(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "or_ce_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // and_constraint (PIPE and_constraint)*
  static boolean or_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_constraint")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = and_constraint(b, l + 1);
    r = r && or_constraint_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (PIPE and_constraint)*
  private static boolean or_constraint_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_constraint_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!or_constraint_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "or_constraint_1", c)) break;
    }
    return true;
  }

  // PIPE and_constraint
  private static boolean or_constraint_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_constraint_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && and_constraint(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN def_name constraint* RPAREN
  static boolean ordered_pattern_CE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ordered_pattern_CE")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && def_name(b, l + 1);
    r = r && ordered_pattern_CE_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // constraint*
  private static boolean ordered_pattern_CE_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ordered_pattern_CE_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!constraint(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ordered_pattern_CE_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // VARIABLE
  public static boolean parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VARIABLE);
    exit_section_(b, m, PARAMETER, r);
    return r;
  }

  /* ********************************************************** */
  // parameter*
  public static boolean parameter_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "parameter_list")) return false;
    Marker m = enter_section_(b, l, _NONE_, PARAMETER_LIST, "<parameter list>");
    while (true) {
      int c = current_position_(b);
      if (!parameter(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "parameter_list", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // ordered_pattern_CE | template_pattern_CE | object_pattern_CE
  static boolean pattern_ce(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pattern_ce")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    r = ordered_pattern_CE(b, l + 1);
    if (!r) r = template_pattern_CE(b, l + 1);
    if (!r) r = object_pattern_CE(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // "deftemplate" | "defclass" | "defglobal" | "deffunction" | "defgeneric"
  static boolean port_construct_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "port_construct_type")) return false;
    boolean r;
    r = consumeToken(b, "deftemplate");
    if (!r) r = consumeToken(b, "defclass");
    if (!r) r = consumeToken(b, "defglobal");
    if (!r) r = consumeToken(b, "deffunction");
    if (!r) r = consumeToken(b, "defgeneric");
    return r;
  }

  /* ********************************************************** */
  // variable_element | (port_construct_type (variable_element | def_name+))
  static boolean port_item(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "port_item")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_element(b, l + 1);
    if (!r) r = port_item_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // port_construct_type (variable_element | def_name+)
  private static boolean port_item_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "port_item_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = port_construct_type(b, l + 1);
    r = r && port_item_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // variable_element | def_name+
  private static boolean port_item_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "port_item_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = variable_element(b, l + 1);
    if (!r) r = port_item_1_1_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // def_name+
  private static boolean port_item_1_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "port_item_1_1_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = def_name(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!def_name(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "port_item_1_1_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN (import_spec | export_spec) RPAREN
  static boolean port_specification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "port_specification")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && port_specification_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // import_spec | export_spec
  private static boolean port_specification_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "port_specification_1")) return false;
    boolean r;
    r = import_spec(b, l + 1);
    if (!r) r = export_spec(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // COLON function_call
  public static boolean predicate_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "predicate_constraint")) return false;
    if (!nextTokenIs(b, COLON)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && function_call(b, l + 1);
    exit_section_(b, m, PREDICATE_CONSTRAINT, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN "range" range_specification range_specification RPAREN
  public static boolean range_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_constraint")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RANGE_CONSTRAINT, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "range");
    p = r; // pin = 2
    r = r && report_error_(b, range_specification(b, l + 1));
    r = p && report_error_(b, range_specification(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // NUMBER | VARIABLE
  public static boolean range_specification(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "range_specification")) return false;
    if (!nextTokenIs(b, "<range specification>", NUMBER, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, RANGE_SPECIFICATION, "<range specification>");
    r = consumeToken(b, NUMBER);
    if (!r) r = consumeToken(b, VARIABLE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // function_name expression*
  static boolean regular_function_guts(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "regular_function_guts")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = function_name(b, l + 1);
    r = r && regular_function_guts_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // expression*
  private static boolean regular_function_guts_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "regular_function_guts_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expression(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "regular_function_guts_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // expression
  static boolean rhs_action(PsiBuilder b, int l) {
    return expression(b, l + 1);
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean rule_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, RULE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN "salience" expression RPAREN
  //                       | LPAREN "auto-focus" ("TRUE" | "FALSE") RPAREN
  static boolean rule_property(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_property")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = rule_property_0(b, l + 1);
    if (!r) r = rule_property_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN "salience" expression RPAREN
  private static boolean rule_property_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_property_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "salience");
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // LPAREN "auto-focus" ("TRUE" | "FALSE") RPAREN
  private static boolean rule_property_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_property_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "auto-focus");
    r = r && rule_property_1_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // "TRUE" | "FALSE"
  private static boolean rule_property_1_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rule_property_1_2")) return false;
    boolean r;
    r = consumeToken(b, "TRUE");
    if (!r) r = consumeToken(b, "FALSE");
    return r;
  }

  /* ********************************************************** */
  // constant
  //   | variable_element
  //   | multifield_variable_element
  //   | global_variable_def
  //   | predicate_constraint
  //   | EQUALS function_call
  //   | WILDCARD
  //   | MULTIFIELD_WILDCARD
  static boolean single_field_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "single_field_constraint")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = constant(b, l + 1);
    if (!r) r = variable_element(b, l + 1);
    if (!r) r = multifield_variable_element(b, l + 1);
    if (!r) r = global_variable_def(b, l + 1);
    if (!r) r = predicate_constraint(b, l + 1);
    if (!r) r = single_field_constraint_5(b, l + 1);
    if (!r) r = consumeToken(b, WILDCARD);
    if (!r) r = consumeToken(b, MULTIFIELD_WILDCARD);
    exit_section_(b, m, null, r);
    return r;
  }

  // EQUALS function_call
  private static boolean single_field_constraint_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "single_field_constraint_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, EQUALS);
    r = r && function_call(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN "slot" slot_name template_attribute* RPAREN
  public static boolean single_slot_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "single_slot_definition")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, SINGLE_SLOT_DEFINITION, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "slot");
    p = r; // pin = 2
    r = r && report_error_(b, slot_name(b, l + 1));
    r = p && report_error_(b, single_slot_definition_3(b, l + 1)) && r;
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // template_attribute*
  private static boolean single_slot_definition_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "single_slot_definition_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!template_attribute(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "single_slot_definition_3", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // single_slot_definition | multislot_definition
  public static boolean slot_definition(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "slot_definition")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = single_slot_definition(b, l + 1);
    if (!r) r = multislot_definition(b, l + 1);
    exit_section_(b, m, SLOT_DEFINITION, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean slot_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "slot_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, SLOT_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // constraint_attribute | default_attribute
  public static boolean template_attribute(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "template_attribute")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = constraint_attribute(b, l + 1);
    if (!r) r = default_attribute(b, l + 1);
    exit_section_(b, m, TEMPLATE_ATTRIBUTE, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER
  public static boolean template_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "template_name")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, TEMPLATE_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN def_name template_slot_pattern+ RPAREN
  static boolean template_pattern_CE(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "template_pattern_CE")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && def_name(b, l + 1);
    r = r && template_pattern_CE_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // template_slot_pattern+
  private static boolean template_pattern_CE_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "template_pattern_CE_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = template_slot_pattern(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!template_slot_pattern(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "template_pattern_CE_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN slot_name constraint* RPAREN
  static boolean template_slot_pattern(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "template_slot_pattern")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && slot_name(b, l + 1);
    r = r && template_slot_pattern_2(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // constraint*
  private static boolean template_slot_pattern_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "template_slot_pattern_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!constraint(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "template_slot_pattern_2", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // LPAREN "test" expression RPAREN
  static boolean test_ce(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "test_ce")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "test");
    r = r && expression(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // LPAREN "type" type_specifier+ RPAREN
  public static boolean type_constraint(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_constraint")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TYPE_CONSTRAINT, null);
    r = consumeToken(b, LPAREN);
    r = r && consumeToken(b, "type");
    p = r; // pin = 2
    r = r && report_error_(b, type_constraint_2(b, l + 1));
    r = p && consumeToken(b, RPAREN) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // type_specifier+
  private static boolean type_constraint_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_constraint_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_specifier(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!type_specifier(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "type_constraint_2", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // IDENTIFIER | VARIABLE
  public static boolean type_specifier(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_specifier")) return false;
    if (!nextTokenIs(b, "<type specifier>", IDENTIFIER, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_SPECIFIER, "<type specifier>");
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = consumeToken(b, VARIABLE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // VARIABLE
  public static boolean variable_element(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_element")) return false;
    if (!nextTokenIs(b, VARIABLE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, VARIABLE);
    exit_section_(b, m, VARIABLE_ELEMENT, r);
    return r;
  }

}
