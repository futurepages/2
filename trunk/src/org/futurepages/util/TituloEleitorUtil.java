package org.futurepages.util;

/**
 *
 * @author celso
 */
public class TituloEleitorUtil {
	
	public static boolean validarNumero(String strTitulo) {
        try {
			
			if (strTitulo.length() > 12) {
                return false;
            }
			else{
				if(strTitulo.length()<12){
					for(int i=strTitulo.length();i<12;i++){
						strTitulo = "0".concat(strTitulo);
					}
				}
			}
			
            int dig1;
            int dig2;
            int dig3;
            int dig4;
            int dig5;
            int dig6;
            int dig7;
            int dig8;
            int dig9;
            int dig10;
            int dig11;
            int dig12;
            int dv1;
            int dv2;


            //Gravar posição dos caracteres
            dig1 = Integer.parseInt(strTitulo.charAt(0) + "");
            dig2 = Integer.parseInt(strTitulo.charAt(1) + "");
            dig3 = Integer.parseInt(strTitulo.charAt(2) + "");
            dig4 = Integer.parseInt(strTitulo.charAt(3) + "");
            dig5 = Integer.parseInt(strTitulo.charAt(4) + "");
            dig6 = Integer.parseInt(strTitulo.charAt(5) + "");
            dig7 = Integer.parseInt(strTitulo.charAt(6) + "");
            dig8 = Integer.parseInt(strTitulo.charAt(7) + "");
            dig9 = Integer.parseInt(strTitulo.charAt(8) + "");
            dig10 = Integer.parseInt(strTitulo.charAt(9) + "");
            dig11 = Integer.parseInt(strTitulo.charAt(10) + "");
            dig12 = Integer.parseInt(strTitulo.charAt(11) + "");

            //Cálculo para o primeiro dígito validador
            dv1 = (dig1 * 2) + (dig2 * 3) + (dig3 * 4) + (dig4 * 5) + (dig5 * 6)
                    + (dig6 * 7) + (dig7 * 8) + (dig8 * 9);
            dv1 = dv1 % 11;

            if (dv1 == 10) {
                dv1 = 0; //Se o resto for igual a 10, dv1 igual a zero
            }

            //Cálculo para o segundo dígito validador
            dv2 = (dig9 * 7) + (dig10 * 8) + (dv1 * 9);
            dv2 = dv2 % 11;

            if (dv2 == 10) {
                dv2 = 0; 
            }

            //Validação dos dígitos validadores, após o cálculo realizado
            if (dig11 == dv1 && dig12 == dv2) {
                return true;
            } else {
                return false;
            }
            
        } catch (Exception e) {
            return false;
        }
    }
	
}
