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
		assertTrue("login válido - maria", Is.validStringKey("maria"));
	}

	@Test
	public void testValidStringKey_valido_comSublinhado() {
		assertTrue("login válido - wiltin", Is.validStringKey("maria_silva"));
	}

	@Test
	public void testValidStringKey_valido_comPonto() {
		assertTrue("login válido - veronica", Is.validStringKey("vero.nica"));
	}

	@Test
	public void testValidStringKey_valido_hifen() {
		assertTrue("login válido - samila", Is.validStringKey("sam-ila"));
	}

  	@Test
	public void testValidStringKey_invalido_vogalComTil() {
		assertFalse("Com vogal acentuada  'ã': 'mão' INVÁLIDO.", Is.validStringKey("mãoasd"));
	}

	@Test
	public void testValidStringKey_invalido_espacos() {
		assertFalse("Com espaços 'maria de fatima'. INVÁLIDO", Is.validStringKey("maria de fatima"));
	}

	@Test
	public void testValidStringKey_invalido_cedilha_espaco() {
		assertFalse("Com cedilha 'ç': 'caça'", Is.validStringKey("caça caça"));
	}

	@Test
	public void testValidStringKey_invalido_vogalAcentuada() {
		assertFalse("Vogal acentuada:'´': 'cacíldas'  INVÁLIDO", Is.validStringKey("cacíldas"));
	}

	@Test
	public void testValidStringKey_invalido_til() {
		assertFalse("Com til'~'- 'm~oasdfasdf'", Is.validStringKey("m~oasdfasdf"));
	}

	@Test
	public void testValidStringKey_invalido_acentoAgudo() {
		assertFalse("Com acento agudo '´' ", Is.validStringKey("asdf´ajkl"));
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
		assertFalse("Com espacos 'sad _asd' INVÁLIDO", Is.validStringKey("sad _asd"));
	}

  	@Test
  	public void testValidURL_SEM_PONTO() {
		assertFalse("sem ponto INVÁLIDO", Is.validURL("http://wwworkutcom"));
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