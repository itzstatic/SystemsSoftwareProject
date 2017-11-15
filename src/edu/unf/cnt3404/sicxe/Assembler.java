package edu.unf.cnt3404.sicxe;

import edu.unf.cnt3404.sicxe.global.Global;
import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.directive.BaseDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.EndDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.ExtdefDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.NoBaseDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.WordDirective;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.AddressMode;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.Format2Instruction;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.Format34Instruction;

//Performs pass two by assembling various commands.
public class Assembler {
	private Program program;
	
	public Assembler(Program program) {
		this.program = program;
	}
	
	public void assemble(Command c) {
		//There's got to be a better way...
		if (c instanceof EndDirective) assemble((EndDirective)c);
		if (c instanceof BaseDirective) assemble((BaseDirective)c);
		if (c instanceof NoBaseDirective) assemble((NoBaseDirective)c);
		if (c instanceof WordDirective) assemble((WordDirective)c);
		
		if (c instanceof Format2Instruction) assemble((Format2Instruction)c);
		if (c instanceof Format34Instruction) assemble((Format34Instruction)c);
	}
	
	private void assemble(Format2Instruction c) {
		String r1 = c.getRegisterOne();
		String r2 = c.getRegisterTwo();
		byte n = c.getNumber();
		
		Byte b1 = null;
		if (r1 != null) {
			b1 = Global.REGISTERS.get(r1);
		}
		Byte b2 = null;
		if (r2 != null) {
			b2 = Global.REGISTERS.get(r2);
		}
		
		if (r1 == null && r2 == null) {
			c.setArgument(n); 								//Format2N
		} else if (r1 == null && r2 != null) {
			c.setArgument(b2.byteValue(), (byte)(n - 1)); 	//Format2RN
		} else if (r1 != null && r2 == null) {
			c.setArgument(b1.byteValue()); 					//Format2R
		} else { //r1 != null && r2 != null
			c.setArgument(b1.byteValue(), b2.byteValue()); 	//Format2RR
		}
	}
	
	private void assemble(Format34Instruction c) {
		Expression expr = c.getExpression();
		//Possibly, if the expression value occupies more than 12bits, 
		//then one might decide to use 15 bit SIC target
		//Format 4 instructions are big enough for virtually everything, though 
		if (expr.isAbsolute() || c.isExtended()) {
			c.setAddressMode(AddressMode.ABSOLUTE);
			c.setArgument(expr.getValue());
			//Modification records will be generated for extended relative 
		//High-net-sign expression, not extended
		} else if (Math.abs(expr.getNetSign()) > 1) {
			throw new AssembleError(c, "More than 1 unpaired relative term and not extended");
		//Relative expression, not extended
		} else {
			int argument; //Either PC Disp or Base Disp
			int target = expr.getValue();
			//Try: PC relative
			//(PC) + argument = target
			argument = target - 3 - program.getLocationCounter();
			if (-2048 <= argument && argument < 2048) {
				c.setAddressMode(AddressMode.PC);
				c.setArgument(argument);
				return;
			}
			
			//Ensure base is enabled
			if (!program.isBaseEnabled()) {
				throw new AssembleError(c, "PC out of range and base disabled");
			}
			
			//Try: Base relative
			//(B) + argument = target
			argument = target - program.getBase();
			if (0 <= argument && argument < 4096) {
				c.setAddressMode(AddressMode.BASE);
				c.setArgument(argument);
				return;
			}		
			throw new AssembleError(c, "Base out of range and not extended");
		}
	}

	private void assemble(WordDirective c) {
		c.setWord(c.getExpression().getValue());
	}

	private void assemble(EndDirective c) {
		program.setFirst(c.getExpression().getValue());
	}

	private void assemble(BaseDirective c) {
		program.setBase(c.getExpression().getValue());
	}

	private void assemble(NoBaseDirective c) {
		program.disableBase();
	}
	
}
