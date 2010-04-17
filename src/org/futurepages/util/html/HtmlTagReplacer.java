package org.futurepages.util.html;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author leandro
 */
public class HtmlTagReplacer {

	private String PLAIN_LI = "&nbsp; - ";

	private boolean styles;
	private boolean lists;
	private boolean image;
	private boolean anchor;
	private boolean table;


	private Map<String, TagReplacement>  tagsToCare = new HashMap<String, TagReplacement>();


	public HtmlTagReplacer(boolean styles, boolean lists, boolean image, boolean anchor, boolean table) {

		reduce("i"      , tag("em"));
		reduce("u"      , tag("span style=\"text-decoration:underline;\""), "span");
		reduce("address", tag("em"));
		reduce("b"      , tag("strong"));
		reduce("big"    , tag("strong"));

		if(styles) {
			keep("p");
			keep("h1");
			keep("h2");
			keep("h3");
			keep("h4");
			keep("h5");
			keep("h6");
		} else {
			reduce("p"); // para manter o alinhamento
			reduce("h1","p");
			reduce("h2","p");
			reduce("h3","p");
			reduce("h4","p");
			reduce("h5","p");
			reduce("h6","p");
		}

		if (lists) {
			keep("ul");
			keep("ol");
			keep("li");
		} else {
			reduce("ul" ,  tag("p") );
			reduce("ol" ,  tag("p") );
			replace("li" ,  PLAIN_LI , stag("br") );
		}

		if (image) {
			keep("img");
		}

		if (anchor) {
			keep("a");
		}

		if (table) {
			keep("table");
			keep("tr");
			keep("th");
			keep("thead");
			keep("td");
		} else{
			replace("tr"              ,  tag("p"));
			replace("th"              ,  tag("p"));
			replace("thead"           ,  tag("p"));
			replace("td",  PLAIN_LI   ,  stag("br"));
		}
	}

	private void register(String tag, TagReplacement tagReplacement){
		tagsToCare.put(tag, tagReplacement);
	}

	private void replace(String tagFrom, String tagTo){
		register(tagFrom, new TagReplacement(tagTo , tagTo ,false));

	}

	private void replace(String tagFrom , String tagToOpen, String tagToClose){
		register(tagFrom, new TagReplacement(tagToOpen , tagToClose ,false));

	}

	private void keep(String tagFrom){
		register(tagFrom, null);

	}

	private void reduce(String tagFrom ){
		register(tagFrom, new TagReplacement(tag(tagFrom) , tag(tagFrom) , true));
	}

	private void reduce(String tagFrom , String tagTo ){
		register(tagFrom, new TagReplacement(tagTo , tagTo , true));
	}

	private void reduce(String tagFrom , String tagToOpen, String tagToClose){
		register(tagFrom, new TagReplacement(tagToOpen , tagToClose ,true));

	}

	//open or close tag
	private static String tag(String tagName) {
		return "<(/?" + tagName + "\\b[^>]*?)>";
	}

	private static String tag() {
		return "<\\1>";
	}

	//open tag
	private static String otag(String tagName) {
		return "<(" + tagName + "\\b.*?)>";
	}

	private static String otag() {
		return "<\\1>";
	}

	//close tag
	private static String ctag(String tagName) {
		return "</(" + tagName + "\\b.*?)>";
	}

	//close tag
	private static String ctag() {
		return "</\\1>";
	}

	//simple tag
	private static String stag(String tagName) {
		return "<(" + tagName + "\\b.*?)/>";
	}
}

class TagReplacement {

	boolean reduce;

	String open;
	String close;

	TagReplacement(String open, String close, boolean reduce) {
		this.open = open;
		this.close = close;
		this.reduce = reduce;
	}
}