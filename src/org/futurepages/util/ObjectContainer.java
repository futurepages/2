package org.futurepages.util;

public class ObjectContainer<VALUETYPE1, VALUETYPE2> {

	private VALUETYPE1 value1;
	private VALUETYPE2 value2;

	public ObjectContainer(VALUETYPE1 value1, VALUETYPE2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}

    public ObjectContainer() {
    }

	public VALUETYPE1 getValue1() {
		return value1;
	}

	public void setValue1(VALUETYPE1 value1) {
		this.value1 = value1;
	}

	public VALUETYPE2 getValue2() {
		return value2;
	}

	public void setValue2(VALUETYPE2 value2) {
		this.value2 = value2;
	}
}
