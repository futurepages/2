package org.futurepages.formatters;

import org.futurepages.util.CNPJUtil;
import org.futurepages.util.CPFUtil;
import java.util.Locale;
import org.futurepages.core.formatter.Formatter;
 
 public class CPFCNPJFormatter implements Formatter {
 	
 	public String format(Object value, Locale loc) {
		String cpfCnpj = (String) value;
		if(cpfCnpj.length() == CPFUtil.QUANTIDADE_DIGITOS_CPF){
			return CPFUtil.formata(cpfCnpj);
		}
		else if(cpfCnpj.length() == CNPJUtil.QUANTIDADE_DIGITOS_CNPJ){
			return CNPJUtil.formata(cpfCnpj);
		}
		else{
			return (String) value;
		} 
 	}
 }