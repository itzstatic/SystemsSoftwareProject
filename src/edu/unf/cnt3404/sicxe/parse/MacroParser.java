package edu.unf.cnt3404.sicxe.parse;

import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.command.directive.EndDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.ElseDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.EndIfDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.IfDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.MacroDefinitionDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.MendDirective;

public class MacroParser implements Locatable {

	private final Parser parser;
	
	public MacroParser(Parser parser) {
		this.parser = parser;
	}
	
	public Command next() throws AssembleError {
		Command c = nextRecursive();
		if (c instanceof MendDirective) {
			throw new AssembleError(c, "Unexpected MEND directive");
		}
		if (c instanceof ElseDirective) {
			throw new AssembleError(c, "Unexpected ELSE directive");
		}
		if (c instanceof EndIfDirective) {
			throw new AssembleError(c, "Unexpected ENDIF directive");
		}
		return c;
	}
	
	// Returns commands but bunches up macros and macro structures
	private Command nextRecursive() throws AssembleError {
		Command c = parser.next();
		if (c == null) {
			return null;
		}
		
		if (c instanceof MacroDefinitionDirective) {
			MacroDefinitionDirective macro = (MacroDefinitionDirective)c;
			while ((c = nextRecursive()) != null && !(c instanceof MendDirective)) {
				macro.addCommand(c);
			}
			if (c == null) {
				throw new AssembleError(parser, "Expected MEND Directive");
			}
			c = macro;
		} else if (c instanceof IfDirective) {
			IfDirective ifd = (IfDirective)c;
			while ((c = nextRecursive()) != null && !(c instanceof EndIfDirective) && !(c instanceof ElseDirective)) {
				ifd.addCommand(c);
			}
			if (c == null) {
				throw new AssembleError(parser, "Expected ELSE or ENDIF directive");
			}
			// We can do nothing if it is an EndIfDirective
			if (c instanceof ElseDirective) {
				ElseDirective elsed = (ElseDirective)c;
				while ((c = nextRecursive()) != null && !(c instanceof EndIfDirective)) {
					elsed.addCommand(c);
				}
				if (c == null) {
					throw new AssembleError(parser, "Expected ENDIF directive");
				}
				ifd.setElse(elsed);
			}
			c = ifd;
		} 
		
		return c;
	}

	@Override
	public int getRow() {
		return parser.getRow();
	}

	@Override
	public int getCol() {
		return parser.getRow();
	}
}
