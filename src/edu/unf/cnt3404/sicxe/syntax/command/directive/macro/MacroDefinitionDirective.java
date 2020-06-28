package edu.unf.cnt3404.sicxe.syntax.command.directive.macro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;

public class MacroDefinitionDirective extends AbstractCommand {

	private List<String> parameters;
	
	private List<Command> commands = new ArrayList<>();
	
	public MacroDefinitionDirective(List<String> parameters) {
		this.parameters = parameters;
	}
	
	public void addCommand(Command c) {
		commands.add(c);
	}
	
	public List<Command> getCommands() {
		return commands;
	}
	
	public List<String> getParameters()
	{
		return parameters;
	}
	
	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public String getArgument() {
		return parameters.stream()
			.map(x -> '&' + x.toUpperCase())
			.collect(Collectors.joining(","));
	}

}
