package edu.unf.cnt3404.sicxe.syntax;

import java.util.Arrays;

//A configuration of bytes, specified by a string of ascii or hex text
public abstract class Data {
	//Gets the bytes for this data
	public abstract byte[] toBytes();
	//Gets the number of bytes for this data. 
	public abstract int getSize();
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Data)) {
			return false;
		}
		Data d = (Data) o;
		byte[] mine = toBytes();
		byte[] your = d.toBytes();
		
		if (mine.length != your.length) {
			return false;
		}
		
		return Arrays.equals(mine, your);
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(toBytes());
	}
}
