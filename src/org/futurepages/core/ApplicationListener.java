package org.futurepages.core;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.futurepages.core.mail.MailConfig;
import org.futurepages.core.config.Params;
import org.futurepages.core.install.InstallersManager;
import org.futurepages.core.persistence.HibernateManager;
import org.futurepages.core.persistence.SchemaGeneration;
import org.futurepages.core.quartz.QuartzManager;
import org.futurepages.core.resource.ResourceMinifier;
import org.futurepages.core.session.SessionListenerManager;
import org.futurepages.core.tags.build.TagLibBuilder;
import org.futurepages.util.The;
import org.quartz.SchedulerException;

/**
 * É nesta classe onde o futurepages age sobre a aplicação,
 * gerando o necessário antes do deploy da aplicação
 * e desalocando o que for necessário no undeploy da mesma.
 * 
 * @author leandro
 */
public class ApplicationListener implements ServletContextListener {

	private String contextName;

    public void contextInitialized(ServletContextEvent evt) {
        try {
            ServletContext servletContext = evt.getServletContext();
			String name = The.tokenAt(1, servletContext.getResource("/").getPath(), "/");
            contextName = (name!=null ? name : "ROOT");

            log("Inicializando " + servletContext.getServletContextName() + "...");
            String realPath = servletContext.getRealPath("/");

            log("Inicializando Parâmetros...");
            Params.initialize(realPath, contextName);
            log("Parâmetros OK");

            log("Carregando módulos...");
            File[] modules = (new File(Params.get("MODULES_CLASSES_REAL_PATH"))).listFiles();
            log("Módulos OK");
            
            if(HibernateManager.isRunning()){
				log("Hibernate OK");
            	// Atualiza/gera esquema do banco como solicitado no arquivo de configuração.
                if (Params.get("SCHEMA_GENERATION_TYPE").equals("update")) {
					log("SCHEMA UPDATE Begin");
                    SchemaGeneration.update();
                } else if (Params.get("SCHEMA_GENERATION_TYPE").equals("export")) {
					log("SCHEMA EXPORT Begin");
					SchemaGeneration.export();
                }

                //Se o modo de instalação estiver ligado, serão feitas as instalações de cada módulo.
				String installMode = Params.get("INSTALL_MODE");
                if (installMode.equals("on")) {
                	log("Install Mode: ON");
                    new InstallersManager(modules).install();
                    log("Install Done");
                } else if(installMode.equals("examples")) {
					log("Install Mode: EXAMPLES");
					new InstallersManager(modules).installExamples();
					log("Install Done");
				} else if(installMode.equals("production")) {
					log("Install Mode: PRODUCTION");
					new InstallersManager(modules).installProduction();
					log("Install Done");
				} else if(installMode.equals("modules")) {
					log("Install Mode: MODULES");
					new InstallersManager(modules).installAllModules();
					log("Install Done");
				} else if(installMode.equals("resources")) {
					log("Install Mode: RESOURCES");
					new InstallersManager(modules).installResources();
					log("Install Done");
				} else{
                	log("Install Mode: off");
                }
            }

            log("Session Listenter...: ");
			new SessionListenerManager(modules).initialize();
			log("Session Listenter...: OK ");

            //Inicializa o gerenciador do Quartz (Agendador de Tarefas) caso solicitado.
            if (Params.get("QUARTZ_MODE").equals("on")) {
                log("Iniciando Quartz...");
                QuartzManager.initialize(modules);
                log("Quartz Inicializado.");
            }

            //Inicializa os parâmetros de configuração de Email se solicitado.
            if (Params.get("EMAIL_ACTIVE").equals("true")) {
				log("Configurando Email...: ");
                MailConfig.initialize();
				log("Config Email OK");
            }

            //Por padrão gera o arquivo taglib.tld com as tags dos módulos da aplicação
            if (Params.get("GENERATE_TAGLIB").equals("true")) {
                log("Iniciando criação da Taglib.");
                (new TagLibBuilder(modules)).build();
                log("Taglib criada com sucesso.");
            }

			//Compacta recursos web
			String minifyMode = Params.get("MINIFY_RESOURCE_MODE");
            if (!minifyMode.equals("none")) {  //none, js, css, both
                log("MINIFY RESOURCE MODE = "+minifyMode);
                (new ResourceMinifier()).execute(minifyMode);
                log("MINIFY RESOURCE DONE.");
            }

            log(servletContext.getServletContextName() + " inicializado.");
        } catch (Exception ex) {
            log("Erro ao inicializar contexto.");
            ex.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent evt) {
        log("Parando: " + evt.getServletContext().getServletContextName());
        if (Params.get("QUARTZ_MODE").equals("on")) {
            try {
                QuartzManager.shutdown();
                log("Schedulers do Quartz parado.");
            } catch (SchedulerException ex) {
                log("Erro ao tentar parar Schedulers do Quartz: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
        HibernateManager.shutdown();
        log("Aplicação parada: " + evt.getServletContext().getServletContextName());
    }

    /**
     * Mensagem de log padrão do listener.
     * @param logText
     */
    private void log(String logText) {
        System.out.println("[::"+contextName+"::] " + logText);
    }
}