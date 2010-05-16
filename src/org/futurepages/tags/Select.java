package org.futurepages.tags;

import java.util.List;
import javax.persistence.Entity;

import javax.servlet.jsp.JspException;

import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.persistence.Dao;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.core.context.Context;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.core.tags.cerne.HTMLTag;

@Tag(bodyContent = ContentTypeEnum.JSP,name="Select")
public class Select extends HTMLTag {

	@TagAttribute
    private String list;
    
    @TagAttribute
    private String selected = null;

    @TagAttribute
    private String id = "";
    
    @TagAttribute
    private String klass = "";
    
    @TagAttribute(required = true)
    private String name = "";
    
    @TagAttribute
    private String style = "";
    
    @TagAttribute
    private String onblur = "";
    
    @TagAttribute
    private String onchange = "";
    
    @TagAttribute
    private String extra = "";
    
    @TagAttribute
    private String idName = null;
    
    @TagAttribute
    private String showAttr = null;
    
    @TagAttribute
    private String defaultText = null;
    
    @TagAttribute
    private String defaultValue = "0";

	@Override
    public String getStringToPrint() throws JspException {
        String[] values = findValues(name);
        StringBuffer sb = new StringBuffer();
        sb.append("<select " + klass + " " + id + " name=\"" + name + "\" " + onchange + " " + style + " " + onblur + " " + extra + ">");

        if (list != null) {

            javax.servlet.jsp.tagext.Tag parent = findAncestorWithClass(this, Context.class);
            List actionList = (List) Out.getValue(parent, list , pageContext, true);
            
            //List actionList = (List) action.getOutput().getValue(list);
            String value_id = "";
            if (defaultText != null) {
                sb.append("<option value=\""+defaultValue+"\">" + defaultText + "</option>");
            }

            if (actionList.size() > 0) {
				Class objectClass = actionList.get(0).getClass();
				boolean entityClass = (objectClass.isAnnotationPresent(Entity.class));
				if (entityClass && idName == null) {
					idName = Dao.getIdName(objectClass);
				}
                for (int i = 0; i < actionList.size(); i++) {
					if(idName!=null){
						value_id = ReflectionUtil.getField(actionList.get(i), idName).toString();
					} else {
						value_id = actionList.get(i).toString();
					}

                    sb.append("<option value=\"" + value_id + "\"");

                    if ((values == null || values.length == 0) && selected != null && selected.equals(value_id)) {
                        sb.append(" selected=\"true\" ");
                    } else if (contains(values, value_id)) {
                        sb.append(" selected=\"true\" ");
                    }
                    sb.append(">");
                    if (showAttr == null) {
                        sb.append(actionList.get(i));
                    } else {
                        sb.append(ReflectionUtil.getField(actionList.get(i), showAttr).toString());
                    }
                    sb.append("</option>");
                }
            }
        }
        sb.append("</select>");
        idName = null; //Nao entendi por que, mas coloquei aqui pra corrigir um bug!
        return sb.toString();
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public void setList(String list) {
        this.list = list;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStyle(String style) {
        this.style = "style=\"" + style + "\"";
    }

    public void setOnblur(String onblur) {
        this.onblur = "onblur=\"" + onblur + "\"";
    }

    public void setKlass(String klass) {
        this.klass = "class=\"" + klass + "\"";
    }

    @Override
    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public void setId(String id) {
        this.id = "id=\"" + id + "\"";
    }

    public void setIdName(String idName) {
        this.idName = idName;
    }

    public void setOnchange(String onchange) {
        this.onchange = "onchange=\"" + onchange + "\"";
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public void setShowAttr(String showAttr) {
        this.showAttr = showAttr;
    }
}