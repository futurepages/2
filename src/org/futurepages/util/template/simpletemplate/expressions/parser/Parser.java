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

	// Tabela que usa as classes dos operadores para os cálculos de prescedência.
	private static final Object [][] precedence;

	static {
		// Povoando a tabela de prescedência.
		// Quanto mais alto na tablena (menor o índice do array),
		// e mais à esquerda (menor o índice do subarray) maior a prescedência.
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
		// Lista de tupas que contém o token e o seu indice na string da expressão
		List<Tuple<String, Integer>> tokens = new Tokenizer(exp).tokenList();
		// Converte as Tuplas de tokens e suas respectivas classes
		List<Exp> exprs = new TokensToExp(tokens, exp).convert();

		// Faz análise semântica da expressão
		new Semantic(exprs, exp, tokens).analise();

		// Constroi um pilha dos operadores e operandos.
		// Porém é avaliada da direita para esquerda,
		// tornando-a sua avaliação (execução) inválida.
		MyStack<Exp> stack = buildStack(exprs);

		// Corrige a pilha de operadores e operandos, transformando-o
		// em uma árvore, garantindo a avaliação (execução) da esquerda para direita.
		return buildTree(stack);
	}

	// Constroi o braço esquerda da árvore binária.
	protected void leftBranch(BinaryOperator bo, MyStack<Exp> stack) {
		Exp left = stack.peek();
		
		if (left instanceof Token) {
			bo.setLeft(stack.pop());
		} else {
			bo.setLeft(buildTreeAux(stack));
		}
	}

	// Constroi o braço direito da árvore binária.
	protected void rightBranch(BinaryOperator bo, MyStack<Exp> stack) {
		Exp right = stack.peek();
		
		if (right instanceof Token) {
			bo.setRight(stack.pop());
		} else {
			bo.setRight(buildTreeAux(stack));
		}
	}

	// Constroi o braço da árbore unária.
	protected void branch(UnaryOperator uo, MyStack<Exp> stack) {
		Exp param = stack.peek();
		
		if (param instanceof Token) {
			uo.setParam(stack.pop());
		} else {
			uo.setParam(buildTreeAux(stack));
		}
	}

	// Constroi a árvore de operadores baseado na pilha de operadores.
	protected Exp buildTreeAux(MyStack<Exp> stack) {
		Operator op = (Operator) stack.pop();

		if (op instanceof BinaryOperator) {
			// braço esquerdo
			rightBranch((BinaryOperator)op, stack);
			//braço direito
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

	// Compara dois operadores op1 e op2, para saber se op1 tem maior
	// prioridade que op1.
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

	/**
	 * Método que constroi a pila de operadores avaliavel à direita.
	 *
	 * O algorítmo:
	 *
	 * Temos três pilhas:
	 * mainStack - a pilha que será retornada
	 *   opStack - guarda temporariamente um operador enquanto carrega os demais operandos
	 *    lastOp - Guarda os últimos operadores
	 *
	 * 1 - Retire um token da fila e guarda em expr
	 *
	 * 2 - Se expr é um operador e difere de ")"
	 *     2.1 - adicione expr a opStack
	 *
	 * 2.2 - Se expr difere de "(" e tem maior prescedência que o topo de lastOp e o topo de mainStack e um operador
	 *     2.2.1 - Coloque topo de mainStack abaixo do atual topo de opStack
	 *     2.2.2 - Remova o topo de lastOp
	 *     2.2.3 - Volte para 2.2
	 *
	 * 2.3 - Volte para 1
	 *
	 * 3 - Se expr for um Token (indentificador ou literal) ou ")"
	 *     3.1 - Se expr for um Token (indentificador ou literal)
	 *         3.1.1 - Coloque em mainStack
	 *     3.2 - Se não, será ")"
	 *         3.2.1 - Remova o topo de opStack e coloque em lastOp
	 *
	 * 4 - Tente remover o máximo possível de operadores de opStack e coloca-los mainStack e lastOp.
	 * Pare somente se lastOp for vazio ou o topo de lastOp for "("
	 *
	 * 5 - Se a fila de token estiver vazia, retorne mainStack, senão, volte para 1
	 *
	 */
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

//	@Deprecated
//	private MyStack<Exp> buildStack_old(List<Exp> itens) {
//		MyStack<Exp> opStack = new MyStack<Exp>();
//		MyStack<Exp> execStack = new MyStack<Exp>();
//
//		for (int i = 0, len = itens.size(); i < len; i++) {
//			Exp expr = itens.get(i);
//
//			if (isOp(expr) && !isRParen(expr)) {
//				opStack.push(expr);
//				continue;
//			}
//
//			if (isToken(expr) || isRParen(expr)) {
//				if (isToken(expr)) {
//					execStack.push(expr);
//				} else { // isRParen(expr) -> true
//					Exp lp = opStack.peek();
//
//					if (isLParen(lp)) {
//						opStack.pop();
//					} else {
//						// throw exeption;
//					}
//				}
//
//				boolean test = true;
//				while (test) {
//					if (opStack.isEmpty() || isLParen(opStack.peek())) {
//						test = false;
//					} else {
//						execStack.push(opStack.pop());
//					}
//				}
//			}
//		}
//
//		return execStack;
//	}
	
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
