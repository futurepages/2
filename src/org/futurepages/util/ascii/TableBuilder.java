package org.futurepages.util.ascii;


public class TableBuilder implements IBuilder {

	private HeadBuilder headB;
	private BodyBuilder bodyB;


	public static TableBuilder init(){
		TableBuilder tb = new TableBuilder();
		return tb;
	}

	public HeadBuilder head(){
		if(this.headB == null){
			headB =new HeadBuilder(this);
		}
		return headB;
	}

	public BodyBuilder body(){
		if(bodyB == null){
			bodyB = new BodyBuilder(this);
		}
		return bodyB;
	}

	int[] getSpans(){
		int[] spans = new int[headB.columns.size()];
		for (int col = 0; col < headB.columns.size(); col++) {
			int span = maior(headB.getSpan(col), bodyB.getSpan(col));
			spans[col] = span;
		}
		return spans;
	}

	private int maior(int a, int b){
		if(a - b> 0){
			return a;
		}
		return b;
	}

	public void print() {
		String press = new TablePrinter(this).toString();
		System.out.println(press);
	}


}
