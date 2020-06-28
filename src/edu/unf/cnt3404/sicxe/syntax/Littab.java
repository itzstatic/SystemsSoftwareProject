package edu.unf.cnt3404.sicxe.syntax;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import edu.unf.cnt3404.sicxe.global.Global;
import edu.unf.cnt3404.sicxe.global.Mnemonic;
import edu.unf.cnt3404.sicxe.syntax.command.directive.ByteDirective;

public class Littab {
	
	final private static Mnemonic BYTE = Global.OPTAB.get("BYTE");
	
	private Map<Data, Integer> allocated = new HashMap<>();
	private Queue<Data> unallocated = new ArrayDeque<>();
	
	public void addLiteral(Data literal) {
		unallocated.add(literal);
	}
	
	public void allocate(Program program, List<Command> commands) {
		int lineNumber = commands.isEmpty() ? 1 : commands.get(commands.size() - 1).getLine();
		while (!unallocated.isEmpty()) {
			Data literal = unallocated.remove();
			if (allocated.containsKey(literal)) {
				continue;
			}
			ByteDirective c = new ByteDirective(literal);
			c.setLine(lineNumber);
			c.setMnemonic(BYTE);
			commands.add(c);
			allocated.put(literal, program.getLocationCounter());
			program.incrementLocationCounter(c.getSize());
		}
	}
	
	public int getLiteral(Data literal) {
		return allocated.get(literal);
	}
}
