package edu.unf.cnt3404.sicxe.syntax.command.directive.macro;

import java.util.List;
import java.util.stream.Collectors;

import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;

public class MacroExpansionDirective extends AbstractCommand {

	private List<Expression> arguments;
	
	public MacroExpansionDirective(List<Expression> arguments) {
		this.arguments = arguments;
	}
	
	public List<Expression> getArguments() {
		return arguments;
	}
	
	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public String getArgument() {
		return arguments.stream()
			.map(Object::toString)
			.collect(Collectors.joining(","));
	}

}
