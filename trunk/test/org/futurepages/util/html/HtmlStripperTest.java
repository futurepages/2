package org.futurepages.util.html;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leandro
 */
public class HtmlStripperTest {

	private void asserts(String in, String result, String expected){
			if(!result.equals(expected)){
				System.out.println("[I]:"+in);
				System.out.println("[E]:"+expected);
				System.out.println("[R]:"+result);
				System.out.println();
			}
			assertEquals(expected,result);
	}


	@Test
	public void testReplaceInTags() {
		testReplaceInTagsProcedure("#<a class='kxp'>#</a>#", "'", "\"","#<a class=\"kxp\">#</a>#");
		testReplaceInTagsProcedure("<a class=\"cidadao brasileiro\" class=\"kxp\"></a>", "class=\".*?\\b\"", "","<a  ></a>");
		testReplaceInTagsProcedure("<a a='xy' b='yz' c='xz'><b a='xy' b='yz' c='xz'><c a='xy' b='yz' c='xz'>", "'.*?'", "'#'","<a a='#' b='#' c='#'><b a='#' b='#' c='#'><c a='#' b='#' c='#'>");
	}

	public void testReplaceInTagsProcedure(String original, String regex, String replacement, String expected){
		asserts(original,HtmlStripper.replaceInTags(original,regex,replacement),expected);
	}



	@Test
	public void testPoorText() {
		testPorTextProcedure("a<p style=\"text-align:center\">b<strong>c</strong>d</p>e"
						   , "abcde");

		testPorTextProcedure("a<p style=\"text-align:center\">b\n<strong>\nc</strong>d</p>e"
						   , "ab\n\ncde");

		testPorTextProcedure(" a <p style=\"text-align:center\"> b <strong> c </strong> d </p> e "
						   , " a  b  c  d  e ");
	}

	private void testPorTextProcedure(String in, String expected){
		asserts(in,new HtmlStripper(in).poorText(),expected);
	}



	@Test
	public void testNoTrashText() {

		testNoTrashProcedure("<xml>  <abc> <a:bc/> \r\n erro <abc/>  </xml>"
							,"");

		testNoTrashProcedure("<XML>  <abc> <a:bc/> \r\n erro <abc/>  </xml>"
							,"");

		testNoTrashProcedure("<a class=kx class=xkp style=x align='right'>abc</a>#<ul></ul>#<p style=\"\"></p>def"
							,"<a align=\"right\">abc</a>##def");

		testNoTrashProcedure("<a CLASS=kx class=xkp style=x align='right'>abc</a>#<UL></ul>#<p style=\"\"></p>def"
							,"<a align=\"right\">abc</a>##def");

		testNoTrashProcedure("<a>#<xml>p:q/><a:c/></xml> abc </a>#<ul></ul><p style=\"\"></p>d<!-- comentário --><xml></xml>ef"
							,"<a># abc </a>#def");

		testNoTrashProcedure("<xml type=\"text\">p:q/><a:c/></xml><ul></ul><p style=\"\"></p><!-- comentário --><xml></xml>"
							,"");

		testNoTrashProcedure("<xml type=\"text\">p:q/><a:c/></xml><ul></ul><script type=\"text/javascript\">alert();</script><p style=\"\"></p><!-- comentário --><xml></xml>"
							,"");



		testNoTrashProcedure("<a href=\"http://www.class/\"><u>clique</u> no <em>link</em> do <strong>site</strong></a>"
							,"<a href=\"http://www.class/\"><u>clique</u> no <em>link</em> do <strong>site</strong></a>");

		testNoTrashProcedure("<a style='text-decoration:underline' href=\"http://www.class/\" class=xpto><span style='text-decoration: underline;'>clique</span> no <em>link</em> do <span style=\"font-weight:bold\">site</span></a>"
							,"<a style=\"text-decoration:underline\" href=\"http://www.class/\"><span style=\"text-decoration: underline;\">clique</span> no <em>link</em> do <span style=\"font-weight:bold\">site</span></a>");
	}

	private void testNoTrashProcedure(String in, String expected){
		asserts(in,HtmlStripper.noTrashText(in),expected);
	}



	@Test
	public void testNoStylesText() {
		testNoStylesTextProcedure("<a>", new String[]{
											"<a class=k>",
											"<a class=kx>",
											"<a class=kxy>",
											"<a class=kxy style=kgb>",
											"<a style=kxy class=kgb style=may>",
											"<a style=kxy class=kgb style=may>",
											"<a style=kxy class=kgb style=may>",
											"<a style=kxy class=kgb style=may>",
											"<a style=kxy class=kgb style=may>",
											"<a class=kxy style=xpto>",
											"<a class=\"k\">",
											"<a style=\"k\"class='topic'>",
											"<a class='k'>",
											"<a style='kx'>",
											"<a style='kxy' class=\"mxp\">",
											"<a style=\n\"kx\">",
											"<a style=\t\"kx\">",
											"<a style\n=\r\n\t\"kx\">",
											"<a style=\"kx\">",
											"<a style=\"kxy\">",
											"<a style=k class=z>",
											"<a class=z style=k>",
											"<a style='k' class='z'>",
											"<a style=\"k\" class=\"z\">",
											"<a style=\"k\" class=\"k1 k2 k3\">",
							});

		testNoStylesTextProcedure("<a >", new String[]{
											"<a class=k ><style type=\"text/css\">.td{}</style>",
											"<a class=kx >",
											"<!-- coment --><a class=kxy >",
											"<a class=kxy  style=kgb>",
											"<xml class=kx></xml><a style=kxy class=kgb  style=may>",
											"<a  class=kxy style=xpto>",
											"<a  class = \"k\">",
											"<a style=\"k\"class='topic' >",
											"<a class = 'k' >",
											"<a style = 'kx' >",
											"<a style = 'kxy' class  =  \"sdfsdf sdf sd3f sdf sd3f sd_f sdfsdf fmxp\" >",
											"<a style \r=\n 'kxy' class \n\r\t =  \"sdfsdf sdf sd3f sdf sd3f sd_f sdfsdf fmxp\" >",
											"<a style=\"kx\" >",
											"<a style    = \"kxy\" >",
											"<a  class=z style=k>",
											"<a style='k' class= 'z' >",
											"<a style=\"k\" class=\"z\" >",
											"<a style = \"k\"  style = \"k\" class=\"k1 k2 k3\">",
							});

		testNoStylesTextProcedure("<a href=\"http://www.class/\"><u>clique</u> no <em>link</em> do <strong>site</strong></a>", new String[]{
								  "<a style='text-decoration:underline' href=\"http://www.class/\" class=xpto><span style='text-decoration: underline;'>clique</span> no <em>link</em> do <span style=\"font-weight:bold;\">site</span></a>",
								  "<a href=\"http://www.class/\" class=xpto style='text-decoration: underline;'><span style=\"text-decoration:underline;\">clique</span> no <em>link</em> do <span style=\"font-weight:bold;text-align:right\">site</span></a>",

							});

		testNoStylesTextProcedure("<a href=\"http://www.class/\">\\n<u>clique\\n</u> no <em>link</em> do \\n <strong>site\\n</strong>\\n</a>\\n", new String[]{
								  "<a style='text-decoration:underline' href=\"http://www.class/\" class=xpto>\\n<span style='text-decoration: underline;'>clique\\n</span> no <em>link</em> do \\n <span style=\"font-weight:bold;\">site\\n</span>\\n</a>\\n",
							});

//											"<a style=\"k\" class=\"k1 k2 k3\" >",
//											"<a style=\"k\" class=\"k1 k2 k3\" align=\"right\">",
//											"<a style=\'k\' class=\'k\'>",
//											"<a style=\"k\" class=\"z\" >",
//											"<a style=\"k\" class='k1 k2 k3'>",
//											"<a style=\"k\" class='k1 k2 k3' >",
//											"<a style=k class='k1 k2 k3' align=\"right\">",
//											"<a style=k>",
//											"<a class=k>",
//											"<a style=k class=z>",
//											"<a style=k class=z >"

//		"<a style=k ><b align=center class=k><c style=k><d style=k class=k>"
//								 ,"<a ><b align=center><c><d>");

//	testNoStylesTextProcedure("<a style=k ><b align='center' class=\"k1 k2 k3 k4\">"
//								 ,"<a ><b align='center'><c > <d>");
//
//		testNoStylesTextProcedure("<a style=\"k\" ><b   class=\"k\"><c style = 'k'><d style='k' class=\"k\">"
//								 ,"<a ><b   ><c><d>");

	}

	private void testNoStylesTextProcedure(String expected, String[] ins){
		for(String in : ins){
			asserts(in , HtmlStripper.noStylesText(in),expected );
		}
	}
}