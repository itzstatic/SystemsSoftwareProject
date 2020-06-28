package edu.unf.cnt3404.sicxe.syntax.command.directive;

import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;

public class ResfDirective extends AbstractCommand {

	private int numFloats;
	
	public ResfDirective(int numFloats) {
		this.numFloats = numFloats;
	}
	
	@Override
	public int getSize() {
		return numFloats * 6;
	}

	@Override
	public String getArgument() {
		return " " + Integer.toString(numFloats);
	}

}
