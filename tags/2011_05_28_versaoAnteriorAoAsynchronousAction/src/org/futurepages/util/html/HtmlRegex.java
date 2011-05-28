
package org.futurepages.util.html;
import java.util.regex.Pattern;
import static org.futurepages.util.StringUtils.concat;

/**
 * Gerador de Regex Patterns para capturar padr�es de tags
 * @author leandro
 */
public class HtmlRegex {

	private static Pattern COMPILED_TAGS_PATTERN;
	/**
	 * Casa padr�o da tag com seu conte�do
	 * @param tagName
	 *  xml  ==> <xml>.*</xml>
	 * @return
	 */
	public static String tagAndContentPattern(String tagName){
		return concat("(?i)(?s)<",tagName,".*?>.*?</",tagName,"*>");
	}

	//(?s)(?i)<span\s*style\s*=\s*"text-decoration:\s*underline\b.*?"[^>]*>.*?</span\s*>
	public static String spanWithStylePropertiePattern(String propertie, String value){
		return "(?s)(?i)<span\\s*style\\s*=\\s*\""+propertie+":\\s*"+value+"\\b.*?\"[^>]*>(.*?)</span\\s*>";
	}

	public static String tagWithContentReplacement(String tagName){
		return "<"+tagName+">$1</"+tagName+">";
	}

	public static String emptyTagsPattern(){
		return "(?s)(?i)<([\\w]+\\b)[^>]*?></\\1>";
	}

	public static String commentPattern() {
		return "(?s)<!--.*?-->";
	}

	//atributos inv�lidos, ex.: class=exampleClass
	public static String invalidAttrPattern() {
		return " [\\w]+=[\\w]+\\b";
	}

	//(?i)(?s) ?\bstyle\s*=\s*"[^"]+"
	public static String attrPattern(String name) {
		return "(?i)(?s) ?\\b"+name+"\\s*=\\s*\"[^\"]+\"";
	}

	public static String attrPatternWithGroups(String name) {
		return "(?i)(?s)((?=.*)) ?\\b"+name+"\\s*=\\s*\"[^\"]+\"( ?(?=.*))";
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
		return concat(has ? "(?s)(?i)</?":"<",tagNamesPattern(has,tagNames),".*?>");
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
		if(COMPILED_TAGS_PATTERN == null){
			COMPILED_TAGS_PATTERN = Pattern.compile(tagsPattern(true));
		}
		return COMPILED_TAGS_PATTERN;
	}
}