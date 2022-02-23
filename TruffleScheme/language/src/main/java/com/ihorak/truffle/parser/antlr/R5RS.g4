
grammar R5RS;

/* the grammar name and file must match */

@header {
    package com.ihorak.truffle.parser.antlr;
}

prog: form* EOF                 # Program
    ;

form
    : literal
    | list
    | quote
    | quasiquote
    | unquote
    | unquote_splicing
    ;

list: '(' form* ')'
    ;

quote
    : '\'' form
    ;

quasiquote
    : '`' form
    ;

unquote
    : ',' form
    ;

unquote_splicing
    : ',@' form
    ;


literal
    : NUMBER                    # Number
    | BOOLEAN                   # Boolean
    | STRING                    # String
    | SYMBOL                    # Symbol
    ;


NUMBER: '-'?[0-9]+;
STRING: '"' ( ~'"' | '\\' '"')* '"';
BOOLEAN: '#' ('t' | 'T' | 'f' | 'F');
/* is it equal to ~('#'|'"'|'\''|[()]|[ \t\r\n])+ ?? */
SYMBOL: ~('@'|','|'`'|'#'|'"'|'\''|[()]|[ \t\r\n]) ~('@'|','|'`'|'"'|'\''|[()]|[ \t\r\n])*;


WS: [ \t\r\n] -> skip;
/* everything after ; is comment up to new line or character return */
COMMENT: ';' ~[\r\n]* -> skip;
