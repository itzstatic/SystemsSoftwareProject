package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;

//Represents a node in an arithmetic expression tree
public interface ExpressionNode {
	//Returns whether this node is absolute
	//otherwise, it is relative
	boolean isAbsolute(Program program);

	//Returns the value of this expression
	int getValue(Command command, Program program);
	
	//Write infix string of this expression
	void write(StringBuilder infix);
	
	//Appends external symbols in this expression to the list
	void addAbsoluteSymbols(Program program, List<String> symbols);
}
