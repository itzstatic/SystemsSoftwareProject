package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.unf.cnt3404.sicxe.parse.Lexer;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		Lexer l = new Lexer(new BufferedReader(new FileReader("test.txt")));
		
		while (true) {
			System.out.println(l.next());
		}
	}
}
