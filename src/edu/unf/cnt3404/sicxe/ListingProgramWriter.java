package edu.unf.cnt3404.sicxe;

import java.io.PrintWriter;
import java.util.Map;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.Comment;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.MacroExpansionDirective;

//Prints a listing file to a PrintWriter
public class ListingProgramWriter {
	
	private Program program;
	private Alignment align;
	private PrintWriter out;
	private Map<Integer, AssembleError> errors;
	
	public ListingProgramWriter(Program program, Alignment align, 
			Map<Integer, AssembleError> errors, PrintWriter out) {
		this.program = program;
		this.align = align;
		this.errors = errors;
		this.out = out;
	}
	
	//Appends the given command to the listing
	public void write(Command c) {
		int line = c.getLine();
		writeColumn(align.getMaxLineLength(), line);
		if (c instanceof Comment) {
			out.printf("%8s", ""); //Location counter place holder
			writeColumn(align.getMaxNameLength(), "."); //Place a dot in the name column
			out.print(c.getComment());
		} else {
			out.printf("%04X    ", program.getLocationCounter()); //4 spaces 
			if (c instanceof MacroExpansionDirective)
			{
				String label = c.getLabel();
				if (label == null) {
					label = "";
				}
				writeColumn(align.getMaxLabelLength(), "." + label);
			} else {
				writeColumn(align.getMaxLabelLength(), c.getLabel());
			}
			writeColumn(align.getMaxNameLength(), c.getName());
			writeColumn(align.getMaxArgumentLength(), c.getArgument());
			
			//If there's an error, then don't output code
			AssembleError e = errors.get(line);
			if (e == null && c instanceof WriteableCommand) {
				byte[] buffer = new byte[c.getSize()];
				((WriteableCommand) c).write(buffer, 0);
				for (byte b : buffer) {
					out.printf("%02X", b);
				}
				out.print(" ");
			}
			if (c.getComment() != null) {
				out.print("." + c.getComment());	
			}
			//Then write the error
			if (e != null) {
				out.println();
				out.printf("[ERROR] (Row %d, Col %d) %s", e.getRow(), e.getCol(), e.getMessage());
			}
		}
		out.println();
		for (AssembleError e : errors.values()) {
			System.err.printf("R: %d, C: %d; %s\n", e.getRow(), e.getCol(), e.getMessage());
		}
	}
	
	private void writeColumn(int width, int i) {
		String format = "%" + width + "d    "; //4 spaces
		out.printf(format, i);
	}
	
	private void writeColumn(int width, String s) {
		String format = "%-" + width + "s "; //1 space
		if (s == null) s = ""; //Don't print null
		out.printf(format, s);
	}
}
