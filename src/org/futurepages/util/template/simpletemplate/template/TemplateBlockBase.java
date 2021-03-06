package org.futurepages.util.template.simpletemplate.template;

import java.util.HashMap;
import java.util.Map;

import org.futurepages.core.config.Params;
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

			if (getNextInnerElse() != null) {
				getNextInnerElse().toString(sb);
			}
		}

		if (getNext() != null) {
			getNextInner().toString(sb);
		}
	}

	public String eval(Map<String, Object> params) {
		TemplateWriter sb = new TemplateWriter(initialBufferSize);

		if (params instanceof ContextTemplateTag) {
			eval((ContextTemplateTag)params, sb);
		} else {
			eval(params, sb);
		}

		return sb.toString();
	}

	public void eval(Map<String, Object> params, TemplateWriter sb) {
		HashMap<String, Object> auxMap = new HashMap<String,Object>();
		Params.getParamsMap();
		for(String key : Params.getParamsMap().keySet()){
			auxMap.put(key, Params.get(key));
		}
		for(String key : params.keySet()){
			auxMap.put(key, params.get(key));
		}
		ContextTemplateTag context = new ContextTemplateTag(auxMap);
		eval(context, sb);
	}

	@Override
	public void eval(ContextTemplateTag context, TemplateWriter sb) {
		getNextInner().eval(context, sb);
	}
}
