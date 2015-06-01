package org.futurepages.util.ascii;

import java.util.List;

public class TablePrinter {

	private static final String LINE_BREAK = "\n";
	private static String TOP_LEFT = "┌";
	private static String TOP_RIGTH = "┐";
	private static String TOP_MIDDLE = "┬";
	private static String BOTTON_MIDDLE = "┴";

	private static String BOTTON_LEFT = "└";
	private static String BOTTON_RIGTH= "┘";
	private static String MIDDLE_LEFT = "├";
	private static String MIDDLE_RIGTH = "┤";
	private static String BRICK_LINE = "─";
	private static String BRICK_COLUMN= "│";
	private static String CROSS= "┼";

	int[] spans;
	public StringBuilder buff;
	public TableBuilder tb;

	public TablePrinter(TableBuilder tb) {
		this.buff = new StringBuilder();
		this.tb = tb;
		this.spans= tb.getSpans();
	}

	public String toString(){
		printhead(tb.head());
		printbody(tb.body());
		return buff.toString();
	}

	private void printhead(HeadBuilder head) {
		printTopBorderLine();
		printline(head.columns);
		printDivLine();
	}

	private void printbody(BodyBuilder body) {
		for (LineBuilder lb : body.lbs) {
			printline(lb.line);
		}
		printBottonBorderLine();
	}


	public void printTopBorderLine(){
		for (int i = 0; i < spans.length; i++) {
			if(i == 0 ){
				printCell(true, TOP_LEFT , spans[i], bricksLine(spans[i]));
			}else if (i == spans.length-1){
				printCell(true, TOP_MIDDLE, 0, "");
				printCell(false, TOP_RIGTH, spans[i], bricksLine(spans[i]));
			}else{

				printCell(true,TOP_MIDDLE, spans[i], bricksLine(spans[i]));
			}

		}
		endLine();
	}

	public void printBottonBorderLine(){
		for (int i = 0; i < spans.length; i++) {
			if(i == 0 ){
				printCell(true, BOTTON_LEFT , spans[i], bricksLine(spans[i]));
			}else if (i == spans.length-1){
				printCell(true, BOTTON_MIDDLE, 0, "");
				printCell(false, BOTTON_RIGTH, spans[i], bricksLine(spans[i]));
			}else{

				printCell(true,BOTTON_MIDDLE, spans[i], bricksLine(spans[i]));
			}

		}
		endLine();
	}

	public void printDivLine(){
		for (int i = 0; i < spans.length; i++) {
			if(i == 0 ){
				printCell(true, MIDDLE_LEFT , spans[i], bricksLine(spans[i]));
			}else if (i == spans.length-1){
				printCell(true, CROSS, 0, "");
				printCell(false, MIDDLE_RIGTH, spans[i], bricksLine(spans[i]));
			}else{

				printCell(true,CROSS, spans[i], bricksLine(spans[i]));
			}

		}
		endLine();
	}

	private void endLine(){
		this.buff.append(LINE_BREAK);
	}

	private String bricksLine(int total){
		String xar = BRICK_LINE;
		return bricks(total, xar);
	}

	private String bricks(int total, String xar) {
		String bricks = "";
		for (int j = 0; j < total; j++) {
			bricks+=xar;
		}
		return bricks;
	}

	private void printCell(boolean first,String separator, int span, String value) {
		if(first){
			this.buff.append(separator);
		}

		this.buff.append(value);
		int resto = span-value.length();
		for (int i = 0; i < resto; i++) {
			this.buff.append(" ");
		}
		if(!first){
			this.buff.append(separator);
		}
	}


	public void printline(List<String> line) {
		for (int i = 0; i < spans.length; i++) {
			int span = spans[i];
			printCell(true, BRICK_COLUMN, span, line.get(i));
		}
		printCell(false, BRICK_COLUMN, 0, "");
		endLine();
	}

}
