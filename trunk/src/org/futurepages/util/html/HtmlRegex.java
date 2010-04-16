
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
	public static String tagAndContentPattern(String tagName){
		return concat("<",tagName,".*?>.*?</",tagName,"*>");
	}

////	(?i)<([^>]+)\b(?:class=["']?(?:(?:.(?!["']? +(?:class)=|[>"']))+.)["']?)(.*?)>
////	(?i)<([\w]+)\b[^>]+(?:class *= *["']?[^ />"']+["']?) ?(.*?)>
////	(?i)<([\w]+)\b[^>]+class *= *(["']?)[^\2>]+\2(.*?)>
//	public static String tagsWithAttributePattern(String attribute){
//		return "<([\\w]+)\\b(?:[^>]+"+attribute+" *= *[\"'][^'\">]+['\"])(.*?)>";
////		return "(?i)<([\\w]+)\\b[^>]+(?:"+attribute+" *= *[\"']?[^ />\"']+[\"']?)(.*?)>";
////		return "(?i)<([\\w]+)\\b[^>]+(?:"+attribute+" *= *[\"']?[^ />\"']+[\"']?) *(.*?)>";
////		return "(?i)<([^>]+)\\b(?:"+attribute+"=[\"']?(?:(?:.(?![\"']? +(?:"+attribute+")=|[>\"']))+.)[\"']?)(.*?)>";
//	}
//	public static String tagsWithAttributeReplacement(){
//		return "<$1$2>";
//	}

	//(?i)<span *style=".*?font-weight: *bold;?[^"]+".*?>(.*?)</span *>
	//(?i)<span *style *= *"font-weight: *bold\b.*".*?>(.*?)</span *>
	public static String spanWithStylePropertiePattern(String propertie, String value){
		return "(?i)<span *style *= *\""+propertie+": *"+value+"\\b.*\".*?>(.*?)</span *>";
	}

	public static String tagWithContentReplacement(String tagName){
		return "<"+tagName+">$1</"+tagName+">";
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

	public static String invalidAttrPattern() {
		return " [\\w]+=[\\w]+\\b";
	}

	public static String attrPattern(String name) {
		return " ?\\b"+name+" *= *\"[^\"]+\"";
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