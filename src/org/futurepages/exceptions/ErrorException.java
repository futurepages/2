package org.futurepages.exceptions;

import java.util.LinkedHashMap;

/**
 *
 * @author leandro
 */
public class ErrorException extends RuntimeException {

	private LinkedHashMap<String, String> validationMap;

	public ErrorException(String msg) {
		super(msg);
	}

	public ErrorException(Exception ex) {
		super(ex);
	}

	public ErrorException(LinkedHashMap<String, String> validationMap) {
		super(validationMessage(validationMap));
		this.setValidationMap(validationMap);
	}

	public LinkedHashMap<String, String> getValidationMap() {
		return validationMap;
	}

	public void setValidationMap(LinkedHashMap<String, String> validationMap) {
		this.validationMap = validationMap;
	}

	private static String validationMessage(LinkedHashMap<String, String> validationMap) {
		StringBuffer sb = new StringBuffer();
		if (validationMap.size() > 1) {
			for (String key : validationMap.keySet()) {
				sb.append(key + ") " + validationMap.get(key) + "\n");
			}
		}else{
			sb.append(validationMap.get("1"));
		}
		return sb.toString();
	}

	@Override
	public String toString() {
		return super.getMessage();
	}
}
