package org.futurepages.util.template.simpletemplate.expressions.tree;

import java.util.Map;
import org.futurepages.util.template.simpletemplate.util.GetFromMap;

/**
 *
 * @author thiago
 */
public class Identifier implements Token {
	//@TODO: Fazer pegar o valor no mapa!
	
	private String id;
	
	public Identifier() {
	}

	public Identifier(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Object eval(Map<String, Object> params) {
		return GetFromMap.getValue(id, params);
	}
	
	@Override
	public String toString() {
		return id;
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(id);
	}
}
