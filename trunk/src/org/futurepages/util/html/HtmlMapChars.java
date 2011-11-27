package org.futurepages.util.html;

import java.util.HashMap;
/**
 * Mapa de caracteres HTLM
 *
 */
public class HtmlMapChars {

    private static final HashMap<Character, String> plainTable    = new HashMap<Character, String>();
    private static final HashMap<String, String> simpleTable    = new HashMap<String, String>();
    private static final HashMap<String, String> completeTable  = new HashMap<String, String>();
    private static final HashMap<Character, String> textareaTable  = new HashMap<Character, String>();

    static
    {

		plainTable.put('<'  ,"&lt;");
        plainTable.put('>'  ,"&gt;");
        plainTable.put('\t' ,"&nbsp;"); //  \t
        plainTable.put('\n' ,"<br/>");  //  \n
        plainTable.put('\r' ,"");
		
        //Povoando tabela de textarea
        textareaTable.put('\"' ,"&quot;");
        textareaTable.put('<'  ,"&lt;");
        textareaTable.put('>'  ,"&gt;");
        textareaTable.put('&'  ,"&amp;");
        textareaTable.put('\t' ,"&nbsp;"); //  \t
        textareaTable.put('\n' ,"<br/>");  //  \n
        textareaTable.put('\r' ,"");       //  \r

		//Povoando tabela simples (somente html brakers)
        simpleTable.put("\"" ,"&quot;");
        simpleTable.put("<"  ,"&lt;");
        simpleTable.put(">"  ,"&gt;");
        simpleTable.put("&"  ,"&amp;");

        //povoando tabela com todos os especiais alfab�ticos.
        completeTable.putAll(simpleTable);
        completeTable.put("�","&Aacute;");
        completeTable.put("�","&aacute;");
        completeTable.put("�","&Acirc;");
        completeTable.put("�","&acirc;");
        completeTable.put("�","&Agrave;");
        completeTable.put("�","&agrave;");
        completeTable.put("�","&Aring;");
        completeTable.put("�","&aring;");
        completeTable.put("�","&Atilde;");
        completeTable.put("�","&atilde;");
        completeTable.put("�","&Auml;");
        completeTable.put("�","&auml;");
        completeTable.put("�","&AElig;");
        completeTable.put("�","&aelig;");
        completeTable.put("�","&Eacute;");
        completeTable.put("�","&eacute;");
        completeTable.put("�","&Ecirc;");
        completeTable.put("�","&ecirc;");
        completeTable.put("�","&Egrave;");
        completeTable.put("�","&egrave;");
        completeTable.put("�","&Euml;");
        completeTable.put("�","&euml;");
        completeTable.put("�","&ETH;");
        completeTable.put("�","&eth;");
        completeTable.put("�","&Iacute;");
        completeTable.put("�","&iacute;");
        completeTable.put("�","&Icirc;");
        completeTable.put("�","&icirc;");
        completeTable.put("�","&Igrave;");
        completeTable.put("�","&igrave;");
        completeTable.put("�","&Iuml;");
        completeTable.put("�","&iuml;");
        completeTable.put("�","&Oacute;");
        completeTable.put("�","&oacute;");
        completeTable.put("�","&Ocirc;");
        completeTable.put("�","&ocirc;");
        completeTable.put("�","&Ograve;");
        completeTable.put("�","&ograve;");
        completeTable.put("�","&Oslash;");
        completeTable.put("�","&oslash;");
        completeTable.put("�","&Otilde;");
        completeTable.put("�","&otilde;");
        completeTable.put("�","&Ouml;");
        completeTable.put("�","&ouml;");
        completeTable.put("�","&Uacute;");
        completeTable.put("�","&uacute;");
        completeTable.put("�","&Ucirc;");
        completeTable.put("�","&ucirc;");
        completeTable.put("�","&Ugrave;");
        completeTable.put("�","&ugrave;");
        completeTable.put("�","&Uuml;");
        completeTable.put("�","&uuml;");
        completeTable.put("�","&Ccedil;");
        completeTable.put("�","&ccedil;");
        completeTable.put("�","&Ntilde;");
        completeTable.put("�","&ntilde;");

		completeTable.put("�","&ordm;");
		completeTable.put("�","&deg;");
		completeTable.put("�","&ordf;");
		completeTable.put("�","&sect;");
    }

    public static String getSimple(char ch){
        return simpleTable.get(String.valueOf(ch));
    }

    public static String getComplete(char ch){
        return completeTable.get(String.valueOf(ch));
    }

    public static String getTextArea(char ch){
        return textareaTable.get(ch);
    }

    /**
     * Retorna o valor correspondente de uma String em condifica��o html;
     * somente para os caracteres mais cr�ticos.
     * Exemplo: entrada: "leandro"  --> sa�da: (&)quot;leandro(&)quot; (sem os parenteses)
     * contempla somente os HTML Brakers: aspa dupla, menor que e maior que.
     * @return a string com os caracteres especiais cr�ticos convertidos para a codifica��o HTML
     */
    public static String htmlSimpleValue(String strIn){
        if(strIn == null) return "&nbsp;";

		StringBuilder outBuffer = new StringBuilder();
        for(int i = 0; i < strIn.length();i++){
            String htmlValue = getSimple(strIn.charAt(i));
            if(htmlValue != null){
                outBuffer.append(htmlValue);
            }
            else{
                outBuffer.append(strIn.charAt(i));
            }
        }
        return outBuffer.toString();
    }

    /**
     * Retorna o valor correspondente de uma String em condifica��o html;
     * Exemplo: entrada: "leandro"  --> sa�da: (&)quot;leandro(&)quot; (sem os parenteses)
     * contempla HTML Brakers e caracteres alfab�ticos acentuados e especiais.
     * @return a string com os caracteres especiais convertidos para a codifica��o HTML
     */
    public static String htmlValue(String strIn){
        if(strIn == null) return "&nbsp;";

        StringBuilder outBuffer = new StringBuilder();
        for(int i = 0; i < strIn.length();i++){
            String htmlValue = getComplete(strIn.charAt(i));
            if(htmlValue != null){
                outBuffer.append(htmlValue);
            }
            else{
                outBuffer.append(strIn.charAt(i));
            }
        }
        return outBuffer.toString();
    }

    /**
     * Converte as quebras de texto e aspas escrito em textarea para os caracteres v�lidos de html.
     * @param strIn
     */
    public static String textAreaValue(String strIn){
        if(strIn == null) return "&nbsp;";

        char[] strInChars = strIn.toCharArray();

        StringBuilder outBuffer = new StringBuilder();
        for(char c : strInChars){
            if(getTextArea(c)==null)
                outBuffer.append(c);
            else
                outBuffer.append(getTextArea(c));
        }
        return outBuffer.toString();
    }
    /**
     * Converte as quebras de texto escrito em textarea para as quebras de html.
     * @param strIn
     */
    public static String plainTextValue(String strIn){
        if(strIn == null) return "&nbsp;";

        char[] strInChars = strIn.toCharArray();

        StringBuilder outBuffer = new StringBuilder();
        for(char c : strInChars){
            if(plainTable.get(c)==null)
                outBuffer.append(c);
            else
                outBuffer.append(plainTable.get(c));
        }
        return outBuffer.toString();
    }

    public static String noHtmlTags(String in){
        return in.replaceAll("<"  ,"&lt;");
    }

}