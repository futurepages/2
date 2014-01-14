package org.futurepages.util.template.simpletemplate.expressions.parser;

import java.util.List;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.BadExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedOperator;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.Unexpected;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.Operator;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.UnaryOperator;
import org.futurepages.util.template.simpletemplate.expressions.operators.unevaluable.LParenthesis;
import org.futurepages.util.template.simpletemplate.expressions.operators.unevaluable.Parenthesis;
import org.futurepages.util.template.simpletemplate.expressions.operators.unevaluable.RParenthesis;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.expressions.tree.Token;
import org.futurepages.util.template.simpletemplate.util.Tuple;


/**
 *
 * @author thiago
 */
public class Semantic {

	private List<Exp> exps;
	private String expression;
	private List<Tuple<String, Integer>> tokens;
	
	public Semantic(List<Exp> exps, String expression, List<Tuple<String, Integer>> tokens) {
		this.exps = exps;
		this.expression = expression;
		this.tokens = tokens;
	}
	
	public void analise() throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected {
		int parenthesisCounter = 0;

		for (int i = 0, len = exps.size(); i < len; i++) {
			Exp exp = exps.get(i);
			
			if (isToken(exp)) {
				token(exps, exp, i);
			} else if (isBinary(exp)) {
				binary(exps, exp, i);
			} else if (isUnary(exp)) {
				unary(exps, exp, i);
			} else if (isLParen(exp)) {
				parenthesisCounter += 1;
			} else if (isRParen(exp)) {
				parenthesisCounter -= 1;
				
				if (parenthesisCounter < 0) {
					throw new Unexpected(expression, tokens.get(i).getB(), "Unexpected ", i + 1, "º token \")\"");
				}
			} //else {
				// ??
			//}
		}
		
		if (parenthesisCounter > 0) {
			throw new ExpectedExpression(expression, expression.length(), "Missing \")\"");
		}
	}
	
	public boolean isBinary(Exp exp) {
		return exp instanceof BinaryOperator;
	}

	public boolean isUnary(Exp exp) {
		return exp instanceof UnaryOperator;
	}

	public boolean isToken(Exp exp) {
		return exp instanceof Token;
	}
	
	public boolean isOperator(Exp exp) {
		return exp instanceof Operator;
	}

	public boolean isParenthesis(Exp exp) {
		return exp instanceof Parenthesis;
	}

	public boolean isLParen(Exp exp) {
		return exp instanceof LParenthesis;
	}

	public boolean isRParen(Exp exp) {
		return exp instanceof RParenthesis;
	}

	private void token(List<Exp> exps, Exp exp, int i) throws ExpectedOperator {

		if (!(i == exps.size() - 1)) {
			int nxtIdx = i + 1;
			Exp next = exps.get(nxtIdx);

			if (!isBinary(next) && !isRParen(next)) {
				throw new ExpectedOperator(expression, tokens.get(i).getB(), "Expected operator after ", i + 1, "º token (", exp.toString(), ")");
			}
		}
	}

	private void binary(List<Exp> exps, Exp exp, int i) throws ExpectedExpression, BadExpression {
		if (i == 0) {
			throw new ExpectedExpression(expression, tokens.get(i).getB(), "Expected left expression before ", i + 1, "º token (", exp.toString(), ")");
		}

		if (i == exps.size() - 1) {
			throw new ExpectedExpression(expression, tokens.get(i).getB(), "Expected right expression before ", i + 1, "º token (", exp.toString(), ")");
		}

		int prvIdx = i - 1;
		int nxtIdx = i + 1;

		Exp prev = exps.get(prvIdx);
		Exp next = exps.get(nxtIdx);

		if (!isToken(prev) && !isRParen(prev)) {
			throw new BadExpression(expression, tokens.get(i).getB(), "Bad expression before ", i + 1, "º token (", exp.toString(), ")");
		}
		
		if (!isToken(next) && !isLParen(next) && !isUnary(next)) {
			throw new BadExpression(expression, tokens.get(i).getB(), "Bad expression after ", i + 1, "º token (", exp.toString(), ")");
		}
	}

	private void unary(List<Exp> exps, Exp exp, int i) throws ExpectedExpression, Unexpected {
		if (i == exps.size() - 1) {
			throw new ExpectedExpression(expression, tokens.get(i).getB(), "Expected right expression before ", i + 1, "º token (", exp.toString(), ")");
		}
		
		int nxtIdx = i + 1;
		Exp next = exps.get(nxtIdx);
		
		if (!isUnary(next) && !isLParen(next) && !isToken(next)) {
			throw new Unexpected(expression, tokens.get(i).getB(), "Unexpected ", i + 1, "º token (", exps.toString(), ")");
		}
	}
}
