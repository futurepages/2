package org.futurepages.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class CPFUtilTest {

    @Test
    public void geraCPF() {
        String cpfGerado = CPFUtil.geraCPF();
        assertNotNull("O CPF n�o deveria ser nulo.",cpfGerado);
        assertEquals("O tamanho do CPF deveria ser 11.",11,cpfGerado.length());
        assertTrue("O CPF gerado n�o � v�lido.",CPFUtil.validaCPF(cpfGerado));
    }

	@Test
    public void formataCPF() {
        assertEquals("O CPF 00351063366 n�o foi formatado corretamente.","003.510.633-66",CPFUtil.formata("00351063366"));
    }

    @Test
    public void validaCPF() {
        String cpfComLetras             = "abcdefghijk";
        String cpfInvalidoIguais        = "11111111111";
        String cpfInvalidoQualquer      = "22022030303";
        String cpfInvalidoMenor         = "22";
        String cpfCaracteresSeparadores = "003.510.633-66";
        String cpfComEspacos   			= " 00351063366 ";
        String cpfValido       			= "00351063366";

        assertFalse("CPF null n�o deveria ser v�lido", CPFUtil.validaCPF(null));
        assertFalse("CPF com letras n�o deveria ser v�lido",CPFUtil.validaCPF(cpfComLetras));        
        assertFalse("O CPF com caracteres iguais n�o deveria ser v�lido", CPFUtil.validaCPF(cpfInvalidoIguais));
        assertFalse("Um CPF n�o-v�lido foi validado.",CPFUtil.validaCPF(cpfInvalidoQualquer));
        assertFalse("Um CPF com menos de 11 caracteres n�o deveria ser v�lido.",CPFUtil.validaCPF(cpfInvalidoMenor));
        assertFalse("O CPF n�o deveria ser v�lido", CPFUtil.validaCPF(cpfCaracteresSeparadores));
        assertFalse("O CPF n�o deveria ser v�lido", CPFUtil.validaCPF(cpfComEspacos));
        
        assertTrue("O CPF deveria ser v�lido", CPFUtil.validaCPF(cpfValido));
    }
}