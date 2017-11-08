package edu.unf.cnt3404.sicxe;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.object.ModificationRecord;
import edu.unf.cnt3404.sicxe.object.TextRecord;
import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.ModifiableCommand;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.Format34Instruction;
import edu.unf.cnt3404.sicxe.syntax.expression.SignedSymbol;

//Prints an object program to a PrintWriter.
public class ObjectProgramWriter {
	
	private Program program;
	private PrintWriter out;
	private List<ModificationRecord> mods = new ArrayList<>();
	
	private TextRecord text; //Current text record
	
	public ObjectProgramWriter(Program program, PrintWriter out) {
		this.program = program;
		this.out = out;
	}
	
	public void writeHeaderReferAndDefineRecords() {
		out.print('H');
		out.printf("%-6s", program.getName());
		out.printf("%06X", program.getStart());
		out.printf("%06X", program.getSize());
		out.println();
		//TODO: Refer and Define records
	}
	
	//Writes the command to the object program
	public void write(Command c) {
		//Resw/Resb and any directive that increments location counter (size > 0)
		//But does not write code (not a WriteableCommand)
		if (c.getSize() > 0 && !(c instanceof WriteableCommand)) {
			writeCurrentTextRecord();
		}
		
		if (c instanceof WriteableCommand) {
			write((WriteableCommand) c);
		}
		
		if (c instanceof ModifiableCommand) {
			modify((ModifiableCommand) c);
		}
	}
	
	//Adds the command to the current text record, writing the text record if required
	private void write(WriteableCommand c) {
		if (text == null) {
			text = new TextRecord(program.getLocationCounter());
		}
		
		if (!text.add(c)) {
			text.write(out);
			text = new TextRecord(program.getLocationCounter());
			text.add(c); //Will always succeed
		}
	}
	
	//Forces the object program to write the current text record
	private void writeCurrentTextRecord() {
		if (text != null) {
			text.write(out);
			text = null;
		}
	}
	
	//Adds modification records associated with the expression in the
	//given command; those records will be appended at the end
	private void modify(ModifiableCommand c) {
		Expression expr = c.getExpression();
		//Only modify relative expressions
		if (expr.isAbsolute()) {
			return; 
		}
		//Relative addressing (probably unextended) Format34 instructions require no modification
		if (c instanceof Format34Instruction && !((Format34Instruction) c).isAbsoluteAddressing()) {
			return;
		}
		
		//Of the mod record
		int start = program.getLocationCounter() + c.getOffset();
		int stride = c.getStride();
		
		for (SignedSymbol s : expr.getExternalSymbols()) {
			mods.add(new ModificationRecord(start, stride, s.getSymbol().getText(), s.isPositive()));
		}
		//Program relative modification record iff |netSign| == 1
		int netSign = expr.getNetSign();
		int abs = Math.abs(netSign);
		if (abs == 1) {
			mods.add(new ModificationRecord(start, stride, program.getName(), netSign > 0));
		} else if (abs > 1) {
			//If >1, then there would require multiple program relative modification records
			//Is this even allowed? 
			throw new AssembleError(c, "Too many unpaired relative symbols (" + abs + ")");
		}
		//If 0, then no record
	}
	
	public void writeModificationAndEndRecords() {
		writeCurrentTextRecord();
		
		for (ModificationRecord mod : mods) {
			mod.write(out);
		}
		
		out.print('E');
		out.printf("%06X", program.getFirst());
		out.println();
	}
}
