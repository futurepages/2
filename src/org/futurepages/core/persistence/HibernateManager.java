package org.futurepages.core.persistence;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.futurepages.core.exception.DefaultExceptionLogger;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.futurepages.exceptions.ConfigFileNotFoundException;

public class HibernateManager {

	public static final String FACTORY_KEY = HibernateManager.DEFAULT;
	public static final String DEFAULT = "default";
	private static boolean running = false;

	private static Map<String, Configurations>       configurations;
	private static Map<String, GenericDao>           genericDaos    = new HashMap<String,GenericDao>();
	private static Map<String, SessionFactory>       factories      = new HashMap<String, SessionFactory>();
	private static Map<String, ThreadLocal<Session>> sessionsTL     = new HashMap<String, ThreadLocal<Session>>();

	/**
	 * Inicializa��o Est�tica da Conex�o do Hibernate com o(s) Banco(s) de Dados.
	 */
	static {
		try {
			configurations = HibernateConfigurationFactory.getInstance().getApplicationConfigurations();
			if (!configurations.isEmpty()) {
				for (String schemaId : configurations.keySet()) {
					System.out.println("registering '"+schemaId+ "' schema.");
					Configuration config;
					config = configurations.get(schemaId).getEntitiesConfig();
					factories.put(  schemaId , config.buildSessionFactory());
					sessionsTL.put( schemaId , new ThreadLocal<Session>());
					genericDaos.put(schemaId , new GenericDao(schemaId));
				}
				running = true;
			}
		} catch (ConfigFileNotFoundException e) {
			log("Arquivo de Configura��es do hibernate n�o encontrado: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log("N�o foi poss�vel carregar as configura��es hibernate: " + e.getMessage());
			System.out.println(e.getMessage());
		} catch (Exception ex) {
			log("Erro Inesperado na inicializa��o do Hibernate: " + ex.getMessage());
			DefaultExceptionLogger.getInstance().execute(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return factories.get(DEFAULT);
	}

	public static SessionFactory getSessionFactory(String sessionFactoryKey) {
		return factories.get(sessionFactoryKey);
	}

	public static Configurations getConfigurations(String configurationKey) {
		return configurations.get(configurationKey);
	}

	public static Configurations getConfigurations() {
		return getConfigurations(DEFAULT);
	}

	public static Map<String, Configurations> getConfigurationsMap() {
		return configurations;
	}

	static Session getSession() {
		return getSession(DEFAULT);
	}

	static Session getSession(String schemaId) {
		Session session = getSessionTL(schemaId).get();

		if (session != null) {
			if (session.isOpen()) {
				return session;
			}
		}

		if(session==null || !session.isOpen()) {
			session = getSessionFactory(schemaId).openSession();
			HibernateManager.getSessionTL(schemaId).set(session);
			return session;
		}

		if(!session.isOpen()){
		}
		return session;
	}

	static void setSessionFactory(String schemaId, SessionFactory sessionFactory) {
		factories.put(schemaId, sessionFactory);
	}

	static ThreadLocal<Session> getSessionTL(String schemaId) {
		return sessionsTL.get(schemaId);
	}

	public static boolean isRunning() {
		return running;
	}

	public static void shutdown() {
		try {
			log("killing sessions...");
			for (ThreadLocal<Session> sTL : sessionsTL.values()) {
				if (sTL != null && sTL.get() != null && sTL.get().isOpen()) {//verifica se a sessao esta aberta se estiver fecha ela
					sTL.get().close();
					sTL.set(null);
					sTL.remove();
				}
			}
			for (SessionFactory sf : factories.values()) {
				if (sf != null && !sf.isClosed()) { //se a fabrica de sessao n�o estiver fechada fecha ela
					sf.close();
				}
			}
			log("sessions killed.");
		} catch (Exception ex) {
			log("N�o foi poss�vel matar hibernate-sessions:");
			DefaultExceptionLogger.getInstance().execute(ex);
		}
	}

	private static void log(String msg) {
		System.out.println("[::hibernate::] " + msg);
	}

	static GenericDao getGenericDao(String schemaId) {
		return genericDaos.get(schemaId);
	}

	static GenericDao getDefaultGenericDao(){
		return genericDaos.get(DEFAULT);
	}

	static void closeSessions() {
		for(String schemaId : configurations.keySet()){
			Session session = getSessionTL(schemaId).get();
			if(session!=null && session.isOpen()){
				session.close();
			}
		}
	}
}