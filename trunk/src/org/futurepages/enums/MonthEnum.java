package org.futurepages.enums;

import java.util.Calendar;

public enum MonthEnum {

	JANEIRO(1, "janeiro", "jan"),
	FEVEREIRO(2, "fevereiro", "fev"),
	MARCO(3, "março", "mar"),
	ABRIL(4, "abril", "abr"),
	MAIO(5, "maio", "mai"),
	JUNHO(6, "junho", "jun"),
	JULHO(7, "julho", "jul"),
	AGOSTO(8, "agosto", "ago"),
	SETEMBRO(9, "setembro", "set"),
	OUTUBRO(10, "outubro", "out"),
	NOVEMBRO(11, "novembro", "nov"),
	DEZEMBRO(12, "dezembro", "dez");

	private int id;
	private String nome;
	private String sigla;

	private MonthEnum(int id, String nome, String sigla) {

		this.id = id;
		this.nome = nome;
		this.sigla = sigla;
	}

	public static String get(int i) {
		return MonthEnum.values()[i - 1].nome;
	}

	public static String get(Calendar cal) {
		return MonthEnum.values()[cal.get(Calendar.MONTH)].nome;
	}

        public static String getMonthSigla(Calendar cal) {
		return MonthEnum.values()[cal.get(Calendar.MONTH)].sigla;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public static int quantidadeDeDias(int ano, int mes) {
		if (mes != 2) {
			int[] mesesQuant = new int[]{0, 31, 0, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
			return mesesQuant[mes];
		} else { //fevereiro e seus bisextos
			return ((ano % 100 != 0) ? (((ano % 4) != 0) ? 28 : 29) : ((ano / 100 % 4 != 0) ? 28 : 29));
		}
	}

	@Override
	public String toString() {
		return this.nome;
	}
}
