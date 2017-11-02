package edu.unf.cnt3404.sicxe.parse;

import java.io.BufferedReader;
import java.util.Stack;

import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNode;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNumber;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionOperator;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionStar;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionSymbol;

//Calls the lexer repeatedly in order to create Commands.
public class Parser {
	
	final private Lexer lexer = null;
	
	//Creates a parser from the reader
	public Parser(BufferedReader reader) {
		
	}
	
	//Returns the next command from the source, or, null if there
	//are no commands left and the parsing is finished
	public Command next() {
		return null;
	}
	
	//Attempts to parse an expression. Will stop parsing when the lexer reaches a newline
	//token, a comment token, or an illegal token. Throws an exception if an illegal token 
	//is reached. Implements Dijkstra's Shunting-Yard Algorithm to turn an infix
	//arithmetic expression into a syntax tree
	private Expression parseExpression() {
		Stack<ExpressionNode> nodes = new Stack<>();
		//Null element in operators indicates parentheses
		Stack<ExpressionOperator.Type> operators = new Stack<>();
		Token token;
		//This flag toggles between tokens. It is required to distinguish star (*) as a
		//multiplication operator versus as a location counter operand.
		boolean expectsOperator = false; //Initially expect a value
		//While there are tokens to be read
		while ((token = lexer.peek()).getType() != Token.Type.COMMENT && !token.is('\n')) {
			if (lexer.accept(Token.Type.WHITESPACE) != null) {
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
						throw new ParseError(token, "Did not expect )");
					}
					//Otherwise, peek() returned null, so pop it
					operators.pop();
					continue; //<-- This is so bad; this is because no operator was read
					//in this branch, so 1) I cannot push an operator onto the stack, and
					//2) I should not toggle expectsOperator
				} else {
					throw new ParseError(token, "Expected operator not " + token);
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
				if (lexer.accept(Token.Type.SYMBOL) != null) {
					nodes.add(new ExpressionSymbol(token.asSymbol()));
				} else if (lexer.accept(Token.Type.NUMBER) != null) {
					nodes.add(new ExpressionNumber(token.asNumber()));
				} else if (lexer.accept('*')) {
					nodes.add(new ExpressionStar());
				} else if (lexer.accept('(')) {
					operators.push(null);
					continue; //Continue so that I do not toggle expectsOperator
				} else {
					throw new ParseError(token, "Expected operand not " + token);
				}
			}
			expectsOperator = !expectsOperator;
		}
		//Pop all excess operators from the stack
		while(!operators.isEmpty()) {
			ExpressionOperator.Type operator = operators.pop();
			//If a parentheses was found on the stack
			if (operator == null) {
				throw new ParseError(token, "Expected ) not " + token);
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
}
