package caceresenzo.frameworks.database.binder;

import java.lang.reflect.Field;
import java.util.List;

import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.libs.reflection.ReflectionUtils;

public class BindableColumn {
	
	/* Variables */
	private final DatabaseTableColumn annotation;
	private final Field field;
	
	/* Constructor */
	public BindableColumn(DatabaseTableColumn annotation, Field field) {
		this.annotation = annotation;
		this.field = field;
		
		ReflectionUtils.silentlyRemoveFinalProtection(field);
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
	
	/**
	 * Find the column with the name {@link DatabaseTableColumn#COLUMN_ID "ID"}.
	 * 
	 * @param bindableColumns
	 *            {@link List} of {@link BindableColumn}.
	 * @return Found {@link BindableColumn} or <code>null</code> if not found.
	 */
	public static BindableColumn findIdColumn(List<BindableColumn> bindableColumns) {
		for (BindableColumn bindableColumn : bindableColumns) {
			if (bindableColumn.getColumnName().equals(DatabaseTableColumn.COLUMN_ID)) {
				return bindableColumn;
			}
		}
		
		return null;
	}
	
}