package edu.unf.cnt3404.sicxe.syntax;

//A configuration of bytes, specified by a string of ascii or hex text
public interface Data {
	//Gets the bytes for this data
	byte[] toBytes();
}
