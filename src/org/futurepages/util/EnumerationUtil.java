package org.futurepages.util;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * 
 * @author Sergio Oliveira
 */
public class EnumerationUtil {
	
   public static Iterator<String> toIterator(final Enumeration en) {

	   return new Iterator() {
		   
		   public boolean hasNext() {
			   
			   return en.hasMoreElements();
			   
		   }
		   
		   public Object next() {
			   
			   return en.nextElement();
			   
		   }
		   
		   public void remove() {
			   
			   throw new UnsupportedOperationException("Iterator is backed by Enumeration!");
			   
		   }
		   
	   };
   }
}

