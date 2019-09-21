package caceresenzo.frameworks.search;

import caceresenzo.frameworks.search.filters.IFilter;

public interface ISearchable<T> {
	
	public boolean search(IFilter<T> filters);
	
}