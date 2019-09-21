package caceresenzo.frameworks.database.setup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.binder.BindableTable;

public class TableAnalizer {
	
	/* Singleton */
	private static TableAnalizer INSTANCE;
	
	/* Private Constructor */
	private TableAnalizer() {
		
	}
	
	/**
	 * Analize a class and extract table data and rows with the Java's Reflection API.
	 * 
	 * @param clazz
	 *            Target class.
	 * @return A {@link BindableTable bindable table} instance.
	 * @throws NullPointerException
	 *             If the <code>clazz</code> is <code>null</code>.
	 * @throws NullPointerException
	 *             If the class does not have a {@link DatabaseTable} annotation.
	 */
	public BindableTable analize(Class<?> clazz) {
		Objects.requireNonNull(clazz, "Cannot analize a null class.");
		DatabaseTable tableAnnotation = clazz.getAnnotation(DatabaseTable.class);
		Objects.requireNonNull(tableAnnotation, "Class does not have the DatabaseTable annotation.");
		
		return new BindableTable(tableAnnotation, clazz, analizeColumns(clazz, false));
	}
	
	public List<BindableColumn> analizeColumns(Class<?> clazz, boolean ignoreColumnId) {
		Objects.requireNonNull(clazz, "Cannot analize a null class.");
		
		List<BindableColumn> bindableColumns = new ArrayList<>();
		
		for (Field field : clazz.getDeclaredFields()) {
			DatabaseTableColumn annotation = field.getAnnotation(DatabaseTableColumn.class);
			
			if (annotation != null) {
				if (ignoreColumnId && annotation.value().equals(DatabaseTableColumn.COLUMN_ID)) {
					continue;
				}
				
				bindableColumns.add(new BindableColumn(annotation, field));
			}
		}
		
		return bindableColumns;
	}
	
	/** @return TableAnalizer's singleton instance. */
	public static final TableAnalizer get() {
		if (INSTANCE == null) {
			INSTANCE = new TableAnalizer();
		}
		
		return INSTANCE;
	}
	
}