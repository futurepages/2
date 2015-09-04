package modules.escola.util;

public class UtilCPF {

	/**
	 * Calcula o dígito verificador do cpf a partir dos números da primeira parte
	 * do mesmo.
	 * 
	 * sendo o CPF "AAAAAAAAADF" passaremos como entrada "AAAAAAAAA" e receberemos como retorno "DF"
	 */
	public static String calculaDigitoVerificador(String num) {
		Integer primDig, segDig;

		int soma = 0, peso = 10;

		for (int i = 0; i< num.length(); i++) {
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

		}

		if (soma % 11 == 0 | soma % 11 == 1) {
			primDig = new Integer(0);

		} else {
			primDig = new Integer(11 - (soma % 11));
		}

		soma = 0;
		peso = 11;

		for (int i = 0; i
				< num.length(); i++) {
			soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;
		}

		soma += primDig.intValue() * 2;

		if (soma % 11 == 0 | soma % 11 == 1) {
			segDig = new Integer(0);

		} else {
			segDig = new Integer(11 - (soma % 11));

		}
		return primDig.toString() + segDig.toString();

	}
}
