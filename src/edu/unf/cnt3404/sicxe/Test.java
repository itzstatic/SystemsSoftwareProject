package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		SicXeAssm asm = new SicXeAssm(new BufferedReader(new FileReader("test.txt")));
		
		asm.passOne();
		
		asm.debug();
	}
}
