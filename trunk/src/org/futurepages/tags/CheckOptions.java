package org.futurepages.tags;

import java.util.List;
import javax.servlet.jsp.JspException;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.core.persistence.Dao;
import org.futurepages.core.tags.build.ContentTypeEnum;
import org.futurepages.util.ReflectionUtil;
import org.futurepages.core.tags.cerne.HTMLTag;

/**
 *
 * @author diogenes
 */
@Tag(bodyContent = ContentTypeEnum.EMPTY)
public class CheckOptions extends HTMLTag {

	@TagAttribute(required = true)
	private String name;

	@TagAttribute(required = true)
	private List fullList;

	@TagAttribute(required = true)
	private List list;

	@TagAttribute(required = false)
	private String attribute = null;

	@TagAttribute(required = true)
	private String showAttr = null;

	@TagAttribute
	private String cssClass = "";

	@Override
	public String getStringToPrint() throws JspException {
		StringBuffer sb = new StringBuffer();
		String value1, value2;
		if (fullList.size() > 0) {
			if (attribute == null) {
				attribute = Dao.getIdName(fullList.get(0).getClass());
			}

			if (list != null) {
				for (int i = 0; i < fullList.size(); i++) {
					value1 = ReflectionUtil.getField(fullList.get(i), attribute).toString();
					sb.append("<input value=\"" + value1 + "\" id=\"" + name + "_" + i + "\" type=\"checkbox\" name=\"" + name + "\" ");
					for (int j = 0; j < list.size(); j++) {
						value2 = ReflectionUtil.getField(list.get(j), attribute).toString();
						if (value1.equals(value2)) {
							sb.append("checked=\"checked\"");
						}
					}
					sb.append(cssClass + "/>&#32;" + ReflectionUtil.getField(fullList.get(i), showAttr).toString() + "<br />");
				}
			} else {
				for (int i = 0; i < fullList.size(); i++) {
					value1 = ReflectionUtil.getField(fullList.get(i), attribute).toString();
					sb.append("<input value=\"" + value1 + "\" id=\"" + name + "_" + i + "\" type=\"checkbox\" name=\"" + name + "\" " + cssClass + "/>&#32;"
							+ ReflectionUtil.getField(fullList.get(i), showAttr).toString() + "<br />");
				}

			}
		}

		return sb.toString();
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;

	}

	public void setShowAttr(String showAttr) {
		this.showAttr = showAttr;
	}

	public void setList(List list) {
		this.list = list;
	}

	public void setFullList(List fullList) {
		this.fullList = fullList;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCssClass(String cssClass) {
			this.cssClass = "class=\"" + cssClass + "\"";
		}
	}
