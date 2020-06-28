package edu.unf.cnt3404.sicxe;

import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;

public interface ObjectWriter {
	void setProgram(Program p);
	void start();
	void write(Command c);
	void end();
}
