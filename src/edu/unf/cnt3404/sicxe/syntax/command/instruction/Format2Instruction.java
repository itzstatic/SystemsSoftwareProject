package edu.unf.cnt3404.sicxe.syntax.command.instruction;

import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;

public class Format2Instruction extends AbstractCommand implements WriteableCommand {
	//The different kinds of Format2 instructions use different fields:
	//Format2R uses only r1 (but not r2)
	//Format2N uses only n; (neither r1 nor r2)
	//Format2RR uses r1 and r2; (both r1 and r2)
	//Format2RN uses r1 and n; (but not r2)
	private String r1;
	private String r2;
	private byte n;
	
	//to be assembled
	private byte argument;
	
	//Constructs a Format2R instruction, where r is a register
	public Format2Instruction(String r) {
		r1 = r;
	}
	
	//Constructs a Format2N instruction, where n is 0 to 15, inclusive
	public Format2Instruction(byte n) {
		this.n = n;
	}
	
	//Constructs a Format2RR instruction, where r1 and r2 are each registers
	public Format2Instruction(String r1, String r2) {
		this.r1 = r1;
		this.r2 = r2;
	}
	
	//Constructs a Format2RN instruction, where n is 1 to 16, inclusive
	public Format2Instruction(String r, byte n) {
		r1 = r;
		this.n = n;
	}
	
	//Sets the 4 higher order bits of the byte argument to the 4 lower bits of higher
	//Sets the 4 lower order bits of the argument to 0
	public void setArgument(byte higher) {
		argument = (byte)((higher & 0xF) << 4); 
	}
	
	//Sets the 4 higher order bits of the argument to the lower four bits of higher
	//Sets the 4 lower order bits of the argument to the lower four bits of lower
	public void setArgument(byte higher, byte lower) {
		argument = (byte)(((higher & 0xF) << 4) | (lower & 0xF));
	}
	
	//Returns the register if this instruction is a Format2R or Format2RN instruction
	//Returns the first register if it is a Format2RR instruction,
	//or null if this is a Format2N instruction
	public String getRegisterOne() {
		return r1;
	}
	//Returns the second register if this instruction is a Format2RR instruction,
	//or null otherwise
	public String getRegisterTwo() {
		return r2;
	}
	//Returns the number if this instruction is a Format2N or Format2RN instruction
	public byte getNumber() {
		return n;
	}
	
	@Override
	public int getSize() {
		return 2;
	}

	@Override
	public void write(byte[] buffer, int pos) {
		buffer[pos] 	= getMnemonic().getOpcode();
		buffer[pos + 1] = argument;
	}

	@Override
	public String getArgument() {
		switch(getMnemonic().getFormat()) {
		case FORMAT2N: return " " + Integer.toString(n);
		case FORMAT2R: return " " + r1;
		case FORMAT2RN: return " " + r2 + "," + n;
		case FORMAT2RR: return " " + r1 + "," + r2;
		default: throw new IllegalStateException(getMnemonic().getFormat().toString());
		}
	}

}
