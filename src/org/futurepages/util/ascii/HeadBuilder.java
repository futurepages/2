package org.futurepages.util.ascii;

import java.util.LinkedList;
import java.util.List;

public class HeadBuilder extends Builder<TableBuilder>{

	List<String> columns = new LinkedList<String>();

	public HeadBuilder(TableBuilder tb) {
		super(tb);
	}

	public HeadBuilder addColumn(String col){
		columns.add(col);
		return this;
	}

	public int getSpan(int col) {
		return columns.get(col).length();
	}

}