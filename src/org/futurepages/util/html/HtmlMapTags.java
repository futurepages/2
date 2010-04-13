
package org.futurepages.util.html;

import java.util.HashMap;
import java.util.Map;
import static org.futurepages.util.html.HtmlRegex.*;

/**
 *
 * @author leandro
 */
public class HtmlMapTags {

	static Map<String,String> corresponding;
	static Map<String,String> discarding;

	static{
		corresponding = new HashMap();
		corresponding.put("i"      , "em");
		corresponding.put("address", "em");
		corresponding.put("b"      , "strong");
		corresponding.put("big"    , "strong");


//	 * @param basicStyle it's allowed:  Bold, Italic, Underline
//	 * @param advancedStyle it's allowed: Alignment, FontFamilies, Headers e CSS Classes
//	 * @param lists it's allowed: UL OL LI tags
//	 * @param image it's allowed: IMG tag
//	 * @param anchor it's allowed: A tag
//	 * @param table it's allowed: TABLE tag
//	 * @return the stripped html
		discarding = new HashMap();
		discarding.put(tag("p"), tag() );


		//basicStyle

		//list
		discarding.put(tag("ul")    , tag("p")   );
		discarding.put(tag("ol")    , tag("p")   );
		discarding.put(otag("li")   , "&nbsp; - ");
		discarding.put(ctag("li")   , stag("br") );

		discarding.put(tag("tr")    , tag("p") );
		discarding.put(otag("td")   , "&nbsp; - ");
		discarding.put(ctag("td")   , stag("br") );
	}
}