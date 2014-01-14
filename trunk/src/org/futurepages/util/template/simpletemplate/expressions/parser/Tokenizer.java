package org.futurepages.util.template.simpletemplate.expressions.parser;

import java.util.ArrayList;
import java.util.List;
import org.futurepages.util.template.simpletemplate.util.Tuple;

/**
 *
 * @author thiago
 */
public class Tokenizer {
	
	private String stream;
	private List<Tuple<String, Integer>> tokenList;
	private boolean executed = false;
	
	// @TODO: Lançar exceção caso a string seja nula?
	public Tokenizer(String stream) {
		this.stream = stream;
	}
	
	public List<Tuple<String, Integer>> tokenList() {
		if (!executed) {
			tokenList = new ArrayList<Tuple<String, Integer>>();
			StringBuilder token = sb();
			int lastIndex = 0;

			for (int i = 0, len = stream.length(); i < len; i++) {
				char ch = stream.charAt(i);
				
				if (!Character.isWhitespace(ch)) {
					switch (ch) {
						case '*':
							i = findOnSequence(i, token, '*', lastIndex);
							token = sb();
							lastIndex = i + 1;

							break;

						case '(':
						case ')':
						case '/':
						case '%':
						case '^':
						case '+':
						case '-':
							if (token.length() > 0) {
								tokenList.add(t(token.toString(), lastIndex));
								token = sb();
							}

							tokenList.add(t(Character.toString(ch), i));
							lastIndex = i + 1;

							break;

						case '!': // !=
						case '<': // <=
						case '>': // >=
						case '=': // ==
							i = findOnSequence(i, token, '=', lastIndex);
							token = sb();
							lastIndex = i + 1;

							break;

						case '&': // &
							i = findOnSequence(i, token, '&', lastIndex);
							token = sb();
							lastIndex = i + 1;

							break;

						case '|': // ||
							i = findOnSequence(i, token, '|', lastIndex);
							token = sb();
							lastIndex = i + 1;

							break;
						
						default:
							token.append(ch);
							break;
					}
				} else if (token.length() > 0) {
					tokenList.add(t(token.toString(), lastIndex));
					token = sb();
					lastIndex = i + 1;
				} else {
					lastIndex = i + 1;
				}
			}
			
			if (token.length() > 0) {
				tokenList.add(t(token.toString(), lastIndex));
			}

			executed = true;
		}

		return tokenList;
	}
	
	private StringBuilder sb() {
		return new StringBuilder();
	}
	
	private int findOnSequence(int idx, StringBuilder sb, char next, int lastIndex) {
		int nxtIdx = idx + 1, len = stream.length();
		char ch = stream.charAt(idx);

		if (nxtIdx >= len) {
			if (sb.length() > 0) {
				tokenList.add(t(sb.toString(), lastIndex));
			}

			tokenList.add(t(Character.toString(ch), idx));
			
			return idx;
		} else {
			if (sb.length() > 0) {
				tokenList.add(t(sb.toString(), lastIndex));
				sb = sb();
			}

			sb.append(ch);

			char nextCh = stream.charAt(nxtIdx);

			if (nextCh == next) {
				sb.append(nextCh);
			}

			tokenList.add(t(sb.toString(), idx));
			
			return nextCh == next ? nxtIdx : idx;
		}
	}
	
	public String getStream() {
		return stream;
	}
	
	private Tuple<String, Integer> t(String token, int index) {
		return new Tuple<String, Integer>(token, index);
	}
}
