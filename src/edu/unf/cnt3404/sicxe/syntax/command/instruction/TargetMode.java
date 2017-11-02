package edu.unf.cnt3404.sicxe.syntax.command.instruction;

//Represents the ability of a Format 3 or 4 instruction to be
//Immediate (#), Indirect (@), or Simple.
public enum TargetMode {
	//Our book's SicXe assembler will not generate this ni = 00,
	//even though a SicXe Machine CPU could execute it
	SIMPLE(false, false),
	IMMEDIATE(false, true),
	INDIRECT(true, false),
	SIMPLE_XE(true, true);
	
	private byte ni;
	
	private TargetMode(boolean n, boolean i) {
		if (n) ni |= 0b0000_0010; //OR with the n mask
		if (i) ni |= 0b0000_0001; //OR with the i mask
	}
	
	//Gets the 1-byte ni mask. The result takes the format of 
	//0000 00ni, where n and i are the indirect and immediate flags
	public byte getNiMask() {
		return ni;
	}
}
