package edu.unf.cnt3404.sicxe.syntax.expression;

import edu.unf.cnt3404.sicxe.syntax.Symbol;

//Represents a symbol and its total sign in an expression
public class SignedSymbol {
	private Symbol symbol;
	private boolean positive;
	
	//Constructs a symbol with the given text and sign
	public SignedSymbol(Symbol symbol, boolean positive) {
		this.symbol = symbol;
		this.positive = positive;
	}
	
	//Toggles the sign of this symbol
	public void invertSign() {
		positive = !positive;
	}
	
	public Symbol getSymbol() {
		return symbol;
	}
	
	//Gets the sign of this symbol, returning true if positive,
	//and false if negative
	public boolean isPositive() {
		return positive;
	}
}
