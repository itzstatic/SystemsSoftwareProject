package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.List;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.Symbol;

public class ExpressionSymbol implements ExpressionNode {

	private String name; //Of the symbol
	
	private Symbol symbol; //To be resolved during assembly
	
	//Creates an expression from a symbol with the given name
	public ExpressionSymbol(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public int getValue(Command command, Program program) {
		if (symbol == null) {
			symbol = program.getSymbol(name);
			if (symbol == null) {
				throw new AssembleError(command, "Unrecognized symbol " + name);
			}
		}
		return symbol.getValue();
	}

	@Override
	public void write(StringBuilder infix) {
		infix.append(name);
	}
	
	@Override
	public void addTerms(List<Term> terms, Program program) {
		if (!symbol.isAbsolute()) {
			terms.add(new Term(symbol));	
		}
	}
}
