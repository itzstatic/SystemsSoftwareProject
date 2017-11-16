package edu.unf.cnt3404.sicxe.syntax.command.directive;

import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;

public class ResbDirective extends AbstractCommand {

	public int numBytes;
	
	public ResbDirective(int numBytes) {
		this.numBytes = numBytes;
	}

	@Override
	public int getSize() {
		return numBytes;
	}

	@Override
	public String getArgument() {
		return " " + Integer.toString(numBytes);
	}
}
