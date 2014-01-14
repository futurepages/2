package org.futurepages.util.template.simpletemplate.template.builtin.tags;

import java.util.HashMap;
import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.BadExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedExpression;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.ExpectedOperator;
import org.futurepages.util.template.simpletemplate.expressions.exceptions.Unexpected;
import org.futurepages.util.template.simpletemplate.expressions.parser.Parser;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.template.AbstractTemplateBlock;
import org.futurepages.util.template.simpletemplate.template.exceptions.TemplateTagDoesNotExists;
import org.futurepages.util.template.simpletemplate.template.exceptions.TemplateWithSameNameAlreadyExistsException;
import static org.futurepages.util.StringUtils.concat;

/**
 *
 * @author thiago
 */
public abstract class TemplateTag {
	
	protected static final HashMap<String, TemplateTag> builtInTags = new HashMap<String, TemplateTag>();
	protected static final HashMap<String, TemplateTag> customTags = new HashMap<String, TemplateTag>();
	
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
	
	private final String tagName;
	
	public TemplateTag(String tagName) {
		this.tagName = tagName;
	}
	
	public String getTagName() {
		return tagName;
	}
	
	public Exp evalExpression(String expression) throws ExpectedOperator, ExpectedExpression, BadExpression, Unexpected {
		Parser p = new Parser(expression);
		return p.parse();
	}
	
	public abstract TemplateTag getNewInstance();
	
	public abstract void eval(AbstractTemplateBlock block, Map<String, Object> params, StringBuilder sb);
}
