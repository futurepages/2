
package org.futurepages.util.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author leandro
 */
public class HtmlMapTags {

	static Map<String,String> corresponding;

	static{
		corresponding = new HashMap();
		corresponding.put("i"      , "em");
		corresponding.put("address", "em");
		corresponding.put("b"      , "strong");
		corresponding.put("big"    , "strong");
	}
}