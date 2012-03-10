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
				
			{"<p>A</p><p>G</p>", "<p>A</p><p>G</p>",			"Tudo certo sem style"},
			{"<p style ='a'>Conteudo</p>",					"<p style ='a'>Conteudo</p>",			"tag �ntegra com estilo"},

			{"asdf",				"<p>asdf</p>",				"conte�do simples sem <p>"},
			{"<p style=\"a\">asdf",	"<p style=\"a\">asdf</p>", 	"n�o fechada mais externa com estilo"},
			{"<p style=\"a\"  >asdf" ,	"<p style=\"a\"  >asdf</p>", 	"n�o fechada mais externa com estilo e espa�o"},
			{"asdf</p>",			"<p>asdf</p>", 				"n�o aberta mais externa"},
			{"as<p>df",				"<p>as</p><p>df</p>", 		"abertura no meio do conte�do sem fechamento"},
			{"as</p>df",			"<p>as</p><p>df</p>", 		"fechameno no meio do conte�do sem abertura"},
			{"A</p>B<p>C",	"<p>A</p><p>B</p><p>C</p>", 	"fechados sem abrir e abertos sem fechar (separados)"},
			{"A</p><p>BC",	"<p>A</p><p>BC</p>", 			"fechados sem abrir e abertos sem fechar (juntos)"},
			{"A</p>B</p>C",	"<p>A</p><p>B</p><p>C</p>", 	"dois fechados sem abrir (separados)"},
			{"A</p></p>B<p>C", "<p>A</p><p>B</p><p>C</p>",	"dois fechados sem abrir (juntos)"},
			
			{"</p></p>",	"", 		"fechamentos sem conte�do"},
			{"<p><p>", 		"", 		"abrindo sem conte�do"},
			{"</p>A<p>", 	"<p>A</p>", "abrindo sem conte�do"},
			
			{"<p>A<p>B<p>C<p>D<p>E<p>F<p>G<p>", 		"<p>A</p><p>B</p><p>C</p><p>D</p><p>E</p><p>F</p><p>G</p>", "v�rios abrindo sem fechamento"},
			{"</p>A</p>B</p>C</p>D</p>E</p>F</p>G</p>", "<p>A</p><p>B</p><p>C</p><p>D</p><p>E</p><p>F</p><p>G</p>", "v�rios fechamentos sem abrir"},

			{"<pp>a<e>nsdf",	"<p><pp>a<e>nsdf</p>",			"com falsos <p> e tag estranha"},
			{"<p\\>",		"",			"com falsos <p>"},
			{"<p\\>aa",		"<p\\>aa</p>",			"com <p> mutante '\'"},
			{"<p style='b'/>aa",		"<p style='b'/>aa</p>",			"com <p> mutante '/' e estilo"},

			{"<p>A</p><ul><li>1</li><li>2</li><p>B</p>",	"<p>A</p><ul><li>1</li><li>2</li><p>B</p>", 		"�ntegro com presen�a de UL"},
			{"<p>A</p><p><ul><li>1</li><li>2</li></ul><p>B</p>", "<p>A</p><ul><li>1</li><li>2</li><p>B</p>", 		"�ntegro com presen�a de UL"},
			{"<span><p>LLLLLL</p></span>", "<p><span><p>LLLLLL</p></span></p>", "�ntegro com presen�a de UL"},
		});
		return col;
	}
}	