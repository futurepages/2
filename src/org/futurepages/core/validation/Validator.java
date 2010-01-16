package org.futurepages.core.validation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.futurepages.exceptions.ErrorException;
import org.futurepages.exceptions.ValidationException;

public abstract class Validator {

    private LinkedHashMap<String,ErrorException> validationMap;

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

    protected <T extends Validator>  T validate(Class<T> t){
        return Validator.validate(t, breakOnFirst);
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

    public HashMap<String, ErrorException> validationMap() {
        if(!validationMapLoaded){
            if(subValidators.size()>0){
                for(Validator v : subValidators){
                    validationMap.putAll(v.validationMap());
                }
            }
            if(breakOnFirst != null){
                throw new ErrorException(validationMap);
            }
        }
        return validationMap;
    }
}