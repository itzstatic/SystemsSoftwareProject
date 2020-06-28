package edu.unf.cnt3404.sicxe.writer;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.ObjectWriter;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.Symbol;
import edu.unf.cnt3404.sicxe.syntax.command.ModifiableCommand;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;
import edu.unf.cnt3404.sicxe.syntax.command.directive.OrgDirective;
import edu.unf.cnt3404.sicxe.syntax.expression.Term;
import edu.unf.cnt3404.sicxe.writer.beck.DefineRecord;
import edu.unf.cnt3404.sicxe.writer.beck.ModificationRecord;
import edu.unf.cnt3404.sicxe.writer.beck.ReferRecord;
import edu.unf.cnt3404.sicxe.writer.beck.TextRecord;

//Prints an object program to a PrintWriter.
public class BeckObjectWriter implements ObjectWriter {
	
	private Program program;
	private PrintWriter out;
	private List<ModificationRecord> mods = new ArrayList<>();
	
	private TextRecord text; //Current text record
	
	public BeckObjectWriter(PrintWriter out) {
		this.out = out;
	}
	
	@Override
	public void setProgram(Program p) {
		program = p;
	}
	
	@Override
	public void start() {
		//Write the header record
		out.print('H');
		out.printf("%-6s", program.getName());
		out.printf("%06X", program.getStart());
		out.printf("%06X", program.getSize());
		out.println();
		//Write define records
		DefineRecord define = null;
		for (Symbol def : program.getExternalDefinitions()) {
			if (define == null) {
				define = new DefineRecord();
			}
			if (!define.add(def)) {
				define.write(out);
				define = new DefineRecord();
				define.add(def); //This call should always succeed
			}
		}
		//Write the final Define record
		if (define != null) {
			define.write(out);
		}
		
		//Write refer records
		ReferRecord refer = null;
		for (String ref : program.getExternalReferences()) {
			if (refer == null) {
				refer = new ReferRecord();
			}
			if (!refer.add(ref)) {
				refer.write(out);
				refer = new ReferRecord();
				refer.add(ref); //This call should always succeed
			}
		}
		//Write the final record
		if (refer != null) {
			refer.write(out);
		}
	}
	
	//Writes the command to the object program
	@Override
	public void write(Command c) {
		//Resw/Resb and any directive that increments location counter (size > 0)
		//But does not write code (not a WriteableCommand)
		if ((c.getSize() > 0 && !(c instanceof WriteableCommand))
			|| c instanceof OrgDirective) {
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
		
		//Of the mod record
		int start = program.getLocationCounter() + c.getOffset();
		int stride = c.getStride();
		
		//Make mod records for external symbols. Period.
		for (Term term : expr.getExternalSymbols()) {
			mods.add(new ModificationRecord(start, stride, term.getSymbol().getName(), term.isPositive()));
		}
		
		//Number of program relative modification records equal to |netSign|. 
		//The signs of these records are equal to sign(netSign).
		if (c.isAbsolute()) {
			int netSign = expr.getNetSign();
			int abs = Math.abs(netSign); 
			//If >1, then there would require multiple program relative modification records
			//Is this even allowed? 
			for (int i = 0; i < abs; i++) {
				mods.add(new ModificationRecord(start, stride, program.getName(), netSign > 0));
			}
		}
	}
	
	@Override
	public void end() {
		writeCurrentTextRecord();
		
		if (program.isRelocatable()) {
			for (ModificationRecord mod : mods) {
				mod.write(out);
			}
		}
		
		out.print('E');
		if (program.isRunnable()) {
			out.printf("%06X", program.getFirst());
		}
		out.println();
	}
}
