package edu.unf.cnt3404.sicxe.syntax.command.directive;

import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;

public class ReswDirective extends AbstractCommand {

	private int numWords;
	
	public ReswDirective(int numWords) {
		this.numWords = numWords;
	}

	@Override
	public int getSize() {
		return numWords * 3;
	}
	
	@Override
	public String toString() {
		return "RESW " + numWords;
	}
}
