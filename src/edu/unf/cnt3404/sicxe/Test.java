package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import edu.unf.cnt3404.sicxe.parse.AssembleError;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		SicXeAssm asm = new SicXeAssm(
				new BufferedReader(new FileReader("test.txt")));
		
		try {
			asm.passOne();
		} catch (AssembleError e) {
			e.printStackTrace();
		}
		
		PrintWriter lst = new PrintWriter(System.err);
		PrintWriter obj = new PrintWriter(System.out);
		
		asm.passTwo(lst, obj);
	}
}
