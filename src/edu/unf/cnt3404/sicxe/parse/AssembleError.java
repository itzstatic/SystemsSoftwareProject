package edu.unf.cnt3404.sicxe.parse;

import edu.unf.cnt3404.sicxe.syntax.Command;

public class AssembleError extends RuntimeException {

	private static final long serialVersionUID = 5125145320795821947L;

	public AssembleError(String message) {
		super(message);
	}
	
	public AssembleError(int row, int col, String message) {
		super("Row: " + row + "; Col: " + col + "; " + message);
	}
	
	public AssembleError(Token token, String message) {
		this(token.getRow(), token.getCol(), message);
	}
	
	public AssembleError(Command command, String message) {
		this(command.getLine(), 1, message);
	}
}
