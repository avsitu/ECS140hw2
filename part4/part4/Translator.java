import java.util.ArrayList;

//generates C code
public class Translator {
  //Constructor
  public Translator() {}
  
  public void addDec() {
    System.out.print("int ");
  }

  public void addPrint() {
    System.out.print("printf(\"");
  }

  public void addID(String str) {
    System.out.print("x_" + str);
  }
  
  public void addAssign() {
    System.out.print(" = ");
  }
  
  public void addDo() {
    System.out.print("do");
  }
  
  public void addIf() {
    System.out.print("if");
  }
  
  public void addElseIf() {
    System.out.print("else if");
  }
  
  public void addElse() {
    System.out.print("else");
  }
  
  public void addWhile() {
    System.out.print("while");
  }
  
  public void addLParen() {
    System.out.print("(");
  }
  
  public void addRParen() {
    System.out.print(")");
  }
  
  public void addPlus() {
    System.out.print(" + ");
  }
  
  public void addMinus() {
    System.out.print(" - ");
  }
  
  public void addTimes() {
    System.out.print(" * ");
  }
  
  public void addDivide() {
    System.out.print(" / ");
  }
  
  public void addNum(String num) {
    System.out.print(num);
  }
  
  public void addLCurly() {
    System.out.print(" {\n");
  }
  
  public void addRCurly() {
    System.out.println("}");
  }
}