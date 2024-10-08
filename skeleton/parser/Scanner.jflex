package parser;

import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.Location;

import interpreter.Interpreter;

%%

%public
%class Lexer
%cup
%implements sym
%char
%line
%column

%{
    StringBuffer string = new StringBuffer();
    public Lexer(java.io.Reader in, ComplexSymbolFactory sf){
    this(in);
    symbolFactory = sf;
    }
    ComplexSymbolFactory symbolFactory;

  private Symbol symbol(String name, int sym) {
      return symbolFactory.newSymbol(name, sym, new Location(yyline+1,yycolumn+1,yyline+1), new Location(yyline+1,yycolumn+yylength(),yycolumn+1));
  }
  
  private Symbol symbol(String name, int sym, Object val) {
      Location left = new Location(yyline + 1, yycolumn + 1, yyline + 1);
      Location right = new Location(yyline + 1, yycolumn + yylength(), yycolumn + 1);
      return symbolFactory.newSymbol(name, sym, left, right, val);
  } 

  private void error(String message) {
    System.out.println("Error at line "+ (yyline + 1) + ", column " + (yycolumn + 1) + " : " + message);
  }
%} 

%eofval{
     return symbolFactory.newSymbol("EOF", EOF, new Location(yyline + 1, yycolumn + 1, yychar), new Location(yyline + 1, yycolumn + 1, yychar + 1));
%eofval}


IntLiteral = 0 | [1-9][0-9]*
IDENT = [a-zA-Z_][a-zA-Z0-9_]*

new_line = \r|\n|\r\n;

white_space = {new_line} | [ \t\f]

%%

<YYINITIAL>{
/* This is where tokens are recognized. Every token recognized by the scanner corresponds to a terminal in the parser's grammar. */

/* int literal token */
{IntLiteral} { return symbol("Intconst", INTCONST, Long.parseLong(yytext())); }


/* Operators and Symbols */
"+"               { return symbol("+",  PLUS); }
"-"               { return symbol("-", MINUS); }
"*"               { return symbol("*", TIMES); }
"="               { return symbol("=", ASSIGN); }
"=="              { return symbol("==", EQ); }
"!="              { return symbol("!=", NEQ); }
"<"               { return symbol("<", LT); }
">"               { return symbol(">", GT); }
"<="              { return symbol("<=", LTE); }
">="              { return symbol(">=", GTE); }
"&&"              { return symbol("&&", AND); }
"||"              { return symbol("||", OR); }
"!"               { return symbol("!", NOT); }
"("               { return symbol("(", LPAREN); }
")"               { return symbol(")", RPAREN); }
"{"               { return symbol("{", LBRACE); }
"}"               { return symbol("}", RBRACE); }
","               { return symbol(",", COMMA); }
";"               { return symbol(";", SEMICOLON); }

/* Keywords */
"int"               { return symbol("int", INT); }
"return"            { return symbol("return", RETURN); }
"if"                { return symbol("if", IF); }
"else"              { return symbol("else", ELSE); }
"print"             { return symbol("print", PRINT); }

{IDENT} { return symbol("IDENT", IDENT, yytext()); }
/* comments */
"/*" [^*] ~"*/" | "/*" "*"+ "/"
                  { /* ignore comments */ }

{white_space}     { /* ignore */ }

}

/* error fallback */
[^]               { Interpreter.fatalError("Illegal character <" + yytext() + ">", Interpreter.EXIT_PARSING_ERROR); }