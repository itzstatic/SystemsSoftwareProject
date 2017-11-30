package edu.unf.cnt3404.sicxe.parse;


@SuppressWarnings("serial")
public class AssembleError extends Exception implements Locatable {

	private int row;
	private int col;
	
	private AssembleError(int row, int col, String message) {
		super(message);
		this.row = row;
		this.col = col;
	}
	
	public AssembleError(Locatable l, String message) {
		this(l.getRow(), l.getCol(), message);
	}

	@Override
	public int getRow() {
		return row;
	}

	@Override
	public int getCol() {
		return col;
	}
}
