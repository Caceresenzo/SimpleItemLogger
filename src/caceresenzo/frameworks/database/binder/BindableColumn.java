package caceresenzo.frameworks.database.binder;

import java.lang.reflect.Field;

import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;

public class BindableColumn {
	
	/* Variables */
	private final DatabaseTableColumn annotation;
	private final Field field;
	
	/* Constructor */
	public BindableColumn(DatabaseTableColumn annotation, Field field) {
		this.annotation = annotation;
		this.field = field;
	}
	
	/** @return Column's name. */
	public String getColumnName() {
		return annotation.value();
	}
	
	/** @return Field's {@link DatabaseTableColumn} annotation. */
	public DatabaseTableColumn getAnnotation() {
		return annotation;
	}
	
	/** @return Field's instance. */
	public Field getField() {
		return field;
	}
	
}