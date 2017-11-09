package edu.unf.cnt3404.sicxe.parse;

import edu.unf.cnt3404.sicxe.syntax.Command;


public class AssembleError extends RuntimeException {

	private static final long serialVersionUID = 5125145320795821947L;

	private AssembleError(int row, int col, String message) {
		super("Row: " + row + "; Col: " + col + "; " + message);
	}
	
	public AssembleError(Locatable l, String message) {
		this(l.getRow(), l.getCol(), message);
	}
	
	public AssembleError(Command c, String message) {
		this(c.getLine(), 1, message);
	}
}
