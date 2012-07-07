package org.futurepages.util.iterator.string;

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
	private Pattern pattern;
	private int pos;
	private String beforeCache;
	
	public IterableString(String regex, String content) {
		this(Pattern.compile(regex), content);
	}
	
	public IterableString(Pattern pattern, String content) {
		this.content = content;
		this.pattern = pattern;
		init();
	}

	private void init(){
		this.hasNext = false;
		this.pos = 0;
		this.beforeCache = null;
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
		hasNext = true;
		return matcher.find();
	}

	@Override
	public MatchedToken next() {
		if(hasNext){
			int start = matcher.start();
			int end = matcher.end();
			String before = before(start);
			String matched = content.substring(start,end);
			pos = end;
			String after = after(start,end);
			beforeCache = after;
			MatchedToken token = new MatchedToken(matched, before, after);
			return token;
		}else{
			return null;
		}
	}
	
	private String after(int startActual, int endActual) {
		String after = "";
		if(matcher.find()){
			int startNext;
			startNext = matcher.start();
			after = content.substring(endActual, startNext);
			matcher.find(startActual);
		}else{
			after = content.substring(startActual);
		}
		return after;
	}

	private String before(int startActual) {
		String antes = beforeCache;
		if(beforeCache == null){
			antes = content.substring(pos, startActual);
		}
		return antes;
	}

	public Matcher getMatcher() {
		return matcher;
	}


}