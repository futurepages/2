package org.futurepages.test.factory;

import java.util.ArrayList;
import java.util.List;

public class ListFactory {

	public static ListFactory instance = new ListFactory();
	
	private ListFactory() {
		super();
	}

	public List<String> createListOfString(int size){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			list.add(StringFactory.getRandom()+i);
		}
		return list;
	}
}
