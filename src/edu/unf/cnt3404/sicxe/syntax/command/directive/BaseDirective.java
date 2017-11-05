package edu.unf.cnt3404.sicxe.syntax.command.directive;

import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;
import edu.unf.cnt3404.sicxe.syntax.command.ExpressionCommand;

public class BaseDirective extends AbstractCommand implements ExpressionCommand {

	private Expression expr;
	
	public BaseDirective(Expression expr) {
		this.expr = expr;
	}
	
	@Override
	public Expression getExpression() {
		return expr;
	}
	
	@Override
	public int getSize() {
		return 0;
	}
	
	@Override
	public String toString() {
		return "BASE " + expr;
	}

	@Override
	public String getName() {
		return "BASE";
	}
}
