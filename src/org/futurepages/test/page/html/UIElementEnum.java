package org.futurepages.test.page.html;


import org.futurepages.test.page.html.exception.UIElementEnumUndentifiedException;
import org.jdom.Attribute;
import org.jdom.Element;

public enum UIElementEnum {

	BUTTON("button"){
		@Override
		protected boolean isAssignableFrom(Element element) {
			if(isPropertyValued(element,"type","submit","button")){
				return true;
			}
			return super.isAssignableFrom(element);
		}
	},

	CHECKBOX("checkBox"){
		@Override
		protected boolean isAssignableFrom(Element element) {
			if(isPropertyValued(element,"type","checkbox")){
				return true;
			}
			return super.isAssignableFrom(element);
		}
	},
	
	DYNSELECT("dynSelect"){
		@Override
		protected boolean isAssignableFrom(Element element) {
			if(isAttributeWithClass(element, "dynSelect")){
				return true;
			}
			return super.isAssignableFrom(element);
		}
	},
	INPUT("input"){
		
		@Override
		protected boolean isAssignableFrom(Element element) {
			if( isAssignableTo(element,BUTTON,CHECKBOX,DYNSELECT, INPUTFILE)){
				return false;
			}
			return super.isAssignableFrom(element);
		}

		private boolean isAssignableTo(Element element,UIElementEnum... uiElement) {
			for (UIElementEnum elementEnum : uiElement) {
				if(elementEnum.isAssignableFrom(element)){
					return true;
				}
			}
			return false;
		}
	},
	
	INPUTFILE("input"){
		@Override
		protected boolean isAssignableFrom(Element element) {
			if(isPropertyValued(element,"type","file")){
				return true;
			}
			return false;
		}
	},
	LINK("a"),
	PAGE("body"),
	SELECT("select"),
	TABLE("table"), 
	TEXTAREA("textarea");

	String tagName;

	UIElementEnum(String name){
		tagName = name;
	}

	public static UIElementEnum getUIElementByHtmlElement(Element element) {
		for (UIElementEnum ele : UIElementEnum.values()) {
			if(ele.isAssignableFrom(element)){
				return ele;
			}
		}
		throw new  UIElementEnumUndentifiedException(element.getName());

	}
	public static UIElementEnum getUIElementByName(String elementName) {
		for (UIElementEnum ele : UIElementEnum.values()) {
			if(ele.tagName.toUpperCase().equals(elementName.toUpperCase())) {
				return ele;

			}
		}
		throw new  UIElementEnumUndentifiedException(elementName);
	}

	protected  boolean isAssignableFrom(Element element){
		if(this.tagName.toUpperCase().equals(element.getName().toUpperCase())){
			return true;
		}
		return false;
	}
	
	/**
	 * verifica se o elemneto possui um atribuo 'class' com a classe passada.
	 * @param element
	 * @param classe
	 * @return true se o elemento possui a class passada
	 */
	protected final boolean isAttributeWithClass(Element element, String classe) {
		Attribute classs = element.getAttribute("class");
		if(classs != null){
			String[] classes = classs.getValue().split(" ");
			for (String klass : classes) {
				if(klass.equals(classe)){
					return true;
				}
			}
			
		}
		return false;
		
	}
	/**
	 * Verifica se o elemeto passsado 
	 * @param element
	 * @param propertyName
	 * @param values
	 * @return
	 */
	protected final boolean isPropertyValued(Element element, String propertyName, String... values ) {
		Attribute attribute = element.getAttribute(propertyName);
		if(attribute != null){
			String value = attribute.getValue();
			for (String optionValue : values) {
				if(value.equals(optionValue)){
					return true;
				}
			}
		}
		return false;
	}
}
