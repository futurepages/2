package org.futurepages.util.template.simpletemplate.expressions.operators.unevaluable;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.Operator;


/**
 *
 * @author thiago
 */
public abstract class Parenthesis extends Operator {

	@Override
	public Object eval(Map<String, Object> params) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
