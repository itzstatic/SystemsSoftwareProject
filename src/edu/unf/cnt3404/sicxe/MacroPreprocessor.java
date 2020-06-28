package edu.unf.cnt3404.sicxe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.ExpressionCommand;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.MacroDefinitionDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.MacroExpansionDirective;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionMacroParameter;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNode;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionOperator;

public class MacroPreprocessor {

	private Map<String, MacroDefinitionDirective> macros = new HashMap<>();
	
	private final Program program;
	
	public MacroPreprocessor(Program program) {
		this.program = program;
	}
	
	public void define(MacroDefinitionDirective macro) throws AssembleError {
		if (macro.getLabel() == null || macro.getLabel().isEmpty()) {
			throw new AssembleError(macro, "Unlabeled macro");
		}
		
		if (new HashSet<>(macro.getParameters()).size() != macro.getParameters().size()) {
			throw new AssembleError(macro, "Duplicate macro symbol");
		}
		
		usePositionalNotation(macro, macro.getParameters());
		
		macros.put(macro.getLabel(), macro);
	}
	
	private void usePositionalNotation(Command c, List<String> parameters) throws AssembleError {
		if (c instanceof MacroDefinitionDirective) {
			for (Command d : ((MacroDefinitionDirective)c).getCommands()) {
				usePositionalNotation(d, parameters);
			}
		} else if (c instanceof ExpressionCommand) {
			Expression e = ((ExpressionCommand)c).getExpression();
			usePositionalNotation(c, e.getRoot(), parameters);
		}
	}
	
	private void usePositionalNotation(Command c, ExpressionNode e, List<String> parameters)
	 throws AssembleError {
		if (e instanceof ExpressionOperator) {
			ExpressionOperator binary = (ExpressionOperator) e;
			usePositionalNotation(c, binary.left, parameters);
			usePositionalNotation(c, binary.right, parameters);
		} else if (e instanceof ExpressionMacroParameter) {
			((ExpressionMacroParameter)e).usePositionalNotation(c, parameters);
		}
	}
	
	public List<Command> expand(MacroExpansionDirective expansion) throws AssembleError {
		String macroName = expansion.getMnemonic().getName();
		MacroDefinitionDirective macro = macros.get(macroName);
		if (macro == null) {
			throw new AssembleError(expansion, "Undeclared macro " + macroName);
		}
		
		int expectedParams = macro.getParameters().size();
		int actualArgs = expansion.getArguments().size();
		if (expectedParams != actualArgs) {
			throw new AssembleError(expansion, String.format(
				"Expected %d macro parameters encountered %d", expectedParams, actualArgs));
		}
		
		List<Command> result = new ArrayList<>();
		new MacroExpander(result, expansion.getArguments(), program).expand(macro);
	
		result.get(0).setLabel(expansion.getLabel());
		
		return result;
	}
}
