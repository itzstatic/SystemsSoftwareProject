package edu.unf.cnt3404.sicxe.syntax.command;

import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;

//Represents a command that has an expression as its operand
//like Format3/4 instructions, and WORD/EQU directives, etc.
public interface ExpressionCommand extends Command {
	Expression getExpression();
}
