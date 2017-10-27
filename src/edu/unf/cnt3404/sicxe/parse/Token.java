package edu.unf.cnt3404.sicxe.parse;

public class Token {
	private Object value;
	private Type type;
	private int row;
	private int col;
	
	//Helper constructor to reduce space
	private Token(int row, int col, Type type, Object value) {
		this.row = row;
		this.col = col;
		this.type = type;
		this.value = value;
	}
	
	public static Token number(int row, int col, int num) {
		return new Token(row, col, Type.NUMBER, num);
	}
	public static Token symbol(int row, int col, String symbol) {
		return new Token(row, col, Type.SYMBOL, symbol);
	}
	public static Token comment(int row, int col, String comment) {
		return new Token(row, col, Type.COMMENT, comment);
	}
	public static Token simple(int row, int col, char c) {
		return new Token(row, col, Type.SIMPLE, c);
	}
	
	public int getRow() {
		return row;
	}
	public int getCol() {
		return col;
	}
	public Type getType() {
		return type;
	}
	
	public int asNumber() {
		return ((Integer)value).intValue();
	}
	public String asSymbol() {
		return (String) value;
	}
	public String asComment() {
		return (String) value;
	}
	
	public boolean is(char c) {
		return ((Character)value).charValue() == c;
	}
	
	public enum Type {
		NUMBER,
		SYMBOL,
		COMMENT,
		DATA, //X'...' or C'...'
		SIMPLE, //A simple token is one character
	}
}
