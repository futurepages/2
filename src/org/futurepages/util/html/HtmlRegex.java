
package org.futurepages.util.html;
import static org.futurepages.util.StringUtils.concat;

/**
 *
 * @author leandro
 */
public class HtmlRegex {
	/**
	 * Casa padr�o da tag com seu conte�do
	 * @param tagName
	 * @return
	 */
	// tagName = xml  ==>   <xml>.*</xml>
	public String tagWithContentPattern(String tagName){
		return concat("<",tagName,">.*?</",tagName,">");
	}

	public String emptyTagsPattern(){
		return "<([\\w]+\\b)[^>]*?></\\1>";
	}

	/**
	 * Cria padr�o regex que casa com as tags passadas como par�metro (has=true) ou
	 * todas as tags exceto as passadas por par�metro  (has=false)
	 * @param has com ou sem as tags passadas como par�metro
	 * @param tagNames s�o os nomes das tags a serem casadas
	 * @return regex pattern para as tags
	 */
	// has = true  para "em","p" =>   </?(em\b|p\b).*?>
	// has = false para "em","p" =>   <(?!em\b)(?!/em)(?!p\b)(?!/p).*?>
	public static String tagsPattern(boolean has, String... tagNames){
		if(!has && tagNames.length==0){
			return null;
		}
		return concat(has?"</?":"<",tagNamesPattern(has,tagNames),".*?>");
	}

	private static String tagNamesPattern(boolean has, String... ids){
		if(ids.length>0){
			StringBuilder sb = new StringBuilder("(");
			sb.append(tagNamePattern(has,ids[0]));
			String con = has?"|":")(";
			for(int i = 1 ; i < ids.length; i++){
				sb.append(con).append(tagNamePattern(has,ids[i]));
			}
			sb.append(")");
			return sb.toString();

		}
		return "";
	}

	private static String tagNamePattern(boolean has, String id) {
		return has? id+"\\b" : "?!"+id+"\\b)(?!/"+id+"";
	}
}