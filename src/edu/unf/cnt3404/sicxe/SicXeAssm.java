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
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.Comment;
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
	private AssembleErrorLogger logger = new AssembleErrorLogger();
	
	public SicXeAssm(BufferedReader reader) {
		parser = new Parser(new Lexer(new Scanner(reader)));
	}
	
	//Eval ORG and modify locctr
	//Populate symtab with labels
	public void passOne() throws AssembleError {
		boolean beforeStart = true;
		boolean afterEnd = false;
		Command c = parser.next();
		
		do {
			align.update(c);
			commands.add(c);
			if (beforeStart) {
				if (!(c instanceof StartDirective) && !(c instanceof Comment)) {
					logger.log(c, "Expected START or comment");
				}
				if (c instanceof StartDirective) {
					if (c.getLabel() == null) {
						logger.log(c, "Expected program name");
					}
					program.setName(c.getLabel());
					int start = ((StartDirective) c).getStart();
					program.setStart(start);
					program.setLocationCounter(start);
					beforeStart = false;
				}
			} else if (afterEnd) {
				if (!(c instanceof Comment)) {
					logger.log(c, "Expected comment");
				}
			}
			if (c instanceof EndDirective) {
				afterEnd = true;
			//Modify locctr by org expr
			} else if (c instanceof OrgDirective) {
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
				if (program.getSymbol(c.getLabel()) != null) {
					logger.log(c, "Duplicate symbol " + c.getLabel());
				}
				program.put(c.getLabel(), program.getLocationCounter(), false);
			}
			
			//Increment locctr by size
			program.incrementLocationCounter(c.getSize());
			
			c = parser.next();
		} while (c != null);
		
		//Loop terminated without an end directive
		if (!afterEnd) {
			logger.log(parser, "Expected END Directive");
		}
	}
	
	//Eval all other expressions
	//Assemble instructions and directives
	//Write commands to listing
	//Write commands to object file
	//Returns whether there are logged errors
	public boolean passTwo(PrintWriter lst, PrintWriter obj) {
		Assembler assembler = new Assembler(program);
		ListingProgramWriter listing = new ListingProgramWriter(program, align, logger.toMap(), lst);
		ObjectProgramWriter object = new ObjectProgramWriter(program, obj);
		
		//Reset location counter
		program.setLocationCounter(program.getStart()); 
		object.writeHeaderReferAndDefineRecords();
		
		for (Command c : commands) {
			if (c instanceof ExpressionCommand) {
				Expression expr = ((ExpressionCommand) c).getExpression();
				if (expr != null) { //END Directive has null expression sometimes
					try {
						expr.evaluate(c, program);
					} catch (AssembleError e) {
						logger.log(e);
					}
				}
			}
			try {
				assembler.assemble(c);
				//c.setAssembled();
			} catch (AssembleError e) {
				logger.log(e);
			}
			listing.write(c);
			if (!logger.hasErrors()) {
				object.write(c);
			}
			if (c instanceof OrgDirective) {
				program.setLocationCounter(((OrgDirective) c).getExpression().getValue());
			}
			program.incrementLocationCounter(c.getSize());
		}
		
		object.writeModificationAndEndRecords();
		
		lst.flush();
		if (!logger.hasErrors()) {
			obj.flush();
		}
		
		
		return logger.hasErrors();
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
		try {	
			assm.passOne();
		} catch (AssembleError e) {
			System.out.printf("Syntax error in pass 1 (Row %d, Col %d) %s", 
				e.getRow(), e.getCol(), e.getMessage());
		}
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
		boolean errors = assm.passTwo(lst, obj);
		//Run pass two, creating .lst and .obj file
		if (errors) {
			System.out.println("Assembly errors in pass 2. See listing file.");
		} else {
			System.out.println("Assembly complete.");
		}
	}
}
