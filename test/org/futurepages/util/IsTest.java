package org.futurepages.util;

import static org.junit.Assert.*;

import org.futurepages.test.factory.StringFactory;
import org.junit.Test;

public class IsTest {

	@Test
	public void testValidStringKey_valido_simples() {
		assertTrue("login v�lido - maria", Is.validStringKey("maria"));
	}

	@Test
	public void testValidStringKey_valido_comSublinhado() {
		assertTrue("login v�lido - wiltin", Is.validStringKey("maria_silva"));
	}

	@Test
	public void testValidStringKey_valido_comPonto() {
		assertTrue("login v�lido - veronica", Is.validStringKey("vero.nica"));
	}

	@Test
	public void testValidStringKey_valido_hifen() {
		assertTrue("login v�lido - samila", Is.validStringKey("sam-ila"));
	}

  	@Test
	public void testValidStringKey_invalido_vogalComTil() {
		assertFalse("Com vogal acentuada  '�': 'm�o' INV�LIDO.", Is.validStringKey("m�oasd"));
	}

	@Test
	public void testValidStringKey_invalido_espacos() {
		assertFalse("Com espa�os 'maria de fatima'. INV�LIDO", Is.validStringKey("maria de fatima"));
	}

	@Test
	public void testValidStringKey_invalido_cedilha_espaco() {
		assertFalse("Com cedilha '�': 'ca�a'", Is.validStringKey("ca�a ca�a"));
	}

	@Test
	public void testValidStringKey_invalido_vogalAcentuada() {
		assertFalse("Vogal acentuada:'�': 'cac�ldas'  INV�LIDO", Is.validStringKey("cac�ldas"));
	}

	@Test
	public void testValidStringKey_invalido_til() {
		assertFalse("Com til'~'- 'm~oasdfasdf'", Is.validStringKey("m~oasdfasdf"));
	}

	@Test
	public void testValidStringKey_invalido_acentoAgudo() {
		assertFalse("Com acento agudo '�' ", Is.validStringKey("asdf�ajkl"));
	}
	
	@Test
	public void testValidStringKey_invalido_pequeno_3() {
		assertFalse("Com tres caracteres- 'qwe'", Is.validStringKey("qwe"));
	}

	@Test
	public void testValidStringKey_invalidoComArroba() {
		assertFalse("com @ n�o � v�lido", Is.validStringKey("usuario@gmail.com"));
	}

	@Test
	public void testValidStringKey_comNumeroValido() {
		assertTrue("iniciando com n�mero � v�lido", Is.validStringKey("123login"));
	}

	@Test
	public void testValidStringKey_comNumeroInvalido() {
		assertFalse("iniciando com n�mero e � inv�lido", Is.validStringKey("123login",5,30,false));
	}

	@Test
	public void testValidStringKey_comNumeroValido2() {
		assertTrue("iniciando com n�mero e � inv�lido", Is.validStringKey("123login",5,30,true));
	}

	@Test
	public void testValidStringKey_invalido_muitoGrande_50() {
		assertFalse("Muito grnade- 'size 50'", Is.validStringKey(StringFactory.getRandom(50)));
	}

  	@Test
  	public void testValidStringKey_invalido_espaco() {
		assertFalse("Com espacos 'sad _asd' INV�LIDO", Is.validStringKey("sad _asd"));
	}

  	@Test
  	public void testValidURL_SEM_PONTO() {
		assertFalse("sem ponto INV�LIDO", Is.validURL("http://wwworkutcom"));
	}
  	
  	@Test
  	public void testValidURL_SEM_http() {
		assertFalse("com ponto sem http", Is.validURL("www.orkut.com"));
	}

  	@Test
  	public void testValidURL_COM_https() {
		assertTrue("com ponto com https", Is.validURL("https://www.orkut.com"));
	}

	@Test
  	public void validCapitalizedPersonName() {
		assertTrue("", Is.validCapitalizedPersonName("Leandro Santana Pereira"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro da Silva Pereira"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro dos Santos e Silva I"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro de Silva e Santos da Costa II"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro dos Santos jr")); //nao � muito interessante ser true
		assertTrue("", Is.validCapitalizedPersonName("Leandro dos Santos XIII")); //por enquanto � true, mas n�o � interessante ser
		assertTrue("", Is.validCapitalizedPersonName("Leandro dos Santos Xiii")); //por enquanto � true, mas n�o � interessante ser
		assertTrue("", Is.validCapitalizedPersonName("Maria da Silva e Jo�o"));
		assertTrue("", Is.validCapitalizedPersonName("Leonardo di Caprio"));
		assertTrue("", Is.validCapitalizedPersonName("Leonardo Di Caprio"));
		assertTrue("", Is.validCapitalizedPersonName("Leonardo d'Caprio"));
		assertTrue("", Is.validCapitalizedPersonName("Leonardo d`Caprio"));
		assertTrue("", Is.validCapitalizedPersonName("Joana D`arque"));
		assertTrue("", Is.validCapitalizedPersonName("Joana D`Arque"));
		assertTrue("", Is.validCapitalizedPersonName("Joana D'arque"));
		assertTrue("", Is.validCapitalizedPersonName("Joana D'Arque"));
		assertTrue("", Is.validCapitalizedPersonName("Leonardo de Caprio"));
		assertTrue("", Is.validCapitalizedPersonName("Leandro A Santana Pereira")); //seria mesmo interessante este caso?

		assertFalse("", Is.validCapitalizedPersonName("leandro"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro"));
		assertFalse("", Is.validCapitalizedPersonName("LEANDRO"));
		assertFalse("", Is.validCapitalizedPersonName("lEANDRO dE sANTANA"));
		assertFalse("", Is.validCapitalizedPersonName("Leonardo diCaprio"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro dos Santos xiii")); //por enquanto � true, mas n�o � interessante ser
		assertFalse("", Is.validCapitalizedPersonName("leandro santana pereira"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro De Santana Pereira"));
		assertFalse("", Is.validCapitalizedPersonName("LEANDRO SANTANA DE PEREIRA"));
		assertFalse("", Is.validCapitalizedPersonName("LEANDRO SANTANA PEREIRA"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro santana pereira"));
		assertFalse("", Is.validCapitalizedPersonName("lEANDRO DE sANTANA pEREIRA"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro santana de Pereira"));
		assertFalse("", Is.validCapitalizedPersonName("Leandro Dos Santos goe"));

	}
}