package caceresenzo.frameworks.database.binder;

import java.util.Collections;
import java.util.List;

import caceresenzo.frameworks.database.annotations.DatabaseTable;

public class BindableTable {
	
	/* Variables */
	private final DatabaseTable annotation;
	private final Class<?> clazz;
	private final List<BindableColumn> bindableFields;
	
	/* Constructor */
	public BindableTable(DatabaseTable annotation, Class<?> clazz, List<BindableColumn> bindableFields) {
		this.annotation = annotation;
		this.clazz = clazz;
		this.bindableFields = Collections.unmodifiableList(bindableFields);
	}
	
	/** @return Table's name. */
	public String getTableName() {
		return annotation.value();
	}
	
	/** @return Table's annotation. */
	public DatabaseTable getAnnotation() {
		return annotation;
	}
	
	/** @return Table's original class. */
	public Class<?> getClazz() {
		return clazz;
	}
	
	/** @return A {@link List list} of analyzed {@link BindableColumn bindable fields}. */
	public List<BindableColumn> getBindableColumns() {
		return bindableFields;
	}
	
}