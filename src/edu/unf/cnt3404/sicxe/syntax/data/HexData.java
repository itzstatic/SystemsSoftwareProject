package edu.unf.cnt3404.sicxe.syntax.data;

import edu.unf.cnt3404.sicxe.syntax.Data;

public class HexData implements Data {

	private String value;
	
	public HexData(String value) {
		this.value = value;
	}

	@Override
	public byte[] toBytes() {
		byte[] result = new byte[value.length() / 2];
		for (int i = 0; i < result.length; i++) {
			String b = value.substring(i * 2, i * 2 + 2);
			result[i] = (byte) Integer.parseInt(b, 16);
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "X'" + value + "'";
	}

}
