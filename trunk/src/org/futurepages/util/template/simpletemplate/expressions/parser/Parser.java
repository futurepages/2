package org.futurepages.util.template.simpletemplate.expressions.parser;

import java.util.List;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.BadExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedOperator;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.Unexpected;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.Operator;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.UnaryOperator;
import org.futurepages.util.template.simpletemplate.expressions.operators.logical.And;
import org.futurepages.util.template.simpletemplate.expressions.operators.logical.Equals;
import org.futurepages.util.template.simpletemplate.expressions.operators.logical.GreaterEqualsThan;
import org.futurepages.util.template.simpletemplate.expressions.operators.logical.GreaterThan;
import org.futurepages.util.template.simpletemplate.expressions.operators.logical.LessEqualsThan;
import org.futurepages.util.template.simpletemplate.expressions.operators.logical.LessThan;
import org.futurepages.util.template.simpletemplate.expressions.operators.logical.Not;
import org.futurepages.util.template.simpletemplate.expressions.operators.logical.NotEquals;
import org.futurepages.util.template.simpletemplate.expressions.operators.logical.Or;
import org.futurepages.util.template.simpletemplate.expressions.operators.logical.Xor;
import org.futurepages.util.template.simpletemplate.expressions.operators.numerical.Add;
import org.futurepages.util.template.simpletemplate.expressions.operators.numerical.Diff;
import org.futurepages.util.template.simpletemplate.expressions.operators.numerical.Div;
import org.futurepages.util.template.simpletemplate.expressions.operators.numerical.Mod;
import org.futurepages.util.template.simpletemplate.expressions.operators.numerical.Mult;
import org.futurepages.util.template.simpletemplate.expressions.operators.numerical.Negative;
import org.futurepages.util.template.simpletemplate.expressions.operators.numerical.Positive;
import org.futurepages.util.template.simpletemplate.expressions.operators.numerical.Power;
import org.futurepages.util.template.simpletemplate.expressions.operators.unevaluable.LParenthesis;
import org.futurepages.util.template.simpletemplate.expressions.operators.unevaluable.RParenthesis;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.expressions.tree.Token;
import org.futurepages.util.template.simpletemplate.util.MyStack;
import org.futurepages.util.template.simpletemplate.util.Tuple;


/**
 *
 * @author thiago
 * 
 * exp   -> term op exp
 * term  -> token | exp
 * token -> id | literal
 * 
 * - operadores por ordem de precedência:
 *		- (,) #futuramente -> []
 *		- !, (modificadores de sinal) -, +
 *		- ** (potenciação)
 *		- *, /, %
 *		- +, -
 *		- <, <=, >, >=
 *		- ==, !=
 *		- ^
 *		- &&, ||
 * 
 */
public class Parser {
	
	private static final Object [][] precedence;

	static {		
		precedence = new Object[][] {
			new Object[] { LParenthesis.class, RParenthesis.class },
			new Object[] { Not.class, Positive.class, Negative.class },
			new Object[] { Power.class },
			new Object[] { Mult.class, Div.class, Mod.class },
			new Object[] { Add.class, Diff.class },
			new Object[] { LessThan.class, LessEqualsThan.class, GreaterThan.class, GreaterEqualsThan.class },
			new Object[] { Equals.class, NotEquals.class },
			new Object[] { Xor.class },
			new Object[] { And.class, Or.class }
		};
	}

	private String exp;

	public Parser() {
	}

	public Parser(String expression) {
		exp = expression;
	}
	
	public String getExp() {
		return exp;
	}

	public void setExp(String expression) {
		exp = expression;
	}

	public Exp parse() throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected {
		List<Tuple<String, Integer>> tokens = new Tokenizer(exp).tokenList();
		List<Exp> exprs = new TokensToExp(tokens, exp).convert();

		new Semantic(exprs, exp, tokens).analise();

		MyStack<Exp> stack = buildStack(exprs);

		return buildTree(stack);
	}
	
	protected void leftBranch(BinaryOperator bo, MyStack<Exp> stack) {
		Exp left = stack.peek();
		
		if (left instanceof Token) {
			bo.setLeft(stack.pop());
		} else {
			bo.setLeft(buildTreeAux(stack));
		}
	}

	protected void rightBranch(BinaryOperator bo, MyStack<Exp> stack) {
		Exp right = stack.peek();
		
		if (right instanceof Token) {
			bo.setRight(stack.pop());
		} else {
			bo.setRight(buildTreeAux(stack));
		}
	}

	protected void branch(UnaryOperator uo, MyStack<Exp> stack) {
		Exp param = stack.peek();
		
		if (param instanceof Token) {
			uo.setParam(stack.pop());
		} else {
			uo.setParam(buildTreeAux(stack));
		}
	}
	
	protected Exp buildTreeAux(MyStack<Exp> stack) {
		Operator op = (Operator) stack.pop();
		
		if (op instanceof BinaryOperator) {
			rightBranch((BinaryOperator)op, stack);
			leftBranch((BinaryOperator)op, stack);
		} else { // op instanceof UnaryOperator
			branch((UnaryOperator)op, stack);
		}
		
		return op;
	}
	
	protected Exp buildTree(MyStack<Exp> stack) {
		Exp expr;

		expr = stack.peek();

		if (expr != null) {
			
			if (!(expr instanceof Token)) {

				Exp expression = buildTreeAux(stack);
				return expression;
			} else {
				if (!(stack.size() > 1)) {
					return stack.pop();
				} else {
					throw new RuntimeException("Mal formated expression");
				}
			}
		} else {
			throw new RuntimeException("No Tree");
		}
	}
	
	protected boolean greaterThan(Operator op1, Operator op2) {
		int i, j, len = precedence.length, len_inner;
		boolean tObreak = false;

		for (i = 0; i < len; i++) {
			len_inner = precedence[i].length;
			for (int k = 0; k < len_inner; k++) {
				if (op1.getClass().equals(precedence[i][k])) {
					tObreak = true;
					break;
				}
			}

			if (tObreak) {
				break;
			}
		}

		tObreak = false;

		for (j = 0; j < len; j++) {
			len_inner = precedence[j].length;
			for (int k = 0; k < len_inner; k++) {
				if (op2.getClass().equals(precedence[j][k])) {
					tObreak = true;
					break;
				}
			}

			if (tObreak) {
				break;
			}
		}

		return i < j;
	}

	protected MyStack<Exp> buildStack(List<Exp> itens) {
		MyStack<Exp> opStack = new MyStack<Exp>();
		MyStack<Exp> mainStack = new MyStack<Exp>();
		MyStack<Operator> lastOp = new MyStack<Operator>();

		for (int i = 0, len  = itens.size(); i < len; i++) {
			Exp expr = itens.get(i);

			if (isOp(expr) && !isRParen(expr)) {
				opStack.push(expr);

				while (!isLParen(expr) && !lastOp.isEmpty() && greaterThan((Operator)expr, lastOp.peek()) && isOp(mainStack.peek())) {
					Exp temp1 = mainStack.pop(), temp2 = opStack.pop();
					opStack.push(temp1).push(temp2);
					lastOp.pop();
				}

				continue;
			}

			if (isToken(expr) || isRParen(expr)) {
				if (isToken(expr)) {
					mainStack.push(expr);
				} else { // isRParen(expr) -> true
					Exp lp = opStack.peek();

					if (isLParen(lp)) {
						lastOp.push((Operator)opStack.pop());
					} else {
						throw new RuntimeException("\")\" does not match");
					}
				}

				boolean test = true;
				while (test) {
					if (opStack.isEmpty() || isLParen(opStack.peek())) {
						test = false;
					} else {
						lastOp.push((Operator)opStack.pop());
						mainStack.push(lastOp.peek());
					}
				}
			}
		}

		return mainStack;
	}

	@Deprecated
	private MyStack<Exp> buildStack_old(List<Exp> itens) {
		MyStack<Exp> opStack = new MyStack<Exp>();
		MyStack<Exp> execStack = new MyStack<Exp>();

		for (int i = 0, len = itens.size(); i < len; i++) {			
			Exp expr = itens.get(i);

			if (isOp(expr) && !isRParen(expr)) {
				opStack.push(expr);
				continue;
			}
			
			if (isToken(expr) || isRParen(expr)) {
				if (isToken(expr)) {
					execStack.push(expr);
				} else { // isRParen(expr) -> true
					Exp lp = opStack.peek();
					
					if (isLParen(lp)) {
						opStack.pop();
					} else {
						// throw exeption;
					}
				}

				boolean test = true;
				while (test) {
					if (opStack.isEmpty() || isLParen(opStack.peek())) {
						test = false;
					} else {
						execStack.push(opStack.pop());
					}
				}
			}
		}
		
		return execStack;
	}
	
	protected boolean isLParen(Exp ex) {
		return ex instanceof LParenthesis;
	}

	protected boolean isRParen(Exp ex) {
		return ex instanceof RParenthesis;
	}
	
	protected boolean isToken(Exp ex) {
		return ex instanceof Token;
	}

	protected boolean isOp(Exp ex) {
		return ex instanceof Operator;
	}
}
