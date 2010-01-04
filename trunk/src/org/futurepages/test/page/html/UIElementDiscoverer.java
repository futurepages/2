package org.futurepages.test.page.html;

import java.io.IOException;

import org.apache.tools.ant.filters.StringInputStream;
import org.futurepages.test.page.html.exception.HTMLParserException;
import org.futurepages.test.page.html.exception.UIElementEnumUndentifiedException;
import org.futurepages.test.page.html.exception.WebElementNotFoundOnPageException;
import org.futurepages.util.XmlUtil;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;

import com.thoughtworks.selenium.DefaultSelenium;

public class UIElementDiscoverer {

	public static UIElementEnum getUIElement(String name, DefaultSelenium selenium) throws WebElementNotFoundOnPageException {

		Element xmlElement = getUIElementsNodes(name,selenium);

		if(xmlElement == null){
			throw new WebElementNotFoundOnPageException(name);
		}
		
		String nodeName="";
		UIElementEnum element = null;
		try{
			nodeName = xmlElement.getName();
			element = UIElementEnum.getUIElementByHtmlElement(xmlElement);
			System.out.println("Elemento tipo '"+element.tagName + "' para '"+name+"' identifier.");
		} catch (UIElementEnumUndentifiedException e) {
			System.out.println("Atributo name='"+name+"' presente em elemento não identificado "+nodeName );
		}
		if(element == null){
			throw new WebElementNotFoundOnPageException(name);
		}
		return element;
	}


	/**
	 * Lê o html da página atual e o transforma num documento XML.
	 * Depois pega o tag body e executa uma consulta XPath,
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	private static Element getUIElementsNodes(String name,DefaultSelenium selenium) {
		String htmlSource = selenium.getHtmlSource();
		try {
			return getElementsByNameAttributeFromHtmlSource(name,htmlSource);
		} catch (JDOMException e) {
			throw new HTMLParserException(e.getCause());
		} catch (IOException e) {
			throw new HTMLParserException(e.getCause());
		}
	}

	public static Element getElementsByNameAttributeFromHtmlSource(String name, String htmlSource) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder("org.ccil.cowan.tagsoup.Parser"); 
		
		// Parse my (HTML) URL into a well-formed document
		org.jdom.Document doc = builder.build(new StringInputStream(htmlSource));
		
		return XmlUtil.searchElementByName(name, doc);

	}

	private static org.w3c.dom.Document jDomToW3cDocument(org.jdom.Document doc) throws JDOMException{
		org.w3c.dom.Document document;
		//convert a JDom document into a w3c document 
		DOMOutputter outputter = new DOMOutputter();
		document =	outputter.output(doc);
		return document;
	}

}
