package org.futurepages.core.persistence;
/**
 *
 * @author Thiago
 */
public class ObjectContainer<KEYTYPE, VALUETYPE> {

	private KEYTYPE key;
	private VALUETYPE value;

	public ObjectContainer(KEYTYPE key, VALUETYPE value) {
		this.key = key;
		this.value = value;
	}

	public KEYTYPE getKey() {
		return key;
	}

	public void setKey(KEYTYPE key) {
		this.key = key;
	}

	public VALUETYPE getValue() {
		return value;
	}

	public void setValue(VALUETYPE value) {
		this.value = value;
	}
}
