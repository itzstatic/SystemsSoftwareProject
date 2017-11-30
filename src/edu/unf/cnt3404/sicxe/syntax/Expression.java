package edu.unf.cnt3404.sicxe.syntax;

import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNode;
import edu.unf.cnt3404.sicxe.syntax.expression.Term;

//Represents a complete expression that can be an operand of 
//certain commands in a program
public class Expression {
	
	//The root of the expression tree
	private ExpressionNode root;
	
	//To be assembled
	private int value;
	//The number of positive relative symbols minus the number of negative ones
	private int netSign;
	//The external symbol references and associated sign
	private List<Term> externals = new ArrayList<>();
	
	private boolean evaluated;
	
	public Expression(ExpressionNode root) {
		this.root = root;
	}
	
	public int getValue() {
		if (evaluated) {
			return value;
		}
		throw new RuntimeException();
		
	}
	
	public boolean isEvaluated() {
		return evaluated;
	}
	
	public boolean isAbsolute() {
		return getNetSign() == 0;
	}
	
	public int getNetSign() {
		if (evaluated) {
			return netSign;
		}
		throw new RuntimeException();
	}
	
	public void evaluate(Command command, Program program) throws AssembleError {
		if (evaluated) {
			return;
		}
		value = root.getValue(command, program);
		
		//Get all symbols
		List<Term> terms = new ArrayList<>();
		root.addTerms(terms, program);
		
		netSign = 0;
		for (Term term : terms) {
			Symbol symbol = term.getSymbol();
			//Star terms have null symbol
			if (symbol != null && symbol.isExternal()) {
				externals.add(term);
			} else if (symbol == null || !symbol.isAbsolute()) {
				netSign += (term.isPositive() ? 1 : -1);
			}
		}
		evaluated = true;
	}
	
	public List<Term> getExternalSymbols() {
		return externals;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		root.write(result);
		return result.toString();
	}
}
