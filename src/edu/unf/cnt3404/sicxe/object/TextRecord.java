package edu.unf.cnt3404.sicxe.object;

import java.io.PrintWriter;

import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;

public class TextRecord {
	
	private int start;
	private int size;
	private byte[] buffer = new byte[30]; //For instructions

	//Creates a text record starting at the given address
	public TextRecord(int start) {
		this.start = start;
	}
	
	//Attempts to add the command to this record, and returns whether 
	//the add was successful
	public boolean add(WriteableCommand c) {
		if (size + c.getSize() > 30) {
			return false; //Too large of a command
		}
		c.write(buffer, size);
		size += c.getSize();
		return true;
	}
	
	public boolean isEmpty() {
		return size == 0;
	}
	
	public void write(PrintWriter out) {
		out.print('T');
		out.printf("%06X", start);
		out.printf("%02X", size);
		for (int i = 0; i < size; i++) {
			out.printf("%02X", buffer[i]);
		}
		out.println();
	}
}
