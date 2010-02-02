package org.futurepages.util;

import java.util.Map;

public class StringUtils {
    
    public static boolean isNotEmpty(String s) {
        return s != null && s.length() > 0;
    }
    
    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }
    
    
	public static String replace(String strIn, Map<Character, String> dirt) {
		if(Is.empty(strIn)) return "";

        StringBuffer outBuffer = new StringBuffer();
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