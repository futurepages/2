package org.futurepages.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leandro
 */
public class SEOUtilTest {

	public void urlFormatTestProcedure(String msg ,String url, String esperado){
		String result = SEOUtil.urlFormat(url);
        assertEquals(esperado, result);
	}
	
	@Test
	public void urlFormat_ComEspacos() {
        String strIn = "Ma sorte em jogo";
        String expResult = "ma-sorte-em-jogo";
        urlFormatTestProcedure("erro quanto a url possui espa�os",strIn, expResult);
	}

	@Test
    public void urlFormat_ComHifen() {
        String strIn = "(pes-de-moleque)";
        String expResult = "pes_de_moleque";
        urlFormatTestProcedure("erro quanto a url possui espa�os",strIn, expResult);
    }

	@Test
	public void urlFormat_caracteresEspeciais() {
		String strIn = "��:<>�:���@";
		String expResult = "eanoau_";
		urlFormatTestProcedure("erro quanto a url possui espa�os",strIn, expResult);
	}
}