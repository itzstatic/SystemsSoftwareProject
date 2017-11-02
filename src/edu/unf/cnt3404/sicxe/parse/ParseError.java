package edu.unf.cnt3404.sicxe.parse;

import edu.unf.cnt3404.sicxe.syntax.Command;

public class ParseError extends RuntimeException {

	private static final long serialVersionUID = 5125145320795821947L;

	public ParseError(int row, int col, String message) {
		super("Row: " + row + "; Col: " + col + "; " + message);
	}
	
	public ParseError(Token token, String message) {
		this(token.getRow(), token.getCol(), message);
	}
	
	public ParseError(Command command, String message) {
		this(command.getLine(), 1, message);
	}
}
