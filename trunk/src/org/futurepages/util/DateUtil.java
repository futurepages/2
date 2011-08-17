package org.futurepages.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.futurepages.enums.DateFormatEnum;
import org.futurepages.enums.MonthEnum;

/**
 * Rotinas �teis de manipula��o de datas String
 */
public class DateUtil {

	public static String dbDate(int ano, int mes, int dia) {
		return ano + "-" + mes + "-" + dia;
	}

	/**
	 * Converte datas no formato DD/MM/AAAA para o formato por extenso em l�ngua portuguesa.
	 * Obs.: converte somente datas entre os anos de  1500 a 2099
	 *
	 * @param date - a data de entrada no formato DD/MM/AAAA
	 * @return - a data por extenso em caracteres todos mn�sculos.
	 * @throws java.lang.Exception
	 */
	public static String literal(String date) throws Exception {
		return NumberUtil.dezenaPorExtenso(date.substring(0, 2))
				+ " de "
				+ MonthEnum.get(Integer.parseInt(date.substring(3, 5)))
				+ " de "
				+ NumberUtil.milharPorExtenso(date.substring(6, 10));
	}

	/**
	 * Converte datas no formato DD/MM/AAAA para o formato por extenso em l�ngua portuguesa.
	 * Obs.: converte somente datas entre os anos de  1500 a 2099
	 *
	 * @param date - a data de entrada no formato DD/MM/AAAA
	 * @return - a data por extenso em caracteres todos m�nusculos.
	 * @throws java.lang.Exception
	 */
	public static String literal(Date date) {
		try {
			return literal(viewDate(date));
		} catch (Exception ex) {
			return null;
		}
	}

	public static String literalDayOfWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return CalendarUtil.literalDayOfWeek(cal);
	}

	/**
	 * Retorna o inteiro correspondente ao m�s atual.
	 */
	public static int getActualMonth() {
		return (new GregorianCalendar()).get(Calendar.MONTH) + 1;
	}

	/**
	 * Retorna o inteiro correspondente ao ano atual.
	 */
	public static int getActualYear() {
		return (new GregorianCalendar()).get(Calendar.YEAR);
	}

	/**
	 * Retorna o inteiro correspondente ao dia atual.
	 */
	public static int getActualDay() {
		return (new GregorianCalendar()).get(Calendar.DAY_OF_MONTH);
	}

	public static Calendar dbDateToCalendar(String in) {
		return new GregorianCalendar(Integer.parseInt(in.substring(0, 4)), Integer.parseInt(in.substring(5, 7)) - 1, Integer.parseInt(in.substring(8)));
	}

	public static Calendar dbDateTimeToCalendar(String in) {
		return new GregorianCalendar(Integer.parseInt(in.substring(0, 4)),
				Integer.parseInt(in.substring(5, 7)) - 1,
				Integer.parseInt(in.substring(8, 10)),
				Integer.parseInt(in.substring(11, 13)),
				Integer.parseInt(in.substring(14, 16)),
				Integer.parseInt(in.substring(17, 19)));
	}

	public static Calendar viewDateToCalendar(String in) {
			try {
				DateFormat df =  new SimpleDateFormat("yyyy-MM-dd"); // change
				df.setLenient(false);
				df.parse(in);
				return new GregorianCalendar(Integer.parseInt(in.substring(6)), Integer.parseInt(in.substring(3, 5)) - 1, Integer.parseInt(in.substring(0, 2)));
			} catch(Exception ex){
				return null;
			}
	}

	public static String literalDateFromDB(Object in) {
		String value = "";
		if (in instanceof Date) {
			value = DateUtil.dbDate((Date) in);

		} else if (in instanceof Calendar) {
			value = DateUtil.dbDate(((Calendar) in).getTime());

		} else if (in instanceof String) {
			value = (String) in;
		}

		try {
			int dia = Integer.parseInt(value.substring(8, 10));
			return dia + ((dia == 1) ? "�" : "") + " de " + MonthEnum.get(Integer.parseInt(value.substring(5, 7))) + " de " + value.substring(0, 4);
		} catch (Exception e) {
			return "<Erro de Convers�o>"; //TODO: fazer exception
		}
	}

	public static String literalDateFromView(String in) {
		try {
			return in.substring(0, 2) + " de " + MonthEnum.get(Integer.parseInt(in.substring(3, 5))) + " de " + in.substring(6, 10);
		} catch (Exception e) {
			return "<Erro na convers�o da data>";
		}
	}

	public static String literalDate() {
		try {
			return getActualDay() + " de " + MonthEnum.get(getActualMonth()) + " de " + getActualYear();
		} catch (Exception e) {
			return "<Erro na convers�o da data>";
		}
	}

	public static String literalDate(Date date) {
		return literalDateFromView(viewDate(date));
	}

	/**
	 * Formato da entrada: DD/MM/YYYY
	 * Formato da sa�da: YYYY-MM-DD
	 */
	public static String dbDate(String in) {
		String dia, mes, ano, data;
		dia = in.substring(0, 2);
		mes = in.substring(3, 5);
		ano = in.substring(6, 10);
		data = ano + "-" + mes + "-" + dia;
		return data;
	}

	public static String dbDate(Date date) {
		String bdDate = new SimpleDateFormat("yyyy-MM-dd").format(date).toString();
		return bdDate;
	}

	/**
	 * Formato da entrada: DD/MM/YYYY
	 * Formato da sa�da: YYYY-MM-DD
	 */
	public static String dbDate(Calendar calendar) {
		String bdDate = "";
		if (calendar != null) {
			bdDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()).toString();
		}

		return bdDate;
	}

	/**
	 * Formato da entrada: Date DD/MM/YYYY HH:mm:ss
	 * Formato da sa�da: YYYY-MM-DD HH:mm
	 */
	public static String dbDateTime(Date date) {
		String bdDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date).toString();
		return bdDate;
	}

	/**
	 * Formato da entrada: YYYY-MM-DD
	 * Formato da sa�da: DD/MM/YYYY
	 */
	public static String viewDate(Object in) {
		try {
			if (in instanceof String) {
				String strIn = (String) in;
				String dia, mes, ano, data;
				dia = strIn.substring(8, 10);
				mes = strIn.substring(5, 7);
				ano = strIn.substring(0, 4);
				data = dia + "/" + mes + "/" + ano;
				return data;
			} else if (in instanceof Date) {
				return new SimpleDateFormat("dd/MM/yyyy").format((Date) in).toString();
			}
			if (in instanceof GregorianCalendar) {
				GregorianCalendar date = (GregorianCalendar) in;
				return new SimpleDateFormat("dd/MM/yyyy").format((Date) date.getTime()).toString();
			}
		} catch (Exception ex) {
		}
		return null;
	}

    /**
	 * Formato da entrada: YYYY-MM-DD
	 * Formato da sa�da: DD/MM/YYYY
	 */
	public static String viewDateTime(Object in, String mask) {
		if (in instanceof String) {
			String dia, mes, ano, horas;
			dia = ((String) in).substring(8, 10);
			mes = ((String) in).substring(5, 7);
			ano = ((String) in).substring(0, 4);
			horas = ((String) in).substring(11, 16);
			return dia + "/" + mes + "/" + ano + " - " + horas;
		} else if (in instanceof Date) {
			return new SimpleDateFormat(mask).format((Date) in).toString();
		} else if (in instanceof Calendar) {
			return new SimpleDateFormat(mask).format(((GregorianCalendar) in).getTime()).toString();
		} else if (in instanceof Long) {
			return new SimpleDateFormat(mask).format(new Date((Long) in)).toString();
		}
		return null;
	}

	/**
	 * Formato da entrada: YYYY-MM-DD
	 * Formato da sa�da: DD/MM/YYYY
	 */
	public static String viewDateTime(Object in) {
		return viewDateTime(in, "dd/MM/yyyy - HH:mm");
	}

	/**
	 * A data do dia corrente no formato YYYY-MM-DD
	 *
	 */
	public static String dbToday() {
		Date hoje = new Date();
		String hojeStr = new SimpleDateFormat("yyyy-MM-dd").format(hoje).toString();
		return hojeStr;
	}

	public static String viewToday() {
		return viewToday("dd/MM/yyyy");
	}

	/**
	 * A data do dia corrente no formato, por ex: dd/MM/yyyy
	 */
	public static String viewToday(String format) {
		Date hoje = new Date();
		String str = new SimpleDateFormat(format).format(hoje).toString();
		return str;
	}

	/**
	 * Recebe como entrada a data no formato DD/MM/YYYY e retorna
	 * o valor da tada do tipo long no formato YYYYMMDD
	 *
	 */
	public static long longDate(String in) {
		String dia, mes, ano, data;
		dia = in.substring(0, 2);
		mes = in.substring(3, 5);
		ano = in.substring(6, 10);
		data = ano + mes + dia;
		return Long.parseLong(data);
	}

	/**
	 * A data de entrada do formato AAAA-MM-DD � retornada no seguinte formato: AAMMDD
	 */
	public static String rawDateIn6(String in) {
		String dia, mes, ano, data;
		ano = in.substring(2, 4);
		mes = in.substring(5, 7);
		dia = in.substring(8, 10);
		data = ano + mes + dia;
		return data;
	}

	public static String rawDateIn6(Date dataIn) {
		String in = new SimpleDateFormat("dd/MM/yyyy").format(dataIn).toString();
		return rawDateIn6(in);
	}

	public static Date date(String in) throws ParseException {
		Date correctDate;
		correctDate = new SimpleDateFormat("dd/MM/yyyy").parse(in);
		return correctDate;
	}

	public static String viewDate(Date date) {
		if (date == null) {
			return "";
		} else {
			return new SimpleDateFormat("dd/MM/yyyy").format(date).toString();
		}
	}

	/**
	 * Valida datas no formato DD/MM/YYYY
	 */
	public static boolean isValidDate(String data) {

		try {
			if (data.length() != 10) {
				return false;
			}
			String dia, mes, ano;
			dia = data.substring(0, 2);
			mes = data.substring(3, 5);
			ano = data.substring(6, 10);

			Integer.parseInt(dia);
			Integer.parseInt(mes);
			Integer.parseInt(ano);
			DateUtil.date(dia + "/" + mes + "/" + ano);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Method to parse a date string in the supplied format to a Date object. If the format is null,
	 * the default "yyyy-MM-dd" is assumed and returns null if the input string does not match the
	 * format.
	 *
	 * @param <b>dateString
	 *            </b> The string to be converted to a Date
	 * @param <b>formatString
	 *            </b> The format of the string
	 *
	 * @return <b>Date</b> The Date object of the string parsed
	 */
	public static Date parse(String dateString, DateFormatEnum formatString) {
		if (formatString == null) {
			formatString = DateFormatEnum.DATE;
		}
		return parse(dateString, formatString.getMask());
	}

	public static Date parse(String dateString, String formatString) {
		Date data = null;
		if (!Is.empty(dateString)) {
			SimpleDateFormat sdf = new SimpleDateFormat(formatString);
			sdf.setLenient(false);
			try {
				data = sdf.parse(dateString);
			} catch (ParseException e) {
			}
		}
		return data;
	}

	public static Date parse(String dateString) {
		return parse(dateString, DateFormatEnum.DATE);
	}

	/**
	 * @see format(Date date)
	 * @return format(calendar.getTime())
	 */
	public static String format(Calendar calendar) {
		return format(calendar.getTime());
	}

	/**
	 * @see DateUtil.format(Date date, DateFormatEnum mask)
	 * @return format(calendar ,mask.getMask())
	 */
	public static String format(Calendar calendar, DateFormatEnum mask) {
		return format(calendar, mask.getMask());
	}

	/**
	 * @see format(Date date, String mask)
	 * @return format(calendar.getTime(),mask)
	 */
	public static String format(Calendar calendar, String mask) {
		Date date = null;
		if (calendar != null) {
			date = calendar.getTime();
		}
		return format(date, mask);
	}

	/** Mask: "yyyy-MM-dd" */
	public static String format(Date date) {
		return format(date, DateFormatEnum.DATE);
	}

	public static String format(Date date, DateFormatEnum mask) {
		return format(date, mask.getMask());
	}

	/**
	 * Formata a data com a m�scara informada.
	 * @param date
	 * @param mask
	 */
	public static String format(Date date, String mask) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(mask).format(date).toString();
	}

	public static String viewHourMin(Date date) {
		return (new SimpleDateFormat("HH:mm").format(date).toString());
	}

	public static Calendar dateTimeToCalendar(Date date) {
		return dbDateTimeToCalendar(dbDateTime(date));
	}

	public static Calendar dateToCalendar(Date date) {
		return dbDateToCalendar(dbDate(date));
	}
}
