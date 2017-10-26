package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.Program;

//Represents a star (*) that is, the current location counter
public class ExpressionStar implements ExpressionNode {

	@Override
	public boolean isAbsolute(Program program) {
		//Location counter is always relative
		return false;
	}

	@Override
	public int getValue(Program program) {
		return program.getLocationCounter();
	}

	@Override
	public void addExternalSymbols(Program program, List<String> symbols) {
		//No symbols in the star expression
	}

}
