package org.futurepages.core.template;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.futurepages.exceptions.TemplateException;

import org.futurepages.core.config.Params;
import org.futurepages.exceptions.BadFormedConfigFileException;
import org.futurepages.exceptions.ConfigFileNotFoundException;

/**
 * Gerenciador de Templates. Configura/inicializa o template.
 * @author leandro
 */
public class TemplateManager extends AbstractTemplateManager {

	public void configurePages(){
		try {
			initialize();
		} catch (Exception ex) {
			System.out.println("[::w7i::] - Erro ao inicializar TemplateManager. ("+ex.getMessage()+")");
		}
	}

	private void initialize(){
		String templateFilePath = Params.get("CLASSES_PATH")+ Params.CONFIGURATION_DIR_NAME+"/app-template.xml";
		File templateFile = new File(templateFilePath);

		SAXBuilder sb = new SAXBuilder();
		Document doc;
		try {
			doc = sb.build(templateFile);
			build(doc);
		} catch (IOException e) {
			throw new ConfigFileNotFoundException("Arquivo de configuração de template não encontrado: "+templateFilePath);
		} catch (JDOMException e) {
			throw new BadFormedConfigFileException("Arquivo de configuração de template mal formado: "+templateFilePath);
		}

	}

	private void build(Document doc) {
		Element appTemplateCfg = doc.getRootElement();

		List<Element> pages = appTemplateCfg.getChildren();
		String rule, path, base;
		for (Element page : pages) {
			rule = page.getAttributeValue("rule");
			path = page.getAttributeValue("path");
			base = page.getAttributeValue("base");
			if (rule != null) { // Página com regra
				Page basePage = new Page(rule, base, ConventionController.class);
				List<Element> blocks = page.getChildren();
				for (Element block : blocks) {
					String id = block.getAttributeValue("id");
					String value = block.getAttributeValue("value");
					basePage.setBlock(id, new Page(value));
				}
				this.add(basePage);
			} else if(path!=null){ // Página única

				Page pageX = new Page(path, new Page(base));
				List<Element> blocks = page.getChildren();
				for (Element block : blocks) {
					String id = block.getAttributeValue("id");
					String value = block.getAttributeValue("value");
					pageX.setBlock(id, new Page(value));
				}
				this.add(pageX);
			}else{
				throw new TemplateException("Erro na passagem dos parâmetros do arquivo de template.");
			}
		}
	}
}