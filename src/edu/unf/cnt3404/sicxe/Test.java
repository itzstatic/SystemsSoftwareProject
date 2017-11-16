package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		SicXeAssm asm = new SicXeAssm(
				new BufferedReader(new FileReader("files\\fig25.txt")));
		
		asm.passOne();
		
		PrintWriter lst = new PrintWriter(System.err);
		PrintWriter obj = new PrintWriter(System.out);
		
		asm.passTwo(lst, obj);
		
		lst.flush();
		obj.flush();
	}
}
