package edu.unf.cnt3404.sicxe.global;

import java.util.HashMap;
import java.util.Map;

//Contains static hashtables used for assembly
public class Global {
	private Global() {}
	
	public static final Map<String, Mnemonic> OPTAB = new HashMap<>();
	public static final Map<String, Byte> REGISTERS = new HashMap<>();
	
	
	//Convenience method so the caller does not have to write out the name twice, 
	//or cast the opcode to byte
	private static void putInstruction(String name, int opcode, Format format) {
		OPTAB.put(name, new Mnemonic(name, (byte)opcode, format));
	}
	
	//Convenience method so the caller does not have to write out the name twice
	private static void putDirective(String name) {
		OPTAB.put(name, new Mnemonic(name, (byte)0, null));
	}
	
	//Convenience method so the caller does not have to write out the name twice
	private static void putRegister(String name, int code) {
		REGISTERS.put(name, (byte)code);
	}
	
	static {
		putInstruction("LDA", 	0x00, Format.FORMAT34M);
		putInstruction("LDX", 	0x04, Format.FORMAT34M);
		putInstruction("LDL", 	0x08, Format.FORMAT34M);
		putInstruction("STA", 	0x0C, Format.FORMAT34M);
		putInstruction("STX", 	0x10, Format.FORMAT34M);
		putInstruction("STL", 	0x14, Format.FORMAT34M);
		putInstruction("ADD", 	0x18, Format.FORMAT34M);
		putInstruction("SUB", 	0x1C, Format.FORMAT34M);
		putInstruction("MUL", 	0x20, Format.FORMAT34M);
		putInstruction("DIV", 	0x24, Format.FORMAT34M);
		putInstruction("COMP", 	0x28, Format.FORMAT34M);
		putInstruction("TIX", 	0x2C, Format.FORMAT34M);
		putInstruction("JEQ", 	0x30, Format.FORMAT34M);
		putInstruction("JGT", 	0x34, Format.FORMAT34M);
		putInstruction("JLT", 	0x38, Format.FORMAT34M);
		putInstruction("J", 	0x3C, Format.FORMAT34M);
		putInstruction("AND", 	0x40, Format.FORMAT34M);
		putInstruction("OR", 	0x44, Format.FORMAT34M);
		putInstruction("JSUB", 	0x48, Format.FORMAT34M);
		putInstruction("RSUB", 	0x4C, Format.FORMAT34);
		putInstruction("LDCH", 	0x50, Format.FORMAT34M);
		putInstruction("STCH", 	0x54, Format.FORMAT34M);
		putInstruction("ADDF", 	0x58, Format.FORMAT34M);
		putInstruction("SUBF", 	0x5C, Format.FORMAT34M);
		putInstruction("MULF", 	0x60, Format.FORMAT34M);
		putInstruction("DIVF", 	0x64, Format.FORMAT34M);
		putInstruction("LDB", 	0x68, Format.FORMAT34M);
		putInstruction("LDS", 	0x6C, Format.FORMAT34M);
		putInstruction("LDF", 	0x70, Format.FORMAT34M);
		putInstruction("LDT", 	0x74, Format.FORMAT34M);
		putInstruction("STB", 	0x78, Format.FORMAT34M);
		putInstruction("STS", 	0x7C, Format.FORMAT34M);
		putInstruction("STF", 	0x80, Format.FORMAT34M);
		putInstruction("STT", 	0x84, Format.FORMAT34M);
		putInstruction("COMPF", 0x88, Format.FORMAT34M);
		//No opcode 8C
		putInstruction("ADDR", 	0x90, Format.FORMAT2RR);
		putInstruction("SUBR", 	0x94, Format.FORMAT2RR);
		putInstruction("MULR", 	0x98, Format.FORMAT2RR);
		putInstruction("DIVR", 	0x9C, Format.FORMAT2RR);
		putInstruction("COMPR", 0xA0, Format.FORMAT2RR);
		putInstruction("SHIFTL",0xA4, Format.FORMAT2RN);
		putInstruction("SHIFTR",0xA8, Format.FORMAT2RN);
		putInstruction("RMO", 	0xAC, Format.FORMAT2RR);
		putInstruction("SVC", 	0xB0, Format.FORMAT2N);
		putInstruction("CLEAR", 0xB4, Format.FORMAT2R);
		putInstruction("TIXR", 	0xB8, Format.FORMAT2R);
		//No opcode BC
		putInstruction("FLOAT", 0xC0, Format.FORMAT1);
		putInstruction("FIX", 	0xC4, Format.FORMAT1);
		putInstruction("NORM", 	0xC8, Format.FORMAT1);
		//No opcode CC
		putInstruction("LPS", 	0xD0, Format.FORMAT34M);
		putInstruction("STI", 	0xD4, Format.FORMAT34M);
		putInstruction("RD", 	0xD8, Format.FORMAT34M);
		putInstruction("WD", 	0xDC, Format.FORMAT34M);
		putInstruction("TD", 	0xE0, Format.FORMAT34M);
		//No opcode E4
		putInstruction("STSW", 	0xE8, Format.FORMAT34M);
		putInstruction("SSK", 	0xEC, Format.FORMAT34M);
		putInstruction("SIO", 	0xF0, Format.FORMAT1);
		putInstruction("HIO", 	0xF4, Format.FORMAT1);
		putInstruction("TIO", 	0xF8, Format.FORMAT1);
		
		//Implemented directives
		putDirective("START");
		putDirective("END");
		putDirective("BASE");
		putDirective("NOBASE");
		putDirective("RESB");
		putDirective("RESW");
		putDirective("BYTE");
		putDirective("WORD");
		putDirective("ORG");
		putDirective("EXTREF");
		putDirective("EXTDEF");
		//Not implemented (Parser will recognize which are and aren't)
		putDirective("LTORG");
		putDirective("EQU");
		putDirective("CSECT");
		putDirective("USE");
		
		putRegister("A", 0);
		putRegister("X", 1);
		putRegister("L", 2);
		putRegister("S", 4);
		putRegister("T", 5);
		putRegister("F", 6);
		//7 is probably the CC register
		putRegister("PC", 8);
		putRegister("SW", 9);
	}
}
