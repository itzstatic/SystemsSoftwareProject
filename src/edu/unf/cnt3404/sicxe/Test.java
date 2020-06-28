package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.writer.BeckObjectWriter;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		SicXeAssm asm = new SicXeAssm(
				new BufferedReader(new FileReader("output.txt")), 4);
		
		try {
			asm.passOne();
		} catch (AssembleError e) {
			e.printStackTrace();
			System.err.println("at " + "R: " + e.getRow() + " C: " + e.getCol());
		}
		
		PrintWriter lst = new PrintWriter(System.err);
		PrintWriter obj = new PrintWriter(System.out);
		
		asm.passTwo(lst, obj, new BeckObjectWriter(obj));
	}
}
