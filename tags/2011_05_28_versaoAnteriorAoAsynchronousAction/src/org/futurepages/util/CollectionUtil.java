package org.futurepages.util;

import java.util.Collection;
import java.util.List;

import org.futurepages.exceptions.EmptyCollectionException;

public class CollectionUtil {

	//TODO FIX ME não estou funcionando :(
	@Deprecated
	public static <T>  T[] toArray(Collection<T> colecao) {
		T[] array = (T[]) colecao.toArray();
		return array;
	}

	public static <T> int addListToList(List<T> origem, List<List<T>> destino, int limite){
			
		int vagasRestantes = limite;
		if(vagasRestantes < 0){
			return vagasRestantes;
		}
		List<T> subList;
		if(origem.size() > limite){
			subList = origem.subList(0, limite);
			vagasRestantes -= subList.size();
		}else{
			subList = origem;
			vagasRestantes -=origem.size();
		}
		if(!subList.isEmpty()){
			destino.add(subList);
		}
		return vagasRestantes;
	}
	/**
	 * 
	 * @param <T> tipo do elemento
	 * @param origem lista origem dos elementos
	 * @param destino lista onde serão adiconados os elementos
	 * @param limite quantidade maxima de elementos que podem ser copiados de origem para destino
	 * @return
	 */
	public static <T> int addElementsToList(List<T> origem, List<T> destino, int limite){
		
		int vagasRestantes = limite;
		if(vagasRestantes < 0){
			return vagasRestantes;
		}
		List<T> subList;
		if(origem.size() > limite){
			subList = origem.subList(0, limite);
			vagasRestantes -= subList.size();
		}else{
			subList = origem;
			vagasRestantes -=origem.size();
		}
		if(!subList.isEmpty()){
			destino.addAll(subList);
		}
		return vagasRestantes;
	}
	
	public static <T> T getLast(List<T> list) throws EmptyCollectionException{
		if(!list.isEmpty()){
			return list.get(list.size()-1);
		}
		throw new EmptyCollectionException();
	}
	
	public static <T> T getFirst(List<T> list) throws EmptyCollectionException{
		if(!list.isEmpty()){
			return list.get(0);
		}
		throw new EmptyCollectionException();
	}
}