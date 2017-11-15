package edu.unf.cnt3404.sicxe.syntax.expression;

import edu.unf.cnt3404.sicxe.syntax.Symbol;

//Represents a relative term AND its total sign in an expression
public class Term {
	private Symbol symbol;
	private boolean positive = true;
	
	//Constructs a positive star (*) term
	public Term() { }
	
	//Constructs a term with a positive signed symbol
	public Term(Symbol symbol) {
		this.symbol = symbol;
	}
	
	//Toggles the sign of this symbol
	public void invertSign() {
		positive = !positive;
	}
	
	//Returns the symbol, if this relative term is a relative symbol
	//Otherwise, returns null if this relative term is a star
	public Symbol getSymbol() {
		return symbol;
	}
	
	//Gets the sign of this symbol, returning true if positive,
	//and false if negative
	public boolean isPositive() {
		return positive;
	}
}
