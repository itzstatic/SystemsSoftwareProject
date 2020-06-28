package edu.unf.cnt3404.sicxe.syntax.expression;

import java.util.List;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.Program;

public class ExpressionMacroParameter implements ExpressionNode {

	public Expression expression; // after macro substitution
	
	public String name;
	
	public int parameterPosition;
	
	public ExpressionMacroParameter(String name) {
		this.name = name;
	}
	

	public void usePositionalNotation(Command c, List<String> parameters) throws AssembleError {
		int index = parameters.indexOf(name);
		if (index < 0) {
			throw new AssembleError(c, "Unrecognized macro parameter &" + name);
		}
		parameterPosition = index;
	}
	
	
	@Override
	public int getValue(Command command, Program program) throws AssembleError {
		if (expression == null) {
			throw new AssembleError(command, "Macro parameter " + name + " not associated to macro time argument");
		}
		
		expression.evaluate(command, program);
		
		return expression.getValue();
	}

	@Override
	public void write(StringBuilder infix) {
		if (expression == null) {
			infix.append('&');
			infix.append(name);
		} else {
			expression.getRoot().write(infix);
		}
	}

	@Override
	public void addTerms(List<Term> terms, Program program) {
		
	}

}
