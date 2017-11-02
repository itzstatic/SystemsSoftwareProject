package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.Program;

//Represents an operation in an expression. These are addition, subtraction,
//multiplication, and division
public class ExpressionOperator implements ExpressionNode {

	private Type operator;
	private ExpressionNode left;
	private ExpressionNode right;
	
	public ExpressionOperator(Type operator, ExpressionNode left, 
			ExpressionNode right) {
		this.operator = operator;
		this.left = left;
		this.right = right;
	}
	
	@Override
	public boolean isAbsolute(Program program) {
		boolean l = left.isAbsolute(program);
		boolean r = right.isAbsolute(program);
		
		if (operator == Type.SUB && !l && !r) {
			//In subtraction, relative left and right operands 
			//Will leave an absolute expression
			return true;
		}
		//Both operations must be absolute
		return l && r;
	}
	
	@Override
	public int getValue(Program program) {
		int l = left.getValue(program);
		int r = right.getValue(program);
		switch(operator) {
		case ADD: return l + r;
		case SUB: return l - r;
		case MUL: return l * r;
		case DIV: return l / r;
		default: throw new IllegalStateException(operator.toString());
		}
	}

	@Override
	public void addAbsoluteSymbols(Program program, List<String> symbols) {
		left.addAbsoluteSymbols(program, symbols);
		right.addAbsoluteSymbols(program, symbols);
	}
	
	@Override
	public String toString() {
		switch(operator) {
		case ADD: return "+";
		case SUB: return "-";
		case MUL: return "*";
		case DIV: return "/";
		default: throw new IllegalStateException(operator.toString());
		}
	}
	
	public static enum Type {
		ADD(0),
		SUB(0),
		MUL(1),
		DIV(1);
		
		//Higher integer means higher precedence: Do it first
		private int precedence;
	
		private Type(int precedence) {
			this.precedence = precedence;
		}
		
		public int getPrecedence() {
			return precedence;
		}
	}
}
