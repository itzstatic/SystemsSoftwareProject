package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.parse.Lexer;
import edu.unf.cnt3404.sicxe.parse.Parser;
import edu.unf.cnt3404.sicxe.parse.Scanner;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.ExpressionCommand;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.IfDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.MacroDefinitionDirective;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionMacroParameter;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNode;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionOperator;

public class MacroExpander {
	
	private final List<Command> commands;
	private final List<Expression> arguments;
	private final Program program;
	
	public MacroExpander(List<Command> commands, List<Expression> arguments, Program program) {
		this.commands = commands;
		this.arguments = arguments;
		this.program = program;
	}
	
	public void expand(Command c) throws AssembleError {
		if (c instanceof MacroDefinitionDirective) {
			for (Command d : ((MacroDefinitionDirective)c).getCommands()) {
				expand(d);
			}
		} else if (c instanceof IfDirective) {
			IfDirective ifD = ((IfDirective)c);
			Expression condition = ifD.getExpression();
			applyPositionalArguments(condition.getRoot());
			condition.evaluate(c, program);
			if (condition.getValue() == 1) {
				for (Command d : ifD.getCommands()) {
					expand(d);
				}
			} else if (ifD.getElse() != null) {
				for (Command d : ifD.getElse().getCommands()) {
					expand(d);
				}
			}
		} else {
			if (c instanceof ExpressionCommand) {
				applyPositionalArguments(((ExpressionCommand)c).getExpression().getRoot());
			}
			commands.add(deepCopy(c));
		}
	}
	
	private Command deepCopy(Command c) {
		String serialized = c.getMnemonic().getName() + " " + c.getArgument();
		Parser deserializer = new Parser(new Lexer(new Scanner(
			new BufferedReader(new StringReader(serialized)), 1)));
		try {
			return deserializer.next();
		} catch (AssembleError e) {
			throw new IllegalStateException(e);
		}
	}
		
	private void applyPositionalArguments(ExpressionNode e) {
		if (e instanceof ExpressionOperator) {
			ExpressionOperator binary = (ExpressionOperator) e;
			applyPositionalArguments(binary.left);
			applyPositionalArguments(binary.right);
		} else if (e instanceof ExpressionMacroParameter) {
			ExpressionMacroParameter mp = (ExpressionMacroParameter) e;
			mp.expression = arguments.get(mp.parameterPosition);
		}	
	}

}
