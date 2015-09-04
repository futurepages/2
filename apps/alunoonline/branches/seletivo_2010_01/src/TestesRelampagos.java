
import modules.escola.util.UtilCPF;

public class TestesRelampagos {

	public static void main(String[] args) {
		// CPF de Leandro Santana Pereira, � pra imprimir 66
		System.out.println(UtilCPF.calculaDigitoVerificador("003510633"));

		// � pra imprimir true, pois o cpf � v�lido
		System.out.println(cpfEhValido("00351063366"));

		// � pra imprimir false, pois o cpf � inv�lido.
		System.out.println(cpfEhValido("03434343446"));
	}

	/**
	 * @param cpf somente caracteres num�ricos
	 * @return true se o cpf � v�lido
	 */
	private static boolean cpfEhValido(String cpf){
		String digitoVerificador = UtilCPF.calculaDigitoVerificador(cpf.substring(0, 9));
		return digitoVerificador.equals(cpf.substring(9, 11));

	}
}