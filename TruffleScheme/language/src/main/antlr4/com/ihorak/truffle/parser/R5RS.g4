
grammar R5RS;

/* the grammar name and file must match */

prog: form* EOF                 # Program
    ;

form
    : literal
    | list
    | quote
    | quasiquote
    | unquote
    | unquote_splicing
    | pair
    | dot_list
    ;


list
    : '(' form* ')'
    ;

dot_list
    : '(' '.' form* ')'
    ;

pair
    : '(' form+ '.' form ')'
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
    | FLOAT                     # Float
    | BOOLEAN                   # Boolean
    | STRING                    # String
    | SYMBOL                    # Symbol
    ;


NUMBER: '-'?DIGIT+;
FLOAT: '-'?DIGIT+ '.' DIGIT*;
STRING: '"' ( ~'"' | '\\' '"')* '"';
BOOLEAN: '#' ('t' | 'T' | 'f' | 'F');
SYMBOL: ~('@'|','|'`'|'"'|'\''|[()]|[ \t\r\n])+;
fragment DIGIT: [0-9];


WS: [ \t\r\n] -> skip;
/* everything after ; is comment up to new line or character return */
COMMENT: ';' ~[\r\n]* -> skip;
