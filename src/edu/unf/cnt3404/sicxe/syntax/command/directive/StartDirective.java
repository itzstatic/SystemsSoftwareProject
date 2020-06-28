package edu.unf.cnt3404.sicxe.syntax.command.directive;

import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;

public class StartDirective extends AbstractCommand {

	private int start; //Address
	
	public StartDirective(int start) {
		this.start = Integer.parseInt(Integer.toString(start), 16);
	}
	
	public int getStart() {
		return start;
	}
	
	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public String getArgument() {
		return " " + Integer.toString(start, 16);
	}
}
