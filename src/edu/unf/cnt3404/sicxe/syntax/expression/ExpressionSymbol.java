package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.List;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.Symbol;

public class ExpressionSymbol implements ExpressionNode {

	private String symbol;
	
	public ExpressionSymbol(String symbol) {
		//Creates an expression from the given symbol
		if (symbol == null) {
			System.out.println("Reee");
		}
		this.symbol = symbol;
	}
	
	@Override
	public boolean isAbsolute(Program program) {
		return program.getSymbol(symbol).isAbsolute();
	}

	@Override
	public int getValue(Command command, Program program) {
		Symbol s = program.getSymbol(symbol);
		if (s == null) {
			throw new AssembleError(command, "Unrecognized symbol " + symbol);
		}
		return s.getValue();
	}

	@Override
	public void write(StringBuilder infix) {
		infix.append(symbol);
	}
	
	@Override
	public void addAbsoluteSymbols(Program program, List<String> symbols) {
		if (program.getSymbol(symbol).isAbsolute()) {
			symbols.add(symbol);
		}
	}

	@Override
	public String toString() {
		return symbol;
	}
}
