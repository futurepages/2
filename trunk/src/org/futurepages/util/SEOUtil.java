package org.futurepages.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilidades para Search Engine Optimization
 * @author leandro
 * @changes Danilo
 */
public class SEOUtil {

    private static final Map<Character, String> urlDirt    = new HashMap<Character, String>();
	private static final Map<Character, String> alphabetics    = new HashMap<Character, String>();
	private static final Map<Character, String> specials    = new HashMap<Character, String>();

    static{
    	String empty =  "";
        specials.put('-',"_");
        specials.put(' ',"-");
        specials.put('@',"_");
        specials.put('�', empty);
        specials.put('!', empty);
        specials.put('?',  empty);
        specials.put('.', empty);
        specials.put(',', empty);
        specials.put(':', empty);
        specials.put(';', empty);
        specials.put('(', empty);
        specials.put(')', empty);
        specials.put('\'', empty);
        specials.put('"', empty);
        specials.put('\\', empty);
        specials.put('/', "-");
        specials.put('<', empty);
        specials.put('>', empty);
        specials.put('\t',empty);
        specials.put('\n',empty);
        specials.put('\r',empty);
        specials.put('%',empty);
        specials.put('�',empty);
        specials.put('�',empty);
        specials.put('�',empty);
        specials.put('�',empty);
        specials.put('�',empty);
        specials.put('�',empty);
        
        alphabetics.put('�',"a");
        alphabetics.put('�',"a");
        alphabetics.put('�',"a");
        alphabetics.put('�',"a");
        alphabetics.put('�',"a");
        alphabetics.put('�',"a");
        alphabetics.put('�',"e");
        alphabetics.put('�',"e");
        alphabetics.put('�',"e");
        alphabetics.put('&',"e");
        alphabetics.put('�',"e");
        alphabetics.put('�',"i");
        alphabetics.put('�',"i");
        alphabetics.put('�',"i");
        alphabetics.put('�',"i");
        alphabetics.put('�',"o");
        alphabetics.put('�',"o");
        alphabetics.put('�',"o");
        alphabetics.put('�',"o");
        alphabetics.put('�',"o");
        alphabetics.put('�',"u");
        alphabetics.put('�',"u");
        alphabetics.put('�',"u");
        alphabetics.put('�',"u");
        alphabetics.put('�',"c");
        alphabetics.put('�',"c");
        alphabetics.put('�',"n");
		
		urlDirt.putAll(specials);
		urlDirt.putAll(alphabetics);
		
    }
    
    public static String get(Character ch){
        return  urlDirt.get(ch).toString();
    }

    /**
     * Devolve a string informada sem acentuacao e pontuacao
     * @see urlMappingAlphabetics
     * @see urlMappingEspeciais
     */
	public static String replaceSpecialAlphas(String strIn){
        strIn = strIn.toLowerCase().trim();
        return StringUtils.replace(strIn, alphabetics);
    }

	
	/**
	 * Devolve a string informada sem acentuacao, pontuacao e sem caracteres especiais
	 * @see urlMappingAlphabetics
	 * @see urlMappingEspeciais
	 */
	public static String urlFormat(String strIn){
		strIn = strIn.toLowerCase().trim();
		return StringUtils.replace(strIn, urlDirt);
	}

	public static String noAccent(String in){
		StringBuffer out = new StringBuffer();
		char[] chars = in.toCharArray();
		for(char ch : chars){
			String newChar = alphabetics.get(ch);
			if(newChar!=null){
				out.append(newChar);
			}else {
				out.append(ch);
			}
		}
		return out.toString();
	}
}