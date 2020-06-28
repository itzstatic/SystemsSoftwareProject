package edu.unf.cnt3404.sicxe.writer.osprey;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;

public class Module {
	public List<WriteableCommand> commands = new ArrayList<>();
	public int first;
	
	private int start;
	
	public Module(int start) {
		this.start = start;
	}
	
	public void write(PrintWriter out) {
		out.printf("%06X\n", start);
		out.printf("%06X\n", first);
		
		for (WriteableCommand c : commands) {
			byte[] buffer = new byte[c.getSize()];
			c.write(buffer, 0);
			for (int i = 0; i < c.getSize(); i++) {
				out.printf("%02X", buffer[i]);
			}
			out.printf("\n");
		}
		
		out.printf("!\n");
	}
}
