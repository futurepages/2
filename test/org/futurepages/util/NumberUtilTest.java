package org.futurepages.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author zezim
 */
public class NumberUtilTest {

	@Test
	public void numeroPorExteso() throws Exception {
		assertEquals("zero", NumberUtil.numeroPorExteso("0"));
		assertEquals("um", NumberUtil.numeroPorExteso("1"));
		assertEquals("dez", NumberUtil.numeroPorExteso("10"));
		assertEquals("doze", NumberUtil.numeroPorExteso("12"));
		assertEquals("dezessete", NumberUtil.numeroPorExteso("17"));
		assertEquals("trinta e tr�s", NumberUtil.numeroPorExteso("33"));
		assertEquals("cinquenta e cinco", NumberUtil.numeroPorExteso("55"));
		assertEquals("noventa e nove", NumberUtil.numeroPorExteso("99"));
		assertEquals("mil e um", NumberUtil.numeroPorExteso("1001"));
		assertEquals("mil e vinte e um", NumberUtil.numeroPorExteso("1021"));
		assertEquals("dois mil e noventa e nove", NumberUtil.numeroPorExteso("2099"));
		assertEquals("mil novecentos e noventa e tr�s", NumberUtil.numeroPorExteso("1993"));
		assertEquals("um milh�o, onze mil novecentos e noventa e tr�s", NumberUtil.numeroPorExteso("1011993"));
		assertEquals("um milh�o e nove", NumberUtil.numeroPorExteso("1000009"));
		assertEquals("quinze milh�es, cento e dezenove mil novecentos e noventa e tr�s", NumberUtil.numeroPorExteso("15119993"));
	}
}
