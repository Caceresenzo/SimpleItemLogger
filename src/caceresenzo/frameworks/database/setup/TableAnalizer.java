package caceresenzo.frameworks.database.setup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.binder.BindableTable;

public class TableAnalizer {
	
	/* Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(TableAnalizer.class);
	
	/* Singleton */
	private static TableAnalizer INSTANCE;
	
	/* Cache */
	private final Map<Class<?>, List<BindableColumn>> bindableColumnCache;
	
	/* Private Constructor */
	private TableAnalizer() {
		this.bindableColumnCache = new HashMap<>();
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
	public BindableTable analizeTable(Class<?> clazz) {
		Objects.requireNonNull(clazz, "Cannot analize a null class.");
		DatabaseTable tableAnnotation = clazz.getAnnotation(DatabaseTable.class);
		Objects.requireNonNull(tableAnnotation, "Class does not have the DatabaseTable annotation.");
		
		return new BindableTable(tableAnnotation, clazz, analizeColumns(clazz));
	}
	
	/**
	 * Extract field with a {@link DatabaseTableColumn} annotation.
	 * 
	 * @param clazz
	 *            Target class.
	 * @return A {@link List} of {@link BindableColumn} found in the target class.
	 */
	public List<BindableColumn> analizeColumns(Class<?> clazz) {
		Objects.requireNonNull(clazz, "Cannot analize a null class.");
		
		return new ArrayList<>(bindableColumnCache.computeIfAbsent(clazz, (key) -> {
			List<BindableColumn> bindableColumns = new ArrayList<>();
			
			for (Field field : clazz.getDeclaredFields()) {
				DatabaseTableColumn annotation = field.getAnnotation(DatabaseTableColumn.class);
				
				if (annotation != null) {
					LOGGER.info("Found valid field \"{}\" in class \"{}\".", field.getName(), clazz.getSimpleName());
					
					bindableColumns.add(new BindableColumn(annotation, field));
				}
			}
			
			return bindableColumns;
		}));
	}
	
	/** @return TableAnalizer's singleton instance. */
	public static final TableAnalizer get() {
		if (INSTANCE == null) {
			INSTANCE = new TableAnalizer();
		}
		
		return INSTANCE;
	}
	
}