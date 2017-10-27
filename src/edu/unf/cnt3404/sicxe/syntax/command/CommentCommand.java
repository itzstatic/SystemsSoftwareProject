package edu.unf.cnt3404.sicxe.syntax.command;

import edu.unf.cnt3404.sicxe.syntax.Command;

public class CommentCommand implements Command {

	private String comment;
	
	public CommentCommand(String comment) {
		this.comment = comment;
	}
	
	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public void setLabel(String label) {
		//No label
	}

	@Override
	public String getLabel() {
		//No label
		return null;
	}

	@Override
	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String getComment() {
		return comment;
	}

}
