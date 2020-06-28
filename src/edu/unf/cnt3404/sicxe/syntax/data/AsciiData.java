package edu.unf.cnt3404.sicxe.syntax.data;

import edu.unf.cnt3404.sicxe.syntax.Data;

//Represents data from an ascii string
public class AsciiData extends Data {

	private String value;
	
	public AsciiData(String value) {
		this.value = value;
	}
	
	@Override
	public byte[] toBytes() {
		int len = value.length();
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = (byte)value.charAt(i);
		}
		return result;
	}
	
	@Override
	public int getSize() {
		return value.length();
	}

	@Override
	public String toString() {
		return "C'" + value + "'";
	}
}
