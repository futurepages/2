package org.futurepages.core.tags.build;

import java.util.ArrayList;
import java.util.List;

/**
 * Tag jsp
 * @author Danilo
 */
public class TagBean {

    private List<TagAttributeBean> attributes;
    private String name;
    private String displayName;
    private ContentTypeEnum contentType;
    private Class tagClass;

    public TagBean() {
        this.attributes = new ArrayList<TagAttributeBean>();
    }

    public TagBean(String name, String displayName, Class type, ContentTypeEnum contentType) {
        this.name = name;
        this.displayName = displayName;
        this.contentType = contentType;
        this.tagClass = type;
        this.attributes = new ArrayList<TagAttributeBean>();
    }

    public void setTagClass(Class tagType) {
        this.tagClass = tagType;
    }

    public Class getTagClass() {
        return tagClass;
    }

    public List<TagAttributeBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<TagAttributeBean> attributes) {
        this.attributes = attributes;
    }

    public ContentTypeEnum getContentType() {
        return contentType;
    }

    public void setContentType(ContentTypeEnum contentType) {
        this.contentType = contentType;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
