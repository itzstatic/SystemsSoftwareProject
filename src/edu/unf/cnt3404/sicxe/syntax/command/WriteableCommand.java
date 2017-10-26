package edu.unf.cnt3404.sicxe.syntax.command;

import edu.unf.cnt3404.sicxe.syntax.Command;

//Represents a command that writes actual object code
//getSize should not return 0
public interface WriteableCommand extends Command {
	//Writes object code to the buffer starting at pos.
	//This command should only modify the number of bytes according to getSize
	void write(byte[] buffer, int pos);
}
