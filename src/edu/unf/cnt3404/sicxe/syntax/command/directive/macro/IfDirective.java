package edu.unf.cnt3404.sicxe.syntax.command.directive.macro;

import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;
import edu.unf.cnt3404.sicxe.syntax.command.ExpressionCommand;

public class IfDirective extends AbstractCommand implements ExpressionCommand {

	private Expression condition;
	private List<Command> commands = new ArrayList<>();
	private ElseDirective elseDirective;
	
	public IfDirective(Expression condition) {
		this.condition = condition;
	}
	
	public void setElse(ElseDirective d) {
		this.elseDirective = d;
	}
	
	public ElseDirective getElse() {
		return elseDirective;
	}
	
	public List<Command> getCommands() {
		return commands;
	}
	
	public void addCommand(Command c) {
		commands.add(c);
	}
	
	@Override
	public int getSize() {
		return 0;
	}
	
	@Override
	public Expression getExpression() {
		return condition;
	}

}
