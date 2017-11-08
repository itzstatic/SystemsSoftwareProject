package edu.unf.cnt3404.sicxe.syntax.command.directive;

import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;
import edu.unf.cnt3404.sicxe.syntax.command.ModifiableCommand;

public class WordDirective extends AbstractCommand implements ModifiableCommand {

	//The value inside the word, to be assembled
	private int word;
	
	private Expression expr;
	
	public WordDirective(Expression expr) {
		this.expr = expr;
	}
	
	@Override
	public Expression getExpression() {
		return expr;
	}

	public void setWord(int word) {
		this.word = word;
	}
	
	@Override
	public int getSize() {
		return 3;
	}

	@Override
	public void write(byte[] buffer, int pos) {
		buffer[pos] 	= (byte)(word >> 16);
		buffer[pos + 1] = (byte)(word >> 8);
		buffer[pos + 2] = (byte)(word);
	}

	@Override
	public int getOffset() {
		return 0; //The entire word needs to be modified
	}

	@Override
	public int getStride() {
		return 6; //Again, the entire word
	}

	@Override
	public String getName() {
		return " WORD";
	}

	@Override
	public String getArgument() {
		return expr.toString();
	}
}
