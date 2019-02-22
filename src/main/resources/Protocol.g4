grammar Protocol;

@header {
    package aoprotogen;
}

parse
  :  line+ EOF
  ;

line
  :  command NEWLINE
  |  comment NEWLINE 
     //{System.out.printf("value=%s\n", $value.text);}
  ;

command
  :  WORD params*
  ;

param
  :  type ':' parameter 
  ;
  
params
  :  ',' param 
  ;
  
type
  :  WORD
  |  WORD '[' size ']'
  |  tuple
  |  tuple '[' size ']'
  ;
  
tuple
  :  '(' param params* ')'
  ;

size
  :  NUMBER
  |  WORD
  |  '.'
  ; 

parameter
  :  any_except_newline*
  ; 
  
comment
  :  '#' any_except_newline* 
  ;
  
any_except_newline
  :  COLON
  |  WORD
  |  SPACE
  |  NUMBER
  |  ANYCHAR
  ;

eol
  :  NEWLINE
  |  EOF
  ;


COLON   : ':';
WORD    : ('a'..'z' | 'A'..'Z')+;
NUMBER  : ('0'..'9')+;
NEWLINE : '\r'? '\n' | '\r';
SPACE   : (' ' | '\t');
ANYCHAR :  . ; 
