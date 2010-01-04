package org.futurepages.test.mockiness;

import net.sf.classmock.ClassMock;


public class ClassMockFactory {

	private static ClassMockFactory instance;

	private ClassMockFactory() {}

	public static ClassMockFactory getInstance() {
		if (instance == null) {
			instance = new ClassMockFactory();
		}
		return instance;
	}
	
	public ClassMock createClass(String name){
		ClassMock classMock  = new ClassMock(name,false);
		return classMock;
	}
}
