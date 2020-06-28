package edu.unf.cnt3404.sicxe;

import edu.unf.cnt3404.sicxe.syntax.Command;

//Helps with the alignment of a listing file by storing maximum
//lengths of certain interior columns (label, mnemonic, argument).
//Just update the alignment with every command in the program
public class Alignment {
	private int maxLineLength;
	private int maxLabelLength;
	private int maxNameLength;
	private int maxArgumentLength;
	
	//Updates the alignment information in light of the given command
	public void update(Command c) {
		maxLineLength = Math.max(maxLineLength, Integer.toString(c.getLine()).length());
		
		String label = c.getLabel();
		if (label != null) {
			maxLabelLength = Math.max(maxLabelLength, label.length());
		}
		String name = c.getName();
		if (name != null) {
			maxNameLength = Math.max(maxNameLength, name.length());
		}
		String arg = c.getArgument();
		if (arg != null) {
			maxArgumentLength = Math.max(maxArgumentLength, arg.length());
		}
	}
	
	public int getMaxLineLength() {
		return maxLineLength;
	}
	public int getMaxLabelLength() {
		return maxLabelLength;
	}
	public int getMaxNameLength() {
		return maxNameLength;
	}
	public int getMaxArgumentLength() {
		return maxArgumentLength;
	}
}
