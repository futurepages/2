package org.futurepages.util;

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

		//TIRA CARACTERES ESPECIAIS MANTENDO SOMENTE PONTO, UNDERLINE E HIFEN
        loginVerify = The.stringKeyIn(loginVerify);

        if (loginVerify.equalsIgnoreCase(str)) {
            return true;
        }

        return false;

    }

	public static boolean validCapitalizedPersonName(String in){
		if(in.equals(in.toUpperCase()) || in.equals(in.toLowerCase())){
			return false;
		}
		String[] palavras = in.split("[ |\\t]+");
		if(palavras.length==1){
			return false;
		}
		for (String palavra : palavras) {
			if(palavra.length()>1){
				String primeiraLetra = String.valueOf(palavra.charAt(0));
				String segundaLetra = String.valueOf(palavra.charAt(1));
				if(primeiraLetra.equals(primeiraLetra.toLowerCase())
				   && (segundaLetra.equals(segundaLetra.toUpperCase()) && !segundaLetra.equals("'") && !segundaLetra.equals("`") ) ){
					return false;
				}
				if(palavra.length()>3){
					if(primeiraLetra.equals(primeiraLetra.toLowerCase())
					&& !palavra.contains("'") && !palavra.contains("`")){
						return false;
					}
				}
			}else if(palavra.equalsIgnoreCase("e")){
				if(palavra.equals("E")){
					return false;
				}
			}
			if(palavra.equalsIgnoreCase("de")
			|| palavra.equalsIgnoreCase("do")
			|| palavra.equalsIgnoreCase("da")
			|| palavra.equalsIgnoreCase("dos")
			|| palavra.equalsIgnoreCase("das")
			|| palavra.equalsIgnoreCase("e")
			){
				if(!palavra.equals(palavra.toLowerCase())){
					return false;
				}
			}else if(
					 palavra.equalsIgnoreCase("I")
				  || palavra.equalsIgnoreCase("II")
				  || palavra.equalsIgnoreCase("III")
				  || palavra.equalsIgnoreCase("IV")
				  || palavra.equalsIgnoreCase("V")
				  || palavra.equalsIgnoreCase("VI")
				  || palavra.equalsIgnoreCase("VII")
				  || palavra.equalsIgnoreCase("VIII")
				  //TO-DO seria interessante os demais??
			){
				if(!palavra.equals(palavra.toUpperCase())){
					return false;
				}
			}
		}
		return true;
	}
}