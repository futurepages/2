
package org.futurepages.util.html;
import java.util.regex.Pattern;
import static org.futurepages.util.StringUtils.concat;

/**
 * Gerador de Regex Patterns para capturar padrões de tags
 * @author leandro
 */
public class HtmlRegex {

	private static Pattern COMPILED_TAGS_PATTERN;
	/**
	 * Casa padrão da tag com seu conteúdo
	 * @param tagName
	 *  xml  ==> <xml>.*</xml>
	 * @return
	 */
	public static String tagWithContentPattern(String tagName){
		return concat("<",tagName,">.*?</",tagName,">");
	}

	public static String emptyTagsPattern(){
		return "<([\\w]+\\b)[^>]*?></\\1>";
	}

	public static String tagNamePattern(){
		return "([\\w]+)\\b";
	}

	public static String commentPattern() {
		return "<!--.*?-->";
	}

	/**
	 * Cria padrão regex que casa com as tags passadas como parâmetro (has=true) ou
	 * todas as tags exceto as passadas por parâmetro  (has=false)
	 * @param has com ou sem as tags passadas como parâmetro
	 * @param tagNames são os nomes das tags a serem casadas
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

	//open or close tag
	static String tag(String tagName){
       return "<(/?"+tagName+"\\b)[^>]*?>";
	}
	
	static String tag(){
       return "<\\1>";
	}

	//open tag
	static String otag(String tagName){
       return "<("+tagName+"\\b).*?>";
	}

	static String otag(){
       return "<\\1>";
	}

	//close tag
	static String ctag(String tagName){
       return "</("+tagName+"\\b).*?>";
	}
	
	//close tag
	static String ctag(){
       return "</\\1>";
	}

	//simple tag
	static String stag(String tagName){
       return "<("+tagName+"\\b).*?/>";
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

	public static Pattern getCompiledTagsPattern() {
		if(COMPILED_TAGS_PATTERN==null){
			COMPILED_TAGS_PATTERN = Pattern.compile(tagsPattern(true));
		}
		return COMPILED_TAGS_PATTERN;
	}
}