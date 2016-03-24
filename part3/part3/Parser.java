/* *** This file is given as part of the programming assignment. *** */
import java.util.ArrayList;

public class Parser {

  // tok is global to all these parsing methods;
  // scan just calls the scanner's scan method and saves the result in tok.
  private Token tok; 
  private Scan scanner;
  
  private Symbols s = new Symbols(); //class handling Symbols
  
  
  private int scope_index = -2; //-2 = any scope, -1 = global
  
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
    block();
//    isRemove();
  }

  private void block(){

    s.newScope();  
	  declaration_list();
	  statement_list();
	  s.remove();
  }

  private void declaration_list() {
	// below checks whether tok is in first set of declaration.
	// here, that's easy since there's only one token kind in the set.
	// in other places, though, there might be more.
	// so, you might want to write a general function to handle that.
	  while( is(TK.DECLARE) ) {
	    declaration();
	  }
  }

  private void declaration() {
    mustbe(TK.DECLARE);
    if(is(TK.ID)) { 
      s.declare(tok);
      scan();
    }  
	  while( is(TK.COMMA) ) {
	    scan();
	    if(is(TK.ID))  
	      s.declare(tok);
	    mustbe(TK.ID);
    }
  }
  
  private void statement_list() {
    while(is(TK.PRINT) | is(TK.ID) | is(TK.TILDE) | is(TK.DO) | is(TK.IF)) {
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
    expr();
  }
    
  private void assignment() {     
    ref_id();
    mustbe(TK.ASSIGN);
    expr();
  }
    
  private void dostmt() {
    scan();
    guarded_cmd();
    mustbe(TK.ENDDO);
  }
    
  private void ifstmt() {  
    scan();
    guarded_cmd();
    while(is(TK.ELSEIF)) {
      scan();
      guarded_cmd();
    }
    if(is(TK.ELSE)) {
      scan();
      block();
    }
    mustbe(TK.ENDIF);
  }
    
  private void ref_id() {     
    if(is(TK.TILDE)) {
      scan();
      scope_index = -1;
      if(is(TK.NUM)) {
        scope_index = Integer.parseInt(tok.string);
        scan();
      }
    }
    if(is(TK.ID)) 
      s.checkSymbol(scope_index, tok); 
    scope_index = -2;  
    mustbe(TK.ID);
  }
    
  private void guarded_cmd() {       
    expr();
    mustbe(TK.THEN);
    block();
  } 
    
  private void expr() {
    term();
    while(is(TK.PLUS) | is(TK.MINUS)) {
      scan();
      term();
    }
  }
    
  private void term() {
    factor();
    while(is(TK.TIMES) | is(TK.DIVIDE)) {
      scan();
      factor();
    }
  }
    
  private void factor() {
    if(is(TK.LPAREN)) {
      scan();
      expr();
      mustbe(TK.RPAREN);
    }
      
    else if(is(TK.NUM))
      mustbe(TK.NUM);
        
    else
      ref_id();
  }
    
  private void addop() {
    if(is(TK.PLUS))
      mustbe(TK.PLUS);
    else
      mustbe(TK.MINUS);
  }
    
  private void mulop() {
    if(is(TK.TIMES))
      mustbe(TK.TIMES);
    else
      mustbe(TK.DIVIDE);
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
