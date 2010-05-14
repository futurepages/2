package org.futurepages.util.html;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.Assert;

import org.futurepages.util.CalendarUtil;
import org.futurepages.util.DateUtil;
import org.futurepages.util.FileUtil;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author leandro
 */
public class HtmlStripperTest {

	static long start;
	static long end;
	
	@BeforeClass
	public static void beforeClass(){
		start = System.currentTimeMillis();
	}
	
	@AfterClass
	public static void afterClass(){
		end = System.currentTimeMillis();
		long time = CalendarUtil.getDifference(start, end, 1);
		System.out.println("HtmlStripperTest.afterClass() Tempo gasto (ms) :"+ time);
	}

	private String getResourcePath(){
		String root = "/home/danilo/workspace/futurepages/test/";
		return root;
	}
	
	/**
	 * Default: P with Bold, Italic & Underline + params
	 * @param styles it's allowed: * style="" class="" , H1,...,H6 e tags <style>
	 * @param lists it's allowed: UL OL LI BLOCKOTE
	 * @param image it's allowed: IMG
	 * @param anchor it's allowed: A
	 * @param table it's allowed: TABLE, TR, TD, TH, TBODY
	 * @return the stripped html
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void richTextTestProcedure(boolean styles, boolean lists, boolean image, boolean anchor, boolean table, String expectedPath, String msg) throws FileNotFoundException, IOException{
		String root = this.getResourcePath();
		String path = root+"org/futurepages/util/html/res/testHtml.html";
		richTextTestProcedure(styles, lists, image, anchor, table, path, expectedPath, msg);
	}
	
	private void richTextTestProcedure(boolean styles, boolean lists, boolean image, boolean anchor, boolean table, String originalPath, String expectedPath, String msg) throws FileNotFoundException, IOException{

		String root = this.getResourcePath();
		String dirtyContent = FileUtil.getStringContent(originalPath);
		String stripped = new HtmlStripper(dirtyContent).richText(styles, lists, image, anchor, table);
		assertEquals(expectedPath, msg, root, originalPath, dirtyContent, stripped);
	}

	private void plainTextTestProcedure(String expectedPath, String msg) throws FileNotFoundException, IOException{
		String root = this.getResourcePath();
		String path = root+"org/futurepages/util/html/res/testHtml.html";
		String dirtyContent = FileUtil.getStringContent(path);
		String stripped = new HtmlStripper(dirtyContent).plainText();
		assertEquals(expectedPath, msg, root, path, dirtyContent, stripped);
	}

	private void assertEquals(String expectedPath, String msg, String root,	String path, String dirtyContent, String stripped)
			throws FileNotFoundException, IOException {
		String expectedContent = FileUtil.getStringContent(root+expectedPath);
		Assert.assertNotNull("O contéudo do elemento original não pode ser nulo. >"+path,dirtyContent);
		Assert.assertNotNull("O contéudo do elemento esperado não pode ser nulo. >"+root+expectedPath,expectedContent);
		Assert.assertEquals(msg, expectedContent, stripped);
	}
	
	@Test
	public void testeRichTextCleanWithStyle() throws FileNotFoundException, IOException{
		richTextTestProcedure(true, false, false, false, false, 
				"org/futurepages/util/html/res/expectedCleanStyle.html",
				"limpar tudo mas manter style");
	}

	@Test
	public void testePlainText() throws FileNotFoundException, IOException{
		plainTextTestProcedure("org/futurepages/util/html/res/expectedCleanNoStyle.html", 
				"limpar tudo e remover style");
	}
	

	@Test
	public void testeRichText_DirtyStyle() throws FileNotFoundException, IOException{
		richTextTestProcedure(true, true, true, true, true, "org/futurepages/util/html/res/expectedDirtyStyle.html",
				"manter tudo (tags e style)");
	}

	@Test
	public void testeRichText_NoStyle() throws FileNotFoundException, IOException{
		richTextTestProcedure(false, true, true, true, true, "org/futurepages/util/html/res/expectedDirtyNoStyle.html", 
				"manter tags mas não manter style");
	}
	
	
	@Test
	public void testeRichText_HtmlBronken() throws FileNotFoundException, IOException{
		String path = getResourcePath()+"org/futurepages/util/html/res/bronkenHtml.html";
		richTextTestProcedure(true, true, true, true, true, path,
				"org/futurepages/util/html/res/expectedBronkenHtml.html", "html quebrado(tags não fechadas, aspas nao fechadas)");
	}
	
	@Test
	public void testeRichText_longHtml() throws FileNotFoundException, IOException{
		String path = getResourcePath()+"org/futurepages/util/html/res/longHtml.html";
		richTextTestProcedure(false, false, false, false, false, path,
				"org/futurepages/util/html/res/expectedLongHtml.html", "html quebrado(tags não fechadas, aspas nao fechadas)");
	}
	
	@Test
	public void testeRichText_longHtmlTable_span() throws FileNotFoundException, IOException{
		String path = getResourcePath()+"org/futurepages/util/html/res/longTableSpan.html";
		richTextTestProcedure(false, false, false, false, false, path,
				"org/futurepages/util/html/res/expectedLongTableSpan.html", "html quebrado(tags não fechadas, aspas nao fechadas)");
	}
	
}