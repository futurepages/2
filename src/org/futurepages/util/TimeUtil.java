package org.futurepages.util;

/**
 * Rotinas �teis de manipula��o de datas String
 */
public class TimeUtil {

	/**
	 * Valida o tempo em hh:mm verificando se � uma hora v�lida ou se � um minuto v�lido
	 * caso n�o seja retorna false
	 */
	public static boolean timeIsValid(String horario) {

		if (horario == null) {
			return false;
		} else if (horario.toString().trim().equals("") || horario.length() != 5) {
			return false;
		} else {

			String separadorDoisPontos = horario.substring(2, 3);
			if (!separadorDoisPontos.equals(":")) {
				return false;
			}
			try {
				int hora = Integer.parseInt(horario.substring(0, 2));
				int minuto = Integer.parseInt(horario.substring(3, 5));
				if (hora < 0 || hora >= 24) {
					return false;
				}
				if (minuto < 0 || minuto > 60) {
					return false;
				}

			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
