package org.futurepages.util.template.simpletemplate.template;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.expressions.tree.Exp;
import org.futurepages.util.template.simpletemplate.template.exceptions.TemplateException;

/**
 *
 * @author thiago
 */
public class TemplateStatic extends AbstractTemplateBlock {
	
	private Object []content;
	
	public TemplateStatic() {
	}
	
	@Override
	@Deprecated
	public void setNextInner(AbstractTemplateBlock nextInner) {
		super.setNextInner(null);
	}

	public Object []getContent() {
		return content;
	}

	public void setContent(String content) throws TemplateException {
		this.content = TemplateParser.getContent(content);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for (Object ob : content) {
			if (ob instanceof Exp) {
				sb.append("${");
				((Exp)ob).toString(sb);
				sb.append("}");
			} else {
				sb.append(ob);
			}
		}

		return sb.toString();
	}
	
	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.toString());

		if (getNext() != null) {
			getNext().toString(sb);
		}
	}

	// Falta ajeitar essa onça
	@Override
	public void eval(Map<String, Object> params, StringBuilder sb) {
		for (Object ob : content) {
			if (ob instanceof Exp) {
				sb.append(((Exp)ob).eval(params));
			} else {
				sb.append(ob);
			}
		}

		if (getNext() != null) {
			getNext().eval(params, sb);
		}
	}
}
