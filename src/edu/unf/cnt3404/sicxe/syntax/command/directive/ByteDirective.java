package edu.unf.cnt3404.sicxe.syntax.command.directive;

import edu.unf.cnt3404.sicxe.syntax.Data;
import edu.unf.cnt3404.sicxe.syntax.command.AbstractCommand;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;

public class ByteDirective extends AbstractCommand implements WriteableCommand {

	private Data data;
	
	public ByteDirective(Data data) {
		this.data = data;
	}

	@Override
	public int getSize() {
		return data.getSize();
	}

	@Override
	public void write(byte[] buffer, int pos) {
		for (byte b : data.toBytes()) {
			buffer[pos++] = b;
		}
	}

	@Override
	public String getName() {
		return "BYTE";
	}

	@Override
	public String getArgument() {
		return data.toString();
	}

}
