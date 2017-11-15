package edu.unf.cnt3404.sicxe.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import edu.unf.cnt3404.sicxe.global.Format;
import edu.unf.cnt3404.sicxe.global.Mnemonic;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Data;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.command.Comment;
import edu.unf.cnt3404.sicxe.syntax.command.directive.BaseDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.ByteDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.EndDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.ExtdefDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.ExtrefDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.OrgDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.ResbDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.ReswDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.StartDirective;
import edu.unf.cnt3404.sicxe.syntax.command.directive.WordDirective;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.Format1Instruction;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.Format2Instruction;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.Format34Instruction;
import edu.unf.cnt3404.sicxe.syntax.command.instruction.TargetMode;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNode;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNumber;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionOperator;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionStar;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionSymbol;

//Calls the lexer repeatedly in order to create Commands.
public class Parser implements Locatable {
	
	private Lexer lexer;
	
	//Creates a parser from the reader
	public Parser(Lexer lexer) {
		this.lexer = lexer;
	}
	
	//Returns the next command from the source, or, null if there
	//are no commands left and the parsing is finished
	public Command next() {
		//Advance to the beginning of the next useful token
		while (lexer.acceptWhitespace() || lexer.acceptNewline());
	
		//Check for end
		if (lexer.peek() == null) {
			return null;
		}
		
		Command result;
		//Then, get the line number and begin parsing
		int line = lexer.getRow();
		
		//Comment only on this line
		String comment = lexer.acceptComment();
		if (comment != null) {
			result = new Comment();
		//Non-comment that has a label and mnemonic
		} else {
			//See if there's a label
			String label = lexer.acceptSymbol();
			if (label != null) {
				lexer.expectWhitespace();
			}
			
			//If it's extended...
			boolean extended = lexer.accept('+');
			
			//Get the mnemonic
			Mnemonic mnemonic = lexer.expectMnemonic();
			
			//Ensure that the source only extends a F34 mnemonic
			//This implies someone can extend an RSUB (The only F34 mnemonic)
			if (extended && mnemonic.getFormat() != Format.FORMAT34 
				&& mnemonic.getFormat() != Format.FORMAT34M) {
				throw new AssembleError(lexer, "Cannot extend mnemonic " + mnemonic);
			}
			
			//Parse the rest of it, which depends on the Mnemonic itself
			if (mnemonic.getFormat() == null) {
				//Directive
				switch(mnemonic.getName()) {
				case "START": result = parseStartDirective(); break;
				case "END": result = parseEndDirective(); break;
				case "RESB": result = parseResbDirective(); break;
				case "RESW": result = parseReswDirective(); break;
				case "BYTE": result = parseByteDirective(); break;
				case "WORD": result = parseWordDirective(); break;
				case "BASE": result = parseBaseDirective(); break;
				case "EXTDEF": result = parseExtdefDirective(); break;
				case "EXTREF": result = parseExtrefDirective(); break;
				case "ORG": result = parseOrgDirective(); break;
				default: throw new AssembleError(lexer, "Directive " + mnemonic.getName() + " not implemented");
				}
			} else {
				//Instruction
				switch(mnemonic.getFormat()) {
				case FORMAT1: result = new Format1Instruction(mnemonic); break;
				case FORMAT2N: result = parseFormat2NCommand(mnemonic); break;
				case FORMAT2R: result = parseFormat2RCommand(mnemonic); break;
				case FORMAT2RN: result = parseFormat2RNCommand(mnemonic); break;
				case FORMAT2RR: result = parseFormat2RRCommand(mnemonic); break;
				case FORMAT34: result = new Format34Instruction(extended, mnemonic); break;
				case FORMAT34M: result = parseFormat34MCommand(extended, mnemonic); break;
				default: throw new IllegalStateException(mnemonic.toString());
				}
			}
			
			result.setLabel(label);
			//Get all whitespace until comment
			//Or no whitespace separating the comment from the rest of the command
			while (lexer.acceptWhitespace());
			
			comment = lexer.acceptComment();
		}
		
		//Get all the whitespace until newline or end
		while (lexer.acceptWhitespace());
		
		//If there's not newline, but still a token, then complain!
		if (!lexer.acceptNewline() && lexer.peek() != null) {
			throw new AssembleError(lexer, "Expected newline or end of stream not " + lexer.peek());
		}
		result.setLine(line);
		result.setComment(comment);
		return result;
	}
	
	//Precondition: mnemonic.getFormat() == Format.FORMAT2N
	//Post condition: do not consume the new line at the end of the command.
	//that new line is consumed by next().
	//Likewise for related parse methods
	private Command parseFormat2NCommand(Mnemonic mnemonic) {
		lexer.expectWhitespace();
		int n = lexer.expectNumber();
		return new Format2Instruction(mnemonic, (byte)n);
	}
	
	private Command parseFormat2RCommand(Mnemonic mnemonic) {
		lexer.expectWhitespace();
		String r = lexer.expectSymbol();
		return new Format2Instruction(mnemonic, r);
	}
	
	private Command parseFormat2RRCommand(Mnemonic mnemonic) {
		lexer.expectWhitespace();
		String r1 = lexer.expectSymbol();
		lexer.expect(',');
		String r2 = lexer.expectSymbol();
		
		return new Format2Instruction(mnemonic, r1, r2);
	}
	
	private Command parseFormat2RNCommand(Mnemonic mnemonic) {
		lexer.expectWhitespace();
		String r = lexer.expectSymbol();
		lexer.expect(',');
		int n = lexer.expectNumber();
		return new Format2Instruction(mnemonic, r, (byte)n);
	}
	
	private Command parseFormat34MCommand(boolean extended, Mnemonic mnemonic) {
		lexer.expectWhitespace();
		
		//Figure out what target mode
		TargetMode target;
		if (lexer.accept('#')) {
			target = TargetMode.IMMEDIATE;
		} else if (lexer.accept('@')) {
			target = TargetMode.INDIRECT;
		} else {
			//XE Assembler NEVER uses SIMPLE, because that is ni = 00.
			target = TargetMode.SIMPLE_XE;
		}
		
		//Get the operand
		Expression expr = parseExpression();
		
		boolean indexed = false;
		//See if it's indexed or not
		if (lexer.accept(',')) {
			String x = lexer.expectSymbol();
			if (!x.equals("X")) {
				throw new AssembleError(lexer, "Expected X not " + x);
			}
			indexed = true;
		}
		
		return new Format34Instruction(extended, mnemonic, target, expr, indexed);
	}
	
	//Precondition: START mnemonic token was already consumed.
	//Postcondition: Do not consume the new line at the end. That is consumed by next()
	//Likewise for other parse...Directive methods
	private Command parseStartDirective() {
		lexer.expectWhitespace();
		int start = lexer.expectNumber();
		return new StartDirective(start);
	}
	
	private Command parseEndDirective() {
		if (lexer.acceptWhitespace()) {
			return new EndDirective(parseExpression());
		}
		return new EndDirective();
	}
	
	private Command parseResbDirective() {
		lexer.expectWhitespace();
		int bytes = lexer.expectNumber();
		return new ResbDirective(bytes);
	}
	
	private Command parseReswDirective() {
		lexer.expectWhitespace();
		int words = lexer.expectNumber();
		return new ReswDirective(words);
	}
	
	private Command parseByteDirective() {
		lexer.expectWhitespace();
		Data data = lexer.expectData();
		return new ByteDirective(data);
	}
	
	private Command parseWordDirective() {
		lexer.expectWhitespace();
		return new WordDirective(parseExpression());
	}
	
	private Command parseBaseDirective() {
		lexer.expectWhitespace();
		return new BaseDirective(parseExpression());
	}
	
	private Command parseExtrefDirective() {
		lexer.expectWhitespace();
		List<String> symbols = new ArrayList<>();
		do {
			symbols.add(lexer.expectSymbol());
		} while (lexer.accept(','));
		return new ExtrefDirective(symbols);
	}
	
	private Command parseExtdefDirective() {
		lexer.expectWhitespace();
		List<String> symbols = new ArrayList<>();
		do {
			symbols.add(lexer.expectSymbol());
		} while (lexer.accept(','));
		return new ExtdefDirective(symbols);
	}
	
	private Command parseOrgDirective() {
		lexer.expectWhitespace();
		return new OrgDirective(parseExpression());
	}
	
	//Attempts to parse an expression. Will stop parsing when the lexer reaches a newline
	//token, a comment token, or an illegal token. Throws an exception if an illegal token 
	//is reached. Implements Dijkstra's Shunting-Yard Algorithm to turn an infix
	//arithmetic expression into a syntax tree
	public Expression parseExpression() {
		Stack<ExpressionNode> nodes = new Stack<>();
		//Null element in operators indicates parentheses
		Stack<ExpressionOperator.Type> operators = new Stack<>();
		//This flag toggles between tokens. It is required to distinguish star (*) as a
		//multiplication operator versus as a location counter operand.
		boolean expectsOperator = false; //Initially expect a value
		//While there are tokens to be read
		while (lexer.peek() != null) {
			//System.out.println("OPERATORS: " + operators);
			//System.out.println("OPERANDS: " + nodes);
			if (lexer.acceptWhitespace()) {
				continue;
			}
			if (expectsOperator) {
				ExpressionOperator.Type operator;
				//Read the operator
				if (lexer.accept('+')) {
					operator = ExpressionOperator.Type.ADD;
				} else if (lexer.accept('-')) {
					operator = ExpressionOperator.Type.SUB;
				} else if (lexer.accept('*')) {
					operator = ExpressionOperator.Type.MUL;
				} else if (lexer.accept('/')) {
					operator = ExpressionOperator.Type.DIV;
				} else if (lexer.accept(')')) {
					//Pop while the operator is not a parentheses (null)
					while (!operators.isEmpty() && operators.peek() != null) {
						//nodes stack reverses order of the nodes, so right pops off first
						ExpressionNode right = nodes.pop();
						ExpressionNode left = nodes.pop();
						nodes.add(new ExpressionOperator(operators.pop(), left, right));
					}
					if (operators.isEmpty()) {
						//Therefore, peek() never returned null, and there was no opening parentheses
						throw new AssembleError(lexer, "Did not expect )");
					}
					//Otherwise, peek() returned null, so pop it
					operators.pop();
					continue; //<-- This is so bad; this is because no operator was read
					//in this branch, so 1) I cannot push an operator onto the stack, and
					//2) I should not toggle expectsOperator
				} else {
					//throw new ParseError(token, "Expected operator not " + token);
					break;
				}
				//Output higher precedence operators into the tree
				//If the stack top is ever null, then it is a parentheses on the stack.
				//Wikipedia did not seem to say what to do, or if this scenario is possible.
				while (!operators.isEmpty() && operators.peek() != null && 
						operators.peek().getPrecedence() >= operator.getPrecedence()) {
					ExpressionNode right = nodes.pop();
					ExpressionNode left = nodes.pop();
					nodes.add(new ExpressionOperator(operators.pop(), left, right));
				}
				operators.push(operator);
			} else { //If the parser expects an operand
				String symbol;
				Integer number;
				
				if ((symbol = lexer.acceptSymbol()) != null) {
					nodes.add(new ExpressionSymbol(symbol));
				} else if ((number = lexer.acceptNumber()) != null) {
					nodes.add(new ExpressionNumber(number));
				} else if (lexer.accept('*')) {
					nodes.add(new ExpressionStar());
				} else if (lexer.accept('(')) {
					operators.push(null);
					continue; //Continue so that I do not toggle expectsOperator
				} else {
					//throw new ParseError(token, "Expected operand not " + token);
					break;
				}
			}
			expectsOperator = !expectsOperator;
		}
		//Pop all excess operators from the stack
		while(!operators.isEmpty()) {
			ExpressionOperator.Type operator = operators.pop();
			//If a parentheses was found on the stack
			if (operator == null) {
				throw new AssembleError(lexer, "Unbalanced parentheses");
			}
			//Ensure left is the first dequeue
			//Pop operator into nodes
			//nodes stack reverses order of the nodes, so right pops off first
			ExpressionNode right = nodes.pop();
			ExpressionNode left = nodes.pop();
			nodes.add(new ExpressionOperator(operator, left, right));
		}
		return new Expression(nodes.pop());
	}

	@Override
	public int getRow() {
		return lexer.getRow();
	}

	@Override
	public int getCol() {
		return lexer.getCol();
	}
}
