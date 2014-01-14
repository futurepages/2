package org.futurepages.util.template.simpletemplate.util;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thiago
 */
public class MyStack<T> {
	
	private List<T> stack;
	
	public MyStack() {
		stack = new ArrayList<T>();
	}

	public MyStack(MyStack other) {
		stack = new ArrayList<T>(other.stack);
	}

	public MyStack(List<T> list) {
		stack = new ArrayList<T>(list);
	}
	
	public T pop() {
		int len = stack.size();
		return (len > 0) ? stack.remove(len - 1) : null;
	}

	public T peek() {
		int len = stack.size();
		return (len > 0) ? stack.get(len - 1) : null;
	}

	public MyStack<T> push(T item) {
		stack.add(item);
		return this;
	}
	
	public int size() {
		return stack.size();
	}
	
	public void clear() {
		stack.clear();
	}
	
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	public List<T> getList() {
		return stack;
	}
}
