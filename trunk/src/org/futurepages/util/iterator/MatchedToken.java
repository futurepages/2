package org.futurepages.util.iterator;


public class MatchedToken {

	private String matched;
	private String before;
	private String after;

	public MatchedToken(String matched, String before, String after) {
		super();
		this.matched = matched;
		this.before = before;
		this.after = after;
	}

	public String getBefore() {
		return before;
	}

	public String getMatched() {
		return matched;
	}

	public String getAfter() {
		return after;
	}
	@Override
	public String toString() {
		return "before: "+before  +"  token: "+matched;
	}
}