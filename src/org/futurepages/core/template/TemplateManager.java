package org.futurepages.core.template;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.futurepages.exceptions.TemplateException;

import org.futurepages.core.config.Params;
import org.futurepages.exceptions.BadFormedConfigFileException;
import org.futurepages.exceptions.ConfigFileNotFoundException;
import org.futurepages.util.EncodingUtil;

/**
 * Gerenciador de Templates. Configura/inicializa o template.
 * @author leandro
 */
public class TemplateManager extends AbstractTemplateManager {

	@Override
	public void configurePages(){
		try {
			initialize();
		} catch (Exception ex) {
			System.out.println("[::templateManager::] - Erro ao inicializar TemplateManager. ("+ex.getMessage()+")");
		}
	}

	private void initialize(){
		try {
			String classRealPath = EncodingUtil.correctPath(this.getClass().getResource("/").getPath());
			String templateFilePath = classRealPath+ Params.CONFIGURATION_DIR_NAME+"/app-template.xml";
			File templateFile = new File(templateFilePath);

			SAXBuilder sb = new SAXBuilder();
			Document doc;
			doc = sb.build(templateFile);
			build(doc);
		} catch (UnsupportedEncodingException ex) {
			throw new ConfigFileNotFoundException(ex);
		} catch (IOException ex) {
			throw new ConfigFileNotFoundException(ex);
		} catch (JDOMException ex) {
			throw new BadFormedConfigFileException(ex);
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
			if (rule != null) { // P�gina com regra
				Page basePage = new Page(rule, base, true);
				List<Element> blocks = page.getChildren();
				for (Element block : blocks) {
					String id = block.getAttributeValue("id");
					String value = block.getAttributeValue("value");
					basePage.setBlock(id, new Page(value));
				}
				this.add(basePage);
			} else if(path!=null){ // P�gina �nica

				Page pageX = new Page(path, new Page(base));
				List<Element> blocks = page.getChildren();
				for (Element block : blocks) {
					String id = block.getAttributeValue("id");
					String value = block.getAttributeValue("value");
					pageX.setBlock(id, new Page(value));
				}
				this.add(pageX);
			}else{
				throw new TemplateException("Erro na passagem dos par�metros do arquivo de template.");
			}
		}
	}
}