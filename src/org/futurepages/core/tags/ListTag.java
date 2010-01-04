package org.futurepages.core.tags;

import org.futurepages.core.tags.cerne.AbstractListTag;

public abstract class ListTag extends AbstractListTag {

	@Override
	protected String getName() {
		return this.getClass().getSimpleName();
	}
}