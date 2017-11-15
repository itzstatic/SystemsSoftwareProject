package edu.unf.cnt3404.sicxe.syntax.command.directive;

import java.util.List;

import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;

public class ExtdefDirective extends AbstractCommand {

	private List<String> symbols;
	
	public ExtdefDirective(List<String> symbols) {
		this.symbols = symbols;
	}
	
	public List<String> getSymbols() {
		return symbols;
	}
	
	@Override
	public int getSize() {
		return 0;
	}
	
	@Override
	public String getName() {
		return " EXTDEF";
	}

	@Override
	public String getArgument() {
		StringBuilder result = new StringBuilder(" ");
		for (int i = 0; i < symbols.size(); i++) {
			result.append(symbols.get(i));
			if (i < symbols.size() - 1) {
				result.append(",");
			}
		}
		return result.toString();
	}
}
