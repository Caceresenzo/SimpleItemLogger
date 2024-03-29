package caceresenzo.frameworks.database.binder;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.automator.AbstractDatabaseColumnValueAutomator;
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
	
	/** @return Weather or not this column is a reference to another table entry. */
	public boolean isReference() {
		return getAnnotation().isReference();
	}
	
	public boolean isAutomatable() {
		return !getAnnotation().automator().equals(AbstractDatabaseColumnValueAutomator.class);
	}
	
	public boolean isEditable() {
		return getAnnotation().isEditable();
	}

	public boolean isVisible() {
		return getAnnotation().isVisible();
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
		return findColumn(bindableColumns, DatabaseTableColumn.COLUMN_ID);
	}
	
	public static BindableColumn findColumn(List<BindableColumn> bindableColumns, String columnName) {
		for (BindableColumn bindableColumn : bindableColumns) {
			if (bindableColumn.getColumnName().equals(columnName)) {
				return bindableColumn;
			}
		}
		
		return null;
	}
	
	public static void removeAutomatable(List<BindableColumn> bindableColumns) {
		Iterator<BindableColumn> iterator = bindableColumns.listIterator();
		
		while (iterator.hasNext()) {
			BindableColumn bindableColumn = iterator.next();
			
			if (bindableColumn.isAutomatable()) {
				iterator.remove();
			}
		}
	}
	
}