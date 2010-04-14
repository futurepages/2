package org.futurepages.util.html;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.futurepages.util.FileUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leandro
 */
public class HtmlStripperTest {


	@Test
	public void testPoorText() {
		assertEquals("abcde",          new HtmlStripper("a<p style=\"text-align:center\">b<strong>c</strong>d</p>e").poorText());
		assertEquals(" a  b  c  d  e ",new HtmlStripper(" a <p style=\"text-align:center\"> b <strong> c </strong> d </p> e ").poorText());
	}

	@Test
	public void testRichText() {
		try {
			String path = "D:/Users/leandro/Documents/WorkSpaces/netbeans/futurepages2/src/org/futurepages/util/html/res/testHTML";
			System.out.println(path);
			String str = FileUtil.getStringContent(path);
			new HtmlStripper(str).richText(true, true, true, true, true, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}