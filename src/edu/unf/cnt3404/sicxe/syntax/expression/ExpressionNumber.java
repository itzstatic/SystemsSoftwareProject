package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;

//Represents a constant numerical value expression
public class ExpressionNumber implements ExpressionNode {

	private int value;
	
	//Creates an expression with the given value
	public ExpressionNumber(int value) {
		this.value = value;
	}

	@Override
	public int getValue(Command command, Program program) {
		return value;
	}

	@Override
	public void write(StringBuilder infix) {
		infix.append(Integer.toString(value));
	}
	
	@Override
	public void addTerms(List<Term> terms, Program program) {
		//No terms to add
	}
}
