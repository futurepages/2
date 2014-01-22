package org.futurepages.util.template.simpletemplate.util;

import java.util.Map;
import org.futurepages.core.config.Params;
import org.futurepages.util.Is;
import org.futurepages.util.ReflectionUtil;

/**
 *
 * @author thiago
 */
public class GetFromMap {
	
	// @TODO: Não está sendo utilizado, fazer com que seja.
	//Entrada é ${VAR_PATH}, saída é o valor encontrado. Vazio é retornado se nada for encontrado.
	public static String getParamValue(String matchedVar, Map<String,Object> mapValues){
		String key = matchedVar.substring(2, matchedVar.length()-1);
		Object value = getValue(key, mapValues);
		return (value==null? (Params.get(key)!=null ? Params.get(key) : "") : value.toString());
	}

	//Entrada é VAR_PATH, saída é o valor encontrado. Vazio é retornado se nada for encontrado.
	public static Object getValue(String key, Map<String,Object> mapValues){
		return getValue(key, "", mapValues);
	}

	/**
	 * Busca recursivamente o atributo
	 * a.b.c.d por exemplo...
	 * procura para ver se tem no mapa a.b.c.d, se tiver retorna, senão
	 * procura a.b.c no mapa, se tiver, retorna obj.getD(), senão...
	 * procura a.b no mapa , se tiver, retorna obj.getC().getD(), senão ...
	 * procura a no mapa , se tiver, retorna obj.getB().getC().getD(), senão ...
	 * senão retorna Texto VAZIO.
	 */
	public static Object getValue(String key, String fieldPath,  Map<String,Object> mapValues){
		Object value = null;
		if(mapValues.get(key)!=null){
			if(!Is.empty(fieldPath)){
				value = ReflectionUtil.getField(mapValues.get(key),fieldPath);
			}else{
				value = mapValues.get(key);
			}
		}else{
			if (key.contains(".")) {
				int lastPoint = key.lastIndexOf(".");
				value = getValue(key.substring(0,lastPoint) , key.substring(lastPoint+1,key.length())+(!Is.empty(fieldPath)?"."+fieldPath:"")  , mapValues);
			}
		}
		return value;
	}

}
