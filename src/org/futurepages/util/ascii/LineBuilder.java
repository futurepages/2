package org.futurepages.util.ascii;

import java.util.LinkedList;
import java.util.List;

public class LineBuilder extends Builder<BodyBuilder>{

	List<String> line = new LinkedList<String>();

	public LineBuilder(BodyBuilder tb) {
		super(tb);
	}

	public LineBuilder addCell(String cell){
		this.line.add(cell);
		return this;
	}

	public int getSpan(int col) {
		return line.get(col).length();
	}

}