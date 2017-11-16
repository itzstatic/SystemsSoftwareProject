package edu.unf.cnt3404.sicxe;

import edu.unf.cnt3404.sicxe.global.Global;
import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.directive.BaseDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.EndDirective;
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
		
		switch(c.getMnemonic().getFormat()) {
		case FORMAT2N: c.setArgument(n); break;
		case FORMAT2R: c.setArgument(b1); break;
		case FORMAT2RN: c.setArgument(b1, n); break;
		case FORMAT2RR: c.setArgument(b1, b2); break;
		default: throw new IllegalStateException(c.getMnemonic().getFormat().toString());
		}
		
	}
	
	private void assemble(Format34Instruction c) {
		Expression expr = c.getExpression();
		//Format 3 instructions cannot have external symbols
		if (!c.isExtended() && !expr.getExternalSymbols().isEmpty()) {
			throw new AssembleError(c, "External symbols and not extended");
		}
		//Format 3 instructions cannot have more than 1 unpaired relative
		if (!c.isExtended() && Math.abs(expr.getNetSign()) > 1) {
			throw new AssembleError(c, "More than 1 unpaired local relative term and not extended");
		}
		//Extended can have anything
		if (c.isExtended()) {
			c.setAddressMode(AddressMode.ABSOLUTE);
			c.setArgument(expr.getValue());
		//Absolute expression, not extended
		} else if (expr.isAbsolute()){
			//Check to see if it fits in 12 bits
			int argument = expr.getValue();
			if (0 <= argument && argument < 4096) {
				c.setAddressMode(AddressMode.ABSOLUTE);
				c.setArgument(argument);
				return;
			}
			//Generating ni=00 with 15 bits might be useful
			//But, the architect decided not to do that.
			
			throw new AssembleError(c, "Absolute expression bigger than 4095 and not extended");
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
		Expression expr = c.getExpression();
		if (expr == null) {
			program.disableFirst();
		} else {
			program.setFirst(expr.getValue());
		}
	}

	private void assemble(BaseDirective c) {
		program.setBase(c.getExpression().getValue());
	}

	private void assemble(NoBaseDirective c) {
		program.disableBase();
	}
	
}
