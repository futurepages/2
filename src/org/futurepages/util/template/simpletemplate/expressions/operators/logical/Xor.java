package org.futurepages.util.template.simpletemplate.expressions.operators.logical;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;


/**
 *
 * @author thiago
 */
public class Xor extends BinaryOperator {

	@Override
	public Object eval(Map<String, Object> params) {
		Object left = getLeft().eval(params);
		Object right = getRight().eval(params);
		
		return execute(left, right);
	}
	
	public static Object execute(Object left, Object right) {
		if (isBool(left) && isBool(right)) {
				return ((Boolean)left) ^ ((Boolean)right);
		} else {
			Object l = And.execute(left, Not.execute(right));
			Object r = And.execute(Not.execute(left), right);

			return Or.execute(l, r);
		}
	}

	@Override
	public String toString() {
		return "^";
	}
}
