package org.futurepages.util.iterator;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 *  
 * @author Danilo Medeiros
 */
public class IterableString implements Iterator<MatchedToken>, Iterable<MatchedToken> {

	private Matcher matcher;
	private String content;
	private boolean hasNext;
	private boolean used;
	private Pattern pattern;
	private int pos;
	private String cacheAntes;
	
	public IterableString(String regex, String content) {
		this(Pattern.compile(regex), content);
	}
	
	public IterableString(Pattern pattern, String content) {
		this.content = content;
		this.pattern = pattern;
		init();
	}

	private void init(){
		this.used = true;
		this.pos = 0;
		this.cacheAntes = null;
		this.matcher = pattern.matcher(this.content);
	}

	@Override
	public Iterator<MatchedToken> iterator() {
		return this;
	}

	@Override
	public void remove() {}

	@Override
	public boolean hasNext() {
		if(used){
			hasNext = matcher.find();
			used = false;
		}
		return hasNext;
	}

	@Override
	public MatchedToken next() {
		if(hasNext()){
			int start = matcher.start();
			int end = matcher.end();
			String antes = antes(start);
			String matched = content.substring(start,end);
			pos = end;
			used = true;
			String depois = depois(end);
			cacheAntes = depois; 
			MatchedToken token = new MatchedToken(matched, antes, depois);
			return token;
		}else{
			return null;
		}
	}
	
	private String depois(int end) {
		int start;
		String depois = ""; 
		if(hasNext()){
			start = matcher.start();
			depois = content.substring(pos, start);
		}else{
			depois = content.substring(pos);
		}
		return depois;
	}

	private String antes(int start) {
		String antes = cacheAntes;
		if(cacheAntes == null){
			antes = content.substring(pos, start);
		}
		return antes;
	}

}
