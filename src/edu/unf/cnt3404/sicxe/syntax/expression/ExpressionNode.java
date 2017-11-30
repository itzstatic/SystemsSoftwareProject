package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.List;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;

//Represents a node in an arithmetic expression tree
public interface ExpressionNode {
	//Returns the value of this expression
	int getValue(Command command, Program program) throws AssembleError;
	
	//Write infix string of this expression
	void write(StringBuilder infix);
	
	//Appends relative terms in this expression to the list
	//Also appends external symbols in this expression
	//If a symbol needs a mod record, Then it is added to the list
	//NOTE: Relative symbols using PC or BASE relative addressing do NOT
	//need mod records; so, the converse is not true
	void addTerms(List<Term> terms, Program program);
}
