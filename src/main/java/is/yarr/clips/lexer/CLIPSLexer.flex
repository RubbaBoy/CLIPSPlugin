package is.yarr.clips.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import is.yarr.clips.psi.CLIPSTypes;
import com.intellij.psi.TokenType;

%%

%class CLIPSLexerImpl
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R
WHITE_SPACE=({CRLF}|[ \t\f])+
COMMENT=;.*

// Literals
STRING=\"([^\\\"]|\\.)*\"
DIGITS=[0-9]+
EXPONENT=[eE][+-]?{DIGITS}
NUMBER=[+-]?({DIGITS}(\.{DIGITS}*)?|\.{DIGITS})({EXPONENT})?

// Symbols and Variables
// A symbol for a variable name. Using a safe, common subset of allowed characters.
VAR_SYMBOL=[a-zA-Z_][a-zA-Z0-9_\-]*
// A general symbol for other identifiers. Matched after more specific tokens.
SYMBOL=[^ \r\n\t\f\"();\?$=<>&|~\^][^ \r\n\t\f\"();]*
//SYMBOL=[^ \r\n\t\f\"();\?$=<>&|~\^][^ \r\n\t\f\"();\?$=<>&|~\^]+

// Variable definitions. Order is important for longest match.
GLOBAL_VARIABLE=\?\*{VAR_SYMBOL}\*
MULTIFIELD_VARIABLE=\$\?{VAR_SYMBOL}
VARIABLE=\?{VAR_SYMBOL}

// Keywords
ALL_KEYWORDS=defrule|deftemplate|deffacts|deffunction|defmodule|defglobal|defclass|definstances|defmessage-handler|defgeneric|defmethod|slot|multislot|declare|salience|auto-focus|and|or|not|test|exists|forall|logical|if|then|else|crlf|t|TRUE|FALSE|nil

// Built-in functions, operators, and special symbols (including wildcards)
//BUILTIN_SYMBOLS=(assert|retract|modify|duplicate|printout|bind|exit|halt|clear|reset|watch|unwatch|agenda|facts|rules|run|gensym|length(\$)?|=>|<-|[~&|\^]|=|<>|!=|<|>|<=|>=|\+|-|\*|\/|eq|neq)
//BUILTIN_SYMBOLS=assert|retract|modify|duplicate|printout|bind|exit|halt|clear|reset|watch|unwatch|agenda|facts|rules|run|gensym|length(\$)?|=>|<-|[~&|\^]|=|<>|!=|<|>|<=|>=|\+|-|\/|eq|neq

BUILTIN_SYMBOLS = (
    "assert" |
    "retract" |
    "modify" |
    "duplicate" |
    "printout" |
    "bind" |
    "exit" |
    "halt" |
    "clear" |
    "reset" |
    "watch" |
    "unwatch" |
    "agenda" |
    "facts" |
    "rules" |
    "run" |
    "gensym" |
    "length" ("$")? |
    "=>" |
    "<-" |
    [~&|\^] |  // Note: This matches a single character: ~, &, |, or ^
    "=" |
    "<>" |
    "!=" |
    "<" |
    ">" |
    "<=" |
    ">=" |
    "+" |
    "-" |
    "*" |
    "/" |
    "eq" |
    "neq"
)


// Delimiters
LPAREN=\(
RPAREN=\)
LBRACKET=\[
RBRACKET=\]

// Single character tokens that could be variable prefixes
QUESTION_MARK=\?
DOLLAR_QUESTION_MARK=\$\?

// Delimiters
LPAREN=\(
RPAREN=\)
LBRACKET=\[
RBRACKET=\]

%%

<YYINITIAL> {
  {WHITE_SPACE}        { return TokenType.WHITE_SPACE; }
  {COMMENT}            { return CLIPSTypes.COMMENT; }

  {STRING}             { return CLIPSTypes.STRING; }
  {NUMBER}             { return CLIPSTypes.NUMBER; }

  {LPAREN}             { return CLIPSTypes.LPAREN; }
  {RPAREN}             { return CLIPSTypes.RPAREN; }
  {LBRACKET}           { return CLIPSTypes.LBRACKET; }
  {RBRACKET}           { return CLIPSTypes.RBRACKET; }

  // Keywords must be matched before identifiers/symbols.
  {ALL_KEYWORDS}       { return CLIPSTypes.KEYWORD; }

  // Built-in functions, operators, etc.
  {BUILTIN_SYMBOLS}    { return CLIPSTypes.BUILTIN_FUNCTION; }

  // Variables must be matched before their prefixes ('?' and '$?') become standalone tokens.
  // Order is crucial to match longest variant first.
  {GLOBAL_VARIABLE}    { return CLIPSTypes.GLOBAL_VARIABLE; }
  {MULTIFIELD_VARIABLE} { return CLIPSTypes.MULTIFIELD_VARIABLE; }
  {VARIABLE}           { return CLIPSTypes.VARIABLE; }

  // Now match the standalone '?' and '$?' wildcards/tokens
  {DOLLAR_QUESTION_MARK} { return CLIPSTypes.BUILTIN_FUNCTION; }
  {QUESTION_MARK}      { return CLIPSTypes.BUILTIN_FUNCTION; }

  // General symbols (identifiers) are matched as a fallback.
  {SYMBOL}             { return CLIPSTypes.IDENTIFIER; }

  [^]                  { return TokenType.BAD_CHARACTER; }
}