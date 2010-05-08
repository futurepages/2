package org.futurepages.util.html;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class HtmlStripperTest_noTrashText {

	private String tag;
	private String expected;
	private String caso;

	public HtmlStripperTest_noTrashText(String tag, String expected) {
		this.tag = tag;
		this.expected = expected;
		this.caso = "";
	}

	@Test
	public void testTreated() {
		String result = HtmlStripper.noStylesText(this.tag);
		Assert.assertEquals(caso, expected, result);
	}

	@Parameters
	public static Collection parameters() {
		Collection col =  Arrays.asList(new Object[][] {
				{"<xml>  <abc> <a:bc/> \r\n erro <abc/>  </xml>",""},
				{"<XML>  <abc> <a:bc/> \r\n erro <abc/>  </xml>",""},
				{"<a class=kx class=xkp style=x align='right'>abc</a>#<ul></ul>#<p style=\"\"></p>def"
					,"<a align=\"right\">abc</a>##def"},
				{"<a CLASS=kx class=xkp style=x align='right'>abc</a>#<UL></ul>#<p style=\"\"></p>def"
					,"<a align=\"right\">abc</a>##def"},
				{"<a>#<xml>p:q/><a:c/></xml> abc </a>#<ul></ul><p style=\"\"></p>d<!-- comentário --><xml></xml>ef"
					,"<a># abc </a>#def"},
				{"<xml type=\"text\">p:q/><a:c/></xml><ul></ul><p style=\"\"></p><!-- comentário --><xml></xml>"
					,""},
				{"<xml type=\"text\">p:q/><a:c/></xml><ul></ul><script type=\"text/javascript\">alert(},</script><p style=\"\"></p><!-- comentário --><xml></xml>"
					,""},
				{"<a href=\"http://www.class/\"><u>clique</u> no <em>link</em> do <strong>site</strong></a>"
					,"<a href=\"http://www.class/\"><u>clique</u> no <em>link</em> do <strong>site</strong></a>"}
		});
		return col;
	}

}