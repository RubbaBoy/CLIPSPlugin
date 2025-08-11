package com.example.simplelang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.example.simplelang.psi.SimpleTypes;
import com.intellij.psi.TokenType;

%%

%class SimpleLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF=\R
WHITE_SPACE=[\ \n\t\f]
FIRST_VALUE_CHARACTER=[^ \n\f\\] | "\\"{CRLF} | "\\".
VALUE_CHARACTER=[^\n\f\\] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("//"[^\r\n]*)
IDENTIFIER=[a-zA-Z_][a-zA-Z0-9_]*
STRING=('([^'\\]|\\.)*'|\"([^\"\\]|\\.)*\")

%state WAITING_VALUE

%%

<YYINITIAL> {
  {WHITE_SPACE}                  { return TokenType.WHITE_SPACE; }
  {END_OF_LINE_COMMENT}          { return SimpleTypes.COMMENT; }
  
  "entity"                       { return SimpleTypes.ENTITY_KEYWORD; }
  "ref"                          { return SimpleTypes.REF_KEYWORD; }
  "as"                           { return SimpleTypes.AS_KEYWORD; }
  "{"                            { return SimpleTypes.LBRACE; }
  "}"                            { return SimpleTypes.RBRACE; }
  ":"                            { return SimpleTypes.COLON; }
  
  {IDENTIFIER}                   { return SimpleTypes.IDENTIFIER; }
  {STRING}                       { return SimpleTypes.STRING; }
  
  [^]                            { return TokenType.BAD_CHARACTER; }
}