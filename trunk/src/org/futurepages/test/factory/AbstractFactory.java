package org.futurepages.test.factory;

import java.io.Serializable;

import org.futurepages.core.persistence.Dao;

public abstract class AbstractFactory<T extends Serializable>  {

    /**
     * used to create a new Instance
     * @return the new instance of the object.
     */
	public abstract T create();

    /**
     * used to create a persistent object on the database.
     * @return the new persisted instance of the object.
     */
	public T persist(){
		T obj = create();
		return Dao.saveTransaction(obj);
	}

    public T persist(T obj){
		return Dao.saveTransaction(obj);
	}
}