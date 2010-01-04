package org.futurepages.core.pagination;

/**
 * Quando uma classe � pagin�vel (pageable) possui constantes como n�mero da p�gina,
 * total de p�ginas, total de registros, etc.
 * 
 * @author leandro
 */
public interface Pageable {
    
     public static final String _PAGE_NUM    = "pNum";          		// N�mero da p�gina
     public static final String _TOTAL_SIZE  = "totalSize";   			// Total de registros
     public static final String _TOTAL_PAGES = "totalPages"; 			// Total de P�ginas
     public static final String _PAGE_SIZE   = "pageSize";     			// N�mero de registros da p�gina

     public static final String _HAS_NEXT_PAGE     = "hasNextPage";     // possui p�gina posterior
     public static final String _HAS_PREVIOUS_PAGE = "hasPreviousPage"; // possui p�gina anterior
     
}