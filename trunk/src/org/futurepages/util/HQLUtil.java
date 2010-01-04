package org.futurepages.util;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HQL - Hibernate Query Language:
 * Utilidades para manipulação de queries HQL
 * 
 * @author leandro
 */
public class HQLUtil {

	/**
	 * retorno = "field like '%word1%word2%'
	 */
	public static String fieldHasAllWordsInSameSequence(String field, String... words) {
		StringBuffer sb = new StringBuffer();
		if(words!=null && words.length!=0){
			sb.append(field + " LIKE '");
			for (int i = 0; i < words.length; i++) {
				sb.append("%"+esc(words[i]));
			}
			sb.append("%'");
		}
		return sb.toString();
	}

	/**
	 * Exemplo, sendo field = 'name' e logicalConect = 'AND':
	 * retorno = "field like '%word1%' AND field like '%word%'
	 */
	public static String fieldHasWords(String field, String[] words, String logicalConect) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < words.length; i++) {
			sb.append(field + " LIKE '%"+esc(words[i])+"%'"+needed(i,words," "+logicalConect+" "));
		}
		return sb.toString();
	}

	/**
	 * Quebra 'value', exemplo: "Nome Sobrenome" e retorna:
	 * "field like '%Nome%Sobrenome%'"
	 */
	public static String fieldHasAllWordsInSameSequence(String field, String value) {
		StringTokenizer in = new StringTokenizer(value, " ");
		StringBuffer out = new StringBuffer();
		out.append(field+ " LIKE '");
		while (in.hasMoreTokens()) {
			out.append("%" + esc(in.nextToken()));
		}
		out.append("%'");

		return out.toString();
	}
	
	public static String imploded(List<String> elements) {
		StringBuffer out = new StringBuffer("");
		String virgula = "";
		for (String element : elements) {
			out.append(virgula);
			out.append("'" + esc(element) + "'");
			if(virgula.equals(""))  virgula = "," ;
		}
		return out.toString();
	}

	public static String imploded(String tokensStr) {
		return imploded(The.explodedToArray(tokensStr, " "));
	}

	private static String needed(int position, String[] list, String string) {
		if(position<list.length-1){
			return string;
		}
		else{
			return "";
		}
	}

	/**
	 * Escape de HQL/SQL para evitar Injections
	 *
	 * @param original HQL de entrada
	 * @return
	 */
	public static String esc(String original) {
		original = escQuote(original);
		original = original.replace("%","\\%");
		original = original.replace("_","\\_");
		return original;
	}
	
	/**
	 * Escape simple cote '
	 * Escape de HQL/SQL para evitar Injections
	 * @param original HQL de entrada
	 * @return
	 */
	public static String escQuote(String original) {
		original = original.replace("'", "''");
		return original;
	}

	public static String imploded(String[] array) {
		StringBuffer out = new StringBuffer("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(",");
			}
			out.append("'" + esc(array[i]) + "'");
		}
		return out.toString();
	}

	public static String imploded(long[] array) {
		StringBuffer out = new StringBuffer("");
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				out.append(",");
			}
			out.append(array[i]);
		}
		return out.toString();
	}

	/**
	 * Implementa uma busca inteligente parecida com o padrão google.
	 * 
	 *<ui>
	 * <li>- Busca sentenças que estiverem entre aspas.
	 * <li>- Desconsidera palavras precedidas do sinal MENOS (-)
	 * <li>- Desconsidera palavras com até dois caracteres.
	 *</ui>
	 *
	 * Exemplo com field = "campo" e o seguinte value:
	 * maria da silva "pereira" a "costinha da silva" -caramba pereira
	 * Temos o seguinte resultado:
	 *   campo LIKE '%pereira%'
	 *   AND campo LIKE '%silva%'
	 *   AND campo LIKE '%maria%'
	 *   AND campo LIKE '%costinha da silva%'
	 *   AND campo NOT LIKE '%caramba%'
	 */	
	public static String matches(String field, String value) {
		value = value.replaceAll("\\*[\\*]*", "*");
		final int TAMANHO_MINIMO_TOKEN = 2;
		Pattern aspasPattern = Pattern.compile("\".*?\"");
		Matcher aspasMatcher = aspasPattern.matcher(value);
		String[] conteudoSemAspas = aspasPattern.split(value);

		HashSet<String> tokensLike = new LinkedHashSet<String>();
		HashSet<String> tokensNotLike = new LinkedHashSet<String>();
		
		while(aspasMatcher.find()){ //palavras entre aspas
			String foundOne = value.substring(aspasMatcher.start()+1,aspasMatcher.end()-1);
			if(!foundOne.trim().equals("") && foundOne.trim().length()>TAMANHO_MINIMO_TOKEN ){
				if(foundOne.contains("*")){
					foundOne = foundOne.replaceAll("[\\s]?\\*[\\s]?", "%");
				}
				tokensLike.add(foundOne);
			}
		}

		for(String token : conteudoSemAspas){
			StringTokenizer st = new StringTokenizer(token);
			while(st.hasMoreTokens()){
				String palavra = st.nextToken();
				if(palavra.charAt(0)=='-'){
					palavra = palavra.substring(1);
					if(palavra.trim().length()>TAMANHO_MINIMO_TOKEN){
						tokensNotLike.add(palavra);
					}
				}
				else if(!palavra.equals("") && (palavra.length()>TAMANHO_MINIMO_TOKEN)){
					tokensLike.add(palavra);
				}
			}
		}

		StringBuffer hqlQueryBuffer = new StringBuffer();
		
		boolean primeiro = true;
		for(String token : tokensLike){
			hqlQueryBuffer.append((!primeiro? " AND ":"")+field+" LIKE '%"+token+"%'");
			primeiro = false;
		}

		for(String token : tokensNotLike){
			hqlQueryBuffer.append((!primeiro? " AND ":"")+field+" NOT LIKE '%"+token+"%'");
			primeiro = false;
		}

		return hqlQueryBuffer.toString();
	}
}