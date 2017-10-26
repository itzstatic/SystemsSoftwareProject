package edu.unf.cnt3404.sicxe.parse;

import java.io.BufferedReader;

import edu.unf.cnt3404.sicxe.syntax.Command;

//Calls the lexer repeatedly in order to create Commands.
public class Parser {
	
	//Creates a parser from the reader
	public Parser(BufferedReader reader) {
		
	}
	
	//Returns the next command from the source, or, null if there
	//are no commands left and the parsing is finished
	public Command next() {
		return null;
	}
}
