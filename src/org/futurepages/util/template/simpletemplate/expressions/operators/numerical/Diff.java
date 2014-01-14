package org.futurepages.util.template.simpletemplate.expressions.operators.numerical;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.Const;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.NumHandle;

/**
 *
 * @author thiago
 */
public class Diff extends BinaryOperator {
	
	private static Number execute(Object l, Object r) {
		if (isNum(l) && isNum(r)) {
			if (NumHandle.isInfinity(l)) {
				return NumHandle.isInfinity(r)
					? (l.equals(r) ? Const.NAN : (Number) l)
					: ((Number) l);
			} else if (NumHandle.isInfinity(r)) {
				return (Number) r;
			} else {
				return diff(l, r);
			}
		} else {
			return Const.NAN;
		}
	}

	protected static Number diff(Object l, Object r) {
		Number [] nums = NumHandle.toLongOrDouble(l, r);
		
		if (NumHandle.isDouble(nums[0])) {
			return ((Double)nums[0]) - ((Double)nums[1]);
		} else {
			return ((Long)nums[0]) - ((Long)nums[1]);
		}
	}

	@Override
	public Object eval(Map<String, Object> params) {
		Object left = getLeft().eval(params);
		Object right = getRight().eval(params);

		return execute(left, right);
	}

	@Override
	public String toString() {
		return "-";
	}
}
