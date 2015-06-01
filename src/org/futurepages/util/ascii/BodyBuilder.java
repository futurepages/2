package org.futurepages.util.ascii;

import java.util.LinkedList;
import java.util.List;

public class BodyBuilder extends Builder<TableBuilder> implements IBuilder{

	List<LineBuilder> lbs;
	public BodyBuilder(TableBuilder tb) {
		super(tb);
		this.lbs = new LinkedList<LineBuilder>();
	}

	public LineBuilder addLine() {
		LineBuilder lb = new LineBuilder(this);
		this.lbs.add(lb);
		return lb;
	}

	public int getSpan(int col) {
		int maior = 0;
		for (LineBuilder lb : lbs) {
			int desafiante = lb.getSpan(col);
			if(desafiante> maior){
				maior = desafiante;
			}
		}
		return maior;
	}

}