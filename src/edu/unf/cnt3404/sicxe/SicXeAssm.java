package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.parse.Parser;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.ExpressionCommand;
import edu.unf.cnt3404.sicxe.syntax.command.ModifiableCommand;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;

//Salim, Brandon Mathis, Brandon Mack
public class SicXeAssm {
	
	private Parser parser;
	private Program program = new Program();
	private List<Command> commands = new ArrayList<>();
	
	public SicXeAssm(BufferedReader reader) {
		parser = new Parser(reader);
	}
	
	public void passOne() {
		Command c;
		while ((c = parser.next()) != null) {
			//Add symbols to symtab (An Equ directive would map symbol to expr value)
			program.putLocal(c.getLabel(), program.getLocationCounter(), false);
			//Increment locctr by size
			program.incrementLocationCounter(c.getSize());
		}
	}
	
	//Performs pass two, 
	public void passTwo(PrintWriter lst, PrintWriter obj) {
		//Reset location counter
		program.setLocationCounter(program.getStart()); 
		//Create header, define, and refer records
		for (Command c : commands) {
			if (c instanceof WriteableCommand) {
				//Add to text record, and flush if needed
			}
			if (c instanceof ExpressionCommand) {
				//Eval expressions
				
			}
			if (c instanceof ModifiableCommand) {
				//Create mod records
			}
			program.incrementLocationCounter(c.getSize());
		}
		//Create end record
	}
	
	public static void main(String[] args) {
		//Ensure there is one argument (file name)
		if (args.length < 1) {
			System.err.println("Requires file name!");
			System.exit(1);
		}
		
		//Ensure file exists with the name
		String fileName = args[0];
		BufferedReader file = null;
		try {
			file = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			System.err.println("Cannot open file '" + fileName + "'!");
			System.exit(2);
		}
		//Do pass one
		SicXeAssm assm = new SicXeAssm(file);
		assm.passOne();
		//Open .lst and .obj files
		PrintWriter lst = null;
		try {
			lst = new PrintWriter(fileName + ".lst");
		} catch (FileNotFoundException e) {
			System.err.println("Cannot create listing file!");
			System.exit(3);
		}
		PrintWriter obj = null;
		try {
			obj = new PrintWriter(fileName + ".obj");
		} catch (FileNotFoundException e) {
			System.err.println("Cannot create object file!");
			System.exit(4);
		}
		//Run pass two, creating .lst and .obj file
		assm.passTwo(lst, obj);
	}
}
