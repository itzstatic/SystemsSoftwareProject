package edu.unf.cnt3404.sicxe.syntax.command.directive;

import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;
import edu.unf.cnt3404.sicxe.syntax.command.ExpressionCommand;

public class EquDirective extends AbstractCommand implements ExpressionCommand {

	private Expression right;
	
	public EquDirective(Expression right) {
		this.right = right;
	}
	
	@Override
	public int getSize() {
		return 0;
	}
	
	@Override
	public Expression getExpression() {
		return right;
	}

}
