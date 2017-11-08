package edu.unf.cnt3404.sicxe.syntax;

import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNode;
import edu.unf.cnt3404.sicxe.syntax.expression.SignedSymbol;

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
	private List<SignedSymbol> externals = new ArrayList<>();
	
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
	
	public boolean isAbsolute() {
		return getNetSign() == 0;
	}
	
	public int getNetSign() {
		if (evaluated) {
			return netSign;
		}
		throw new RuntimeException();
	}
	
	public void evaluate(Command command, Program program) {
		if (evaluated) {
			return;
		}
		value = root.getValue(command, program);
		
		//Get all symbols
		List<SignedSymbol> symbols = new ArrayList<>();
		root.addSignedSymbols(symbols);
		
		netSign = 0;
		for (SignedSymbol s : symbols) {
			if (s.getSymbol().isExternal()) {
				externals.add(s);
			} else if (!s.getSymbol().isAbsolute()) {
				netSign += (s.isPositive() ? 1 : -1);
			}
		}
		evaluated = true;
	}
	
	public List<SignedSymbol> getExternalSymbols() {
		return externals;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		root.write(result);
		return result.toString();
	}
}
