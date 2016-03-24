import java.util.ArrayList;

public class Symbols {
  
  //the symbols table
  private ArrayList<ArrayList<String>> al = new ArrayList<ArrayList<String>>();
  
  //constructor
  public Symbols() {}
  
  //adds new scope
  public void newScope() {
    al.add(new ArrayList<String>());
  }
  
  //removes current scope
  public void remove() {
      al.remove(al.size()-1);
  }
  
  //adds new variable if it doesn't exist
  public void declare(Token token) {
    if(cScope(0).indexOf(token.string) == -1){
	    //System.out.println("added " + token.string + " at scope " + al.size());
	    cScope(0).add(token.string);
    }
	  else {  //redeclaration
	    System.out.println("redeclaration of variable " + token.string);
	    //System.exit(1);
	  } 
  }  
  
  //checks if valid reference
  public void checkSymbol(int scope_index, Token token) {
    boolean any = false;
    //System.out.println("access ~" + scope_index + token.string);
    
    if(scope_index >= al.size()) //scope out of bounds
      bad_ref(scope_index, token.string, token.lineNumber);
    else if(scope_index == -1) { //global scope
      if(al.get(0).indexOf(token.string) == -1) 
        bad_ref(scope_index, token.string, token.lineNumber); 
    }
    else if(scope_index == -2) { //any scope
      for(int i = al.size() - 1; i >= 0; i--) {
        if(al.get(i).indexOf(token.string) != -1) {
          any = true;
          break;
        }
      }
      if(!any) {
        System.out.println(token.string + " is an undeclared variable on line "
          + token.lineNumber);
        System.exit(1);
      }
    }
    else if(cScope(scope_index).indexOf(token.string) == -1) //specified scope
      bad_ref(scope_index, token.string, token.lineNumber);
    any = false;  
  } 
  
  //returns strings at index
  private ArrayList<String> cScope(int index) {
      return al.get(al.size() - index - 1);
  }
  
  //bad reference
  public void bad_ref(int index, String s, int line) {
    if(index == -1) 
      System.out.println("no such variable ~" + s + " on line " + line);
    else  
      System.out.println("no such variable ~" + index + s + " on line " + line);
    System.exit(1);
  }
}  