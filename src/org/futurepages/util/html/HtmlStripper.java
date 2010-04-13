package org.futurepages.util.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Aplicação de filtro em strings originalmente HTML
 * 
 * @author leandro
 */
public class HtmlStripper {

	private String originalHTML;

	private HtmlStripper() {
	}

	public HtmlStripper(String htmlToStrip) {
		this.originalHTML = htmlToStrip;
	}

	//somente o texto sem as tags html é coletado
	public String poorText() {
		return originalHTML.replaceAll(HtmlRegex.tagsPattern(true), "");
	}

	/**
	 *
	 * @return somente parágrafos <p></p>, negrito, itálico e nada mais.
	 */
	public String plainText() {
		return richText(false, false, false, false, false, false);
	}

	/**
	 * 
	 * @param basicStyle it's allowed:  Bold, Italic, Underline
	 * @param advancedStyle it's allowed: Alignment, FontFamilies, Headers e CSS Classes
	 * @param lists it's allowed: UL OL LI tags
	 * @param image it's allowed: IMG tag
	 * @param anchor it's allowed: A tag
	 * @param table it's allowed: TABLE tag
	 * @return the stripped html
	 */
	public static void main(String[] args){
		System.out.println(new HtmlStripper("<!--oi!--><xml>ei!</xml><b>a       </b>b<p></p>c</p>d").richText(true, true, true, true, true, true));
	}

	public String richText(boolean basicStyle, boolean advancedStyle, boolean lists, boolean image, boolean anchor, boolean table) {

		String richText = noTrashText();

		Pattern tagsPattern = HtmlRegex.getCompiledTagsPattern();
		Matcher matcher = tagsPattern.matcher(richText);
		StringBuilder sb = new StringBuilder();

		int pos = 0;
		if (matcher.find()) {
			do {
				sb.append(richText.substring(pos, matcher.start()));

				String foundOne = richText.substring(matcher.start(),matcher.end());
//				sb.append("");

				
				pos = matcher.end();
				
			} while (matcher.find()); //varrendo tag a tag
		}
		if (pos < richText.length()) {
			sb.append(richText.substring(pos));
		}
		System.out.println(sb.toString());
		return sb.toString();
	}

	/**
	 * retira lixo (comentário, tag xml e tags vazias)
	 */
	public String noTrashText() {
		String xmlPattern = HtmlRegex.tagWithContentPattern("xml");
		String commentPattern = HtmlRegex.commentPattern();
		String emptyTagsPattern = HtmlRegex.emptyTagsPattern();

		return originalHTML.replaceAll(commentPattern  , "") //comentários
						   .replaceAll(xmlPattern      , "") //xml gerado pelo word
						   .replaceAll(emptyTagsPattern, "") //tags vazias
						   .replaceAll("  *"           , " ") //vários espaços viram um único
			    ;
	}
}
