package edu.unf.cnt3404.sicxe.parse;

import java.io.BufferedReader;
import java.io.IOException;

//Yields a character stream, and keeps track of location
//in the source code. Also, \r\n, microsoft's line terminator, 
//will be yielded as a single character \n.
public class Scanner implements Locatable {
	
	public static final char EOS = (char)-1;
	
	private final int tabWidth;
	
	private BufferedReader reader;
	private int row = 1;
	private int col = 1;
	
	public Scanner(BufferedReader reader, int tabWidth) {
		this.reader = reader;
		this.tabWidth = tabWidth;
	}
	
	@Override
	public int getRow() {
		return row;
	}
	@Override
	public int getCol() {
		return col;
	}
	
	//Consumes the next character, but see class comment for 
	//special behavior for Microsoft line separator \r\n
	public char next() {
		try {
			char c = (char)reader.read();
			if (c == '\t') {
				col += tabWidth;
			} else {
				col++;
			}
			if (c == '\r' && peek() == '\n') {
				//Microsoft line terminator:
				reader.read(); //Consume the \n
				c = '\n';
			}
			if (c == '\n' || c == '\r') {
				row++;
				col = 1;
			}
			return c;
		} catch (IOException e) {
			e.printStackTrace();
			return EOS;
		}
	}
	
	//Gets, but does not consume, the next character. See special
	//behavior for \r\n
	public char peek() {
		try {
			reader.mark(1);
			//Cache location, because next() could unpredictably change it
			int oldRow = row;
			int oldCol = col;
			char c = next();
			//Undo all side effects of next()
			reader.reset();
			row = oldRow;
			col = oldCol;
			return c;
		} catch (IOException e) {
			e.printStackTrace();
			return EOS;
		}
	}
}
