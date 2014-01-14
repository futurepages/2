package org.futurepages.util.template.simpletemplate.expressions.operators.logical;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;

/**
 *
 * @author thiago
 */
public class GreaterThan extends BinaryOperator {
	
	@Override
	public String toString() {
		return ">";
	}

	@Override
	public Object eval(Map<String, Object> params) {
		Object left = getLeft().eval(params);
		Object right = getRight().eval(params);

		return !LessEqualsThan.execute(left, right);
	}
}
