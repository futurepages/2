package org.futurepages.util.template.simpletemplate.template;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.util.ContextTemplateTag;

/**
 *
 * @author thiago
 */
public class TemplateBlockBase extends AbstractTemplateBlock {
	
	private int initialBufferSize;
	
	public TemplateBlockBase() {
		initialBufferSize = 128;
	}

	public TemplateBlockBase(int initialBufferSize) {
		this.initialBufferSize = initialBufferSize;
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
			getNextInner().toString(sb);
		}

		if (getNext() != null) {
			getNextInner().toString(sb);
		}
	}

	public String eval(Map<String, Object> params) {
		TemplateWritter sb = new TemplateWritter(initialBufferSize);
		
		if (params instanceof ContextTemplateTag) {
			eval((ContextTemplateTag)params, sb);
		} else {
			eval(params, sb);
		}
		
		return sb.toString();
	}

	public void eval(Map<String, Object> params, TemplateWritter sb) {
		ContextTemplateTag context = new ContextTemplateTag(params);
		eval(context, sb);
	}

	@Override
	public void eval(ContextTemplateTag context, TemplateWritter sb) {
		getNextInner().eval(context, sb);
	}
}
