package org.futurepages.util;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HQL - Aplicação de filtro em strings originalmente HTML
 * 
 * @author leandro
 */
public class HTMLStripUtil {

	public static String simplePs(String htmlOriginal) {
		String patternStr = "(<(/?[.*^p].*)?>)?";
		Pattern pattern = Pattern.compile(patternStr);
//		Matcher aspasMatcher = aspasPattern.matcher(htmlOriginal);

		return htmlOriginal.replaceAll(patternStr,"");

//		while(aspasMatcher.find()){ //palavras entre aspas
//			String foundOne = htmlOriginal.substring(aspasMatcher.start()+1,aspasMatcher.end()-1);
//		}
//
//		return hqlQueryBuffer.toString();
	}
}