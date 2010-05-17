/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.futurepages.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
}