package org.futurepages.core.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Entity;
import org.futurepages.annotations.View;

import org.futurepages.core.config.Mapper;
import org.futurepages.core.config.Params;
import org.futurepages.exceptions.BadFormedConfigFileException;
import org.futurepages.exceptions.ConfigFileNotFoundException;
import org.futurepages.exceptions.ModuleWithoutBeanDirException;
import org.futurepages.util.ClassesUtil;
import org.futurepages.util.EncodingUtil;
import org.futurepages.util.XmlUtil;
import org.hibernate.cfg.Configuration;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * Classe de instanciação das Configurações Hibernate; 
 * @author Danilo Medeiros
 */
public class HibernateConfigurationFactory extends Mapper {

	public static HibernateConfigurationFactory uniqueInstance;
	public static File rootDir;

	/**
	 * Singleton Pattern
	 * @return
	 */
	public static HibernateConfigurationFactory getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new HibernateConfigurationFactory();
		}

		return uniqueInstance;
	}

	/**
	 * Retorna as configurações 'hibernate' da aplicação 
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 * @throws JDOMException 
	 */
	public Map<String, Configurations> getApplicationConfigurations() throws ConfigFileNotFoundException, UnsupportedEncodingException {
		String classPath = this.getClass().getResource("/").getPath();
		rootDir = new File(EncodingUtil.correctPath(classPath));
		File modulesDir = new File(rootDir + "/" + Params.MODULES_PATH);
		return config(modulesDir.listFiles());
	}

	/**
	 * Cria um {@link Map} de {@link Configuration} com as configurações 'hibernate' da aplicação  
	 * @param modules
	 * @return Map<String,Configuration> (Nome do módulo, Configuration)
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public Map<String, Configurations> config(File[] modules) throws ConfigFileNotFoundException {
		Configurations defaultConfiguration = new Configurations();
		Map<String, Configurations> configurationsMap = new HashMap<String, Configurations>();

		if (modules != null) {
			for (File module : modules) {
				if (!moduleHasDB(module)) {
					mapModule(module, defaultConfiguration);

				} else if (moduleHasDB(module)) {
					mapModule(module);
					Configurations moduleConfiguration = new Configurations();
					moduleConfiguration.getEntitiesConfig().createMappings();
					configurationsMap.put(module.getName(), moduleConfiguration);
				}
			}
		}
		configurationsMap.put(HibernateManager.DEFAULT, defaultConfiguration);
		insertPropertiesConfiguration(defaultConfiguration, rootDir);
		defaultConfiguration.createMappings();
		return configurationsMap;

	}

	/**
	 * Povoa uma {@link Configuration} hibernate com as classes annotadas com {@link Entity} presentes no {@link File} passado.
	 * @param module
	 * @param configurations
	 * @return
	 */
	private Configurations mapModule(File module, Configurations configurations)  {
		Collection<Class<Object>> classes;
		try {
			classes = listBeansAnnotatedFromModule(module);
			for (Class<?> annotatedClass : classes) {
				configurations.getEntitiesConfig().addAnnotatedClass(annotatedClass);
				if (!annotatedClass.isAnnotationPresent(View.class)) {
					configurations.getTablesConfig().addAnnotatedClass(annotatedClass);
				}
			}
		} catch (ModuleWithoutBeanDirException ex) {
			//Módulo não possui o diretório de beans
			//System.out.println(ex.getMessage());
		}
		return configurations;
	}

	private Configurations mapModule(File module) throws ConfigFileNotFoundException {
		Configurations config = new Configurations();
		config = mapModule(module, config);
		insertPropertiesConfiguration(config, module);
		return config;
	}

	private void insertPropertiesConfiguration(Configurations configurations, File path) throws ConfigFileNotFoundException {
		String file = path.getAbsolutePath() + "/" + Params.CONFIGURATION_DIR_NAME + "/" + Params.BASE_HIBERNATE_PROPERTIES_FILE;
		Properties properties = new Properties();
		InputStream inputStream;
		boolean naoCarregado = false;
		try {
			inputStream = new FileInputStream(file);
			properties.load(inputStream);
			configurations.getEntitiesConfig().addProperties(properties);
			configurations.getTablesConfig().addProperties(properties);
			configurations.getEntitiesConfig().getProperties();
			configurations.getTablesConfig().getProperties();
		} catch (FileNotFoundException e) {
			naoCarregado = true;
		} catch (IOException e) {
			naoCarregado = true;
		}
		if (naoCarregado) {
			insertSessionFactoryXMLProperties(configurations, path);
		}
	}

	private void insertSessionFactoryXMLProperties(Configurations config, File module) throws ConfigFileNotFoundException {

		Element rootElement = null;
		String file = module.getAbsolutePath() + "/" + Params.CONFIGURATION_DIR_NAME + "/" + Params.BASE_HIBERANTE_XML_FILE;
		try {
			rootElement = XmlUtil.getRootElement(file);
		} catch (IOException e) {
			throw new ConfigFileNotFoundException("Arquivo de configuração hibernate não encontrado: " + file);
		} catch (JDOMException e) {
			throw new BadFormedConfigFileException("Arquivo de configuração hibenrnate mal formado: " + file);
		}
		insertSessionFactoryProperties(config, rootElement);
	}

	private void insertSessionFactoryProperties(Configurations configuration, Element element) {
		Element sessionFactory = element.getChild("session-factory");
		for (Element child : (List<Element>) sessionFactory.getChildren()) {
			configuration.getEntitiesConfig().setProperty(child.getAttributeValue("name"), child.getValue());
			configuration.getTablesConfig().setProperty(child.getAttributeValue("name"), child.getValue());
		}
	}

	private Collection<Class<Object>> listBeansAnnotatedFromModule(File module) throws ModuleWithoutBeanDirException {
		File beansDirectory = new File(module.getAbsolutePath() + "/" + Params.BEANS_PACK_NAME);

		if (beansDirectory.listFiles() != null) {

			return ClassesUtil.getInstance().listClassesFromDirectory(beansDirectory,
					rootDir.getAbsolutePath(), null, Entity.class, true);
		}

		throw new ModuleWithoutBeanDirException(module.getName());
	}
}
