package edu.unf.cnt3404.sicxe.syntax.command.instruction;

import edu.unf.cnt3404.sicxe.global.Mnemonic;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;

public class Format1Instruction extends Instruction implements WriteableCommand {

	public Format1Instruction(Mnemonic mnemonic) {
		super(mnemonic);
	}
	
	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public void write(byte[] buffer, int pos) {
		buffer[pos] = mnemonic.getOpcode();
	}

	@Override
	public String getArgument() {
		return null;
	}

}
