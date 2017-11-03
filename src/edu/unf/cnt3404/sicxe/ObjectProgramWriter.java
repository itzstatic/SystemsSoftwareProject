package edu.unf.cnt3404.sicxe;

import java.io.PrintWriter;

import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.ExpressionCommand;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;

//Prints an object program to a PrintWriter.
public class ObjectProgramWriter {
	
	private Program program;
	private PrintWriter out;
	
	public ObjectProgramWriter(Program program, PrintWriter out) {
		this.program = program;
		this.out = out;
	}
	
	public void writeHeaderReferAndDefineRecords() {
		out.print('H');
		out.printf("%6s", program.getName());
		out.printf("%6X", program.getStart());
		//TODO: implement getSize()
		// out.printf("%6X", program.getSize());
		out.println();
		//TODO: Refer and Define records
	}
	
	//Adds the command to the current text record
	public void write(WriteableCommand c) {
		
	}
	
	//Adds modification records associated with the expression in the
	//given command; those records will be appended at the end
	public void modify(ExpressionCommand c) {
		
	}
	
	public void writeModificationAndEndRecords() {
		out.print('E');
		out.printf("%6X", program.getFirst());
		out.println();
	}
}
