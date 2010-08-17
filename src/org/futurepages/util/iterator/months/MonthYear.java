package org.futurepages.util.iterator.months;
/**
 *  
 * @author Leandro Santana
 */
public class MonthYear {

	private int month;
	private int year;

	public MonthYear(int month, int year) {
		this.month = month;
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public MonthYear getNext() {
		int nextMonth;
		int nextYear = year;
		if (month != 12) {
			nextMonth = month+1;
		} else {
			nextYear = year+1;
			nextMonth = 1;
		}
		return new MonthYear(nextMonth, nextYear);
	}
}
