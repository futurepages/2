package org.futurepages.util.html;

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
		assertEquals("abcde",HtmlStripper.poorText("a<p style=\"text-align:center\">b<strong>c</strong>d</p>e"));
		assertEquals(" a  b  c  d  e ",HtmlStripper.poorText(" a <p style=\"text-align:center\"> b <strong> c </strong> d </p> e "));
	}

	@Test
	public void testPlainText() {
	}

}