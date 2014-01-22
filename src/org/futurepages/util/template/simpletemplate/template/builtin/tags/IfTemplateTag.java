package org.futurepages.util.template.simpletemplate.template.builtin.tags;

import java.util.Map;
import java.util.regex.Pattern;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.BadExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedOperator;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.Unexpected;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.template.AbstractTemplateBlock;
import org.futurepages.util.template.simpletemplate.template.TemplateBlock;
import org.futurepages.util.template.simpletemplate.template.TemplateWritter;

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
	
	@Override
	public Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected {
		String ps = l_brackets.matcher(expression).replaceFirst("");
		ps = r_brackets.matcher(ps).replaceFirst("");
		
		return super.evalExpression(ps);
	}

	@Override
	public int doBody(AbstractTemplateBlock block, Map<String, Object> params, TemplateWritter sb) {
		TemplateBlock actualBlock = (TemplateBlock) block;

		Exp exp = actualBlock.getParams();

		Object t = exp.eval(params);

		boolean test = (t != null) && (!isBool(t) || ((Boolean)t));

		return test ? EVAL_BODY : SKIP_BODY;
	}

	@Override
	public TemplateTag getNewInstance() {
		return this;
	}
}
