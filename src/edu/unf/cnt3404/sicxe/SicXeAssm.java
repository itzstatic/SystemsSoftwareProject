package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
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
	
	public static void main(String[] args) {
		//Ensure there is one argument (file name)
		
		//Ensure file exists with the name
		
		//Parse file into syntax tree, consisting of pass one
		
		//Run pass two, creating .lst and .obj file
	}
	
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
		}
		//Create end record
	}
}
