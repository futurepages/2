package org.futurepages.util.template.simpletemplate.template.builtin.tags;

import java.util.HashMap;
import java.util.regex.Pattern;
import static org.futurepages.util.StringUtils.concat;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.BadExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedOperator;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.Unexpected;
import org.futurepages.util.template.simpletemplate.expressions.parser.Parser;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.template.AbstractTemplateBlock;
import org.futurepages.util.template.simpletemplate.template.TemplateWritter;
import org.futurepages.util.template.simpletemplate.template.exceptions.TemplateTagDoesNotExists;
import org.futurepages.util.template.simpletemplate.template.exceptions.TemplateWithSameNameAlreadyExistsException;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public abstract class TemplateTag {
	
	protected static final HashMap<String, TemplateTag> builtInTags = new HashMap<String, TemplateTag>();
	protected static final HashMap<String, TemplateTag> customTags = new HashMap<String, TemplateTag>();
	
	// Split as strings pelo caractere |. Se houver ||, não vai fazer o split nest ponto.
	private static Pattern splitParams = Pattern.compile("(?<!\\|)\\|(?!\\|)");

	public static final int SKIP_BODY = 0;
	public static final int EVAL_BODY = 1;
	
	protected static synchronized void addBuiltinTag(TemplateTag tag) {
		if (!builtInTags.containsKey(tag.getTagName())) {
			builtInTags.put(tag.getTagName(), tag);
		} else {
			String className = builtInTags.get(tag.getTagName()).getClass().getName();
			throw new TemplateWithSameNameAlreadyExistsException(concat("A built in TemplateTag whith the same name (", tag.getTagName(), ") already exists: ", className));
		}
	}
	
	public static synchronized void addCustomTag(TemplateTag tag) {
		if (builtInTags.containsKey(tag.getTagName())) {
			String className = builtInTags.get(tag.getTagName()).getClass().getName();
			throw new TemplateWithSameNameAlreadyExistsException(concat("A built in TemplateTag whith the same name (", tag.getTagName(), ") already exists: [", className, "]"));
		} else if (customTags.containsKey(tag.getTagName())) {
			String className = customTags.get(tag.getTagName()).getClass().getName();
			throw new TemplateWithSameNameAlreadyExistsException(concat("A custom TemplateTag whith the same name (", tag.getTagName(), ") already exists: [", className, "]"));
		}
		
		customTags.put(tag.getTagName(), tag);
	}
	
	public static TemplateTag getByTagName(String tagName) {
		TemplateTag tag;
		if (((tag = builtInTags.get(tagName)) != null) || ((tag = customTags.get(tagName)) != null)) {
			return tag;
		}

		throw new TemplateTagDoesNotExists(concat("There is no ", tagName, " registred."));
	}
	
	public static Exp defaultEvalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected {
		Parser p = new Parser(expression);
		return p.parse();
	}
	
	public static String []splitParams(String str) {
		String []params = splitParams.split(str);

		for (int i = 0, len = params.length; i < len; i++) {
			params[i] = params[i].trim();
		}

		return params;
	}
	
	private final String tagName;
	
	public TemplateTag(String tagName) {
		this.tagName = tagName;
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public abstract Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected;
	
	public abstract TemplateTag getNewInstance();
	
	public abstract boolean hasOwnContext();
	
	public void eval(AbstractTemplateBlock block, ContextTemplateTag context, TemplateWritter sb) {		
		int isDoBody = doBody(block, context, sb);
		AbstractTemplateBlock inner = block.getNextInner();

		switch (isDoBody) {
			case EVAL_BODY:
				if (inner != null) {
					boolean hasOwnContext;

					if (hasOwnContext = hasOwnContext()) {
						context.createNewContext();
					}

					evalBody(block, context, sb);

					if (hasOwnContext) {
						context.popContext();
					}
				}
				break;

			case SKIP_BODY:
				break;

			default:
				break;
		}

		AbstractTemplateBlock next = block.getNext();
		
		if (next != null) {
			next.eval(context, sb);
		}
	}
	
	public abstract int doBody(AbstractTemplateBlock block, ContextTemplateTag context, TemplateWritter sb);
	
	protected void evalBody(AbstractTemplateBlock block, ContextTemplateTag context, TemplateWritter sb) {
		AbstractTemplateBlock inner = block.getNextInner();

		if (inner != null) {
			inner.eval(context, sb);
		}
	}
}
