package edu.unf.cnt3404.sicxe.parse;

import java.io.BufferedReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;

import edu.unf.cnt3404.sicxe.parse.Token.Type;
import edu.unf.cnt3404.sicxe.syntax.Command;
import edu.unf.cnt3404.sicxe.syntax.Expression;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNumber;
import edu.unf.cnt3404.sicxe.syntax.expression.ExpressionNode;
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
		Queue<ExpressionNode> nodes = new ArrayDeque<>();
		//Null element in operator indicates parentheses
		Stack<ExpressionOperator.Type> operators = new Stack<>();
		ExpressionOperator.Type operator;
		Token token;
		//This flag toggles between tokens. It is required to distinguish star (*) as a
		//multiplication operator versus as a location counter operand.
		boolean expectsOperator = false; //Initially expect a value
		//While there are tokens to be read
		while ((token = lexer.peek()).getType() != Type.COMMENT && !token.is('\n')) {
			if (lexer.accept(Type.WHITESPACE) != null) {
				continue;
			} else if (lexer.accept(Type.SYMBOL) != null) {
				nodes.add(new ExpressionSymbol(token.asSymbol()));
			} else if (lexer.accept(Type.NUMBER) != null) {
				nodes.add(new ExpressionNumber(token.asNumber()));
			} else if (lexer.accept('*')) {
				if (expectsOperator) {
					operators.push(ExpressionOperator.Type.MUL);
				} else {
					nodes.add(new ExpressionStar());
				}
			} else if (lexer.accept('+')) {
				operators.push(ExpressionOperator.Type.ADD);
			} else if (lexer.accept('-')) {
				operators.push(ExpressionOperator.Type.SUB);
			} else if (lexer.accept('/')) {
				operators.push(ExpressionOperator.Type.DIV);
			} else if (lexer.accept('(')) {
				operators.push(null);
			} else if (lexer.accept(')')) {
				//While there are more operators and the operator is not a parentheses
				while(!operators.isEmpty() && (operator = operators.pop()) != null) {
					//Ensure left is the first dequeue
					ExpressionNode left = nodes.poll();
					ExpressionNode right = nodes.poll();
					//Pop operators into nodes
					nodes.add(new ExpressionOperator(operator, left, right));
				}
				//If there was no left parentheses (null) found on the stack,
				if (operators.isEmpty()) {
					throw new ParseError(token, "Expected ) not " + token);
				}
			} else {
				//error
			}
			expectsOperator = !expectsOperator;
		}
		//Pop all excess operators from the stack
		while(!operators.isEmpty() && (operator = operators.pop()) != null) {
			//Ensure left is the first dequeue
			ExpressionNode left = nodes.poll();
			ExpressionNode right = nodes.poll();
			//Pop operators into nodes
			nodes.add(new ExpressionOperator(operator, left, right));
		}
		//If there was no left parentheses (null) found on the stack,
		if (operators.isEmpty()) {
			throw new ParseError(token, "Expected ) not " + token);
		}
		return new Expression(nodes.poll());
	}
}
