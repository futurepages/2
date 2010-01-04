package org.futurepages.core.pagination;

import java.util.List;

public class PaginationSlice<T> {

	/** Total de registros n�o paginados*/
    private Long totalSize;
    
    /** Tamanho da p�gina*/
    private Integer pageSize;
    
    /** n�mero total de p�ginas*/
    private int totalPages;
    
    /** n�mero da p�gina*/
    private int pageNumber;
    
    /** lista referente � p�gina*/
    private List<T> list;

    public PaginationSlice(Long totalSize, Integer pageSize, int numPages, int page, List<T> list) {
        this.totalSize = totalSize;
        this.pageSize = pageSize;
        this.totalPages = numPages;
        this.list = list;
        this.pageNumber = page;
    }

    public void setTotalPages(int total) {
        this.totalPages = total;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int page) {
		this.pageNumber = page;
	}

	public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
