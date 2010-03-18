package org.futurepages.core.persistence;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.futurepages.core.action.Action;
import org.futurepages.core.control.InvocationChain;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Classe para teste de {@link HibernateFilter#isTransactional(Method)}
 * @author Danilo Medeiros
 */
@RunWith(Parameterized.class)
public class HibernateFilterTest_isTransactional {

	String caso;
	Class<Action> actionClass;
	String nomeMetodo;
	Boolean esperado;

	public HibernateFilterTest_isTransactional(String caso, Class<Action> klass, String nomeMetodo,	Boolean esperado) {
		super();
		this.caso = caso;
		this.actionClass = klass;
		this.nomeMetodo = nomeMetodo;
		this.esperado = esperado;
	}

	@Parameters
	public static Collection<Object[]> parameters() {
		Collection<Object[]> col =  Arrays.asList(new Object[][] {
				{"Classe Transactional, metodo com as duas anota��es.",Transacional.class, 	"biTransacional",	false},
				{"Classe Transactional, m�todo com "+ "@Transactional", Transacional.class, 	"transacional",		true},
				{"Classe Transactional, m�todo com @NonTransactional.", Transacional.class, 	"naoTransacional",	false},
				{"Classe Transactional, m�todo sem anota��es.", Transacional.class, 	"metodo",			true},
				{"Classe N�o anotada com @Transactional, m�todo com as duas anota��es.", NaoTransacional.class, "biTransacional",	false},
				{"Classe N�o anotada com @Transactional, m�todo com @Transactional.", NaoTransacional.class, "transacional",		true},
				{"Classe N�o anotada com @Transactional, m�todo com @NonTransactional.", NaoTransacional.class, "naoTransacional",	false},
				{"Classe N�o anotada com @Transactional, m�todo sem anota��es.", NaoTransacional.class, "metodo",			false}
		});
		return col;
	}

	@Test
	public void testeIsTransational() throws SecurityException, NoSuchMethodException, InstantiationException, IllegalAccessException{
		HibernateFilter filter = new HibernateFilter();
		
		Action action = actionClass.newInstance();
		InvocationChain chain = new InvocationChain("Cadeia", action);
		chain.setInnerAction(nomeMetodo);
		Boolean result = filter.isTransactional(chain);
		Assert.assertEquals(caso,esperado, result);
	}

}