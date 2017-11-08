package edu.unf.cnt3404.sicxe.syntax.command;

import edu.unf.cnt3404.sicxe.syntax.Command;

//Convenience class that implements label and comment methods
public abstract class AbstractCommand implements Command {
	private String label;
	private String comment;
	private int line;

	@Override
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String getLabel() {
		return label;
	}
	
	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	@Override
	public String getComment() {
		return comment;
	}

	@Override
	public void setLine(int line) {
		this.line = line;
	}
	
	@Override
	public int getLine() {
		return line;
	}
}
