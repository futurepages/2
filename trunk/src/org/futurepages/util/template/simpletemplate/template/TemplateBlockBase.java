package org.futurepages.util.template.simpletemplate.template;

import java.util.Map;

/**
 *
 * @author thiago
 */
public class TemplateBlockBase extends AbstractTemplateBlock {
	
	public TemplateBlockBase() {
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
		TemplateWritter sb = new TemplateWritter();		
		eval(params, sb);
		
		return sb.toString();
	}

	@Override
	public void eval(Map<String, Object> params, TemplateWritter sb) {
		getNextInner().eval(params, sb);
	}
}
