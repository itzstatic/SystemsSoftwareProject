package edu.unf.cnt3404.sicxe.object;

import java.io.PrintWriter;

public class ReferRecord {
	
	//12 is the maximum number of references per refer record
	private String[] symbols = new String[12];
	private int size;
	
	//Attempts to add the symbol to the refer record, otherwise,
	//returns false if the add failed
	public boolean add(String symbol) {
		if (size < 12) {
			symbols[size++] = symbol;
			return true;
		}
		return false;
	}
	
	public void write(PrintWriter out) {
		out.print('R');
		for (int i = 0; i < size; i++) {
			out.printf("%-6s", symbols[i]);
		}
		out.println();
	}
}
