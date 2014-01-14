package org.futurepages.util.template.simpletemplate.expressions.operators.logical;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.operators.core.BinaryOperator;
import org.futurepages.util.template.simpletemplate.expressions.primitivehandle.NumHandle;

/**
 *
 * @author thiago
 */
public class Equals extends BinaryOperator {
	
	@Override
	public String toString() {
		return "==";
	}
	
	public static boolean execute(Object left, Object right) {
		if (left != null) {
			if (right != null) {
				if (isNum(left) && isNum(right)) {
					Number [] nums = NumHandle.toLongOrDouble(left, right);

					return nums[0].equals(nums[1]);
				} else {
					return left.equals(right);
				}
			} else {
				return false;
			}
		} else {
			if (right != null) {
				return false;
			} else {
				return true;
			}
		}
	}

	@Override
	public Object eval(Map<String, Object> params) {
		Object left = getLeft().eval(params);
		Object right = getRight().eval(params);

		return execute(left, right);
	}
}
