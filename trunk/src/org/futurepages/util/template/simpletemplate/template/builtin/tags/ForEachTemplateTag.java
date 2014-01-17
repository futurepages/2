package org.futurepages.util.template.simpletemplate.template.builtin.tags;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.BadExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedOperator;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.Unexpected;
import org.futurepages.util.template.simpletemplate.expressions.parser.Parser;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.template.AbstractTemplateBlock;
import org.futurepages.util.template.simpletemplate.template.TemplateBlock;
import org.futurepages.util.template.simpletemplate.template.builtin.customtagparams.ForEachArguments;
import org.futurepages.util.template.simpletemplate.template.builtin.customtagparams.NumericalList;
import org.futurepages.util.template.simpletemplate.template.builtin.customtagparams.ObjectArrayIterator;

/**
 *
 * @author thiago
 */
public class ForEachTemplateTag extends TemplateTag {
	// 1..10:1|var
	//                                                           1                  2            3   4              5   6
	private static final Pattern isBuildArray = Pattern.compile("([0-9]+|\\w+)\\.\\.([0-9]+|\\w+)(\\:([0-9]+|\\w+))?(\\|(\\w+))?");
	private static final int built_in_group_begin = 1;
	private static final int built_in_group_length = 2;
	private static final int built_in_group_step = 4;
	private static final int built_in_group_var = 6;
	
	// lista|var|counter
	//                                                     1    2          34   5      67   8
	private static final Pattern isList = Pattern.compile("(\\w+(\\.\\w+)*)((\\|(\\w+))((\\|(\\w+)))?)?");
	private static final int list_group_id = 1;
	private static final int list_group_var = 5;
	private static final int list_group_counter = 8;
	
	public ForEachTemplateTag() {
		super("forEach");
	}
	
	protected void fromBuildingArray(TemplateBlock block, Map<String, Object> params, ForEachArguments fparams, NumericalList nlist, StringBuilder sb) {
		Map<String, Object> ps = new HashMap<String, Object>(params);

		for (int el : nlist) {
			if (fparams.getVar() != null) {
				ps.put(fparams.getVar(), el);
			}
			
			block.getNextInner().eval(ps, sb);
		}
	}
	
	protected void fromList(TemplateBlock block, Map<String, Object> params, ForEachArguments fparams, Object result, StringBuilder sb) {

		if (result!= null) {
			
			Iterator it = null;
			
			if (result.getClass().isArray()) {
				it = new ObjectArrayIterator(result);
			} else if (result instanceof List) {
				it = ((List)result).iterator();
			}
			
			if (it != null) {
				Map<String, Object> ps = new HashMap<String, Object>(params);
				
				int i = 0;
				while (it.hasNext()) {
					Object el = it.next();

					if (fparams.getVar() != null) {
						ps.put(fparams.getVar(), el);
					}
					if (fparams.getCounter() != null) {
						ps.put(fparams.getCounter(), i);
					}
					
					block.getNextInner().eval(ps, sb);
					
					i += 1;
				}
			}
		}
	}
	
	protected Object builtInItem(String str) throws BadExpression, ExpectedExpression, ExpectedOperator, Unexpected {

		if (str != null && !str.isEmpty()) {
			try {
				return Integer.valueOf(str);
			} catch (NumberFormatException ex) {
				return new Parser(str).parse();
			}
		}

		return 1;
	}
	
	@Override
	public Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected {
		Matcher m1 = isBuildArray.matcher(expression);
		Matcher m2 = isList.matcher(expression);
		
		if (m1.find()) {
			Object begin = builtInItem(m1.group(built_in_group_begin));
			Object length = builtInItem(m1.group(built_in_group_length));
			Object step = builtInItem(m1.group(built_in_group_step));
			String var = m1.group(built_in_group_var);

			NumericalList nl = new NumericalList(begin, length, step);

			return new ForEachArguments(nl, var);
		} else if (m2.find()) {
			String list = m2.group(list_group_id);
			String var = m2.group(list_group_var);
			String counter = m2.group(list_group_counter);

			Exp exp = super.evalExpression(list);

			return new ForEachArguments(exp, var, counter);
		} else {
			// @TODO: melhorar esta mensagem
			throw new BadExpression("Invalid tag parameter");
		}
	}

	@Override
	public void eval(AbstractTemplateBlock block, Map<String, Object> params, StringBuilder sb) {
		TemplateBlock actualBlock = (TemplateBlock) block;
		ForEachArguments ps = (ForEachArguments) actualBlock.getParams();

		if (block.getNextInner() != null) {
			Object result = ps.eval(params);
			
			if (result instanceof NumericalList) {
				fromBuildingArray(actualBlock, params, ps, (NumericalList)result, sb);
			} else {
				fromList(actualBlock, params, ps, result, sb);
			}
		}

		if (block.getNext() != null) {
			block.getNext().eval(params, sb);
		}
	}

	@Override
	public TemplateTag getNewInstance() {
		return this;
	}
}
