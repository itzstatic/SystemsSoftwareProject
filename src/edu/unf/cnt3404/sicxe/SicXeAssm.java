package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.parse.Lexer;
import edu.unf.cnt3404.sicxe.parse.MacroParser;
import edu.unf.cnt3404.sicxe.parse.Parser;
import edu.unf.cnt3404.sicxe.parse.Scanner;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.Comment;
import edu.unf.cnt3404.sicxe.syntax.command.ExpressionCommand;
import edu.unf.cnt3404.sicxe.syntax.command.directive.EndDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.EquDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.ExtdefDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.ExtrefDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.LtorgDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.OrgDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.StartDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.MacroDefinitionDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.macro.MacroExpansionDirective;
import edu.unf.cnt3404.sicxe.writer.BeckObjectWriter;

//Salim, Brandon Mathis, Brandon Mack
public class SicXeAssm {
	
	private MacroParser parser;
	private Alignment align = new Alignment();
	private Program program = new Program();
	private MacroPreprocessor preprocessor;
	private List<Command> commands = new ArrayList<>();
	private AssembleErrorLogger logger = new AssembleErrorLogger();
	
	public SicXeAssm(BufferedReader reader, int tabWidth) {
		parser = new MacroParser(
			new Parser(new Lexer(new Scanner(reader, tabWidth))));
		preprocessor = new MacroPreprocessor(program);
	}
	
	//Eval ORG and modify locctr
	//Populate symtab with labels
	public void passOne() throws AssembleError {
		boolean beforeStart = true;
		boolean afterEnd = false;
		Command c = parser.next();
		
		do {
			align.update(c);
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
				program.allocateLiterals(commands);
				afterEnd = true;
			//Modify locctr by org expr
			}
			commands.add(c);
			if (c instanceof MacroDefinitionDirective) {
				preprocessor.define((MacroDefinitionDirective) c);
			} else if (c instanceof MacroExpansionDirective) {
				List<Command> expandedCommands = preprocessor.expand((MacroExpansionDirective) c);
				for (Command expandedcommand : expandedCommands) {
					commands.add(expandedcommand);
					program.incrementLocationCounter(expandedcommand.getSize());
				}
			
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
			//Add literals
			} else if (c instanceof ExpressionCommand) {
				Expression e = ((ExpressionCommand) c).getExpression();
				if (e != null) {
					e.collectLiterals(program);
				}
			//Allocate literals
			} else if (c instanceof LtorgDirective) {
				program.allocateLiterals(commands);
			}
			//Add symbols to symtab
			if (c.getLabel() != null) {
				if (program.getSymbol(c.getLabel()) != null) {
					logger.log(c, "Duplicate symbol " + c.getLabel());
				}
				//Evaluate Equ Expressions
				if (c instanceof EquDirective) {
					Expression e = ((EquDirective) c).getExpression();
					e.evaluate(c, program);
					program.put(c.getLabel(), e.getValue(), e.isAbsolute());
				} else {
					program.put(c.getLabel(), program.getLocationCounter(), false);
				}
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
	public boolean passTwo(PrintWriter lst, PrintWriter obj, ObjectWriter object) {
		Assembler assembler = new Assembler(program);
		ListingProgramWriter listing = new ListingProgramWriter(program, align, logger.toMap(), lst);
		
		object.setProgram(program);
		
		//Reset location counter
		program.setLocationCounter(program.getStart());
		object.start();
		
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
		object.end();
		
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
		SicXeAssm assm = new SicXeAssm(file, 4);
		try {	
			assm.passOne();
		} catch (AssembleError e) {
			System.out.printf("Syntax error in pass 1 (Row %d, Col %d) %s\n", 
				e.getRow(), e.getCol(), e.getMessage());
			System.exit(5);
		}
		//Open .lst and .obj files
		String mainName = fileName.split("\\.")[0];
		PrintWriter lst = null;
		try {
			lst = new PrintWriter(mainName + ".lst");
		} catch (FileNotFoundException e) {
			System.err.println("Cannot create listing file!");
			System.exit(3);
		}
		PrintWriter obj = null;
		try {
			obj = new PrintWriter(mainName + ".obj");
		} catch (FileNotFoundException e) {
			System.err.println("Cannot create object file!");
			System.exit(4);
		}
		ObjectWriter object = new BeckObjectWriter(obj);
		boolean errors = assm.passTwo(lst, obj, object);
		//Run pass two, creating .lst and .obj file
		if (errors) {
			System.out.println("Assembly errors in pass 2. See listing file.");
		} else {
			System.out.println("Assembly complete.");
		}
	}
}
