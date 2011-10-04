package org.futurepages.util.html;

import static org.futurepages.util.html.HtmlRegex.attrPattern;
import static org.futurepages.util.html.HtmlRegex.commentPattern;
import static org.futurepages.util.html.HtmlRegex.getCompiledTagsPattern;
import static org.futurepages.util.html.HtmlRegex.invalidAttrPattern;
import static org.futurepages.util.html.HtmlRegex.spanWithStylePropertiePattern;
import static org.futurepages.util.html.HtmlRegex.tagAndContentPattern;
import static org.futurepages.util.html.HtmlRegex.tagWithContentReplacement;
import static org.futurepages.util.html.HtmlRegex.tagsPattern;

import java.util.regex.Pattern;

import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;

/**
 * Aplicação de filtro em strings originalmente HTML
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

	//somente o texto sem as tags html é coletado
	public String poorText() {
		return strippedHtml.replaceAll(tagsPattern(true), "");
	}

	/**
	 *
	 * @return somente parágrafos <p></p>, negrito <strong>, itálico <em>, sublinhado <span style="text-decoration:underline"> e nada mais.
	 */
	public String plainText() {
		return richText(false, false, false, false, false);
	}

	public String richText(boolean styles, boolean lists, boolean image, boolean anchor, boolean table) {
		if(!styles){
			strippedHtml = noStylesText(originalHtml);
		} else {
			strippedHtml = noTrashText(originalHtml);
		}

		HtmlTagReplacer tagRep = new HtmlTagReplacer(styles, lists, image, anchor, table);
		Pattern tagsPattern  = getCompiledTagsPattern();
		IterableString iter = new IterableString(tagsPattern, strippedHtml);
		StringBuilder sb     = new StringBuilder();
		String end = strippedHtml;
		for (MatchedToken token : iter) {
			sb.append(token.getBefore());
			sb.append(tagRep.treated(token.getMatched()));
			end = token.getAfter();
		}
		sb.append(end);
		return sb.toString();
	}

	/**
	 * Substitui texto 'str' encontrado somente dentro das tags pela regex
	 * com o valor do atributo 'replacement'
	 *
	 * @param str texto html a ser varrido
	 * @param regex padrão para substituição
	 * @param replacement novo valor
	 *
	 * @return texto substituído
	 */
	public static String replaceInTags(String str, String regex, String replacement){


		Pattern tagsPattern  = getCompiledTagsPattern();
		IterableString iter = new IterableString(tagsPattern, str);

		String end = str;
		Pattern regexPattern = Pattern.compile(regex);
		StringBuilder sb     = new StringBuilder();
		for (MatchedToken token : iter) {
			sb.append(token.getBefore());
			sb.append(regexPattern.matcher(token.getMatched()).replaceAll(replacement));
			end = token.getAfter();
		}
		sb.append(end);
		return sb.toString();
	}

	/**
	 * retira lixo do html:
	 * 1) remove comentários
	 * 2) remove tag xml gerada pelo word
	 * 3) remove html script (javascript por exemplo)
	 * 4) remove tags vazias
	 * 5  troca aspas simples por aspas duplas dentro das tags
	 * 6) remove atributos inválidos
	 */
	public static String noTrashText(String htmlContent) {
		String commentPattern   = commentPattern();
		String xmlPattern       = tagAndContentPattern("xml");
		String headPattern       = tagAndContentPattern("head");
		String scriptPattern    = tagAndContentPattern("script");


		htmlContent =htmlContent.replaceAll(commentPattern   ,  ""); //remove comentários
		htmlContent =htmlContent.replaceAll(xmlPattern       ,  ""); //remove tag xml gerada pelo word
		htmlContent =htmlContent.replaceAll(headPattern      ,  ""); //remove tag xml gerada pelo word
		htmlContent =htmlContent.replaceAll(scriptPattern    ,  ""); //remove html script (javascript por exemplo)

		//Comentado - decidiu-se não tirar tags vazias
		//String emptyTagsPattern = emptyTagsPattern();
		//htmlContent =htmlContent.replaceAll(emptyTagsPattern ,  ""); //remove tags vazias

		htmlContent = replaceInTags(htmlContent, "'" , "\"");                 //aspas simples por aspas duplas
		htmlContent = replaceInTags(htmlContent, invalidAttrPattern()  ,  "");//atributos inválidos

		return htmlContent;
	}

	/**
	 * 0) Retira-se o lixo utilizando-se do método noTrashText
	 * 1) Retira-se as tags [style] com seu conteúdo
	 * 2) Retira os atributos "style" e "class" das tags com duas ressalvas:
	 *  - [span style="font-weight:bold"] é substituído e [strong]
	 *  - [span style="text-decoration:underline"] é substituído por [u]
	 * @return texto sem estilo, exceto estilos com bold e underline
	 */
	public static String noStylesText(String htmlContent) {
		htmlContent = noTrashText(htmlContent);
		htmlContent = htmlContent.replaceAll(tagAndContentPattern("style") , "");

		htmlContent = replaceInTags(htmlContent, attrPattern("class"), "");
		htmlContent = htmlContent.replaceAll(spanWithStylePropertiePattern("font-weight","bold"),tagWithContentReplacement("strong"));    //estilizados com negrito
		htmlContent = htmlContent.replaceAll(spanWithStylePropertiePattern("text-decoration","underline"),tagWithContentReplacement("u"))//estilizados com sublinhado
		;
		htmlContent = replaceInTags(htmlContent, attrPattern("style"), "");
		return htmlContent;
	}
}