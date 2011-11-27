package org.futurepages.formatters;

import org.futurepages.util.html.HtmlMapChars;
import java.util.Locale;
import java.util.regex.Pattern;
import org.futurepages.core.formatter.Formatter;
import org.futurepages.util.StringUtils;
import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;
 
/**
 * Formata o texto escrito em textaerea em texto com quebras de linhas de html.
 */
 public class SmartTextFormatter implements Formatter {

	private Pattern urlPattern = Pattern.compile("(?i)\\b(?:(?:https?|ftp|file)://|www\\.|ftp\\.)[-A-Z0-9+&@#/%=~_|$?!:,\\.]*[A-Z0-9+&@#/%=~_|$]");

	private static final int MAX_CHARS = 28;

	@Override
 	public String format(Object value, Locale loc) {
        String result =  HtmlMapChars.plainTextValue((String) value);

		
		IterableString iter = new IterableString(urlPattern, result);
		StringBuilder sb     = new StringBuilder();
		String end = result;
		for (MatchedToken token : iter) {
			sb.append(token.getBefore());
			String url = token.getMatched();
			sb.append("<a href=\"")
			  .append(url)
			  .append("\" target=\"_blank\" title=\"").append(url).append("\">")
			  .append(shortUrl(url))
			  .append("</a>");
			end = token.getAfter();
		}
		sb.append(end);
		return sb.toString();
 	}

	private String shortUrl(String url){
		if(url.startsWith("http://")){
			url = url.substring(7);
		}else if(url.startsWith("https://")){
			url = url.substring(8);
		}
		if(url.length()>MAX_CHARS){
			if(url.length()<=MAX_CHARS+8){
				return url;
			}else{
				String sufix = url.substring(url.length()-8,url.length());
				return StringUtils.concat(url.substring(0, MAX_CHARS),"...",sufix);
			}
		}else{
			return url;
		}
	}
 }