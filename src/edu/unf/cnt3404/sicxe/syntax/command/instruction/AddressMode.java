package edu.unf.cnt3404.sicxe.syntax.command.instruction;

//Represents the ability of a format 3 or 4 instruction to be 
//PC-relative, Base-relative, or absolute.
public enum AddressMode {	
	ABSOLUTE(false, false),
	PC(false, true),
	BASE(true, false);
	
	private byte bp;
	
	private AddressMode(boolean b, boolean p) {
		if (b) bp |= 0b0100_0000; //OR with b mask
		if (p) bp |= 0b0010_0000; //OR with p mask
	}
	
	//Gets a 1-byte xbpe flag, by combining this addressing mode with
	//the specified x and e flags. Returns the byte according to
	//the format of xbpe 0000
	public byte getXbpeFlag(boolean x, boolean e) {
		byte result = bp;
		if (x) result |= 0b1000_0000; //OR with x mask
		if (e) result |= 0b0001_0000; //Or with e mask
		return result;
	}
}
