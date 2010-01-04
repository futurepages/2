package org.futurepages.core.tags.cerne;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.futurepages.tags.Out;

/**
 * @author Sergio Oliveira
 */
public abstract class AbstractListTag extends AbstractListContext {

   private String value;

   private String orderBy = null;

   private boolean desc = false;

   public void setValue(String value) {
      this.value = value;
   }

   public void setOrderBy(String orderBy) {
      this.orderBy = orderBy;
   }

   public void setDesc(boolean decreasing) {
      this.desc = decreasing;
   }

   protected String getName() {
      return value;
   }

   public List<Object> getList() throws JspException {
      Tag parent = findAncestorWithClass(this, Context.class);

      if (parent != null) {
         Context ctx = (Context) parent;
         Object obj = ctx.getObject();
         if (obj != null) {
            Object object = Out.getValue(obj, value, false);
            if (object instanceof List) {

               if (orderBy != null)
                  return ListSorter.sort((List<Object>) object, orderBy, desc);

               return (List<Object>) object;

            } else if (object instanceof Object[]) {

               if (orderBy != null)
                  return ListSorter.sort(Arrays.asList((Object[]) object), orderBy, desc);

               return Arrays.asList((Object[]) object);

            } else if (object instanceof Set) {

               // TODO:
               // this is not good, but for now let's do it to support sets...
               // A ListWrapper for a Set would be better to avoid copying...

               Set set = (Set) object;

               List<Object> list = new ArrayList<Object>(set);

               if (orderBy != null)
                  return ListSorter.sort(list, orderBy, desc);

               return list;

            } else if (object instanceof Collection) {

               // TODO:
               // this is not good, but for now let's do it to support sets...
               // A CollectionWrapper for a Collection would be better to avoid
               // copying...

               Collection coll = (Collection) object;

               List<Object> list = new ArrayList<Object>(coll);

               if (orderBy != null)
                  return ListSorter.sort(list, orderBy, desc);

               return list;
               
            } else if( object instanceof Map) {
            	
				Collection coll = ((Map) object).values();
            	
				List<Object> list = new ArrayList<Object>(coll);

	               if (orderBy != null)
	                  return ListSorter.sort(list, orderBy, desc);

	               return list;
            }
         }
      }

      /*
       * if (action != null) { Output output = action.getOutput(); Object obj =
       * output.getValue(value); if (obj instanceof List) { return (List) obj; }
       * else if (obj instanceof Object[]) { return Arrays.asList((Object[])
       * obj); } }
       */

      Object obj = Out.getValue(value, pageContext, false);

      if (obj == null)
         return null;

      if (obj instanceof List) {

         if (orderBy != null)
            return ListSorter.sort((List<Object>) obj, orderBy, desc);

         return (List<Object>) obj;

      } else if (obj instanceof Object[]) {

         if (orderBy != null)
            return ListSorter.sort(Arrays.asList((Object[]) obj), orderBy, desc);

         return Arrays.asList((Object[]) obj);

      } else if (obj instanceof Set) {

         // TODO:
         // this is not good, but for now let's do it to support sets...
         // A ListWrapper for a Set would be better to avoid copying...

         Set set = (Set) obj;

         List<Object> list = new ArrayList<Object>(set);

         if (orderBy != null)
            return ListSorter.sort(list, orderBy, desc);

         return list;

      } else if (obj instanceof Collection) {

         // TODO:
         // this is not good, but for now let's do it to support collection...
         // A CollectionWrapper for a Collection would be better to avoid
         // copying...

         Collection coll = (Collection) obj;

         List<Object> list = new ArrayList<Object>(coll);

         if (orderBy != null)
            return ListSorter.sort(list, orderBy, desc);

         return list;

      } else if( obj instanceof Map) {
      	
			Collection coll = ((Map) obj).values();
      	
			List<Object> list = new ArrayList<Object>(coll);

             if (orderBy != null)
                return ListSorter.sort(list, orderBy, desc);

             return list;
      }

      throw new JspException("Tag List: Value " + value + " (" + obj.getClass().getName() + ") is not an instance of List or Object[], List or Set!");
   }

}