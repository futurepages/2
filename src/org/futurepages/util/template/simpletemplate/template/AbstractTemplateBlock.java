package org.futurepages.util.template.simpletemplate.template;

import java.util.Map;

/**
 *
 * @author thiago
 */
public abstract class AbstractTemplateBlock {
	
	private AbstractTemplateBlock next;
	private AbstractTemplateBlock nextInner;
	
	public AbstractTemplateBlock() {
	}

	public AbstractTemplateBlock getNext() {
		return next;
	}

	public void setNext(AbstractTemplateBlock next) {
		this.next = next;
	}

	public void putInEnd(AbstractTemplateBlock last) {
		AbstractTemplateBlock item = this;

		while (item.getNext() != null) {
			item = item.getNext();
		}

		item.setNext(last);
	}
	
	public void append(AbstractTemplateBlock other) {
		if (this.getNextInner() != null) {
			this.getNextInner().putInEnd(other);
		} else {
			this.setNextInner(other);
		}
	}

	public AbstractTemplateBlock getNextInner() {
		return nextInner;
	}

	public void setNextInner(AbstractTemplateBlock nextInner) {
		this.nextInner = nextInner;
	}
	
	@Override
	public abstract String toString();
	
	public abstract void toString(StringBuilder sb);

	public abstract void eval(Map<String, Object> params, StringBuilder sb);
}
