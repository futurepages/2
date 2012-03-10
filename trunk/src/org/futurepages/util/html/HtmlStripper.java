package org.futurepages.util.html;

import java.util.regex.Pattern;

import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;

/**
 * Aplica��o de filtro em strings originalmente HTML
 *
 * @author Leandro
 */
public class HtmlStripper {

	private String originalHtml;
	private String strippedHtml;

	public HtmlStripper(String htmlToStrip) {
		this.originalHtml = htmlToStrip;
		this.strippedHtml = htmlToStrip;
	}

	//somente o texto sem as tags html � coletado
	public String poorText() {
		return strippedHtml.replaceAll(HtmlRegex.tagsPattern(true), "");
	}

	/**
	 *
	 * @return somente par�grafos <p></p>, negrito <strong>, it�lico <em>, sublinhado <span style="text-decoration:underline"> e nada mais.
	 */
	public String plainText() {
		return richText(false, false, false, false, false);
	}

	public String treated(HtmlTagReplacer tagsReplacer) {

		tagsReplacer.init();
		strippedHtml = tagsReplacer.beforeTreatment(originalHtml);
		Pattern tagsPattern  = HtmlRegex.getCompiledTagsPattern();
		IterableString iter = new IterableString(tagsPattern, strippedHtml);
		StringBuilder sb     = new StringBuilder();
		String end = strippedHtml;
		for (MatchedToken token : iter) {
			sb.append(token.getBefore());
			sb.append(tagsReplacer.treated(token.getMatched()));
			end = token.getAfter();
		}
		sb.append(end);
		
		return tagsReplacer.afterTreatment(sb.toString());
	}

	public String richText(boolean styles, boolean lists, boolean image, boolean anchor, boolean table) {
		return treated(new RichTextTagReplacer(styles, lists, image, anchor, table));
	}
}