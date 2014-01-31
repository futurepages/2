package org.futurepages.util.template.simpletemplate.expressions.operators.numerical;

import org.futurepages.util.template.simpletemplate.expressions.operators.core.UnaryOperator;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.Const;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.Infinity;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.NumHandle;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;


/**
 *
 * @author thiago
 */
public class Negative extends UnaryOperator {

	public static Object execute(Object param) {
		if (isNum(param)) {
			return !NumHandle.isInfinity(param)
				? negative(param)
				: ((Infinity)param).isNegative() ? Const.INFINITY : Const._INFINITY;
		} else {
			return Const.NAN;
		}
	}

	protected static Number negative(Object param) {
		Number n = NumHandle.toLongOrDouble(param);
		
		if (NumHandle.isDouble(n)) {
			return -((Double)n);
		} else { // Long
			return -((Long)n);
		}
	}

	@Override
	public Object eval(ContextTemplateTag context) {
		Object param = getParam().eval(context);
		
		return execute(param);
	}
	
	@Override
	public String toString() {
		return "-";
	}
}
