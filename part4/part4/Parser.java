/* *** This file is given as part of the programming assignment. *** */
import java.util.ArrayList;

public class Parser {

  // tok is global to all these parsing methods;
  // scan just calls the scanner's scan method and saves the result in tok.
  private Token tok; 
  private Scan scanner;
  
  private Symbols s = new Symbols(); //class handling Symbols
  private Translator trans = new Translator();
  private int scope_index = -2; //-2 = any scope, -1 = global
  private boolean first = false;
  
  private void scan() {
	  tok = scanner.scan();
  }

  Parser(Scan scanner) {
	  this.scanner = scanner;
	  scan();
	  
	  program();
	  if( tok.kind != TK.EOF )
	    parse_error("junk after logical end of program");
  }

  private void program() {
    System.out.print("#include <stdio.h>\nint main() {\n");
    block();
    System.out.println("return 0; \n}");
  }

  private void block(){
    s.add(); //add scope at new block
	  declaration_list();
	  statement_list();
	  s.remove(); //remove scope 
  }

  private void declaration_list() {
	  while( is(TK.DECLARE) ) {
	    declaration();
	  }
  }

  private void declaration() {
    mustbe(TK.DECLARE);
    trans.addDec();
    if(is(TK.ID)) { 
      s.declare(tok);
      if(s.getError() == false) { //no print on var declaration error
        System.out.print("x_" + s.getVar(tok, 0));
        first = true;
      }
    }  
    mustbe(TK.ID);
	  while( is(TK.COMMA) ) {
	    scan();
	    if(is(TK.ID)) {  
	      s.declare(tok);
	      if(s.getError() == false) { //no print on var declaration error
	        if(first) 
	          System.out.print(", ");
	        trans.addID(s.getVar(tok, 0));
	        first = true;
	      }
	      //else
	        //s.setError();
	    }
	    mustbe(TK.ID);
    }
    first = false;
    System.out.print(";\n");
  }
  
  private void statement_list() {
    while(is(TK.PRINT) | is(TK.ID) | is(TK.TILDE) | is(TK.DO) | is(TK.IF))
    {
      statement();
    }    
  }
    
  private void statement() {
    if(is(TK.PRINT))
      print();
    if(is(TK.ID) | is(TK.TILDE))
      assignment();
    if(is(TK.DO))
      dostmt();
    if(is(TK.IF))
      ifstmt();
  }
    
  private void print() {      
    scan();
    System.out.print("printf(\"%d\\n\", ");
    expr();
    System.out.println(");");
  }
    
  private void assignment() {     
    ref_id();
    mustbe(TK.ASSIGN);
    trans.addAssign();
    expr();
    System.out.print(";\n");
  }
    
  private void dostmt() {
    trans.addWhile();
    scan();
    guarded_cmd();
    mustbe(TK.ENDDO);
  }
    
  private void ifstmt() { 
    trans.addIf();
    scan();
    guarded_cmd();
    while(is(TK.ELSEIF)) {
      trans.addElseIf();
      scan();
      guarded_cmd();
    }
    if(is(TK.ELSE)) {
      trans.addElse();
      scan();
      trans.addLCurly();
      block();
      trans.addRCurly();
    }
    mustbe(TK.ENDIF);
  }
    
  private void ref_id() {  
    scope_index = -2; 
    if(is(TK.TILDE)) {
      scan();
      scope_index = -1;
      if(is(TK.NUM)) {
        scope_index = Integer.parseInt(tok.string);
        scan();
      }
    }
    if(is(TK.ID)) 
      scope_index = s.checkSymbol(scope_index, tok); 
    trans.addID(s.getVar(tok, scope_index)); //print var name
    mustbe(TK.ID);
    scope_index = -2; 
  }
    
  private void guarded_cmd() { 
    trans.addLParen();
    expr();
    System.out.print(" <= 0 ");
    trans.addRParen();
    mustbe(TK.THEN);
    trans.addLCurly();
    block();
    trans.addRCurly();
  } 
    
  private void expr() {
    term();
    while(addop()) {
      term();
    }
  }
    
  private void term() {
    factor();
    while(mulop()) {
      factor();
    }
  }
    
  private void factor() {
    if(is(TK.LPAREN)) {
      trans.addLParen();
      scan();
      expr();
      mustbe(TK.RPAREN);
      trans.addRParen();
    }
      
    else if(is(TK.NUM)) {
      trans.addNum(tok.string); 
      mustbe(TK.NUM);
    }
        
    else {
      ref_id();
    }
  }
    
  private boolean addop() {
    if(is(TK.PLUS)) {
      mustbe(TK.PLUS);
      trans.addPlus();
      return true;
    }
    else if(is(TK.MINUS)) {
      mustbe(TK.MINUS);
      trans.addMinus();
      return true;
    }
    return false;
  }
    
  private boolean mulop() {
    if(is(TK.TIMES)) {
      mustbe(TK.TIMES);
      trans.addTimes();
      return true;
    }
    else if(is(TK.DIVIDE)) {
      mustbe(TK.DIVIDE);
      trans.addDivide();
      return true;
    }
    return false;
  }
    
  // is current token what we want?
  private boolean is(TK tk) {
    return tk == tok.kind;
  }
  
  // ensure current token is tk and skip over it.
  private void mustbe(TK tk) {
	   if( tok.kind != tk ) {
	     System.err.println( "mustbe: want " + tk + ", got " +
				tok);
	     parse_error( "missing token (mustbe)" );
	   }

	   scan();
  }

  private void parse_error(String msg) {
	  System.err.println( "can't parse: line "
		    + tok.lineNumber + " " + msg );
    System.exit(1);
  }
  
}
