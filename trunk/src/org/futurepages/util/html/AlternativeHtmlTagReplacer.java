package org.futurepages.util.html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.futurepages.util.Is;
import org.futurepages.util.StringUtils;
import org.futurepages.util.The;
import org.futurepages.util.iterator.string.IterableString;
import org.futurepages.util.iterator.string.MatchedToken;
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


	private String host;
	private boolean styles;
	private static final int MAX_CHARS = 28;

	private AlternativeHtmlTagReplacer() {
	}


	public AlternativeHtmlTagReplacer(String host, boolean styles) {
		this.styles = styles;
		this.host=host;
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
		if (host != null) {
			if (styles) {
				keep("a");
			} else {
				reduce("a", attrs("href", "target"));
			}
		}
//		else {
//			reduce("a", "span " + STYLE_UNDERLINE, "span");
//		}


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
		//Tratamento da anchor
		Pattern tagsPattern = HtmlRegex.getCompiledTagsWithContentPattern("a");
		IterableString iter = new IterableString(tagsPattern, treatedHtml);
		StringBuilder sb = new StringBuilder();
		String end = treatedHtml;
		for (MatchedToken token : iter) {
			sb.append(token.getBefore());
			sb.append(treatedAnchor(token.getMatched()));
			end = token.getAfter();
		}
		sb.append(end);
		return sb.toString();
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

	public String treatedAnchor(String tagA){
		
		Pattern linkPattern = Pattern.compile("(?i)(?s)(<a(\\s+).*?>)(.*?)(</a>)");
		Matcher matcher = linkPattern.matcher(tagA);
		StringBuilder sb = new StringBuilder();
		if (matcher.find()) {
			String parteAbertura = matcher.group(1);
			String conteudo = matcher.group(3).trim();
			String parteFechar = matcher.group(4);

			IterableString iter = new IterableString(Pattern.compile(HtmlRegex.attrsPattern()), parteAbertura);

			sb.append("<a ");
			for (MatchedToken tokenOpen : iter) {
				String[] group = tokenOpen.getMatched().split("\\s*=",2);
				String attr = group[0].trim();
				String url = group[1].trim().replaceAll("(\")", "");
				if (attr.equalsIgnoreCase("href")) {
					if (url.startsWith(host)) {
						sb.append("href=\"").append(url.replaceAll(host, "")).append("\" ");
					} else {
						if (!url.startsWith("javascript")) {
							sb.append("href=\"").append(url).append("\" ").append(TARGET_BLANK).append(" ");
						}
					}
					if (attr.equalsIgnoreCase("href") && url.equalsIgnoreCase(conteudo)) {
						conteudo = shortUrl(conteudo);
						if(!tagA.contains("title=\"")){
							sb.append("title=\"").append(url).append("\" ");
						}
					}
				}
				else if (styles && !attr.equalsIgnoreCase("target") && !attr.equalsIgnoreCase("title")) {
					sb.append(tokenOpen.getMatched());
				}
				
				if(attr.equalsIgnoreCase("title") && !sb.toString().contains("title=\"") ){
					sb.append(tokenOpen.getMatched());
				}
			}
			sb.append(">").append(conteudo).append(parteFechar);

			return sb.toString();
		}
		else{
			Matcher matcherOutro = Pattern.compile("(?i)(?s)(<a>)(.*?)(</a>)").matcher(tagA);
			return matcherOutro.group(2);
		}
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
