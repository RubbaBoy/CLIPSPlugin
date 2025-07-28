package is.yarr.clips.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import is.yarr.clips.psi.CLIPSElementTypes;  // Use the generated types
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
//SYMBOL=[^ \r\n\t\f\"();\?$=<>&|~\^][^ \r\n\t\f\"();\?$=<>&|~\^]+

// Variable definitions. Order is important for longest match.
GLOBAL_VARIABLE=\?\*{VAR_SYMBOL}\*
MULTIFIELD_VARIABLE=\$\?{VAR_SYMBOL}
VARIABLE=\?{VAR_SYMBOL}

// Keywords
ALL_KEYWORDS=defrule|deftemplate|deffacts|deffunction|defmodule|defglobal|defclass|definstances|defmessage-handler|defgeneric|defmethod|import|slot|multislot|declare|salience|auto-focus|and|or|not|test|exists|forall|logical|if|then|else|crlf|t|TRUE|FALSE|nil|type

// Built-in functions, operators, and special symbols (including wildcards)
//BUILTIN_SYMBOLS=(assert|retract|modify|duplicate|printout|bind|exit|halt|clear|reset|watch|unwatch|agenda|facts|rules|run|gensym|length(\$)?|=>|<-|[~&|\^]|=|<>|!=|<|>|<=|>=|\+|-|\*|\/|eq|neq)
//BUILTIN_SYMBOLS=assert|retract|modify|duplicate|printout|bind|exit|halt|clear|reset|watch|unwatch|agenda|facts|rules|run|gensym|length(\$)?|=>|<-|[~&|\^]|=|<>|!=|<|>|<=|>=|\+|-|\/|eq|neq

BUILTIN_FUNCTION =
    // Control Flow Functions
//    "if" |
//    "then" |
//    "else" |
    "while" |
    "loop-for-count" |
    "return" |
    "progn" |
    "progn$" |
    "do" |
    "break" |
    "switch" |
    "case" |
    "default" |
    
    // I/O Functions
    "printout" |
    "open" |
    "close" |
    "read" |
    "readline" |
    "format" |
//    "crlf" |
    "tab" |
//    "t" |
    
    // Fact Functions
    "assert" |
    "retract" |
    "modify" |
    "duplicate" |
    "facts" |
    "fact-index" |
    "fact-relation" |
    "fact-slot-value" |
    
    // String Functions
    "str-cat" |
    "str-length" |
    "str-compare" |
    "str-index" |
    "sub-string" |
    "sym-cat" |
    "upcase" |
    "lowcase" |
    
    // Multifield Functions
    "length" |
    "length$" |
    "member" |
    "member$" |
    "subseq" |
    "subseq$" |
    "nth" |
    "nth$" |
    "create$" |
    "delete$" |
    "replace$" |
    "insert$" |
    "explode$" |
    "implode$" |
    
    // Math Functions
    "div" |
    "mod" |
    "max" |
    "min" |
    "abs" |
    "sqrt" |
    "exp" |
    "log" |
    "log10" |
    "round" |
    "integer" |
    "float" |
    
    // Other Functions and Operators
    "bind" |
    "exit" |
    "halt" |
    "clear" |
    "reset" |
    "watch" |
    "unwatch" |
    "agenda" |
    "rules" |
    "run" |
    "gensym" |
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


// Delimiters
//LPAREN=\(
//RPAREN=\)
//LBRACKET=\[
//RBRACKET=\]

// Single character tokens that could be variable prefixes
QUESTION_MARK=\?
DOLLAR_QUESTION_MARK=\$\?

// Delimiters
LPAREN="("
RPAREN=")"
LBRACKET="["
RBRACKET="]"

%%

<YYINITIAL> {
  {WHITE_SPACE}        { return TokenType.WHITE_SPACE; }

  {COMMENT}            { return CLIPSElementTypes.COMMENT; }

  {STRING}             { return CLIPSElementTypes.STRING; }
  {NUMBER}             { return CLIPSElementTypes.NUMBER; }

  {LPAREN}             { return CLIPSElementTypes.LPAREN; }
  {RPAREN}             { return CLIPSElementTypes.RPAREN; }
//  {LBRACKET}           { return CLIPSElementTypes.LBRACKET; }
//  {RBRACKET}           { return CLIPSElementTypes.RBRACKET; }

  // Keywords must be matched before identifiers/symbols.
  {ALL_KEYWORDS}       { return CLIPSElementTypes.KEYWORD; }

  // Built-in functions, operators, etc.
  {BUILTIN_FUNCTION}    { return CLIPSElementTypes.BUILTIN_FUNCTION; }

  // Variables must be matched before their prefixes ('?' and '$?') become standalone tokens.
  // Order is crucial to match longest variant first.
  {MULTIFIELD_VARIABLE} { return CLIPSElementTypes.MULTIFIELD_VARIABLE; }
  {GLOBAL_VARIABLE}    { return CLIPSElementTypes.GLOBAL_VARIABLE; }
  {VARIABLE}           { return CLIPSElementTypes.VARIABLE; }

  // Now match the standalone '?' and '$?' wildcards/tokens
  {DOLLAR_QUESTION_MARK} { return CLIPSElementTypes.BUILTIN_FUNCTION; }
  {QUESTION_MARK}      { return CLIPSElementTypes.BUILTIN_FUNCTION; }

  // General symbols (identifiers) are matched as a fallback.
  {SYMBOL}             { return CLIPSElementTypes.IDENTIFIER; }

  [^]                  { return TokenType.BAD_CHARACTER; }
}