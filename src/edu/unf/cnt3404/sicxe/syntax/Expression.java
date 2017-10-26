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
	
	public int getValue(Program program) {
		return root.getValue(program);
	}
	
	public boolean isAbsolute(Program program) {
		return root.isAbsolute(program);
	}
	
	public List<String> getExternalSymbols(Program program) {
		List<String> result = new ArrayList<>();
		root.addExternalSymbols(program, result);
		return result;
	}
}
