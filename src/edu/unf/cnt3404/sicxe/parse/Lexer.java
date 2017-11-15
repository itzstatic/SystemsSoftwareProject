package edu.unf.cnt3404.sicxe.parse;

import edu.unf.cnt3404.sicxe.global.Global;
import edu.unf.cnt3404.sicxe.global.Mnemonic;
import edu.unf.cnt3404.sicxe.parse.Token;
import edu.unf.cnt3404.sicxe.syntax.Data;
import edu.unf.cnt3404.sicxe.syntax.data.AsciiData;
import edu.unf.cnt3404.sicxe.syntax.data.HexData;

//Reads many characters at a time and creates tokens
public class Lexer implements Locatable {
	
	private static final String SIMPLE_CHARACTERS = "#@,+-*/()";
	
	private Scanner scanner;
	//For peaking: If null, then a call to peek() needs to invoke next(),
	//and then make buffer non-null.
	//Otherwise, a call to peek() will yield the buffer.
	private Token buffer; 
	
	//Creates a lexer from a buffered reader. The reader can be from a file
	//Or from the standard in, etc.
	public Lexer(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public int getRow() {
		return scanner.getRow();
	}
	
	@Override
	public int getCol() {
		return scanner.getCol();
	}
	
	private boolean isLineSeparator(char c) {
		return c == '\n' || c == '\r';
	}
	
	private boolean isWhitespace(char c) {
		return c == '\t' || c == ' ';
	}
	
	private boolean isSimple(char c) {
		return SIMPLE_CHARACTERS.contains(Character.toString(c));
	}
	
	//Gets the next token and consumes it.
	//Throws an exception if there is no available token.
	//Never returns null
	public Token next() {
		//If buffer is not null, then there was a call to peek() AFTER the last 
		//call to next(). in other words, only consume the buffer
		if (buffer != null) {
			Token result = buffer;
			buffer = null;
			return result;
		}
		char c;
		int row = scanner.getRow();
		int col = scanner.getCol();
		if ((c = Character.toUpperCase(scanner.next())) == Scanner.EOS) {
			throw new AssembleError(scanner, "Expected token not end of stream");	
		}
		if (isWhitespace(c)) {
			//Many sequential whitespace chars will yield a single whitespace token
			while (isWhitespace(c = scanner.peek())) {
				scanner.next();
			}
			return Token.whitespace(row, col);
		}
		if (isLineSeparator(c)) {
			Token result = Token.newline(row, col);
			row++;
			col = 1;
			return result;
		}
		if (Character.isDigit(c)) {
			StringBuilder number = new StringBuilder();
			number.append(c);
			//read all digits
			while (Character.isDigit(c = Character.toUpperCase(scanner.peek())) 
				&& c != Scanner.EOS) {
				scanner.next();
				number.append(c);
			}
			//c is the first non-digit
			//All characters of number satisfied isDigit, so parseInt should
			//never throw an exception
			return Token.number(row, col, Integer.parseInt(number.toString()));
		}
		if (c == '.') {
			StringBuilder comment = new StringBuilder();
			//read until the next line
			while (!isLineSeparator(c = Character.toUpperCase(scanner.peek())) 
				&& c != Scanner.EOS) {
				scanner.next();
				comment.append(c);
			}
			return Token.comment(row, col, comment.toString());
		}
		if (Character.isLetter(c) || c == '_') {
			StringBuilder string = new StringBuilder();
			if (scanner.peek() == '\'') {
				scanner.next(); //Consume the open quote
				//A data instead
				boolean isAscii = c == 'C'; //Otherwise, is hex
				//Read until the next quote
				//Don't upper case
				while ((c = scanner.next()) != '\'' && c != Scanner.EOS) {
					string.append(c);
				}
				//c was not a quote
				if (c == Scanner.EOS) {
					throw new AssembleError(scanner, "Expected ' not end of stream");
				}
				//Create the data object
				Data data;
				if (isAscii) {
					data = new AsciiData(string.toString());
				} else if (string.length() % 2 != 0) {
					//Odd lengthed hex data
					throw new AssembleError(scanner, "Hex data must be of even legnth");
				} else {
					data = new HexData(string.toString());
				}
				return Token.data(row, col, data);
			}
			string.append(c);
			while ((Character.isLetter(c = Character.toUpperCase(scanner.peek())) 
				|| Character.isDigit(c) || c == '_') && c != Scanner.EOS) {
				scanner.next();
				string.append(c);
			}
			String s = string.toString();
			
			//Check if the string is a mnemonic
			Mnemonic mnemonic = Global.OPTAB.get(s);
			if (mnemonic != null) {
				return Token.mnemonic(row, col, mnemonic);
			}
			
			return Token.symbol(row, col, string.toString());
		}
		if(isSimple(c)) {
			return Token.simple(row, col, c);
		}
		throw new AssembleError(scanner, "Bad character " + c);
	}
	
	//Gets the next token but doesn't consume it
	//Returns null if there are no more tokens
	public Token peek() {
		if (buffer == null) {
			if (scanner.peek() == Scanner.EOS) {
				return null;
			}
			return buffer = next();
		} 
		return buffer;
	}
	
	//Returns whether the next token is simple and matches the given character
	//If this method returns true, then the token is consumed.
	public boolean accept(char c) {
		Token token = peek();
		boolean result = token != null && token.is(Token.Type.SIMPLE) && token.is(c);
		if (result) {
			next();
		}
		return result;
	}
	
	//Return whether the next token is of the given type and returns the token if possible
	//If this method returns null, then the token was not of the given type
	private Token accept(Token.Type type) {
		Token result = peek();
		if (result != null && result.is(type)) {
			next();
			return result;
		}
		return null;
	}
	
	public boolean acceptWhitespace() {
		return accept(Token.Type.WHITESPACE) != null;
	}
	
	public boolean acceptNewline() {
		return accept(Token.Type.NEWLINE) != null;
	}
	
	public String acceptComment() {
		Token token = accept(Token.Type.COMMENT);
		if (token != null) {
			return token.asComment();
		}
		return null;
	}
	
	public String acceptSymbol() {
		Token token = accept(Token.Type.SYMBOL);
		if (token != null) {
			return token.asSymbol();
		}
		return null;
	}
	
	public Integer acceptNumber() {
		Token token = accept(Token.Type.NUMBER);
		if (token != null) {
			return token.asNumber();
		}
		return null;
	}
	
	//Parses the next token and returns, or throws an exception if the next token was not
	//a simple token, or did not match the given character.
	public void expect(char c) {
		Token token = next();
		if (!token.is(Token.Type.SIMPLE) || !token.is(c)) {
			throw new AssembleError(token, "Expected " + c + " not " + token);
		}
	}
	
	//Parses the next token and returns it, or throws an exception if the next token was 
	//not of the given type
	private Token expect(Token.Type type) {
		Token result = next();
		if (!result.is(type)) {
			throw new AssembleError(result, "Expected " + type + " not " + result);
		}
		return result;
	}
	
	public void expectWhitespace() {
		expect(Token.Type.WHITESPACE);
	}
	
	public Mnemonic expectMnemonic() {
		return expect(Token.Type.MNEMONIC).asMnemonic();
	}
	
	public int expectNumber() {
		return expect(Token.Type.NUMBER).asNumber();
	}

	public String expectSymbol() {
		return expect(Token.Type.SYMBOL).asSymbol();
	}
	
	public Data expectData() {
		return expect(Token.Type.DATA).asData();
	}
}
