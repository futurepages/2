
import modules.escola.util.UtilCPF;

public class TestesRelampagos {

	public static void main(String[] args) {
		// CPF de Leandro Santana Pereira, é pra imprimir 66
		System.out.println(UtilCPF.calculaDigitoVerificador("003510633"));

		// É pra imprimir true, pois o cpf é válido
		System.out.println(cpfEhValido("00351063366"));

		// É pra imprimir false, pois o cpf é inválido.
		System.out.println(cpfEhValido("03434343446"));
	}

	/**
	 * @param cpf somente caracteres numéricos
	 * @return true se o cpf é válido
	 */
	private static boolean cpfEhValido(String cpf){
		String digitoVerificador = UtilCPF.calculaDigitoVerificador(cpf.substring(0, 9));
		return digitoVerificador.equals(cpf.substring(9, 11));

	}
}