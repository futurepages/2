package org.futurepages.errors;

import org.futurepages.exceptions.ErrorException;

/**
 *
 * @author leandro
 */
public class InvalidDateError extends ErrorException {

    public InvalidDateError(){
        super("A data espeficificada � inv�lida");
    }

    public InvalidDateError(String msg){
        super(msg);
    }

}
