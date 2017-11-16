package edu.unf.cnt3404.sicxe.syntax;

import edu.unf.cnt3404.sicxe.global.Mnemonic;


//Represents a line of useful code in a SicXe program.
//A command is created when the source is parsed, and the location counter
//must be incremented. A command might have a corresponding label definition
//or comment.
public interface Command {
	//Returns the number of bytes that this command would take up 
	//in the object program. 
	//Alternatively, the amount by which to increment location counter
	int getSize();
	
	void setLabel(String label);
	//Gets the label for this command, or null if there is no label
	String getLabel();
	
	void setComment(String comment);
	//Gets the comment for this command, or null if there is no comment
	String getComment();
	
	void setLine(int line);
	//Gets the line number for this command, or a negative number if this 
	//command was not in the source
	int getLine();
	
	void setMnemonic(Mnemonic mnemonic);
	//Gets the mnemonic, or null if there is no mnemonic
	Mnemonic getMnemonic();
	
	//Listing file methods
	//Returns a string containing a mnemonic name
	default String getName() {
		Mnemonic mnemonic = getMnemonic();
		if (mnemonic == null) {
			return null;
		}
		return " " + mnemonic.getName();
	}
	//Returns a string containing arguments (to the right of name)
	//Or null if there are no arguments
	String getArgument();
}
