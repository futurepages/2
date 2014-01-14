package org.futurepages.util.template.simpletemplate.expressions.tree;

import java.util.Map;

/**
 *
 * @author thiago
 */
public interface Exp {

	public Object eval(Map<String, Object> params);

	public void toString(StringBuilder sb);
}
