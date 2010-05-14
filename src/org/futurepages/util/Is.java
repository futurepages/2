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

    /*
     * Se a string possui algum token de tamanho 1 retorna 'false'
     */
    public static boolean anyWordInvalidIn(String str) {
        StringTokenizer st = new StringTokenizer(str, " ");
        while (st.hasMoreTokens()) {
            if (st.nextToken().length() == 1) {
                return true;
            }
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

    public static String validGoogleMapURL(String googleMapsURL) {

        if ((googleMapsURL.startsWith("http://maps.google.com.br/") ||
                googleMapsURL.startsWith("HTTP://maps.google.com.br/")) &&
                googleMapsURL.contains(".")) {
            try {
                if (!googleMapsURL.contains("&output=embed")) {
                    googleMapsURL += "&output=embed";
                }

                return googleMapsURL;
            } catch (Exception exc) {
                throw new ErrorException("Informe uma url válida do GoogleMaps");
            }

        } else {

            if ((googleMapsURL.toLowerCase().startsWith("<iframe"))) {
                try {
                    String extracaoDaUrl = "";//variavel que armazenara a url valida para acessar o mapa

                    int inicioIndice = googleMapsURL.lastIndexOf("src=\"") + 5;
                    int finalIndice = googleMapsURL.indexOf("\"></iframe>");
                    extracaoDaUrl = googleMapsURL.substring(inicioIndice, finalIndice);

                    return extracaoDaUrl;
                } catch (Exception exc) {
                    throw new ErrorException("Informe uma url válida do GoogleMaps");
                }
            } else {
                throw new ErrorException("Informe uma url válida do GoogleMaps");
            }
        }
    }

    /**
     * Verifica a valiade do email dado com entrada
     * @param mailStr email de entrada
     * @return verdadeiro se o email é válido
     * TODO altera para regex
     */
    public static boolean validMail(String mailStr) {
        String[] mailParts = mailStr.split("@");

        if (mailParts.length == 2) {
            if (!mailParts[1].contains(".")) {
                return false;
            }
            if (mailParts[0].contains(",")) {
                return false;
            }
            if (mailParts[1].contains(",")) {
                return false;
            }
            if (mailParts[0].contains(":")) {
                return false;
            }
            if (mailParts[1].contains(":")) {
                return false;
            }
            if (mailParts[0].contains(";")) {
                return false;
            }
            if (mailParts[1].contains(";")) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * TODO altera para regex
     * @param urlStr
     */
    public static boolean validURL(String urlStr) {
        if (urlStr != null) {
            if (urlStr.startsWith("http://") || urlStr.startsWith("https://") ||
                urlStr.startsWith("HTTPS://") || urlStr.startsWith("HTTP://") && urlStr.contains(".")) {
                return true;
            } else {
                return false;
            }
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

        if (str.length() < 4 || str.length() >= 50) {
            return false;
        }
        String loginVerify = The.stringKeyIn(str);//TIRA CARACTERES ESPECIAIS
        loginVerify = SEOUtil.stringKeyValid(loginVerify);//TIRA ACENTOS E Ç

        if (loginVerify.equalsIgnoreCase(str)) {
            return true;
        }

        return false;

    }
}
