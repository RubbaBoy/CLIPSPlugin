// This is a generated file. Not intended for manual editing.
package is.yarr.clips.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import is.yarr.clips.psi.impl.*;

public interface CLIPSTypes {

  IElementType CARDINALITY_CONSTRAINT = new CLIPSElementType("CARDINALITY_CONSTRAINT");
  IElementType CARDINALITY_SPECIFICATION = new CLIPSElementType("CARDINALITY_SPECIFICATION");
  IElementType CLASS_NAME = new CLIPSElementType("CLASS_NAME");
  IElementType CONSTANT = new CLIPSElementType("CONSTANT");
  IElementType CONSTRAINT = new CLIPSElementType("CONSTRAINT");
  IElementType CONSTRAINT_ATTRIBUTE = new CLIPSElementType("CONSTRAINT_ATTRIBUTE");
  IElementType CONSTRUCT = new CLIPSElementType("CONSTRUCT");
  IElementType DEFAULT_ATTRIBUTE = new CLIPSElementType("DEFAULT_ATTRIBUTE");
  IElementType DEFCLASS_CONSTRUCT = new CLIPSElementType("DEFCLASS_CONSTRUCT");
  IElementType DEFFACTS_CONSTRUCT = new CLIPSElementType("DEFFACTS_CONSTRUCT");
  IElementType DEFFACTS_NAME = new CLIPSElementType("DEFFACTS_NAME");
  IElementType DEFFACTS_RHS_PATTERN = new CLIPSElementType("DEFFACTS_RHS_PATTERN");
  IElementType DEFFUNCTION_CONSTRUCT = new CLIPSElementType("DEFFUNCTION_CONSTRUCT");
  IElementType DEFGLOBAL_CONSTRUCT = new CLIPSElementType("DEFGLOBAL_CONSTRUCT");
  IElementType DEFMODULE_CONSTRUCT = new CLIPSElementType("DEFMODULE_CONSTRUCT");
  IElementType DEFRULE_CONSTRUCT = new CLIPSElementType("DEFRULE_CONSTRUCT");
  IElementType DEFTEMPLATE_CONSTRUCT = new CLIPSElementType("DEFTEMPLATE_CONSTRUCT");
  IElementType DEF_NAME = new CLIPSElementType("DEF_NAME");
  IElementType EXPORT_SPEC = new CLIPSElementType("EXPORT_SPEC");
  IElementType EXPRESSION = new CLIPSElementType("EXPRESSION");
  IElementType FUNCTION_CALL = new CLIPSElementType("FUNCTION_CALL");
  IElementType GLOBAL_VARIABLE_DEF = new CLIPSElementType("GLOBAL_VARIABLE_DEF");
  IElementType IMPORT_SPEC = new CLIPSElementType("IMPORT_SPEC");
  IElementType MODULE_NAME = new CLIPSElementType("MODULE_NAME");
  IElementType MULTIFIELD_VARIABLE_ELEMENT = new CLIPSElementType("MULTIFIELD_VARIABLE_ELEMENT");
  IElementType MULTISLOT_DEFINITION = new CLIPSElementType("MULTISLOT_DEFINITION");
  IElementType PARAMETER = new CLIPSElementType("PARAMETER");
  IElementType PARAMETER_LIST = new CLIPSElementType("PARAMETER_LIST");
  IElementType PREDICATE_CONSTRAINT = new CLIPSElementType("PREDICATE_CONSTRAINT");
  IElementType RANGE_CONSTRAINT = new CLIPSElementType("RANGE_CONSTRAINT");
  IElementType RANGE_SPECIFICATION = new CLIPSElementType("RANGE_SPECIFICATION");
  IElementType RULE_NAME = new CLIPSElementType("RULE_NAME");
  IElementType SINGLE_SLOT_DEFINITION = new CLIPSElementType("SINGLE_SLOT_DEFINITION");
  IElementType SLOT_DEFINITION = new CLIPSElementType("SLOT_DEFINITION");
  IElementType SLOT_NAME = new CLIPSElementType("SLOT_NAME");
  IElementType TEMPLATE_ATTRIBUTE = new CLIPSElementType("TEMPLATE_ATTRIBUTE");
  IElementType TEMPLATE_NAME = new CLIPSElementType("TEMPLATE_NAME");
  IElementType TYPE_CONSTRAINT = new CLIPSElementType("TYPE_CONSTRAINT");
  IElementType TYPE_SPECIFIER = new CLIPSElementType("TYPE_SPECIFIER");
  IElementType VARIABLE_ELEMENT = new CLIPSElementType("VARIABLE_ELEMENT");

  IElementType AMPERSAND = new CLIPSTokenType("AMPERSAND");
  IElementType BUILTIN_FUNCTION = new CLIPSTokenType("BUILTIN_FUNCTION");
  IElementType COLON = new CLIPSTokenType("COLON");
  IElementType COMMENT = new CLIPSTokenType("COMMENT");
  IElementType EQUALS = new CLIPSTokenType("EQUALS");
  IElementType GLOBAL_VARIABLE = new CLIPSTokenType("GLOBAL_VARIABLE");
  IElementType IDENTIFIER = new CLIPSTokenType("IDENTIFIER");
  IElementType KEYWORD = new CLIPSTokenType("KEYWORD");
  IElementType LPAREN = new CLIPSTokenType("LPAREN");
  IElementType MULTIFIELD_VARIABLE = new CLIPSTokenType("MULTIFIELD_VARIABLE");
  IElementType MULTIFIELD_WILDCARD = new CLIPSTokenType("MULTIFIELD_WILDCARD");
  IElementType NUMBER = new CLIPSTokenType("NUMBER");
  IElementType PIPE = new CLIPSTokenType("PIPE");
  IElementType RPAREN = new CLIPSTokenType("RPAREN");
  IElementType STRING = new CLIPSTokenType("STRING");
  IElementType TILDE = new CLIPSTokenType("TILDE");
  IElementType VARIABLE = new CLIPSTokenType("VARIABLE");
  IElementType WILDCARD = new CLIPSTokenType("WILDCARD");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == CARDINALITY_CONSTRAINT) {
        return new CLIPSCardinalityConstraintImpl(node);
      }
      else if (type == CARDINALITY_SPECIFICATION) {
        return new CLIPSCardinalitySpecificationImpl(node);
      }
      else if (type == CLASS_NAME) {
        return new CLIPSClassNameImpl(node);
      }
      else if (type == CONSTANT) {
        return new CLIPSConstantImpl(node);
      }
      else if (type == CONSTRAINT) {
        return new CLIPSConstraintImpl(node);
      }
      else if (type == CONSTRAINT_ATTRIBUTE) {
        return new CLIPSConstraintAttributeImpl(node);
      }
      else if (type == CONSTRUCT) {
        return new CLIPSConstructImpl(node);
      }
      else if (type == DEFAULT_ATTRIBUTE) {
        return new CLIPSDefaultAttributeImpl(node);
      }
      else if (type == DEFCLASS_CONSTRUCT) {
        return new CLIPSDefclassConstructImpl(node);
      }
      else if (type == DEFFACTS_CONSTRUCT) {
        return new CLIPSDeffactsConstructImpl(node);
      }
      else if (type == DEFFACTS_NAME) {
        return new CLIPSDeffactsNameImpl(node);
      }
      else if (type == DEFFACTS_RHS_PATTERN) {
        return new CLIPSDeffactsRhsPatternImpl(node);
      }
      else if (type == DEFFUNCTION_CONSTRUCT) {
        return new CLIPSDeffunctionConstructImpl(node);
      }
      else if (type == DEFGLOBAL_CONSTRUCT) {
        return new CLIPSDefglobalConstructImpl(node);
      }
      else if (type == DEFMODULE_CONSTRUCT) {
        return new CLIPSDefmoduleConstructImpl(node);
      }
      else if (type == DEFRULE_CONSTRUCT) {
        return new CLIPSDefruleConstructImpl(node);
      }
      else if (type == DEFTEMPLATE_CONSTRUCT) {
        return new CLIPSDeftemplateConstructImpl(node);
      }
      else if (type == DEF_NAME) {
        return new CLIPSDefNameImpl(node);
      }
      else if (type == EXPORT_SPEC) {
        return new CLIPSExportSpecImpl(node);
      }
      else if (type == EXPRESSION) {
        return new CLIPSExpressionImpl(node);
      }
      else if (type == FUNCTION_CALL) {
        return new CLIPSFunctionCallImpl(node);
      }
      else if (type == GLOBAL_VARIABLE_DEF) {
        return new CLIPSGlobalVariableDefImpl(node);
      }
      else if (type == IMPORT_SPEC) {
        return new CLIPSImportSpecImpl(node);
      }
      else if (type == MODULE_NAME) {
        return new CLIPSModuleNameImpl(node);
      }
      else if (type == MULTIFIELD_VARIABLE_ELEMENT) {
        return new CLIPSMultifieldVariableElementImpl(node);
      }
      else if (type == MULTISLOT_DEFINITION) {
        return new CLIPSMultislotDefinitionImpl(node);
      }
      else if (type == PARAMETER) {
        return new CLIPSParameterImpl(node);
      }
      else if (type == PARAMETER_LIST) {
        return new CLIPSParameterListImpl(node);
      }
      else if (type == PREDICATE_CONSTRAINT) {
        return new CLIPSPredicateConstraintImpl(node);
      }
      else if (type == RANGE_CONSTRAINT) {
        return new CLIPSRangeConstraintImpl(node);
      }
      else if (type == RANGE_SPECIFICATION) {
        return new CLIPSRangeSpecificationImpl(node);
      }
      else if (type == RULE_NAME) {
        return new CLIPSRuleNameImpl(node);
      }
      else if (type == SINGLE_SLOT_DEFINITION) {
        return new CLIPSSingleSlotDefinitionImpl(node);
      }
      else if (type == SLOT_DEFINITION) {
        return new CLIPSSlotDefinitionImpl(node);
      }
      else if (type == SLOT_NAME) {
        return new CLIPSSlotNameImpl(node);
      }
      else if (type == TEMPLATE_ATTRIBUTE) {
        return new CLIPSTemplateAttributeImpl(node);
      }
      else if (type == TEMPLATE_NAME) {
        return new CLIPSTemplateNameImpl(node);
      }
      else if (type == TYPE_CONSTRAINT) {
        return new CLIPSTypeConstraintImpl(node);
      }
      else if (type == TYPE_SPECIFIER) {
        return new CLIPSTypeSpecifierImpl(node);
      }
      else if (type == VARIABLE_ELEMENT) {
        return new CLIPSVariableElementImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
