package edu.unf.cnt3404.sicxe.syntax;

import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNode;

//Represents a complete expression that can be an operand of 
//certain commands in a program
public class Expression {
	
	//The root of the expression tree
	private ExpressionNode root;
	
	public Expression(ExpressionNode root) {
		this.root = root;
	}
	
	public int getValue(Command command, Program program) {
		return root.getValue(command, program);
	}
	
	//A degree 0 expression is absolute. 
	//A degree +/-1 expression is relative.
	//Any degree higher is not valid in a SicXe program
	public int getDegree() {
		return 0;
	}
	
	public List<String> getExternalSymbols(Program program) {
		List<String> result = new ArrayList<>();
		root.addAbsoluteSymbols(program, result);
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		root.write(result);
		return result.toString();
	}
}
