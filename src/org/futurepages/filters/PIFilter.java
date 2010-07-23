package org.futurepages.filters;

import java.io.Serializable;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.input.Input;
import org.futurepages.core.control.InvocationChain;

import org.futurepages.core.persistence.Dao;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;

/**
 * PIFilter - Persistent Injection Filter
 * Recupera no banco um objeto do tipo 'classToInject' com o id passado no input com a chave 'keyToInject'
 * em seguida injeta no objeto que está referenciado no input com a chave 'targetKey'
 * @author leandro
 * Deprecated Classe. Use {@link PersistenceInjectionFilter}
 */
@Deprecated
public class PIFilter implements Filter {

	private Class classToInject;   //classe do objeto a ser injetado
	private String keyToInject;  //chave do input a ser injetado
	private String targetKey;   //chave do input do objeto que sofrerá a injeção

	public PIFilter(String targetKey, Class classToInject) {
		this.targetKey = targetKey;
		this.classToInject = classToInject;
		this.keyToInject = The.uncapitalizedWord(classToInject.getSimpleName());
	}

	public PIFilter(String targetKey, String keyToInject, Class classToInject) {
		this.targetKey = targetKey;
		this.classToInject = classToInject;
		this.keyToInject = keyToInject;
	}

	@Override
	public String filter(InvocationChain chain) throws Exception {
		Input input = chain.getAction().getInput();
		
		Object keyFound = input.getValue(keyToInject);
		if (keyFound!=null) {
			Serializable obj = null;
			Class pkType = Dao.getIdType(classToInject);
			if (pkType == StringType.class) {
				obj = Dao.get(classToInject, input.getStringValue(keyToInject));
			} else if(pkType == LongType.class) {
				final long idLong = input.getLongValue(keyToInject);
				obj = Dao.get(classToInject, idLong);
			} else if(pkType == IntegerType.class) {
				obj = Dao.get(classToInject, input.getIntValue(keyToInject));
			}
			
			inject(input, obj);
		}
		return chain.invoke();
	}

	/**
	 * Se o alvo é targetKey é não nulo então:
	 * <li> se o objeto já existe no input, injeta-se o valor encontrato em tal objeto:setField( obj, targetObjetc)
	 * <li> se o objeto ainda não existe no input: input.setValue(targetKey, obj)
	 * 	
	 * @param input
	 * @param obj
	 */
	private void inject(Input input, Serializable obj) {
		if(targetKey != null){
			String[] explodedTarget = The.explodedToArray(targetKey, ".");
			Object targetObject = input.getValue(explodedTarget[0]);
			if(targetObject != null){
				setField(obj, explodedTarget, targetObject);
			}else{
				input.setValue(targetKey, obj);
			}
		}
	}

	private void setField(Serializable obj, String[] explodedTarget, Object targetObject) {
		if(explodedTarget.length > 1)
			for(int i = 1 ; i < explodedTarget.length; i++){
				targetObject = ReflectionUtil.getField(targetObject, explodedTarget[i]);
			}
		ReflectionUtil.setField(targetObject, keyToInject, obj);
	}

	@Override
	public void destroy() {
	}
}