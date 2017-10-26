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
		switch(operator) {
		case ADD: return left.getValue(program) + right.getValue(program);
		case SUB: return left.getValue(program) - right.getValue(program);
		case MUL: return left.getValue(program) * right.getValue(program);
		case DIV: return left.getValue(program) / right.getValue(program);
		default: throw new IllegalStateException(operator.toString());
		}
	}

	@Override
	public void addExternalSymbols(Program program, List<String> symbols) {
		left.addExternalSymbols(program, symbols);
		right.addExternalSymbols(program, symbols);
	}
	
	public static enum Type {
		ADD,
		SUB,
		MUL,
		DIV
	}
	
}
