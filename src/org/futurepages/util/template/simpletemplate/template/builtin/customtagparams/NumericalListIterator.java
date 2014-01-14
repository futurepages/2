package org.futurepages.util.template.simpletemplate.template.builtin.customtagparams;

import java.util.Iterator;
import org.futurepages.util.StringUtils;

/**
 *
 * @author thiago
 */
public class NumericalListIterator implements Iterator<Integer> {

	private final NumericalList numList;
	private int current;
	private boolean started;
	private int incr;
	
	public NumericalListIterator(NumericalList numList) {
		this.numList = numList;
		current = numList.getStart();
		started = false;
		
		if (numList.getEnd() > numList.getStart()) {
			incr = Math.abs(numList.getIncrement());
		} else {
			incr = -Math.abs(numList.getIncrement());
		}
	}
	
	@Override
	public boolean hasNext() {
		return (numList.size() > 0) && (!started || current < (numList.getEnd() - 1));
	}

	@Override
	public Integer next() {
		if (hasNext()) {
			if (started) {
				current += incr;
			} else {
				started = true;
			}

			return current;
		}

		throw new RuntimeException(StringUtils.concat("Iterating over the limit of list [", numList.getStart(), "..", numList.getEnd(), "]"));
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
