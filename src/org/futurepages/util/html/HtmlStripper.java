package org.futurepages.util.html;

/**
 * HQL - Aplicação de filtro em strings originalmente HTML
 * 
 * @author leandro
 */
public class HtmlStripper {


	//somente o texto sem as tags html é coletado
	public static String poorText(String htmlText){
		return htmlText.replaceAll(HtmlRegex.tagsPattern(true), "");
	}

	//somente parágrafos <p></p>
	//plain text - texto somente com parágrafos <p></p> e nada mais.
	public static String plainText(String htmlText){
		return richText(false, false, false, false, false, false);
	}

	/**
	 * 
	 * @param basicStyle it's allowed:  Bold, Italic, Underline
	 * @param advancedStyle it's allowed: Alignment, FontFamilies, Headers
	 * @param lists it's allowed: UL OL LI tags
	 * @param image it's allowed: IMG tag
	 * @param anchor it's allowed: A tag
	 * @param table it's allowed: TABLE tag
	 * @return the stripped html
	 */
	public static String richText(boolean basicStyle, boolean advancedStyle, boolean lists, boolean image, boolean anchor, boolean table){
		return "";
	}
}