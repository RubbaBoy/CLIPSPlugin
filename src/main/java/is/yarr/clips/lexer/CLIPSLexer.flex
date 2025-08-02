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
STRING=\"([^\\\"]|\\.)*\"?
DIGITS=[0-9]+
EXPONENT=[eE][+-]?{DIGITS}
NUMBER=[+-]?({DIGITS}(\.{DIGITS}*)?|\.{DIGITS})({EXPONENT})?

// Symbols and Variables
// A symbol for a variable name. Using a safe, common subset of allowed characters.
VAR_SYMBOL=[a-zA-Z_][a-zA-Z0-9_\-]*
// A general symbol for other identifiers. Matched after more specific tokens.
SYMBOL=[^ \r\n\t\f\"();\?$=<>&|~\^][^ \r\n\t\f\"();]*

// Variable definitions. Order is important for longest match.
GLOBAL_VARIABLE=\?\*{VAR_SYMBOL}\*
MULTIFIELD_VARIABLE=\$\?{VAR_SYMBOL}
VARIABLE=\?{VAR_SYMBOL}

// Keywords
KEYWORD=defrule|deftemplate|deffacts|deffunction|defmodule|defglobal|defclass|definstances|defmessage-handler|defgeneric|defmethod|import|slot|multislot|declare|salience|auto-focus|and|or|not|test|exists|forall|logical|if|then|else|crlf|t|TRUE|FALSE|nil|type

// Built-in functions
BUILTIN_FUNCTION=while|loop-for-count|return|progn|progn\$|do|break|switch|case|default|printout|open|close|read|readline|format|tab|assert|retract|modify|duplicate|facts|fact-index|fact-relation|fact-slot-value|str-cat|str-length|str-compare|str-index|sub-string|sym-cat|upcase|lowcase|length|length\$|member|member\$|subseq|subseq\$|nth|nth\$|create\$|delete\$|replace\$|insert\$|explode\$|implode\$|div|mod|max|min|abs|sqrt|exp|log|log10|round|integer|float|bind|exit|halt|clear|reset|watch|unwatch|agenda|rules|run|gensym

// Operators and special symbols
ARROW_OP==>|<-
COMPARISON_OP==|<>|\!=|<|>|<=|>=
MATH_OP=\+|-|\*|\/
LOGICAL_OP=eq|neq

AMPERSAND="&"
PIPE="|"
TILDE="~"
COLON=":"
MULTIFIELD_WILDCARD="$?"
WILDCARD="?"

// Delimiters
LPAREN="("
RPAREN=")"

%%

<YYINITIAL> {
  {WHITE_SPACE}        { return TokenType.WHITE_SPACE; }

  {COMMENT}            { return CLIPSTypes.COMMENT; }

  {STRING}             { return CLIPSTypes.STRING; }
  {NUMBER}             { return CLIPSTypes.NUMBER; }

  {AMPERSAND}          { return CLIPSTypes.AMPERSAND; }
  {PIPE}               { return CLIPSTypes.PIPE; }
  {TILDE}              { return CLIPSTypes.TILDE; }
  {COLON}              { return CLIPSTypes.COLON; }
  {LPAREN}             { return CLIPSTypes.LPAREN; }
  {RPAREN}             { return CLIPSTypes.RPAREN; }

  // Variables must be matched before their prefixes ('?' and '$?') become standalone tokens.
  // Order is crucial to match longest variant first.
  {MULTIFIELD_VARIABLE}  { return CLIPSTypes.MULTIFIELD_VARIABLE; }
  {GLOBAL_VARIABLE}      { return CLIPSTypes.GLOBAL_VARIABLE; }
  {VARIABLE}             { return CLIPSTypes.VARIABLE; }
  {MULTIFIELD_WILDCARD}  { return CLIPSTypes.MULTIFIELD_WILDCARD; }
  {WILDCARD}             { return CLIPSTypes.WILDCARD; }

  // Keywords must be matched before identifiers/symbols.
  {KEYWORD}            { return CLIPSTypes.KEYWORD; }

  // Built-in functions and operators
  {BUILTIN_FUNCTION}   { return CLIPSTypes.BUILTIN_FUNCTION; }
  {ARROW_OP}           { return CLIPSTypes.BUILTIN_FUNCTION; }
  {COMPARISON_OP}      { return CLIPSTypes.BUILTIN_FUNCTION; }
  {MATH_OP}            { return CLIPSTypes.BUILTIN_FUNCTION; }
  {LOGICAL_OP}         { return CLIPSTypes.BUILTIN_FUNCTION; }

  // General symbols (identifiers) are matched as a fallback.
  {SYMBOL}             { return CLIPSTypes.IDENTIFIER; }

  [^]                  { return TokenType.BAD_CHARACTER; }
}