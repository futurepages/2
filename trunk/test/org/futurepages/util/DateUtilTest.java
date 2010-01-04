package org.futurepages.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leandro
 */
public class DateUtilTest {

    @Test
    public void testLiteral() throws Exception {
        assertLiteral("01/01/1800","um de janeiro de mil e oitocentos");
        assertLiteral("29/06/1900","vinte e nove de junho de mil e novecentos");
        assertLiteral("11/10/1900","onze de outubro de mil e novecentos");
        assertLiteral("13/09/1955","treze de setembro de mil novecentos e cinquenta e cinco");
        assertLiteral("30/02/1999","trinta de fevereiro de mil novecentos e noventa e nove");
        assertLiteral("01/01/2002","um de janeiro de dois mil e dois");
        assertLiteral("16/12/2010","dezesseis de dezembro de dois mil e dez");
        assertLiteral("31/12/2099","trinta e um de dezembro de dois mil e noventa e nove");
    }
    
    private void assertLiteral(String in, String expOut) throws Exception{
        String out = DateUtil.literal(in);
        assertEquals("Erro no número por extenso com a entrada "+in, expOut, out);
    }
}