package edu.unf.cnt3404.sicxe.syntax.command.instruction;

import edu.unf.cnt3404.sicxe.global.Mnemonic;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;
import edu.unf.cnt3404.sicxe.syntax.command.ModifiableCommand;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNumber;

public class Format34Instruction extends AbstractCommand implements
		ModifiableCommand {

	//Provided by constructor at parse-time
	private boolean extended;
	private Mnemonic mnemonic;
	private TargetMode target;
	private Expression expr;
	private boolean indexed;
	
	//To be computed during assembly (hence the setters)
	private short argument; 
	private AddressMode address;
	
	//Format34M
	public Format34Instruction(boolean extended, Mnemonic mnemonic, 
		TargetMode target, Expression expr, boolean indexed) {
		this.extended = extended;
		this.mnemonic = mnemonic;
		this.target = target;
		this.expr = expr;
		this.indexed = indexed;
	}
	
	//TODO can you extend a Format34 Instruction? RSUB is the only known one
	//Format34 
	public Format34Instruction(boolean extended, Mnemonic mnemonic) {
		//Zero out the expression
		this(extended, mnemonic, TargetMode.SIMPLE_XE, 
			new Expression(new ExpressionNumber(0)), false);
	}
	
	public void setAddressMode(AddressMode address) {
		this.address = address;
	}
	
	public void setArgument(int argument) {
		this.argument = (short)argument;
	}
	
	public boolean isExtended() {
		return extended;
	}
	
	@Override
	public Expression getExpression() {
		return expr;
	}
	
	@Override
	public int getSize() {
		return extended ? 4 : 3;
	}

	@Override
	public void write(byte[] buffer, int pos) {
		buffer[pos] = (byte)(mnemonic.getOpcode() | target.getNiMask());
		if (extended) {
			buffer[pos + 1] = (byte)((argument >> 16));
			buffer[pos + 2] = (byte)((argument >> 8));
			buffer[pos + 3] = (byte)(argument);
		} else {
			buffer[pos + 1] = (byte)((argument >> 8));
			buffer[pos + 2] = (byte)(argument);
		}
		buffer[pos + 1] |= address.getXbpeFlag(indexed, extended);
	}

	@Override
	public int getOffset() {
		return 1; //Don't modify the opcode
	}

	@Override
	public int getStride() {
		return 5; //5-half-bytes of format-4 address
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		if (extended) result.append('+');
		result.append(mnemonic.getName());
		result.append(' ');
		switch(target) {
		case IMMEDIATE: result.append('#'); break;
		case INDIRECT: result.append('@'); break;
		default:
		}
		result.append(expr);
		return result.toString();
	}
}
