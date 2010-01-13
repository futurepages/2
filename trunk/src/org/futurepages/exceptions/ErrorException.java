package org.futurepages.exceptions;

import java.util.LinkedHashMap;

/**
 *
 * @author leandro
 */
public class ErrorException extends RuntimeException {

    private LinkedHashMap<String,ErrorException> validationMap;

    public ErrorException(String msg) {
        super(msg);
    }

    public ErrorException(Exception ex) {
        super(ex.getMessage());
    }

    public ErrorException(LinkedHashMap<String, ErrorException> validationMap) {
        super(validationMessage(validationMap));
        this.setValidationMap(validationMap);
    }

    public LinkedHashMap<String, ErrorException> getValidationMap() {
        return validationMap;
    }

    public void setValidationMap(LinkedHashMap<String, ErrorException> validationMap) {
        this.validationMap = validationMap;
    }

    private static String validationMessage(LinkedHashMap<String,ErrorException> validationMap){
        StringBuffer sb = new StringBuffer();
        for(ErrorException ex : validationMap.values()){
            sb.append("- "+ex.getMessage()+"\n");
        }
        return sb.toString();
    }
}