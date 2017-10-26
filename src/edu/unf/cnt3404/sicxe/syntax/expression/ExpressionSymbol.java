package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.Program;

public class ExpressionSymbol implements ExpressionNode {

	private String symbol;
	
	public ExpressionSymbol(String symbol) {
	//Creates an expression from the given symbol
		this.symbol = symbol;
	}
	
	@Override
	public boolean isAbsolute(Program program) {
		return program.getSymbol(symbol).isAbsolute();
	}

	@Override
	public int getValue(Program program) {
		return program.getSymbol(symbol).getValue();
	}

	@Override
	public void addExternalSymbols(Program program, List<String> symbols) {
		if (program.getSymbol(symbol).isExternal()) {
			symbols.add(symbol);
		}
	}

}
