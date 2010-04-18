package org.futurepages.util.html;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import static org.futurepages.util.StringUtils.concat;

/**
 *
 * @author leandro
 */
public class HtmlTagReplacer {

	private String PLAIN_LI        = "&nbsp; - ";
	private String STYLE_UNDERLINE = "style=\"text-decoration:underline;\"";
	private String BREAK_LINE = "<br/>";

	private Pattern NON_WORD_REGEX_PATTERN;

	private boolean styles;
	private boolean lists;
	private boolean image;
	private boolean anchor;
	private boolean table;

	private Map<String, TagReplacement>  tagsToCare = new HashMap<String, TagReplacement>();

	public HtmlTagReplacer(boolean styles, boolean lists, boolean image, boolean anchor, boolean table) {
		initParams(styles, lists, image, anchor, table);
		makeTagsToCare();
	}

	private void initParams(boolean styles, boolean lists, boolean image, boolean anchor, boolean table) {
		this.styles = styles;
		this.lists  = lists;
		this.image  = image;
		this.anchor = anchor;
		this.table  = table;
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
		reduce("strong","strong");
		reduce("i", "em");
		reduce("u", "span "+STYLE_UNDERLINE, "span");
		reduce("address", "em");
		reduce("b", "strong");
		reduce("big", "strong");

		if (styles) {
			keep("p");
			keep("h1");
			keep("h2");
			keep("h3");
			keep("h4");
			keep("h5");
			keep("h6");

		} else {
			reduce("p");
			reduce("h1", "p");
			reduce("h2", "p");
			reduce("h3", "p");
			reduce("h4", "p");
			reduce("h5", "p");
			reduce("h6", "p");
		}

		if (lists) {
			if (styles) {
				keep("ul");
				keep("ol");
				keep("li");
			} else {
				reduce("ul");
				reduce("ol");
				reduce("li");
			}
		} else {
			reduce("ul", "p");
			reduce("ol", "p");
			replace("li", PLAIN_LI, BREAK_LINE);
		}

		if (image) {
			keep("img");
		}

		if (anchor) {
			keep("a");
		} else {
			reduce("a", "span " + STYLE_UNDERLINE, "span");
		}

		if (table) {
			if(styles){
				keep("table");
				keep("tr");
				keep("th");
				keep("thead");
				keep("tfoot");
				keep("td");
			} else {
				reduce("table");
				reduce("tr");
				reduce("th");
				reduce("thead");
				reduce("tfoot");
				reduce("td");
			}
		} else { //!table
			if(styles){
				keep("tr"   ,"p");
				keep("th"   ,"p");
				keep("thead","p");
				keep("tfoot","p");
			}else{
				reduce("tr"   ,"p");
				reduce("th"   ,"p");
				reduce("thead","p");
				reduce("tfoot","p");

			}
			replace("td", PLAIN_LI, BREAK_LINE);
		}
	}

	public String treated(String tag) {
		boolean isClosing = isClosingTag(tag);
		String[] tagParts = tagParts(tag,isClosing);

		TagReplacement tagRep = tagsToCare.get(tagParts[0]);
		if(tagRep==null){
			return "";
		}
		return treat(tagParts, isClosing, tagRep);
	}

	private String treat(String[] tagParts, boolean closing, TagReplacement tagRep) {
		if(tagRep.reduce==null){
			return closing ? tagRep.close : tagRep.open;
		} else {
			if(tagRep.reduce){
					return concat("<",(closing?"/"+tagRep.close:tagRep.open),">");
			} else {
					return concat("<",(closing?"/"+tagRep.close:tagRep.open),tagParts[1]);
			}
		}
	}


	private String[] tagParts(String tag, boolean closing) {
		int pos = (closing)? 2:1;
	 	String tagId =  NON_WORD_REGEX_PATTERN.split(tag.substring(pos))[0].toLowerCase();
		return new String[]{tagId,tag.substring(pos+tagId.length())};
	}

	private boolean isClosingTag(String tag) {
		return tag.charAt(1)=='/';
	}

	private void register(String tagId, TagReplacement tagReplacement){
		tagsToCare.put(tagId, tagReplacement);
	}

	private void replace(String tagIdFrom, String tagIdTo){
		register(tagIdFrom, new TagReplacement(tagIdTo , tagIdTo ,null));
	}

	private void replace(String tagIdFrom , String tagIdToOpen, String tagIdToClose){
		register(tagIdFrom, new TagReplacement(tagIdToOpen , tagIdToClose ,null));
	}

	private void keep(String tagId){
		register(tagId, new TagReplacement(tagId , tagId ,false));
	}

	private void keep(String tagIdFrom, String tagIdTo){
		register(tagIdFrom, new TagReplacement(tagIdTo , tagIdTo ,false));
	}

	private void keep(String tagIdFrom , String tagIdToOpen, String tagIdToClose){
		register(tagIdFrom, new TagReplacement(tagIdToOpen , tagIdToClose ,false));
	}

	private void reduce(String tagId){
		register(tagId, new TagReplacement(tagId , tagId , true));
	}

	private void reduce(String tagIdFrom , String tagIdTo ){
		register(tagIdFrom, new TagReplacement(tagIdTo , tagIdTo , true));
	}

	private void reduce(String tagIdFrom , String tagToOpen, String tagToClose){
		register(tagIdFrom, new TagReplacement(tagToOpen , tagToClose ,true));
	}

}

class TagReplacement {

	Boolean reduce;

	String open;
	String close;

	TagReplacement(String open, String close, Boolean reduce) {
		this.open = open;
		this.close = close;
		this.reduce = reduce;
	}
}