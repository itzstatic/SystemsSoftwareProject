package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.unf.cnt3404.sicxe.parse.Lexer;
import edu.unf.cnt3404.sicxe.parse.Parser;
import edu.unf.cnt3404.sicxe.parse.Scanner;
import edu.unf.cnt3404.sicxe.syntax.Command;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		Parser p = new Parser(new Lexer(new Scanner(
				new BufferedReader(new FileReader("test.txt")))));
		
		Command c;
		while ((c = p.next()) != null) {
			System.out.println(c.getLine() + " " + c.getLabel() + " " 
					+ c.getName() + " " + c.getArgument() + " " + c.getComment());
		}
	}
}
