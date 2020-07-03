package edu.unf.cnt3404.sicxe.syntax.command.directive.macro;

import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.Command;

public interface MacroDirective extends Command {
	List<Command> getCommands();
}
