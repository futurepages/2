/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.futurepages.filters;

import java.util.List;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.persistence.HQLProvider;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.util.The;
import org.futurepages.core.input.Input;
import org.futurepages.core.filter.Filter;
import org.futurepages.core.control.InvocationChain;

/**
 *
 * @author diogenes
 */
public class PLIFilter extends HQLProvider implements Filter {

    private Class classToInject;   //classe dos objetos a serem injetados
    private String keyToInject;  //chave do input a ser injetado
    private String targetKey;   //chave do input do objeto que sofrer� a inje��o

    public PLIFilter(String targetKey, String keyToInject, Class classToInject) {
        this.targetKey = targetKey;
        this.classToInject = classToInject;
        this.keyToInject = keyToInject;

    }

    public String filter(InvocationChain chain) throws Exception {
        Input input = chain.getAction().getInput();
        String[] keyValues = input.getStringValues(keyToInject);
        String[] explodedTarget = The.explodedToArray(targetKey, ".");
        Object targetObject = input.getValue(explodedTarget[0]);
        if (explodedTarget.length > 1) {
            for (int i = 1; i < explodedTarget.length; i++) {
                targetObject = ReflectionUtil.getField(targetObject, explodedTarget[i]);
            }
        }

        List aux = keyObjects(keyValues, classToInject);

        ReflectionUtil.setField(targetObject, keyToInject, keyObjects(keyValues, classToInject));

        return chain.invoke();
    }

    public void destroy() {
    }

    private static List keyObjects(String[] keyValues, Class classToInject) {
        if (keyValues != null) {
            return Dao.list(classToInject, field(Dao.getIdName(classToInject)).in(keyValues));
        }
        return null;
    }
}
