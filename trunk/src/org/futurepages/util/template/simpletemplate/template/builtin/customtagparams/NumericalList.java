package org.futurepages.util.template.simpletemplate.template.builtin.customtagparams;

import java.util.Iterator;
import org.futurepages.util.StringUtils;

/**
 *
 * @author thiago
 */
public class NumericalList implements Iterable<Integer> {

	private final int start;
	private final int end;
	private final int increment;
	private final int size;
		
	public NumericalList(int start, int end, int increment) {
		this.start = start;
		this.end = end;
		this.increment = increment;
		
		if (end > start) {
			size = end - start;
		} else {
			size = start - end;
		}
	}

	public NumericalList(int start, int end) {
		this(start, end, 1);
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getIncrement() {
		return increment;
	}
	
	public int size() {
		return size;
	}

	@Override
	public Iterator<Integer> iterator() {
		return new NumericalListIterator(this);
	}
	
	@Override
	public String toString() {
		return StringUtils.concat("[", start, "..", end, "|", increment, "]");
	}
}
