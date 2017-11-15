package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.parse.Lexer;
import edu.unf.cnt3404.sicxe.parse.Parser;
import edu.unf.cnt3404.sicxe.parse.Scanner;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.ExpressionCommand;
import edu.unf.cnt3404.sicxe.syntax.command.directive.EndDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.ExtdefDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.ExtrefDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.OrgDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.StartDirective;

//Salim, Brandon Mathis, Brandon Mack
public class SicXeAssm {
	
	private Parser parser;
	private Alignment align = new Alignment();
	private Program program = new Program();
	private List<Command> commands = new ArrayList<>();
	
	public SicXeAssm(BufferedReader reader) {
		parser = new Parser(new Lexer(new Scanner(reader)));
	}
	
	//Eval ORG and modify locctr
	//Populate symtab with labels
	public void passOne() {
		Command c = parser.next();
		if (c == null) {
			return; //Empty program
		}
		
		if (c instanceof StartDirective) {
			//Can a program be nameless?
			if (c.getLabel() == null) {
				throw new AssembleError(c, "Expected program name");
			}
			program.setName(c.getLabel());
			program.setStart(((StartDirective) c).getStart());
		} else {
			throw new AssembleError(c, "Expected START Directive");
		}
		
		program.setLocationCounter(program.getStart());
		do {
			align.update(c);
			commands.add(c);
			//Modify locctr by org expr
			if (c instanceof OrgDirective) {
				((OrgDirective) c).getExpression().evaluate(c, program);
				program.setLocationCounter(((OrgDirective) c).getExpression().getValue());
			//Add extdef symbols
			} else if (c instanceof ExtdefDirective) {
				for (String def : ((ExtdefDirective) c).getSymbols()) {
					program.addExternalDefintion(def);
				}
			//Add extref symbols
			} else if (c instanceof ExtrefDirective) {
				for (String ref : ((ExtrefDirective) c).getSymbols()) {
					program.addExternalReference(ref);
				}
			}
			//Add symbols to symtab
			if (c.getLabel() != null) {
				program.put(c.getLabel(), program.getLocationCounter(), false);
			}
			
			//Increment locctr by size
			program.incrementLocationCounter(c.getSize());
			
			if (c instanceof EndDirective) {
				break;
			}
			
			c = parser.next();
		} while (c != null);
		
		//Loop terminated without an end directive
		if (c == null) {
			throw new AssembleError(parser, "Expected END Directive");
		}
	}
	
	//Eval all other expressions
	//Assemble instructions and directives
	//Write commands to listing
	//Write commands to object file
	public void passTwo(PrintWriter lst, PrintWriter obj) {
		Assembler assembler = new Assembler(program);
		ListingProgramWriter listing = new ListingProgramWriter(program, align, lst);
		ObjectProgramWriter object = new ObjectProgramWriter(program, obj);
		
		//Reset location counter
		program.setLocationCounter(program.getStart()); 
		object.writeHeaderReferAndDefineRecords();
		
		for (Command c : commands) {
			if (c instanceof ExpressionCommand) {
				((ExpressionCommand) c).getExpression().evaluate(c, program);
			}
			assembler.assemble(c);
			listing.write(c);
			object.write(c);
			if (c instanceof OrgDirective) {
				program.setLocationCounter(((OrgDirective) c).getExpression().getValue());
			}
			program.incrementLocationCounter(c.getSize());
		}
		
		object.writeModificationAndEndRecords();
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
		lst.flush();
		obj.flush();
	}
}
