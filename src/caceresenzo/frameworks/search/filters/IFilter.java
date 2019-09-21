package caceresenzo.frameworks.search.filters;

public interface IFilter<T> {
	
	public boolean accept(T object);
	
}