package org.futurepages.util.template.simpletemplate.expressions.operators.unevaluable;

import org.futurepages.util.template.simpletemplate.expressions.operators.core.Operator;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class Comma extends Operator {
	
	@Override
	public String toString() {
		return ",";
	}

	@Override
	public void toString(StringBuilder sb) {
	}
	
	@Override
	public Object eval(ContextTemplateTag context) {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
