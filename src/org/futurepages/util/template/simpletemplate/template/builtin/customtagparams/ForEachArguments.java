package org.futurepages.util.template.simpletemplate.template.builtin.customtagparams;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;

/**
 *
 * @author thiago
 */
public class ForEachArguments implements Exp {

	private final Exp exp;
	private final NumericalList iterable;
	private final String var;
	private final String counter;
	
	public ForEachArguments(Exp exp, String var, String counter) {
		this.exp = exp;
		this.var = var;
		this.counter = counter;

		this.iterable = null;
	}

	public ForEachArguments(NumericalList iterable, String var) {
		this.iterable = iterable;
		this.var = var;

		this.exp = null;
		this.counter = null;
	}

	public NumericalList getIterable() {
		return iterable;
	}

	public String getVar() {
		return var;
	}

	public String getCounter() {
		return counter;
	}

	public Exp getExp() {
		return exp;
	}

	@Override
	public Object eval(Map<String, Object> params) {
		if (exp != null) {
			return exp.eval(params);
		} else {
			return iterable.eval(params);
		}
	}

	@Override
	public void toString(StringBuilder sb) {
		if (exp != null) {
			exp.toString(sb);
		} else {
			sb.append(iterable.toString());
		}
	}
}
