package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.Command;
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
	public int getValue(Command command, Program program) {
		int l = left.getValue(command, program);
		int r = right.getValue(command, program);
		switch(operator) {
		case ADD: return l + r;
		case SUB: return l - r;
		case MUL: return l * r;
		case DIV: return l / r;
		default: throw new IllegalStateException(operator.toString());
		}
	}

	@Override
	public void write(StringBuilder infix) {
		//Whether to parenthesize the left
		boolean l = left instanceof ExpressionOperator
			&& ((ExpressionOperator)left).operator.precedence
			< operator.precedence;
		//Likewise for the right
		boolean r;
		if (right instanceof ExpressionOperator) {
			//This hard-coded solution is so trashy...
			r = operator == ExpressionOperator.Type.SUB
				|| ((ExpressionOperator)right).operator.precedence < operator.precedence;
		} else {
			r = false;
		}
		
		if (l) infix.append('(');
		left.write(infix);
		if (l) infix.append(')');
		
		infix.append(' ');
		infix.append(operator.toString());
		infix.append(' ');
		
		if (r) infix.append('(');
		right.write(infix);
		if (r) infix.append(')');
	}
	
	@Override
	public void addTerms(List<Term> terms, Program program) {
		//No relative terms may enter into multiplication or division
		
		
		left.addTerms(terms, program);
		
		if (operator == Type.SUB) {
			//Invert the sign of the right side's symbols
			List<Term> buffer = new ArrayList<>();
			right.addTerms(buffer, program);
			for (Term symbol : buffer) {
				symbol.invertSign();
			}
			
			terms.addAll(buffer);
		} else {
			right.addTerms(terms, program);
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
		
		@Override
		public String toString() {
			switch(this) {
			case ADD: return "+";
			case SUB: return "-";
			case MUL: return "*";
			case DIV: return "/";
			default: throw new IllegalStateException();
			}
		}
	}
}
