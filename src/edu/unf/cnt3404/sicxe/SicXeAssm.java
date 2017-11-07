package edu.unf.cnt3404.sicxe;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import edu.unf.cnt3404.sicxe.parse.Lexer;
import edu.unf.cnt3404.sicxe.parse.AssembleError;
import edu.unf.cnt3404.sicxe.parse.Parser;
import edu.unf.cnt3404.sicxe.parse.Scanner;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Program;
import edu.unf.cnt3404.sicxe.syntax.command.ModifiableCommand;
import edu.unf.cnt3404.sicxe.syntax.command.WriteableCommand;
import edu.unf.cnt3404.sicxe.syntax.command.directive.BaseDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.EndDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.NoBaseDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.OrgDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.StartDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.WordDirective;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.Format2Instruction;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.Format34Instruction;

//Salim, Brandon Mathis, Brandon Mack
public class SicXeAssm {
	
	private Parser parser;
	private Alignment align = new Alignment();
	private Program program = new Program();
	private List<Command> commands = new ArrayList<>();
	
	public SicXeAssm(BufferedReader reader) {
		parser = new Parser(new Lexer(new Scanner(reader)));
	}
	
	//Add labels to symtab
	public void passOne() {
		Command c = parser.next();
		if (c == null) {
			return; //Empty program
		}
		if (!(c instanceof StartDirective)) {
			throw new AssembleError(c, "Expected START Directive");
		}
		
		 while (c != null && !(c instanceof EndDirective)) {
			align.update(c);
			commands.add(c);
			//Modify locctr by org expr
			if (c instanceof OrgDirective) {
				program.setLocationCounter(((OrgDirective) c).getExpression().getValue(c, program));
			}
			//Add symbols to symtab
			if (c.getLabel() != null) {
				program.put(c.getLabel(), program.getLocationCounter(), false);
			}
			//Increment locctr by size
			program.incrementLocationCounter(c.getSize());
			c = parser.next();
		}
		
		//Loop terminated without an end directive
		if (c == null) {
			throw new AssembleError("Expected END Directive");
		}
	}
	
	public void debug() {
		byte[] buffer;
		for (Command c : commands) {
			if (c instanceof WriteableCommand) {
				buffer = new byte[c.getSize()];
				((WriteableCommand) c).write(buffer, 0);
				for (byte b : buffer) {
					System.out.printf("%02X", b);
				}
				System.out.println();
			}
		}
	}
	
	//Evaluate expressions
	//Create all object records
	public void passTwo(PrintWriter lst, PrintWriter obj) {
		Assembler assembler = new Assembler(program);
		ListingProgramWriter listing = new ListingProgramWriter(program, align, lst);
		ObjectProgramWriter object = new ObjectProgramWriter(program, obj);
		
		//Reset location counter
		program.setLocationCounter(program.getStart()); 
		object.writeHeaderReferAndDefineRecords();
		
		for (Command c : commands) {
			//There's got to be a better way...
			if (c instanceof StartDirective) assembler.assemble((StartDirective)c);
			if (c instanceof EndDirective) assembler.assemble((EndDirective)c);
			if (c instanceof BaseDirective) assembler.assemble((BaseDirective)c);
			if (c instanceof NoBaseDirective) assembler.assemble((NoBaseDirective)c);
			if (c instanceof WordDirective) assembler.assemble((WordDirective)c);
			
			if (c instanceof Format2Instruction) assembler.assemble((Format2Instruction)c);
			if (c instanceof Format34Instruction) assembler.assemble((Format34Instruction)c);
			
			listing.write(c);
			
			if (c instanceof WriteableCommand) object.write((WriteableCommand)c);
			if (c instanceof ModifiableCommand) object.modify((ModifiableCommand)c);
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
	}
}
