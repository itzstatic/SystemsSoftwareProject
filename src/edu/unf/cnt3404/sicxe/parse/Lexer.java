package edu.unf.cnt3404.sicxe.parse;

import java.io.BufferedReader;

//Reads many characters at a time and creates tokens
public class Lexer {
	
	//Creates a lexer from a buffered reader. The reader can be from a file
	//Or from the standard in, etc.
	public Lexer(BufferedReader reader) {
		
	}

	//Gets the next token and consumes it, or,
	//Returns null if there are no more tokens
	public Token next() {
		return null;
	}
	
	//Gets the next token but doesn't consume it
	public Token peek() {
		return null;
	}
}
