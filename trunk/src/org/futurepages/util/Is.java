package org.futurepages.util;

import java.util.StringTokenizer;
import org.futurepages.exceptions.ErrorException;

/**
 * Classe responsável por comparações diversas com retornos lógicos (true/false).
 *
 */
public class Is {

    /**
     * Se o parâmetro é vazio ou nulo ou possui somente espaços, retorna true
     */
    public static boolean empty(Object fieldObj) {
        if (fieldObj == null) {
            return true;
        } else if (fieldObj.toString().trim().equals("")) {
            return true;
        }
        return false;
    }

    /**
     * Retorna true se um campo numérico foi selecionado
     * ou seja, não é nem nulo e nem -1.
     */
    public static boolean selected(Long fieldName) {
        return (fieldName != null) && (fieldName > 0);
    }

    /**
     * Retorna true se um campo numérico foi selecionado
     * ou seja, não é nem nulo e nem -1.
     */
    public static boolean selected(Integer fieldName) {
        return (fieldName != null) && (fieldName > 0);
    }

    /**
     * Verifica a valiade do email dado com entrada
     * @param mailStr email de entrada
     * @return verdadeiro se o email é válido
     * TODO altera para regex
     */
    public static boolean validMail(String mailStr) {
        String[] mailParts = mailStr.split("@");

        // Como o operador && é curto-circuito, as duas próximas operações
		// só serão realizadas se a primeira operação "mailParts.length == 2"
		// for verdadeira. Evitando assim que hava uma exceção, caso o array
		// possua menos de 2 elementos.
		if (mailParts.length == 2 && !empty(mailParts[0]) && !empty(mailParts[1])) {
            return validStringKey(mailParts[0],null,null,true) && validStringKey(mailParts[1],null,null,true) && (mailParts[1].indexOf(".") > 0);
        } else {
            return false;
        }
    }

    /**
     * TODO altera para regex
     * @param urlStr
     */
    public static boolean validURL(String urlStr) {
        if (urlStr != null) {
            return urlStr.contains(".") && ((urlStr.startsWith("http://") || urlStr.startsWith("https://") ||
                urlStr.startsWith("HTTPS://") || urlStr.startsWith("HTTP://")));
        } else {
            return false;
        }
    }

    /**
     * Testa se todos os caracteres de uma String "str" são iguais ao caracter "testStr"
     * @param str
     * @param testStr
     */
    public static boolean everyCharsLike(String str, String testStr) {
        if (testStr.length() > 1) {
            return false;
        }
        char testChar = testStr.charAt(0);
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != testChar) {
                return false;
            }
        }
        return true;
    }

    /**
     *  Verificar se a string não possui acentuacao, nem especiais com excecao do de '_', '.', '-',' '
     */
    public static boolean validStringKey(String str) {

        return validStringKey(str, 4, 50, true);

    }

	public static boolean validStringKey(String str, Integer min, Integer max, boolean allowsInitialNumber) {
		        
		if ((min != null && (str.length() < min)) || (max != null && (str.length() >= max))) {
            return false;
        }

		String loginVerify;

		if (!allowsInitialNumber){
			loginVerify = The.stringWithoutInitialNumbers(str);
		} else{
			loginVerify = str;
		}

        loginVerify = The.stringKeyIn(loginVerify);//TIRA CARACTERES ESPECIAIS E NÚMEROS NO INÍCIO
        loginVerify = SEOUtil.stringKeyValid(loginVerify);//TIRA ACENTOS E Ç

        if (loginVerify.equalsIgnoreCase(str)) {
            return true;
        }

        return false;

    }
}