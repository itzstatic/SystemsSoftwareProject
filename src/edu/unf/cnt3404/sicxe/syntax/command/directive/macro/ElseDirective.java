package edu.unf.cnt3404.sicxe.syntax.command.directive.macro;

import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;

public class ElseDirective extends AbstractCommand {

	private List<Command> commands = new ArrayList<>();
	
	public void addCommand(Command c){
		this.commands.add(c);
	}
	
	public List<Command> getCommands() {
		return commands;
	}
	
	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public String getArgument() {
		return null;
	}

}
