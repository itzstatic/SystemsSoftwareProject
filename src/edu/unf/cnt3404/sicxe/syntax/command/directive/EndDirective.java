package edu.unf.cnt3404.sicxe.syntax.command.directive;

import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;
import edu.unf.cnt3404.sicxe.syntax.command.ExpressionCommand;

public class EndDirective extends AbstractCommand implements ExpressionCommand {

	private Expression first; //Executable instruction
	
	//When there is no label
	public EndDirective() {
		this(null);
	}
	
	public EndDirective(Expression first) {
		this.first = first;
	}
	//If this method returns null, then this end directive didn't
	//have a first expression
	@Override
	public Expression getExpression() {
		return first;
	}
	
	@Override
	public int getSize() {
		return 0;
	}

	//The only expression command that might contain a null expression
	@Override
	public String getArgument() {
		if (first == null) {
			return null;
		}
		return " " + first.toString();
	}
}
