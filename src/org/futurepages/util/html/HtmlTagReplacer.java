package org.futurepages.util.html;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.futurepages.util.StringUtils.concat;

/**
 *
 * @author Leandro
 */
public class HtmlTagReplacer {

	private String PLAIN_LI = "&nbsp; - ";
	private String STYLE_UNDERLINE = "style=\"text-decoration:underline;\"";
	private String BREAK_LINE = "<br/>";
	private String P_STRONG_OPEN  = "<p><strong>";
	private String P_STRONG_CLOSE = "</strong></p>";

	private Pattern NON_WORD_REGEX_PATTERN;
	private boolean styles;
	private boolean lists;
	private boolean image;
	private boolean anchor;
	private boolean table;
	private Map<String, TagReplacement> tagsToCare = new HashMap<String, TagReplacement>();

	public HtmlTagReplacer(boolean styles, boolean lists, boolean image, boolean anchor, boolean table) {
		initParams(styles, lists, image, anchor, table);
		makeTagsToCare();
	}

	private void initParams(boolean styles, boolean lists, boolean image, boolean anchor, boolean table) {
		this.styles = styles;
		this.lists = lists;
		this.image = image;
		this.anchor = anchor;
		this.table = table;
		NON_WORD_REGEX_PATTERN = Pattern.compile("\\W");
	}

	/**
	 * Ao construir-se o replacer, serão adicionados ao mapa as tags a serem
	 * tratadas de acordo com os parâmetros.
	 *
	 * Formas de tratar as tags:
	 * 1) KEEP    - Mantém toda a estrutura da tag, modificando somente o
	 *				identificador passado como parâmetro.
	 *    -> ex.: in = <a href="#">  keep("a","b")  ==>  <b href="#">
	 * 2) REDUCE  - reduz a estrutura de atributos da tag
	 *     -> ex.: in = <a href="#">  reduce("a","b")  ==>  <b>
	 * 3) REPLACE - substitui toda a tag pelo conteúdo passado por parâmetro
	 *     -> ex.: in = <a href="#">  reduce("a","b")  ==>  b
	 *
	 */
	private void makeTagsToCare() {
		reduce("br");
		if (styles) {
			keep("div");
			keep("p");
			keep("strong", "strong");
			keep("em");

			keep("i"   , "em");
			keep("cite", "em");
			keep("var" , "em");
			keep("code", "em");
			keep("samp", "em");
			keep("kbd" , "em");
			keep("dfn" , "em");

			keep("u", "span " + STYLE_UNDERLINE, "span");
			keep("address", "em");
			keep("b", "strong");
			keep("big", "strong");
			keep("h1");
			keep("h2");
			keep("h3");
			keep("h4");
			keep("h5");
			keep("h6");

		} else {
			reduce("div","p");
			reduce("p");
			reduce("strong");

			reduce("em");
			reduce("i"   , "em");
			reduce("cite", "em");
			reduce("var" , "em");
			reduce("code", "em");
			reduce("samp", "em");
			reduce("kbd" , "em");
			reduce("dfn" , "em");

			reduce("u", "span " + STYLE_UNDERLINE, "span");
			reduce("address", "em");
			reduce("b", "strong");
			reduce("big", "strong");
			replace("h1", P_STRONG_OPEN , P_STRONG_CLOSE);
			replace("h2", P_STRONG_OPEN , P_STRONG_CLOSE);
			replace("h3", P_STRONG_OPEN , P_STRONG_CLOSE);
			replace("h4", P_STRONG_OPEN , P_STRONG_CLOSE);
			replace("h5", P_STRONG_OPEN , P_STRONG_CLOSE);
			replace("h6", P_STRONG_OPEN , P_STRONG_CLOSE);
		}

		if (lists) {
			if (styles) {
				keep("ul");
				keep("ol");
				keep("li");
				keep("blockote");
				keep("cite");
				keep("var");
				keep("code");
				keep("samp");
				keep("kbd");
				keep("dfn");
			} else {
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
			}
		} else { // !lists
			reduce("ul", "p");
			reduce("ol", "p");
			replace("li", PLAIN_LI, BREAK_LINE);
		}

		if (image) {
			keep("object");
			keep("embed");
			keep("param");
			keep("iframe");
			if(styles){
				keep("img");
			} else {
				reduce("img", attrs("src","alt"));
			}
		}

		if (anchor) {
			if(styles){
				keep("a");
			} else {
				reduce("a",attrs("href","target"));
			}
		} else {
			reduce("a", "span "+STYLE_UNDERLINE, "span");
		}

		if (table) {
			if (styles) {
				keep("table");
				keep("caption");
				keep("tbody");
				keep("thead");
				keep("tfoot");
				keep("tr");
				keep("th");
				keep("td");
			} else {
				reduce("table");
				reduce("caption");
				reduce("tbody");
				reduce("thead");
				reduce("tfoot");
				reduce("tr", attrs("rowspan"));
				reduce("th", attrs("colspan"));
				reduce("td", attrs("colspan"));
			}
		} else { //!table
			if (styles) {
				keep("tr",    "p");
				keep("th",    "p");
				keep("thead", "p");
				keep("tfoot", "p");
			} else {
				reduce("tr",    "p");
				reduce("th",    "p");
				reduce("thead", "p");
				reduce("tfoot", "p");
			}
			replace("td", PLAIN_LI, BREAK_LINE);
		}
	}

	/**
	 *
	 * @param tag tanto de abertura como de fechamento, com os limitadores <>
	 * @return retorna a tag convertida de acordo com os parâmetros passados.
	 */
	public String treated(String tag) {
		boolean isClosing = isClosingTag(tag);
		String[] tagParts = tagParts(tag, isClosing);
		String treated ;
		TagReplacement tagRep = tagsToCare.get(tagParts[0]);
		if (tagRep == null) {
			treated = "";
		}else{
			treated = treat(tagParts, isClosing, tagRep);
		}
		return treated;
	}

	private String treat(String[] tagParts, boolean closing, TagReplacement tagRep) {
		if (tagRep.reduce == null) {
			return closing ? tagRep.close : tagRep.open;
		} else {
			return tagRep.execute(tagParts, closing);
		}
	}

	private String[] tagParts(String tag, boolean closing) {
		int start = (closing) ? 2 : 1;
		String tagId = NON_WORD_REGEX_PATTERN.split(tag.substring(start))[0].toLowerCase();


		int end = (tag.charAt(tag.length()-2)=='/') ? 1 : 0;

		String attributes = tag.substring(start+tagId.length() ,tag.length() - end-1);
		String endFlag = (end==1)?"/>":">";

		return new String[]{tagId,attributes,endFlag};
	}

	private boolean isClosingTag(String tag) {
		return tag.charAt(1) == '/';
	}

	private void register(String tagId, TagReplacement tagReplacement) {
		tagsToCare.put(tagId, tagReplacement);
	}

	// REPLACE ###################################
	private void replace(String tagIdFrom, String tagIdTo) {
		register(tagIdFrom, new TagReplacement(tagIdTo, tagIdTo, null));
	}

	private void replace(String tagIdFrom, String tagIdToOpen, String tagIdToClose) {
		register(tagIdFrom, new TagReplacement(tagIdToOpen, tagIdToClose, null));
	}

	// KEEP #####################################
	private void keep(String tagId) {
		register(tagId, new TagReplacement(tagId, tagId, false));
	}

	private void keep(String tagIdFrom, String tagIdTo) {
		register(tagIdFrom, new TagReplacement(tagIdTo, tagIdTo, false));
	}

	private void keep(String tagIdFrom, String tagIdToOpen, String tagIdToClose) {
		register(tagIdFrom, new TagReplacement(tagIdToOpen, tagIdToClose, false));
	}

	private void keep(String tagId, String[] tagsAttributes) {
		register(tagId, new TagReplacement(tagId, tagId, false, tagsAttributes));
	}

	private void keep(String tagIdFrom, String tagIdTo, String[] tagsAttributes) {
		register(tagIdFrom, new TagReplacement(tagIdTo, tagIdTo, false, tagsAttributes));
	}

	private void keep(String tagIdFrom, String tagIdToOpen, String tagIdToClose, String[] tagsAttributes) {
		register(tagIdFrom, new TagReplacement(tagIdToOpen, tagIdToClose, false, tagsAttributes));
	}

	// REDUCE #####################################
	private void reduce(String tagId) {
		register(tagId, new TagReplacement(tagId, tagId, true));
	}

	private void reduce(String tagIdFrom, String tagIdTo) {
		register(tagIdFrom, new TagReplacement(tagIdTo, tagIdTo, true));
	}

	private void reduce(String tagIdFrom, String tagToOpen, String tagToClose) {
		register(tagIdFrom, new TagReplacement(tagToOpen, tagToClose, true));
	}

	private void reduce(String tagId, String[] tagsAttributes) {
		register(tagId, new TagReplacement(tagId, tagId, true, tagsAttributes));
	}

	private void reduce(String tagIdFrom, String tagIdTo, String[] tagsAttributes) {
		register(tagIdFrom, new TagReplacement(tagIdTo, tagIdTo, true, tagsAttributes));
	}

	private void reduce(String tagIdFrom, String tagToOpen, String tagToClose, String[] tagsAttributes) {
		register(tagIdFrom, new TagReplacement(tagToOpen, tagToClose, true, tagsAttributes));
	}

	private String[] attrs(String... attrIds){
		return attrIds;
	}
}

class TagReplacement {

	Boolean reduce;
	String open;
	String close;
	String[] attributesToCare;

	TagReplacement(String open, String close, Boolean reduce) {
		this.open = open;
		this.close = close;
		this.reduce = reduce;
	}

	TagReplacement(String open, String close, Boolean reduce, String[] attributesToKeep) {
		this(open, close, reduce);
		this.attributesToCare = attributesToKeep;
	}

	String execute(String[] tagParts, boolean closing) {
		if (!closing) {
			return concat("<", this.open , careAttrs(tagParts[1]) , tagParts[2]);
		} else {
			return concat("</", this.close , tagParts[2]);
		}
	}

	String careAttrs(String tagSecondPart){
		if(attributesToCare==null){
			if(reduce){
				return "";
			}
		} else { //has attributes to care
			if(reduce){ //need to keep some attributes when reducing
				String attributes = "";

				for(String attr : attributesToCare){
					Matcher matcher = Pattern.compile(regexAttr(attr)).matcher(tagSecondPart);
					if(matcher.find()){
						String foundOne = tagSecondPart.substring(matcher.start(),matcher.end());
						attributes = concat(attributes,(foundOne.charAt(0)==' ')?"":" ",foundOne);
					}
				}
				tagSecondPart = attributes;
			}else{ //need to remove some attributes when keeping
				for(String attr : attributesToCare){
					tagSecondPart = tagSecondPart.replaceAll(regexAttrWithGroups(attr), "$1$2");
				}
			}
		}
		return tagSecondPart;
	}

	private String regexAttr(String attr){
		return HtmlRegex.attrPattern(attr);
	}
	private String regexAttrWithGroups(String attr){
		return HtmlRegex.attrPatternWithGroups(attr);
	}
}