package org.futurepages.formatters;

import junit.framework.Assert;
import org.futurepages.util.DateUtil;
import org.junit.Test;

public class ElapsedTimeFormatterTest {


	@Test
	public void testFormat() {
		assertFormatValue("2011-11-07 00:30:35", "2011-11-07 00:30:20", "h� � 1 minuto");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-07 00:29:20", "h� � 1 minuto");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-07 00:29:30", "h� � 1 minuto");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-07 00:29:00", "h� � 1 minuto");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-07 00:20:00", "h� � 10 minutos");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-07 00:00:00", "h� � 30 minutos");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 23:59:00", "h� � 31 minutos");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 23:55:00", "h� � 35 minutos");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 23:50:00", "h� � 40 minutos");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 23:20:00", "h� � 1 hora");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 23:00:00", "h� � 1 hora");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 22:00:00", "h� � 2 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 21:00:00", "h� � 3 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 20:00:00", "h� � 4 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 19:00:00", "h� � 5 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 18:00:00", "h� � 6 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 17:00:00", "h� � 7 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 16:00:00", "h� � 8 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 15:00:00", "h� � 9 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 14:00:00", "h� � 10 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 13:00:00", "h� � 11 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:32:00", "h� � 11 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:31:00", "h� � 11 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:30:50", "h� � 12 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:30:45", "h� � 12 horas");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:30:40", "h� � 12 horas");

		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:30:30", "ontem �s 12:30");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:30:00", "ontem �s 12:30");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:20:00", "ontem �s 12:20");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:10:00", "ontem �s 12:10");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:05:50", "ontem �s 12:05");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:05:40", "ontem �s 12:05");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:05:30", "ontem �s 12:05");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:05:20", "ontem �s 12:05");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:05:10", "ontem �s 12:05");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:02:10", "ontem �s 12:02");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:01:10", "ontem �s 12:01");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 12:00:00", "ontem �s 12:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 11:00:00", "ontem �s 11:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 10:00:00", "ontem �s 10:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 09:00:00", "ontem �s 09:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 08:00:00", "ontem �s 08:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 07:00:00", "ontem �s 07:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 06:00:00", "ontem �s 06:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 05:00:00", "ontem �s 05:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 04:00:00", "ontem �s 04:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 03:00:00", "ontem �s 03:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 02:00:00", "ontem �s 02:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 01:00:00", "ontem �s 01:00");
		assertFormatValue("2011-11-07 00:30:35", "2011-11-06 00:00:00", "ontem �s 00:00");

		assertFormatValue("2011-11-07 00:30:35", "2011-11-05 20:00:00", "em 5 de novembro");
		assertFormatValue("2011-11-07 00:30:35", "2011-10-04 20:00:00", "em 4 de outubro");
		assertFormatValue("2011-11-07 00:30:35", "2011-08-04 20:00:00", "em 4 de agosto");
		assertFormatValue("2011-11-07 00:30:35", "2011-04-04 20:00:00", "em 4 de abril");
		assertFormatValue("2011-11-07 00:30:35", "2011-02-04 20:00:00", "em 4 de fevereiro");
		assertFormatValue("2011-11-07 00:30:35", "2011-01-01 20:00:00", "em 1� de janeiro");
		assertFormatValue("2011-11-07 00:30:35", "2010-12-20 20:00:00", "em 20 de dezembro de 2010");

		assertFormatValue("2011-02-01 00:30:35", "2011-01-01 20:00:00", "em 1� de janeiro");
		assertFormatValue("2011-01-31 00:30:35", "2010-12-01 20:00:00", "em 1� de dezembro");
		assertFormatValue("2011-02-01 00:30:35", "2010-12-01 20:00:00", "em 1� de dezembro de 2010");
		assertFormatValue("2011-02-01 00:30:35", "2010-11-01 20:00:00", "em 1� de novembro de 2010");
		assertFormatValue("2011-02-01 00:30:35", "2010-10-01 20:00:00", "em 1� de outubro de 2010");
		assertFormatValue("2011-02-01 00:30:35", "2010-08-01 20:00:00", "em 1� de agosto de 2010");
		assertFormatValue("2011-02-01 00:30:35", "2010-06-01 20:00:00", "em 1� de junho de 2010");
		assertFormatValue("2011-02-01 00:30:35", "2010-02-01 20:00:00", "em 1� de fevereiro de 2010");
		assertFormatValue("2011-02-01 00:30:35", "2010-01-01 20:00:00", "em 1� de janeiro de 2010");
		assertFormatValue("2011-01-01 00:30:35", "2009-12-01 20:00:00", "em 1� de dezembro de 2009");
		assertFormatValue("2011-01-01 00:30:35", "2009-11-01 20:00:00", "em 1� de novembro de 2009");
		assertFormatValue("2011-01-01 00:30:35", "2010-11-01 20:00:00", "em 1� de novembro de 2010");
	}

	private void assertFormatValue(String bdBaseDateTime, String bdElapsedDateTime, String expectedText) {
//		System.out.println(
//				ElapsedTimeFormatter.formatValue(DateUtil.dbDateTimeToCalendar(bdBaseDateTime),
//				DateUtil.dbDateTimeToCalendar(bdElapsedDateTime))
//	);
		Assert.assertEquals(
				expectedText,
				ElapsedTimeFormatter.formatValue(DateUtil.dbDateTimeToCalendar(bdBaseDateTime),
				DateUtil.dbDateTimeToCalendar(bdElapsedDateTime))
				);
	}
}