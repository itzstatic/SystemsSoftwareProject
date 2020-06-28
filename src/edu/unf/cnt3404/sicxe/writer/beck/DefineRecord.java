package edu.unf.cnt3404.sicxe.writer.beck;

import java.io.PrintWriter;

import edu.unf.cnt3404.sicxe.syntax.Symbol;

public class DefineRecord {
	//6 is the maximum number of definition-address pair per define record
	private Symbol[] symbols = new Symbol[6];
	private int size;
	
	//Attempts to add the symbol to the define record, otherwise,
	//returns false if the add failed
	public boolean add(Symbol symbol) {
		if (size < 6) {
			symbols[size++] = symbol;
			return true;
		}
		return false;
	}
		
	public void write(PrintWriter out) {
		out.print('D');
		for (int i = 0; i < size; i++) {
			if (symbols[i] == null) {
				System.out.println(i + " out of " + size);
				continue;
			}
			out.printf("%-6s", symbols[i].getName());
			out.printf("%06X", symbols[i].getValue());
		}
		out.println();
	}	
}
