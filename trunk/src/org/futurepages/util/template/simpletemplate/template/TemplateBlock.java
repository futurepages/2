package org.futurepages.util.template.simpletemplate.template;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.template.builtin.tags.TemplateTag;

/**
 *
 * @author thiago
 */
public class TemplateBlock extends AbstractTemplateBlock {
	
	private TemplateTag tag;
	private Exp params;

	public TemplateBlock() {
	}

	public TemplateTag getTag() {
		return tag;
	}

	public void setTag(TemplateTag tag) {
		this.tag = tag;
	}

	public Exp getParams() {
		return params;
	}

	public void setParams(Exp params) {
		this.params = params;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		toString(sb);
		return sb.toString();
	}
	
	@Override
	public void toString(StringBuilder sb) {
		if (getNextInner() != null) {
			sb.append("<!--").append(tag.getTagName()).append(" ").append(params).append("-->");
			getNextInner().toString(sb);
			sb.append("<!--end").append(tag.getTagName()).append("-->");
		}

		if (getNext() != null) {
			getNext().toString(sb);
		}
	}

	@Override
	public void eval(Map<String, Object> params, StringBuilder sb) {
		tag.eval(this, params, sb);
	}
}
