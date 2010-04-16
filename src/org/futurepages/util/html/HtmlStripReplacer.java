package org.futurepages.util.html;

import java.util.Map;

/**
 *
 * @author leandro
 */
public class HtmlStripReplacer {

	private String PLAIN_LI = "&nbsp; - ";

	private Map<String, String> tagsToCare;

	public HtmlStripReplacer(boolean style, boolean lists, boolean image, boolean anchor, boolean table) {
		if (style) {

		} else {

		}
		if (lists) {

		} else {

		}
		if (image) {

		} else {

		}
		if (anchor) {

		} else {

		}
		if (table) {

		} else {

		}
	}

//	static{
//		discarding = new HashMap();
//
//        //default
//
//		discarding.put(tag("p")      , tag());
//		discarding.put(tag("i")      , tag("em"));
//		discarding.put(tag("address"), tag("em"));
//		discarding.put(tag("b")      , tag("strong"));
//		discarding.put(tag("big")    , tag("strong"));
//
//       	//list
//		discarding.put(tag ("ul")    , tag("p"));
//		discarding.put(tag ("ol")    , tag("p"));
//		discarding.put(otag("li")    , PLAIN_LI);
//		discarding.put(ctag("li")    , stag("br") );
//
//        //table
//		discarding.put(tag("tr")    ,  tag("p"));
//		discarding.put(tag("th")    ,  tag("p"));
//		discarding.put(tag("thead") ,  tag("p"));
//		discarding.put(otag("td")   ,  PLAIN_LI);
//		discarding.put(ctag("td")   ,  stag("br"));
//	}
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