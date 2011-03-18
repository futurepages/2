package org.futurepages.util;

public class PisPasepNitUtil {

    public static int QUANTIDADE_DIGITOS_PISPASEPNIT = 11;


    /**
     * Valida o PISPASEPNIT
     * @param str_pispasepnit
     * @return
     */
    public static boolean validaPisPasepNit(String str_pispasepnit) {

	if (str_pispasepnit == null || str_pispasepnit.length() != QUANTIDADE_DIGITOS_PISPASEPNIT) {
	    return false;
	}

	if (str_pispasepnit.equals("00000000000") || (str_pispasepnit.equals(""))) {
	    return false;
	}

	String ftap = "3298765432";
	Integer total = 0;

	try {

	    for (int i = 1; i <= 10; i++) {
		Integer parte1 = Integer.valueOf(str_pispasepnit.substring(i-1, i));
		Integer parte2 = Integer.valueOf(ftap.substring(i-1, i));
		total += parte1 * parte2;
	    }

	    Integer resto = (total % 11);

	    if ((int) resto != (int) 0) {
		resto = 11 - resto;
	    }

	    if((int)resto == (int)10 || (int)resto == (int)11){
		String restoStr = String.valueOf(resto);
	        resto = Integer.valueOf(restoStr.substring(1, 2));
            }

	    Integer digitoVerificador = Integer.valueOf(str_pispasepnit.substring(10, 11));
	    if ((int) resto != (int) digitoVerificador) {
		return false;
	    }

	} catch (Exception ex) {
	    ex.printStackTrace();
	    return false;
	}

	return true;

    }
}
