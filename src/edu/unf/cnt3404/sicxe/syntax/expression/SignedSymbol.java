package edu.unf.cnt3404.sicxe.syntax.expression;

import edu.unf.cnt3404.sicxe.syntax.Symbol;

//Represents a symbol and its total sign in an expression
public class SignedSymbol {
	private Symbol symbol;
	private boolean positive = true;
	
	//Constructs a symbol with a positive sign
	public SignedSymbol(Symbol symbol) {
		this.symbol = symbol;
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
