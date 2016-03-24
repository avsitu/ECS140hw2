import java.util.ArrayList;

public class Symbols {
  //the symbols table
  private ArrayList<ArrayList<String>> al = new ArrayList<ArrayList<String>>();
  private boolean error = false;
  
  //constructor
  public Symbols() {}
  
  //adds new scope
  public void add() {
    al.add(new ArrayList<String>());
  }
  
  //removes current scope
  public void remove() {
      al.remove(al.size()-1);
  }
  
  public void setError() {
    error = false;
  } 
  
  public boolean getError() {
    return error;
  }
  
  //returns var name for C code
  public String getVar(Token tok, int index) {
    if(index == -1) 
      return tok.string + "0";
    else 
      return tok.string + String.valueOf(al.size() - 1 - index);
  }
  
  //adds new variable if it doesn't exist
  public void declare(Token token) {
    if(cScope(0).indexOf(token.string) == -1) {
	    error = false;
	    cScope(0).add(token.string);
    }
	  else {  //redeclaration
	    System.err.println("redeclaration of variable " + token.string);
	    error = true;
	  }
  }  
  
  //checks if valid reference
  public int checkSymbol(int scope_index, Token token) {
    boolean any = false; //for checking index = -2 
    if(scope_index >= al.size()) //scope out of bounds
      bad_ref(scope_index, token.string, token.lineNumber);
    else if(scope_index == -1) { //global scope
      if(al.get(0).indexOf(token.string) == -1) 
        bad_ref(scope_index, token.string, token.lineNumber); 
    }
    else if(scope_index == -2) { //any scope
      for(int i = al.size() - 1; i >= 0; i--) {
        if(al.get(i).indexOf(token.string) != -1) {
          scope_index = al.size() - i - 1; //change scope_index to first match
          any = true; //if a match found
          break;
        }
      }
      if(!any) {
        System.err.println(token.string + 
        " is an undeclared variable on line " + token.lineNumber);
        System.exit(1);
      }
      any = false; 
    }
    else if(cScope(scope_index).indexOf(token.string) == -1) //specific scope
      bad_ref(scope_index, token.string, token.lineNumber);
    return scope_index;  
  } 
  
  //returns strings at index
  private ArrayList<String> cScope(int index) {
      return al.get(al.size() - index - 1);
  }

  //bad reference
  public void bad_ref(int index, String s, int line) {
    if(index == -1) 
      System.err.println("no such variable ~" + s + " on line " + line);
    else  
      System.err.println("no such variable ~" + 
        index + s + " on line " + line);
    System.exit(1);
  }
}  