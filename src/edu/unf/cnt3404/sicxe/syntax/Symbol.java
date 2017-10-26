package edu.unf.cnt3404.sicxe.syntax;

public class Symbol {
	
	private String text;
	private int value;
	private boolean external;
	private boolean absolute;
	
	//Defines a local symbol
	public Symbol(String text, int value, boolean absolute) {
		this.text = text;
		this.value = value;
		this.absolute = absolute;
	}
	
	//Defines an external symbol
	public Symbol(String text) {
		this.text = text;
		external = true;
		absolute = true;
	}
	
	//Returns whether the symbol is external; otherwise, it is local
	public boolean isExternal() {
		return external;
	}
	//Returns whether the symbol is absolute; otherwise, it is relative
	public boolean isAbsolute() {
		return absolute;
	}
	//Returns the text of the symbol
	public String getText() {
		return text;
	}
	//Returns the address or value
	public int getValue() {
		return value;
	}
}
