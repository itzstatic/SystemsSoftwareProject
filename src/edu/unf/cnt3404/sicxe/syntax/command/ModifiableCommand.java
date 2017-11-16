package edu.unf.cnt3404.sicxe.syntax.command;


//Represents a command that has an expression, and writes object code.
//Therefore, a modification record must be generated for each relative symbol. 
//in the expression. The modification record needs to know exactly how to modify the command
public interface ModifiableCommand extends ExpressionCommand, WriteableCommand {
	//The number of bytes between the address of the command, and the
	//beginning of the modification
	int getOffset();
	
	//The number of half-bytes to modify the expression, starting at the offset.
	int getStride();
	
	//Whether the assembled value in the object code is absolutely the expression's
	//value. Or, false, if the value in the code is relative to something else.
	//This is only relevant to modifying the local symbols in the expression.
	boolean isAbsolute();
}
