package edu.unf.cnt3404.sicxe.global;

//Represents an instruction or directive mnemonic containing an abbreviation (name),
//a 1-byte opcode, and a format
//Instructions contain name and opcode and Format as expected.
//Directives contain a null format and meaningless opcode. 
public class Mnemonic {
	private String name;
	private byte opcode;
	private Format format;
	
	public Mnemonic(String name, byte opcode, Format format) {
		this.name = name;
		this.opcode = opcode;
		this.format = format;
	}
	
	public String getName() {
		return name;
	}
	
	public byte getOpcode() {
		return opcode;
	}
	
	public Format getFormat() {
		return format;
	}
}
