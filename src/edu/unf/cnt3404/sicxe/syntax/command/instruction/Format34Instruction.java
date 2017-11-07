package edu.unf.cnt3404.sicxe.syntax.command.instruction;

import edu.unf.cnt3404.sicxe.global.Mnemonic;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.command.ModifiableCommand;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNumber;

public class Format34Instruction extends Instruction implements ModifiableCommand {

	//Provided by constructor at parse-time
	private boolean extended;
	private TargetMode target;
	private Expression expr;
	private boolean indexed;
	
	//To be computed during assembly (hence the setters)
	private short argument; 
	private AddressMode address;
	
	//Format34M
	//If indexed, then TargetMode must be Simple
	public Format34Instruction(boolean extended, Mnemonic mnemonic, 
		TargetMode target, Expression expr, boolean indexed) {
		super(mnemonic);
		this.extended = extended;
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
		//Mask so that a negative 12-bit disp will not overflow into xbpe
		int mask;
		if (extended) { 
			//20 bit mask
			mask = (1 << 20) - 1;
		} else if (target == TargetMode.SIMPLE){ 
			//15 bit mask
			mask = (1 << 15) - 1;
		} else { 
			//12 bit mask
			mask = (1 << 12) - 1;
		}
		this.argument = (short)(mask & argument);
	}
	
	public boolean isExtended() {
		return extended;
	}
	
	public TargetMode getTargetMode() {
		return target;
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
			buffer[pos + 2] = (byte)(argument >> 8);
			buffer[pos + 3] = (byte)(argument);
		} else {
			buffer[pos + 1] = (byte)((argument >> 8));
			buffer[pos + 2] = (byte)(argument);
		}
		buffer[pos + 1] |= address.getXbpeFlag(indexed, extended);
		
		if (getLine() == 10 ) {
			System.out.println(Integer.toString(argument, 2));
		}
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
	public String getName() {
		return (extended ? '+' : ' ') + mnemonic.getName();
	}

	@Override
	public String getArgument() {
		StringBuilder result = new StringBuilder();
		switch(target) {
		case IMMEDIATE: result.append('#'); break;
		case INDIRECT: result.append('@'); break;
		default: result.append(' '); break;
		}
		result.append(expr);
		if (indexed) {
			result.append(",X");
		}
		return result.toString();
	}
}
