package edu.unf.cnt3404.sicxe.syntax.command.instruction;

import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;

public class Format1Instruction extends AbstractCommand implements WriteableCommand {

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public void write(byte[] buffer, int pos) {
		buffer[pos] = getMnemonic().getOpcode();
	}

	@Override
	public String getArgument() {
		return null;
	}

}
