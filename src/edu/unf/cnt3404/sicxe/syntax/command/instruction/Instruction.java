package edu.unf.cnt3404.sicxe.syntax.command.instruction;

import edu.unf.cnt3404.sicxe.global.Mnemonic;
import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;

public abstract class Instruction extends AbstractCommand implements WriteableCommand {

	protected Mnemonic mnemonic;
	
	@Override
	public String getName() {
		return mnemonic.getName();
	}

}
