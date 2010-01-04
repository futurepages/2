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
        urlMappingEspeciais.put("�","");
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
        urlMappingEspeciais.put("�","");
        urlMappingEspeciais.put("�","");
        urlMappingEspeciais.put("�","");
        urlMappingEspeciais.put("�","");
        urlMappingEspeciais.put("�","");
        urlMappingEspeciais.put("�","");
        
        urlMappingAlphabetics.put("�","a");
        urlMappingAlphabetics.put("�","a");
        urlMappingAlphabetics.put("�","a");
        urlMappingAlphabetics.put("�","a");
        urlMappingAlphabetics.put("�","a");
        urlMappingAlphabetics.put("�","a");
        urlMappingAlphabetics.put("�","e");
        urlMappingAlphabetics.put("�","e");
        urlMappingAlphabetics.put("�","e");
        urlMappingAlphabetics.put("&","e");
        urlMappingAlphabetics.put("�","e");
        urlMappingAlphabetics.put("�","i");
        urlMappingAlphabetics.put("�","i");
        urlMappingAlphabetics.put("�","i");
        urlMappingAlphabetics.put("�","i");
        urlMappingAlphabetics.put("�","o");
        urlMappingAlphabetics.put("�","o");
        urlMappingAlphabetics.put("�","o");
        urlMappingAlphabetics.put("�","o");
        urlMappingAlphabetics.put("�","o");
        urlMappingAlphabetics.put("�","u");
        urlMappingAlphabetics.put("�","u");
        urlMappingAlphabetics.put("�","u");
        urlMappingAlphabetics.put("�","u");
        urlMappingAlphabetics.put("�","c");
        urlMappingAlphabetics.put("�","c");
        urlMappingAlphabetics.put("�","n");
		
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