package org.futurepages.core.tags.build;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.futurepages.annotations.SuperTag;
import org.futurepages.annotations.Tag;
import org.futurepages.annotations.TagAttribute;
import org.futurepages.util.Is;
import org.futurepages.util.The;

/**
 *Leitor das Annotations para Tag
 * @author Danilo
 */
public class ClassTagAnnotationReader {

    public TagBean readTag(Class klass) {
        TagBean tag = null;
        klass.getAnnotations();
        
        if (klass.isAnnotationPresent(Tag.class)) {
            tag = colectTag(klass);
            tag.setAttributes(colectAttributes(klass));
        }
        return tag;
    }

    private TagBean colectTag(Class klass) {
        TagBean tagBean = new TagBean();
        if (klass.isAnnotationPresent(Tag.class)) {
            Tag tag = (Tag) klass.getAnnotation(Tag.class);

            if (Is.empty(tag.name())) {
                tagBean.setName(The.uncapitalizedWord(klass.getSimpleName()));
            } else {
                tagBean.setName(tag.name());
            }
            
            if (Is.empty(tag.displayName())) {
                tagBean.setDisplayName(klass.getSimpleName());
            } else {
                tagBean.setDisplayName(tag.displayName());
            }
            
            tagBean.setTagClass(klass);
            tagBean.setContentType(tag.bodyContent());
        }

        return tagBean;
    }

    private List<TagAttributeBean> colectAttributes(Class klass) {

    	List<TagAttributeBean> allAttributes = new ArrayList<TagAttributeBean>();
    	Class superKlass = klass.getSuperclass();
    	if(superKlass.isAnnotationPresent(SuperTag.class)){
    		Field[] superFields = superKlass.getDeclaredFields();
    		allAttributes.addAll(filterAnnotatedFields(superFields));
    	}

        Field[] subFields = klass.getDeclaredFields();
        allAttributes.addAll(filterAnnotatedFields(subFields));
        return allAttributes;
    }

	private List<TagAttributeBean>  filterAnnotatedFields(Field[] superFields) {
		List<TagAttributeBean> attributes = new ArrayList<TagAttributeBean>();
		TagAttributeBean attribute;
		for (Field field : superFields) {
            if ( field.isAnnotationPresent(TagAttribute.class) ) {
                
            	attribute = createTagAttribute(field);
                attributes.add(attribute);
            }
        }
		return attributes;
	}

    private TagAttributeBean createTagAttribute(Field field) {

        TagAttributeBean attribute = new TagAttributeBean();
        TagAttribute tag = field.getAnnotation(TagAttribute.class);
        attribute.setRequired(tag.required());
        attribute.setRtexprvalue(tag.rtexprvalue());
        attribute.setType(field.getType());
        
        if (Is.empty(tag.name())) {
            attribute.setName(field.getName());
        } else {
            attribute.setName(tag.name());
        }

        if (Is.empty(tag.displayName())) {
            attribute.setType(field.getType());
        } else {
            attribute.setName(tag.displayName());
        }

        return attribute;
    }
}