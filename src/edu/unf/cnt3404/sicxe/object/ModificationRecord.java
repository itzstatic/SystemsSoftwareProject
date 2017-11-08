package edu.unf.cnt3404.sicxe.object;

import java.io.PrintWriter;

public class ModificationRecord {
	private int start;
	private int stride;
	private char sign;
	private String symbol;
	
	//Creates a mod record for an external symbol
	public ModificationRecord(int start, int stride, String symbol, boolean isPositive) {
		this.start = start;
		this.stride = stride;
		this.sign = isPositive ? '+' : '-';
		this.symbol = symbol;
	}
	
	public void write(PrintWriter out) {
		out.print('M');
		out.printf("%06X", start);
		out.printf("%02X", stride);
		out.print(sign);
		out.printf("%-6s", symbol);
		out.println();
	}
}
