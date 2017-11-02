package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import edu.unf.cnt3404.sicxe.parse.Lexer;
import edu.unf.cnt3404.sicxe.parse.Parser;
import edu.unf.cnt3404.sicxe.parse.Scanner;
import edu.unf.cnt3404.sicxe.syntax.Expression;

public class Test {
	public static void main(String[] args) throws FileNotFoundException {
		Lexer l = new Lexer(new Scanner(new BufferedReader(new FileReader("test.txt"))));
		Parser p = new Parser(l);
		Expression e = p.parseExpression();
		System.out.println(e + " = " + e.getValue(null));
	}
}
