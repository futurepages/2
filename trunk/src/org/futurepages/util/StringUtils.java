package org.futurepages.util;

import java.util.Map;

public class StringUtils {

	public static String concat(String... args){
		StringBuilder sb = new StringBuilder();
		for (String string : args) {
			sb.append(string);
		}
		return sb.toString();
	}
	
	public static String concatWith(String inserted, String... array) {
		if(inserted == null){
			return concat(array);
		}else{
			StringBuffer out = new StringBuffer();
			for (int i = 0; i < array.length; i++) {
				if (i != 0) {
					out.append(inserted);
				}
				out.append(array[i]);
			}
			return out.toString();
		}
	}
	
	public static boolean isNotEmpty(String s) {
		return s != null && s.length() > 0;
	}

	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}


	public static String replace(String strIn, Map<Character, String> dirt) {
		if(Is.empty(strIn)) return "";

		StringBuilder outBuffer = new StringBuilder();
		String clean;
		for (Character charDirty : strIn.toCharArray()) {

			if(dirt.containsKey(charDirty)){
				clean = dirt.get(charDirty);
			}else{
				clean = charDirty.toString();
			}
			outBuffer.append(clean);
		}
		return outBuffer.toString();
	}


}