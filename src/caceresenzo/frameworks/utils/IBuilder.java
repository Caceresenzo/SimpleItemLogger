package caceresenzo.frameworks.utils;

public interface IBuilder<T> {
	
	/** @return Build instance with previously provided information. */
	public T build();
	
}