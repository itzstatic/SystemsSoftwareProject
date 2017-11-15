package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;

//Represents a node in an arithmetic expression tree
public interface ExpressionNode {
	//Returns the value of this expression
	int getValue(Command command, Program program);
	
	//Write infix string of this expression
	void write(StringBuilder infix);
	
	//Appends relative terms in this expression to the list
	//Also appends external symbols in this expression
	void addTerms(List<Term> terms, Program program);
}
