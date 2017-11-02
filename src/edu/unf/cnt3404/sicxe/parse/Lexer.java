package edu.unf.cnt3404.sicxe.parse;

import java.io.BufferedReader;
import java.io.IOException;

import edu.unf.cnt3404.sicxe.parse.Token.Type;
import edu.unf.cnt3404.sicxe.syntax.Data;
import edu.unf.cnt3404.sicxe.syntax.data.AsciiData;
import edu.unf.cnt3404.sicxe.syntax.data.HexData;

//Reads many characters at a time and creates tokens
public class Lexer {
	
	private static final char EOS = (char)-1;
	private static final String SIMPLECHARACTERS = "#@,+-*/()";
	
	private BufferedReader reader;
	private int row = 1;
	private int col = 1;
	
	//Creates a lexer from a buffered reader. The reader can be from a file
	//Or from the standard in, etc.
	public Lexer(BufferedReader reader) {
		this.reader = reader;
	}

	private boolean isLineSperator(char c) {
		return c == '\n' || c == '\r';
	}
	
	private boolean isWhitespace(char c) {
		return c == '\t' || c == ' ';
	}
	
	private boolean isDataInitializer(char c) {
		return c == 'C' || c == 'X';
	}
	
	private boolean isSimple(char c) {
		return SIMPLECHARACTERS.contains(Character.toString(c));
	}
	
	private char read() {
		try {
			col++;
			reader.mark(1);
			return (char)reader.read();
		} catch (IOException e) {
			e.printStackTrace();
			return EOS;
		}
	}
	
	private char readUpper() {
		return Character.toUpperCase(read());
	}
	
	private void unread() {
			col--;
			try {
				reader.reset();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	//Gets the next token and consumes it.
	//Throws an exception if there is no available token.
	//Never returns null
	public Token next() {
		char c;
		if ((c = readUpper()) == EOS) {
			throw new ParseError(row, col, "Expected token not end of stream");	
		}
		if (isWhitespace(c)) {
			int row = this.row;
			int col = this.col;
			//Many sequential whitespace chars will yield a single whitespace token
			while (isWhitespace(c = read())); //Read
			unread();
			return Token.whitespace(row, col);
		}
		if (isLineSperator(c)) {
			Token result = Token.newline(row, col);
			row++;
			col = 1;
			return result;
		}
		if (Character.isDigit(c)) {
			StringBuilder number = new StringBuilder();
			number.append(c);
			//read all digits
			while (Character.isDigit(c = readUpper()) && c != EOS) {
				number.append(c);
			}
			//c is the first non-digit
			unread(); //Returns the 1 char back to the buffer
			//All characters of number satisfied isDigit, so parseInt should
			//never throw an exception
			return Token.number(row, col, Integer.parseInt(number.toString()));
		}
		if (c == '.') {
			StringBuilder comment = new StringBuilder();
			comment.append(c);
			//read until the next line
			while (!isLineSperator(c = readUpper()) && c != EOS) {
				comment.append(c);
			}
			unread();
			return Token.comment(row, col, comment.toString());
		}
		if (Character.isLetter(c)) {
			StringBuilder string = new StringBuilder();
			if (read() == '\'') {
				//A data instead
				boolean isAscii = c == 'C'; //Otherwise, is hex
				//Read until the next quote
				//Don't read upper
				while ((c = read()) != '\'' && c != EOS) {
					string.append(c);
				}
				//c was not a quote
				if (c == EOS) {
					throw new ParseError(row, col, "Expected ' not end of stream");
				}
				//Create the data object
				Data data;
				if (isAscii) {
					data = new AsciiData(string.toString());
				} else {
					data = new HexData(string.toString());
				}
				return Token.data(row, col, data);
			}
			unread();
			string.append(c);
			while ((Character.isLetter(c = readUpper()) || Character.isDigit(c)) && c != EOS) {
				string.append(c);
			}
			unread();
			return Token.symbol(row, col, string.toString());
		}
		if (isDataInitializer(c)) {
			System.out.println("If data");
			
		}
		if(isSimple(c)) {
			return Token.simple(row, col, c);
		}
		throw new ParseError(row, col, "Bad character " + c);
	}
	
	//Gets the next token but doesn't consume it
	//Returns null if there are no more tokens
	public Token peek() {
		return null;
	}
	
	//Returns whether the next token is simple and matches the given character
	//If this method returns true, then the token is consumed.
	public boolean accept(char c) {
		Token token = peek();
		boolean result = token != null && token.getType() == Type.SIMPLE
			&& token.is(c);
		if (result) {
			next();
		}
		return result;
	}
	
	//Return whether the next token is of the given type and returns the token if possible
	//If this method returns null, then the token was not of the given type
	public Token accept(Type type) {
		Token result = peek();
		if (result.getType() == type) {
			next();
			return result;
		}
		return null;
	}
	
	//Parses the next token and returns, or throws an exception if the next token was not
	//a simple token, or did not match the given character.
	public void expect(char c) {
		Token token = next();
		if (token.getType() != Type.SIMPLE || !token.is(c)) {
			throw new ParseError(token, "Expected " + c + " not " + token);
		}
	}
	
	//Parses the next token and returns it, or throws an exception if the next token was 
	//not of the given type
	public Token expect(Type type) {
		Token result = next();
		if (result.getType() != type) {
			throw new ParseError(result, "Expected " + type + " not " + result);
		}
		return result;
	}
}
