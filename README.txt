Compile: javac *.java
Run: java e2c < 'filename' 

See pdf for detail BNF grammar of the E language

Part 5 implements for loop functions in E. 
We chose for loop because it's the one essential functionality missing in E. 
Six test files and their respective correct outputs are provided for part 5.

//For Loop Grammar
//add to 'statement' in E's BNF
statement ::= for

//new rule for for-loop
//follows closely the format of for-loops in C/C++/Java
for ::= '{' assignment ref_id compOp expr assignment block '}'

//new rule for comparison operator in for-loop conditions
compOp ::= '&<' | '&>'
