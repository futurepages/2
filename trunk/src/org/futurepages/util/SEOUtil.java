package org.futurepages.util;

import java.util.HashMap;

/**
 * Utilidades para Search Engine Optimization
 * @author leandro
 */
public class SEOUtil {

    private static HashMap urlMapping    = new HashMap();
	private static HashMap urlMappingAlphabetics    = new HashMap();
	private static HashMap urlMappingEspeciais    = new HashMap();

    static{
        urlMappingEspeciais.put("-","_");
        urlMappingEspeciais.put(" ","-");
        urlMappingEspeciais.put("@","_");
        urlMappingEspeciais.put("º","");
        urlMappingEspeciais.put("!","");
        urlMappingEspeciais.put("?","");
        urlMappingEspeciais.put(".","");
        urlMappingEspeciais.put(",","");
        urlMappingEspeciais.put(":","");
        urlMappingEspeciais.put(";","");
        urlMappingEspeciais.put("(","");
        urlMappingEspeciais.put(")","");
        urlMappingEspeciais.put("'","");
        urlMappingEspeciais.put("\"","");
        urlMappingEspeciais.put("\\","");
        urlMappingEspeciais.put("/","-");
        urlMappingEspeciais.put("<","");
        urlMappingEspeciais.put(">","");
        urlMappingEspeciais.put("\t","");
        urlMappingEspeciais.put("\n","");
        urlMappingEspeciais.put("\r","");
        urlMappingEspeciais.put("%","");
        urlMappingEspeciais.put("Ø","");
        urlMappingEspeciais.put("ø","");
        urlMappingEspeciais.put("Ð","");
        urlMappingEspeciais.put("ð","");
        urlMappingEspeciais.put("Æ","");
        urlMappingEspeciais.put("æ","");
        
        urlMappingAlphabetics.put("á","a");
        urlMappingAlphabetics.put("â","a");
        urlMappingAlphabetics.put("à","a");
        urlMappingAlphabetics.put("å","a");
        urlMappingAlphabetics.put("ã","a");
        urlMappingAlphabetics.put("ä","a");
        urlMappingAlphabetics.put("é","e");
        urlMappingAlphabetics.put("ê","e");
        urlMappingAlphabetics.put("è","e");
        urlMappingAlphabetics.put("&","e");
        urlMappingAlphabetics.put("ë","e");
        urlMappingAlphabetics.put("í","i");
        urlMappingAlphabetics.put("î","i");
        urlMappingAlphabetics.put("ì","i");
        urlMappingAlphabetics.put("ï","i");
        urlMappingAlphabetics.put("ó","o");
        urlMappingAlphabetics.put("ô","o");
        urlMappingAlphabetics.put("ò","o");
        urlMappingAlphabetics.put("õ","o");
        urlMappingAlphabetics.put("ö","o");
        urlMappingAlphabetics.put("ú","u");
        urlMappingAlphabetics.put("û","u");
        urlMappingAlphabetics.put("ù","u");
        urlMappingAlphabetics.put("ü","u");
        urlMappingAlphabetics.put("Ç","c");
        urlMappingAlphabetics.put("ç","c");
        urlMappingAlphabetics.put("ñ","n");
		
		urlMapping.putAll(urlMappingEspeciais);
		urlMapping.putAll(urlMappingAlphabetics);
		
    }
    
    public static String get(char ch){
        return (String) urlMapping.get(String.valueOf(ch));
    }

	private static String getAlphabetics(char ch) {
		return (String) urlMappingAlphabetics.get(String.valueOf(ch));
	}
    
    public static String urlFormat(String strIn){
        if(Is.empty(strIn)) return "";

        strIn = strIn.toLowerCase().trim();
		StringBuffer outBuffer = new StringBuffer();

        for(int i = 0; i < strIn.length();i++){
            String urlFormat = get(strIn.charAt(i));
            if(urlFormat != null){
                outBuffer.append(urlFormat);
            }
            else{
                outBuffer.append(strIn.charAt(i));
            }
        }
        return outBuffer.toString();
    }

	public static String stringKeyValid(String strIn){
        if(Is.empty(strIn)) return "";

        strIn = strIn.toLowerCase().trim();
		StringBuffer outBuffer = new StringBuffer();

        for(int i = 0; i < strIn.length();i++){
            String urlFormat = getAlphabetics(strIn.charAt(i));
            if(urlFormat != null){
                outBuffer.append(urlFormat);
            }
            else{
                outBuffer.append(strIn.charAt(i));
            }
        }
        return outBuffer.toString();
    }

}