package edu.unf.cnt3404.sicxe.writer;

import java.io.PrintWriter;

import edu.unf.cnt3404.sicxe.ObjectWriter;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;
import edu.unf.cnt3404.sicxe.syntax.command.directive.OrgDirective;
import edu.unf.cnt3404.sicxe.writer.osprey.Module;

public class OspreyObjectWriter implements ObjectWriter {
	
	private PrintWriter out;
	private Program program;
	private Module current;
	
	public OspreyObjectWriter(PrintWriter out) {
		this.out = out;
	}
	
	@Override
	public void setProgram(Program p) {
		program = p;
	}
	
	@Override
	public void start() {
		
	}
	
	@Override
	public void write(Command c) {
		if (c instanceof WriteableCommand) {
			if (current == null) {
				current = new Module(program.getLocationCounter());
			}
			current.commands.add((WriteableCommand)c);
		} else if (c.getSize() > 0 || c instanceof OrgDirective){
			if (current != null) {
				current.write(out);
				current = null;
			}
		}
	}
	
	@Override
	public void end() {
		if (current == null) {
			current = new Module(program.getLocationCounter());
		}
		current.first = program.getFirst();
		current.write(out);
		current = null;
	}
}
