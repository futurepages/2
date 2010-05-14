package org.futurepages.util.html;

import org.futurepages.util.Is;
import org.futurepages.util.iterator.IterableString;
import org.futurepages.util.iterator.MatchedToken;
/**
 * Encapsula uma string em blocos <p></p> e torna consistente os grupos <p>*</p> existentes 
 * @author Danilo Medeiros
 *
 */
public class ParagraphApplier {

	private static final String oP = "<p>";
	private static final Object cP = "</p>";

	/**
	 * Corrige os <p> existentes em rawContent: fecha os abertos e abre os não abertos(todo o conteúdo deverá estar contido dentro de tags <p>).
	 * Retorna uma string contida em tags <p>
	 * 	 * @param rawContent
	 * @return rawContent com tags <p> consistentes e encapsulando todo o conteudo 'rawContent'
	 */
	public String apply(String rawContent){
		StringBuilder sb = new StringBuilder();
		IterableString iter = new IterableString(HtmlRegex.tagsPattern(true, "p"), rawContent);
		MatchedToken ultimo;
		boolean ultimoJaUsado= false;

		if(iter.hasNext()){
			ultimo = iter.next();
			ultimoJaUsado = appendBefore(sb, ultimo);
			ultimoJaUsado = appendAfter(sb, ultimo);
		}else{
			return oP+rawContent+cP;
		}

		for (MatchedToken token : iter) {
			if(iter.hasNext()){
				ultimoJaUsado = appendAfter(sb, token);
			}else{
				ultimoJaUsado = false;
				ultimo = token;
			}
		}

		if(ultimo != null){
			if(!ultimoJaUsado){
				appendAfter(sb, ultimo);
			}
		}
		return sb.toString();
	}

	private boolean appendBefore(StringBuilder sb, MatchedToken token) {
		if(!Is.empty(token.getBefore())){
			appendOpen(sb, token);
			sb.append(token.getBefore());
			sb.append(cP);
			return true;
		}
		return false;

	}

	private boolean appendAfter(StringBuilder sb, MatchedToken token) {
		if(!Is.empty(token.getAfter())){
			appendOpen(sb, token);
			sb.append(token.getAfter());
			sb.append(cP);
			return true;
		}
		return false;
	}
	
	private void appendOpen(StringBuilder sb, MatchedToken token) {
		char[] tag = token.getMatched().toCharArray();
		if(tag[1] == '/'){
			sb.append(oP);
		}else{
			sb.append(tag);
		}
	}
	
}
