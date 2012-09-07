package org.futurepages.util;

import org.futurepages.util.html.HtmlMapChars;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;

/**
 * Esta � "A" Classe
 * @author leandro
 */
public class The {

	public static boolean bool(Boolean x) {
		if(x!=null){
			return x.booleanValue();
		}
		return false;
	}

	public static String implodedArray(String[] array, String delim, String quote) {
		if(quote == null){
			quote = "";
		}
		StringBuffer out = new StringBuffer("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(delim);
			}
			out.append(quote+array[i]+quote);
		}
		return out.toString();
	}

	public static String implodedArray(Object[] array, String delim, String quote) {
		if(quote == null){
			quote = "";
		}
		StringBuffer out = new StringBuffer("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(delim);
			}
			out.append(quote+array[i].toString()+quote);
		}
		return out.toString();
	}

    public static String implodedArray(long[] array, String delim, String quote) {
		if(quote == null){
			quote = "";
		}
		StringBuffer out = new StringBuffer("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(delim);
			}
			out.append(quote+array[i]+quote);
		}
		return out.toString();
	}

    /**
     * Retorna o primeiro token da String
     */
    public static String firstTokenOf(String str, String separator) {
		if(str!=null){
			return tokenAt(0,str,separator);
		} else {
			return null;
		}
    }

	/**
     * Retorna o primeiro token da String
     */
    public static String lastTokenOf(String str, String separator) {
		if(str!=null){
			String[] tokens = str.split(separator);
			return tokens[tokens.length-1];
		} else {
			return null;
		}
    }

    /**
     * Retorna o token localizado numa determinada posi��o dentro de uma string com separadoes
     * caso n�o encontre, o retorno � null
     * @param posi � a posi��o que se deseja... a primeira posi�ao � 0
     * @param str a string que deseja-se pesquisar o token
     * @param separator o separador dos tokens
     * @return o token da posi�ao 'posi'. nulo caso n�o exista a posi��o.
     */
    public static String tokenAt(int posi,String str, String separator){
    	StringTokenizer st = new StringTokenizer(str, separator);
    	int i = 0;
    	while(st.hasMoreTokens()){
    		String token = st.nextToken();
    		if(i==posi){
    			return token;
    		}
    		i++;
    	}
    	return null;
    }

	public static String strWithRightSpaces(String s, int len){
        // converts integer to left-zero padded string, len  chars long.
        if (s.length() > len) {
            return s.substring(0, len);
        } else if (s.length() < len) // pad on left with zeros
        {
            return s + "                                                        ".substring(0, len - s.length());
        } else {
            return s.substring(0,len);
        }
	}

	public static String strWithLeftSpaces(String s, int len){
        // converts integer to left-zero padded string, len  chars long.
        if (s.length() > len) {
            return s.substring(0, len);
        } else if (s.length() < len) // pad on left with zeros
        {
            return "                                                                             ".substring(0, len - s.length())+s;
        } else {
            return s.substring(0,len);
        }
	}

    /**
     * Retorna o array com os tokens separados pelo 'separator'
     */
    public static String[] explodedToArray(String str, String separator) {
        StringTokenizer st = new StringTokenizer(str, separator);
        String[] explodedTokens = new String[st.countTokens()];
        int i = 0;
        while (st.hasMoreTokens()) {
            explodedTokens[i] = st.nextToken();
            i++;
        }
        return explodedTokens;
    }

    /**
     * Retorna a lista com os tokens separados pelo 'separator'
     */
    public static List<String> explodedToList(String str, String separator) {
        StringTokenizer st = new StringTokenizer(str, separator);
        List<String> explodedTokens = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            explodedTokens.add(st.nextToken());
        }
        return explodedTokens;
    }

    /**
     * Retorna o n�mero de tokens separados por espa�o
     */
    public static int countTokensIn(String str) {
        StringTokenizer st = new StringTokenizer(str, " ");
        return st.countTokens();
    }

    /**
     *  Retorna o inteiro 'i' com o n�mero de zeros 'len'
     */
    public static String intWithLeftZeros(long i, int len) {
        // converts integer to left-zero padded string, len  chars long.
        String s = Long.toString(i);
        if (s.length() > len) {
            return s.substring(0, len);
        } else if (s.length() < len) // pad on left with zeros
        {
            return "000000000000000000000000000000000000000000000000000".substring(0, len - s.length()) + s;
        } else {
            return s;
        }
    }

    public static String strWithLeftZeros(String s, int len) {
        // converts integer to left-zero padded string, len  chars long.
        if (s.length() > len) {
            return s.substring(0, len);
        } else if (s.length() < len) // pad on left with zeros
        {
            return "000000000000000000000000000000000000000000000000000".substring(0, len - s.length()) + s;
        } else {
            return s;
        }
    }

    /**
     * Capitaliza a primeira letra da string passada como entrada.
     * Exemplo: "palavraComposta" --> "PalavraComposta"
     */
    public static String capitalizedWord(String in) {
        if (in.length() > 1) {
            return (in.substring(0, 1).toUpperCase().concat(in.substring(1)));
        } else {
            return in;
        }
    }

    /**
     * "des"capitaliza a primeira letra da string passada como entrada.
     * Exemplo: "PalavraComposta" --> "palavraComposta"
     */
    public static String uncapitalizedWord(String in) {
        if (in.length() > 1) {
            return (in.substring(0, 1).toLowerCase().concat(in.substring(1)));
        } else {
            return in;
        }
    }

    /**
     * Retorna a string como o HTMLParser - ou seja: texto com tokens somente entre espa�o,
     * retira '\t', '\n', '\r', '\h' e ' ' sequenciais trocando por um �nico ' '
     */
    public static String stringWithNoSpecials(String str) {
        StringTokenizer st = new StringTokenizer(str);
        StringBuffer newStr = new StringBuffer(st.nextToken());
        while (st.hasMoreTokens()) {
            newStr.append(" " + st.nextToken());
        }
        return newStr.toString();
    }

    /**
     * Retorna o valor correspondente de uma String em condifica��o html;
     * Exemplo: entrada: "leandro"  --> sa�da: &quot;leandro&quot;
     * @return a string com os caracteres especiais convertidos para a codifica��o HTML
     */
    public static String htmlSimpleValue(String strIn){
        return HtmlMapChars.htmlSimpleValue(strIn);
    }

    /**
     * Mediante um objeto como entrada, retorno o nome do pacote mais interno a que ele
     * pertence.
     *
     * @param obj objeto de entrada
     * @return o nome do pacote mais interno
     */
    public static String lastPackageName(Object obj) {
        String name = obj.getClass().getName();
        String[] explodedName = name.split("\\.");
        return explodedName[explodedName.length - 2];
    }

    public static String firstTokenAfter(String str, String discarding, String separator) {
        String str2 = str.substring(str.indexOf(discarding)+discarding.length());
        return firstTokenOf(str2, separator);
    }
    /**
     * Retorna o nome da classe mediante o nome do arquivo como entrada.
     * Ex.: fileName = "ClasseName.class" retorna "ClasseName"
     * @param fileName nome do arquivo
     * @return no da classe sem a extens�o
     */
    public static String className(String fileName) {
        StringTokenizer st = new StringTokenizer(fileName, ".");
        return st.nextToken();
    }
    
    /**
     * Retornar wIn sem caracteres especiais.
     * @param wIn
     */
    public static String wordIn(String str){


        str = SEOUtil.replaceSpecialAlphas(str);  //CONVERTE ACENTUADOS E �

		 String regexPatternChars = "[\\d|A-Z|a-z]";

		return wordInRegex(str, regexPatternChars);
    }

    /**
     * Remove caracteres especiais.
     * @param str somente alfanumericos puros sem acentua�ao, ponto, underline e hifen
     * @return
     */
	 public static String stringKeyIn(String str){

        str = SEOUtil.replaceSpecialAlphas(str);  //CONVERTE ACENTUADOS E �

		 String regexPatternChars = "[\\d|A-Z|a-z|\\.|\\-|_]";

		return wordInRegex(str, regexPatternChars);

    }
	 
    /**
     *
     * @param in objeto que deseja-se alterar
     * @return o mesmo objeto com os campos String em caixa alta
     */
    public static Object upperCasedBean(Object in){
        Field[] fields = in.getClass().getDeclaredFields();
        for(int i = 0; i<fields.length;i++){
            if(fields[i].getType() == String.class){
                String field = (String) ReflectionUtil.getField(in, fields[i].getName());
				if (field != null) {
                    ReflectionUtil.setField(in, fields[i].getName(), field.toUpperCase());
            }
        }
		}
        return in;
    }

	private static String wordWithoutSpecials(String[] specials, String wIn, String newChar) {
		for (String special : specials) {
			if (wIn.contains(special)) {
				wIn = wIn.replace(special, newChar);
			}
		}
		return wIn;
	}

	public static String javascriptText(Object value) {
		String valor = "";
		if (String.class.isAssignableFrom(value.getClass())) {
			valor = (String) value;
			if ((valor).contains("\n")) {
				valor = valor.replaceAll("\n", "\\\\n");
			}
			if(((String)value).contains("\r")){
					valor = valor.replaceAll("\r", "\\\\r");
			}
			if(((String)value).contains("'")){
					valor = valor.replaceAll("'", "\\\\'");
			}
			if(((String)value).contains("</script>")){
					valor = valor.replaceAll("</script>", "&lt;/script>");
			}
		}else{
			valor = value.toString();
		}
		return valor;
	}

	static String stringWithoutInitialNumbers(String str) {
		char[] c = str.toCharArray();		
		while(c!=null && Character.isDigit(c[0])){
			str = str.substring(1, str.length());
			c = (str.length()>0? str.toCharArray() : null);
		}					
		return str;
	}

	public static String concat(String... strs){
		return StringUtils.concat(strs);
	}

	public static String sequence(char ch, int len){
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < len; i++){
			sb.append(ch);
		}
		return sb.toString();
	}

	public static String strWithoutExtraBlanks(String inputText) {
		inputText = inputText.trim();

		String[] lista = inputText.split(" ");
		inputText = "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lista.length; i++) {
			if (lista[i].length() > 0) {
				sb.append(lista[i]);
				if(i < lista.length-1){
					sb.append(" ");
				}
			}
		}
		return sb.toString();
	}

	private static String wordInRegex(String str, String regexPatternChars) {
		Pattern tagsPattern  = Pattern.compile(regexPatternChars);
		IterableString iter = new IterableString(tagsPattern, str);
		StringBuilder sb     = new StringBuilder();
		for (MatchedToken token : iter) {
			sb.append(token.getMatched());
		}
		return sb.toString();
	}
}