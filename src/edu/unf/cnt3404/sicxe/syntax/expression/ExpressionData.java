package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.List;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Data;
import edu.unf.cnt3404.sicxe.syntax.Program;

public class ExpressionData implements ExpressionNode {

	private Data literal;
	
	public ExpressionData(Data literal) {
		this.literal = literal;
	}
	
	@Override
	public int getValue(Command command, Program program) throws AssembleError {
		return program.getLiteral(literal);
	}
	
	@Override
	public void collectLiterals(Program program) {
		program.addLiteral(literal);
	}

	@Override
	public void write(StringBuilder infix) {
		infix.append('=');
		infix.append(literal);
	}

	@Override
	public void addTerms(List<Term> terms, Program program) {
		terms.add(new Term());
	}

}
