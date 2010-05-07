package org.futurepages.core.persistence;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.futurepages.exceptions.ConfigFileNotFoundException;

public class HibernateManager {

	public static final String FACTORY_KEY = HibernateManager.DEFAULT;
	public static final String DEFAULT = "default";
	private static boolean running = false;
	private static Map<String, Configuration> configurations;
	private static Map<String, SessionFactory> factories = new HashMap<String, SessionFactory>();
	private static Map<String, ThreadLocal<Session>> sessionTL = new HashMap<String, ThreadLocal<Session>>();

	/**
	 * Inicialização Estática da Conexão do Hibernate com o(s) Banco(s) de Dados.
	 */
	static {
		try {
			configurations = HibernateConfigurationFactory.getInstance().getApplicationConfigurations();
			Configuration config;
			if (!configurations.isEmpty()) {
				for (String configurationName : configurations.keySet()) {
					config = configurations.get(configurationName);

					factories.put(configurationName, config.buildSessionFactory());
					sessionTL.put(configurationName, new ThreadLocal<Session>());
				}
				running = true;
			}
		} catch (ConfigFileNotFoundException e) {
			log("Arquivo de Configurações do hibernate não encontrado: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log("Não foi possível carregar as configurações hibernate: " + e.getMessage());
			System.out.println(e.getMessage());
		} catch (Exception ex) {
			log("Erro Inesperado na inicialização do Hibernate: " + ex.getMessage());
		}
	}

	public static SessionFactory getSessionFactory() {
		return factories.get(DEFAULT);
	}

	public static SessionFactory getSessionFactory(String sessionFactoryKey) {
		return factories.get(sessionFactoryKey);
	}

	public static Configuration getConfiguration(String configurationKey) {
		return configurations.get(configurationKey);
	}

	public static Configuration getConfiguration() {
		return getConfiguration(DEFAULT);
	}

	public static Map<String, Configuration> getConfigurations() {
		return configurations;
	}

	static Session getSession() {
		Session session = getSessionTL(DEFAULT).get();

		if (session != null) {
			if (session.isOpen()) {
				return session;
			}
		}

		session = getSessionFactory().openSession();
		HibernateManager.getSessionTL(DEFAULT).set(session);
		return session;
	}

	static void setSessionFactory(String factoryKey, SessionFactory sessionFactory) {
		factories.put(factoryKey, sessionFactory);
	}

	static ThreadLocal<Session> getSessionTL(String sessionFactoryKey) {
		return sessionTL.get(sessionFactoryKey);
	}

	public static boolean isRunning() {
		return running;
	}

	public static void shutdown() {
		try {
			log("killing sessions...");
			for (ThreadLocal<Session> sTL : sessionTL.values()) {
				if (sTL != null && sTL.get() != null && sTL.get().isOpen()) {//verifica se a sessao esta aberta se estiver fecha ela
					sTL.get().close();
					sTL.remove();
				}
			}
			for (SessionFactory sfTL : factories.values()) {
				if (sfTL != null && !sfTL.isClosed()) { //se a fabrica de sessao não estiver fechada fecha ela
					sfTL.close();
				}
			}
			log("sessions killed.");
		} catch (Exception ex) {
			log(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			log("Não foi possível matar hibernate-sessions:");
			ex.printStackTrace();
			log("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		}
	}

	private static void log(String msg) {
		System.out.println("[::hibernate::] " + msg);
	}
}