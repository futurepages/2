package org.futurepages.util.iterator.months;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.futurepages.util.CalendarUtil;

/**
 *  
 * @author Leandro Santana
 */
public class MonthYear implements Comparable<MonthYear> {

	private int month;
	private int year;

	public MonthYear(){
	}

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

	@Override
	public int compareTo(MonthYear that) {
		Calendar calThis = new GregorianCalendar(this.year,this.month-1,1);
		Calendar calThat = new GregorianCalendar(that.getYear(),that.getMonth()-1,1);
		return CalendarUtil.compareCalendarDate(calThis, calThat);
	}
}
