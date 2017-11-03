package edu.unf.cnt3404.sicxe.syntax.command.directive;

import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;

public class NoBaseDirective extends AbstractCommand {

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public String toString() {
		return "NOBASE";
	}

	@Override
	public String getName() {
		return "NOBASE";
	}

	@Override
	public String getArgument() {
		return null;
	}
}
