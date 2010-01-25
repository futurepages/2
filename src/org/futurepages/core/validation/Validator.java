package org.futurepages.core.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.futurepages.exceptions.ErrorException;

public abstract class Validator {

	private LinkedHashMap<String, ErrorException> validationMap;
	private boolean validationMapLoaded;
	private ArrayList<Validator> subValidators;
	protected Boolean breakOnFirst;

	public static <T extends Validator> T validate(Class<T> t, boolean breakOnFirst) {
		T validator;
		try {
			validator = t.newInstance();
			validator.setBreakOnFirst(breakOnFirst);
			return validator;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public Validator() {
		validationMap = new LinkedHashMap<String, ErrorException>();
		subValidators = new ArrayList<Validator>();
		validationMapLoaded = false;
	}

	protected <T extends Validator> T validate(Class<T> t) {
		return Validator.validate(t, breakOnFirst);
	}

	public void error(String key, String msg) {
		putError(key, new ErrorException(msg));
	}

	public void error(String key, ErrorException ex) {
		putError(key, ex);
	}

	public void error(String msg) {
		error(null, msg);

	}

	public void error(ErrorException ex) {
		error(null, ex);
	}

	private void putError(String key, ErrorException ex) {
		if (breakOnFirst) {
			throw ex;
		} else {
			if (key == null) {
				key = String.valueOf(validationMap.size()+1);
			}
			validationMap.put(key, ex);
		}
	}

	public void setBreakOnFirst(boolean breakOnFirst) {
		this.breakOnFirst = breakOnFirst;
	}

	public void validate(){
		validationMap();
	}

	public HashMap<String, ErrorException> validationMap() {
		if (!validationMapLoaded) {
			if (subValidators.size() > 0) {
				for (Validator v : subValidators) {
					validationMap.putAll(v.validationMap());
				}
			}
			if ((breakOnFirst != null) && (validationMap.size() > 0)) {
				throw new ErrorException(validationMap);
			}
		}
		return validationMap;
	}
}
