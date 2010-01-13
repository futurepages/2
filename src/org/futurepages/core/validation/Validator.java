package org.futurepages.core.validation;

import java.util.HashMap;
import java.util.LinkedHashMap;
import org.futurepages.exceptions.ErrorException;
import org.futurepages.exceptions.ValidationException;

public abstract class Validator {

    private LinkedHashMap<String,ErrorException> validationMap;
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
    }

    public void error(String key, String msg){
        error(msg);
        validationMap.put(key, new ErrorException(msg));
    }

    public void error(String key, ErrorException ex){
        error(ex);
        validationMap.put(key, ex);
    }

    public void error(String msg){
        error();
        if(breakOnFirst){
            throw new ErrorException(msg);
        }
    }

    public void error(ErrorException ex){
        error();
        if(breakOnFirst){
            throw ex;
        }
    }

    private void error(){
            if(validationMap.size()>0 && (breakOnFirst!=null && breakOnFirst)){
                throw new ValidationException("É necessário definir uma chave para o mapa de validações");
            }
    }

    public void setBreakOnFirst(boolean breakOnFirst) {
        this.breakOnFirst = breakOnFirst;
    }

    public HashMap<String, ErrorException> getValidationMap() {
        if(breakOnFirst != null){
            throw new ErrorException(validationMap);
        }
        return validationMap;
    }
}