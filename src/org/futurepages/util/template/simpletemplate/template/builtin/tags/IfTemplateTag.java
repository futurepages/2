package org.futurepages.util.template.simpletemplate.template.builtin.tags;

import java.util.regex.Pattern;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.BadExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedOperator;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.FunctionDoesNotExists;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.Unexpected;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.template.AbstractTemplateBlock;
import org.futurepages.util.template.simpletemplate.template.TemplateBlock;
import org.futurepages.util.template.simpletemplate.template.TemplateWriter;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class IfTemplateTag extends TemplateTag {

	private static Pattern l_brackets = Pattern.compile("^\\[");
	private static Pattern r_brackets = Pattern.compile("\\]$");

	public IfTemplateTag() {
		super("if");
	}

	protected boolean isBool(Object ob) {
		return ob instanceof Boolean;
	}

	protected boolean isNumber(Object obj) {
		return obj instanceof Number;
	}

	protected boolean isString(Object obj) {
		return obj instanceof String;
	}

	protected boolean isZero(Number obj) {
		if (obj instanceof Integer) {
			return (Integer)obj == 0;
		} else if (obj instanceof Long) {
			return (Long)obj == 0;
		} else if (obj instanceof Short) {
			return (Short)obj == 0;
		} else if (obj instanceof Byte) {
			return (Byte)obj == 0;
		} else if (obj instanceof Float) {
			return (Float)obj == 0.0f;
		} else { // Double
			return (Double)obj == 0.0d;
		}
	}

	protected boolean isEmptyStr(Object obj) {
		return ((String)obj).isEmpty();
	}

	@Override
	public Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected, FunctionDoesNotExists {
		String ps = l_brackets.matcher(expression).replaceFirst("");
		ps = r_brackets.matcher(ps).replaceFirst("");

		return defaultEvalExpression(ps);
	}

	@Override
	public int doBody(AbstractTemplateBlock block, ContextTemplateTag context, TemplateWriter sb) {
		TemplateBlock actualBlock = (TemplateBlock) block;

		Exp exp = actualBlock.getParams();

		Object t = exp.eval(context);

		boolean test = (t != null) && ((isNumber(t) && !isZero((Number)t)) || (isString(t) && !isEmptyStr((String)t)) || (isBool(t) && ((Boolean)t)));

		return test ? EVAL_BODY : EVAL_ELSE;
	}

	@Override
	public TemplateTag getNewInstance() {
		return this;
	}

	@Override
	public boolean hasOwnContext() {
		return false;
	}
}
