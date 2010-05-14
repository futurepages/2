package org.futurepages.util.html;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParagraphApplierTest {

	private String raw;
	private String expected;
	private String kase;

	public ParagraphApplierTest(String raw, String expected, String kase) {
		super();
		this.raw = raw;
		this.expected = expected;
		this.kase = kase;
	}

	@Test
	public final void testApply_MaiorNaoFechada() {
		applyTestProcedure(kase, raw, expected);
	}

	private void applyTestProcedure(String msg, String raw, String expected) {
		String result = new ParagraphApplier().apply(raw);
		Assert.assertEquals(kase, expected, result);
	}

	@Parameters
	public static Collection parameters() {
		Collection col =  Arrays.asList(new Object[][] {
				
			{"asdf",				"<p>asdf</p>",				"conteúdo simples sem <p>"},
			{"<p style=\"a\">asdf",	"<p style=\"a\">asdf</p>", 	"não fechada mais externa com estilo"},
			{"asdf</p>",			"<p>asdf</p>", 				"não aberta mais externa"},
			{"as<p>df",				"<p>as</p><p>df</p>", 		"abertura no meio do conteúdo sem fechamento"},
			{"as</p>df",			"<p>as</p><p>df</p>", 		"fechameno no meio do conteúdo sem abertura"},
			
			{"<p style ='a'>Conteudo</p>",					"<p style ='a'>Conteudo</p>",			"tag íntegra com estilo"},
			{"A</p>B<p>C",	"<p>A</p><p>B</p><p>C</p>", 	"fechados sem abrir e abertos sem fechar (separados)"},
			{"A</p><p>BC",	"<p>A</p><p>BC</p>", 			"fechados sem abrir e abertos sem fechar (juntos)"},
			{"A</p>B</p>C",	"<p>A</p><p>B</p><p>C</p>", 	"dois fechados sem abrir (separados)"},
			{"A</p></p>B<p>C", "<p>A</p><p>B</p><p>C</p>",	"dois fechados sem abrir (juntos)"},
			
			{"</p></p>",	"", 		"fechamentos sem conteúdo"},
			{"<p><p>", 		"", 		"abrindo sem conteúdo"},
			{"</p>A<p>", 	"<p>A</p>", "abrindo sem conteúdo"},
			
			{"<p>A<p>B<p>C<p>D<p>E<p>F<p>G<p>", 		"<p>A</p><p>B</p><p>C</p><p>D</p><p>E</p><p>F</p><p>G</p>", "vários abrindo sem fechamento"},
			{"</p>A</p>B</p>C</p>D</p>E</p>F</p>G</p>", "<p>A</p><p>B</p><p>C</p><p>D</p><p>E</p><p>F</p><p>G</p>", "vários fechamentos sem abrir"},

			{"<pp>ansdf",	"<p><pp>ansdf</p>",			"com falsos <p>"},
			{"<p\\>",		"",			"com falsos <p>"},
			{"<p\\>aa",		"<p\\>aa</p>",			"com <p> mutante '\'"},
			{"<p style='b'/>aa",		"<p style='b'/>aa</p>",			"com <p> mutante '/' e estilo"},
		});
		return col;
	}
}
