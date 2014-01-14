package org.futurepages.util.template.simpletemplate.expressions.operators.logical;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;

/**
 *
 * @author thiago
 */
public class LessEqualsThan extends BinaryOperator {
	
	public static boolean execute(Object l, Object r) {
		return LessThan.execute(l, r) || Equals.execute(l, r);
	}

	@Override
	public Object eval(Map<String, Object> params) {
		Object left = getLeft().eval(params);
		Object right = getRight().eval(params);

		return execute(left, right);
	}

	@Override
	public String toString() {
		return "<=";
	}
}
