package org.futurepages.util.html;

import org.futurepages.util.StringUtils;
import static org.futurepages.util.html.HtmlRegex.*;

/**
 *
 * @author Leandro
 *
 *	<span style="white-space: pre;"> </span> é gerado a cada tab dado dentro de um parágrafo.
 *	utilizar algum tipo de substituição por text-indent
 *	------------------------------------------------
 *	<cite> e <var> sao mantidos quando list é true
 *
 */
public class AlternativeHtmlTagReplacer extends HtmlTagReplacer {

	private String internalAnchors;
	private boolean styles;

	private AlternativeHtmlTagReplacer() {
	}

	public AlternativeHtmlTagReplacer(String internalAnchors, boolean styles) {
		this.internalAnchors = internalAnchors;
		this.styles = styles;
	}

	/**
	 * Se internalAnchors == null , significa que não manteremos os links (serão eliminados)
	 */
	public void init() {

		//STYLES GERAIS
		reduce("br");
		reduce("strong");
		reduce("em");
		reduce("i", "em");

		reduce("u", "span " + STYLE_UNDERLINE, "span");
		reduce("address", "em");
		reduce("b", "strong");
		reduce("big", "strong");
		replace("h1", P_STRONG_OPEN, P_STRONG_CLOSE);
		replace("h2", P_STRONG_OPEN, P_STRONG_CLOSE);
		replace("h3", P_STRONG_OPEN, P_STRONG_CLOSE);
		replace("h4", P_STRONG_OPEN, P_STRONG_CLOSE);
		replace("h5", P_STRONG_OPEN, P_STRONG_CLOSE);
		replace("h6", P_STRONG_OPEN, P_STRONG_CLOSE);

		if(styles){
			keep("div", "p");
			keep("p");
		}else{
			reduce("div", "p");
			reduce("p");
		}

		//LISTS
		reduce("ul");
		reduce("ol");
		reduce("li");
		reduce("blockote");
		reduce("cite");
		reduce("var");
		reduce("code");
		reduce("samp");
		reduce("kbd");
		reduce("dfn");

		//reduce("img", attrs("src","alt"));

		//âncoras
		if (internalAnchors != null) {
			if (styles) {
				keep("a");
			} else {
				reduce("a", attrs("href", "target"));
			}
		} else {
			reduce("a", "span " + STYLE_UNDERLINE, "span");
		}


		//tabelas
		reduce("tr", "p");
		reduce("th", "p");
		reduce("thead", "p");
		reduce("tfoot", "p");
		replace("td", PLAIN_LI, BREAK_LINE);
	}

	@Override
	public String beforeTreatment(String htmlContent) {
		htmlContent = noTrashText(htmlContent);
		htmlContent = htmlContent.replaceAll(tagAndContentPattern("style") , "");
		htmlContent = replaceInTags(htmlContent, attrPattern("class"), "");
		htmlContent = htmlContent.replaceAll(spanWithStylePropertiePattern("font-weight","bold"),tagWithContentReplacement("strong"));    //estilizados com negrito
		htmlContent = htmlContent.replaceAll(spanWithStylePropertiePattern("text-decoration","underline"),tagWithContentReplacement("u"));//estilizados com sublinhado
		if(!styles){
			htmlContent = replaceInTags(htmlContent, attrPattern("style"), "");
		}else {
			//indentação de parágrafo.
			htmlContent = htmlContent.replaceAll("<p><span style=\"white-space: pre;\"> </span>","<p style=\"text-indent: 30px\">");
		}
		return htmlContent;

	}

	@Override
	public String afterTreatment(String treatedHtml) {
		return super.afterTreatment(treatedHtml);
	}

	/**
	 *
	 * @param tag tanto de abertura como de fechamento, com os limitadores <>
	 * @return retorna a tag convertida
	 */
	@Override
	public String treated(String tag) {
		if (StringUtils.isEmpty(tag)) {
			return "";
		}
		boolean isClosing = isClosingTag(tag);
		String[] tagParts = tagParts(tag, isClosing);
		String treated;
		TagReplacement tagRep = getReplacement(tagParts[0]);
		if (tagRep == null) {
			treated = "";
		} else {
			treated = treat(tagParts, isClosing, tagRep);
		}
		return treated;
	}
}
