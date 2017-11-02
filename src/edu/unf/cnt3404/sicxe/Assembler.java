package edu.unf.cnt3404.sicxe;

import edu.unf.cnt3404.sicxe.parse.ParseError;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.directive.BaseDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.EndDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.NoBaseDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.OrgDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.StartDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.WordDirective;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.AddressMode;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.Format34Instruction;

//Performs pass two by assembling various commands.
public class Assembler {
	private Program program;
	
	public Assembler(Program program) {
		this.program = program;
	}
	
	public void assemble(Format34Instruction c) {
		if (c.isExtended()) {
			c.setAddressMode(AddressMode.ABSOLUTE);
			c.setArgument(c.getExpression().getValue(program));
		} else {
			int argument; //Either PC Disp or Base Disp
			int target = c.getExpression().getValue(program);
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
				throw new ParseError(c, "PC out of range and base disabled");
			}
			
			//Try: Base relative
			//(B) + argument = target
			argument = target - program.getBase();
			if (0 <= argument && argument < 4096) {
				c.setAddressMode(AddressMode.BASE);
				c.setArgument(argument);
				return;
			}
			
			throw new ParseError(c, "Base out of range and not extended");
		}
	}

	public void assemble(WordDirective c) {
		c.setWord(c.getExpression().getValue(program));
	}

	public void assemble(StartDirective c) {
		program.setStart(c.getStart());
	}
	
	public void assemble(EndDirective c) {
		program.setFirst(c.getExpression().getValue(program));
	}

	public void assemble(BaseDirective c) {
		program.setBase(c.getExpression().getValue(program));
	}

	public void assemble(NoBaseDirective c) {
		program.disableBase();
	}

	public void assemble(OrgDirective c) {
		program.setLocationCounter(c.getExpression().getValue(program));
	}
	
	
}
