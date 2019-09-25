package caceresenzo.frameworks.database.synchronization;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.binder.BindableTable;
import caceresenzo.frameworks.database.connections.AbstractDatabaseConnection;
import caceresenzo.frameworks.database.setup.TableAnalizer;
import caceresenzo.frameworks.database.synchronization.convertor.AbstractDatabaseObjectConvertor;
import caceresenzo.frameworks.database.synchronization.convertor.DatabaseObjectConvertorManager;

public class DatabaseSynchronizer {
	
	/* Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(DatabaseSynchronizer.class);
	
	/* Connection */
	private final AbstractDatabaseConnection databaseConnection;
	
	/* Constructor */
	public DatabaseSynchronizer(AbstractDatabaseConnection databaseConnection) {
		this.databaseConnection = databaseConnection;
	}
	
	/**
	 * Analize a class model.
	 * 
	 * @param <T>
	 *            Paramerized class model.
	 * @param modelClass
	 *            Model's class.
	 * @return Analized {@link BindableTable}.
	 * @see TableAnalizer#analizeTable(Class)
	 */
	private <T> BindableTable getTable(Class<T> modelClass) {
		return TableAnalizer.get().analizeTable(modelClass);
	}
	
	/**
	 * Load data from a model class.
	 * 
	 * @param <T>
	 *            Paramerized class model.
	 * @param modelClass
	 *            Model's class.
	 * @return A {@link List list} of model class instance filled with information found in the database.
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> load(Class<T> modelClass) {
		BindableTable bindableTable = getTable(modelClass);
		List<T> items = new ArrayList<>();
		
		try (ResultSet resultSet = databaseConnection.prepareStatement(String.format("SELECT * FROM %s", bindableTable.getTableName())).executeQuery()) {
			while (resultSet.next()) {
				T instance = (T) modelClass.getConstructors()[0].newInstance();
				
				bindableTable.getBindableColumns().forEach((bindableColumn) -> {
					Field field = bindableColumn.getField();
					
					try {
						Object object = resultSet.getObject(bindableColumn.getColumnName());
						
						if (object != null) {
							AbstractDatabaseObjectConvertor<?> convertor = DatabaseObjectConvertorManager.get().find(field.getType(), object.getClass());
							
							if (convertor != null) {
								object = convertor.convert(this, (Class<? extends IDatabaseEntry>) field.getType(), object.getClass(), object);
							}
						}
						
						field.set(instance, object);
					} catch (IllegalArgumentException | IllegalAccessException | SQLException exception) {
						LOGGER.error("Failed to set model field.", exception);
					}
				});
				
				items.add(instance);
			}
		} catch (Exception exception) {
			LOGGER.error("Failed to load model rows.", exception);
		}
		
		return items;
	}
	
	/**
	 * Update a model class instance data to the database.
	 * 
	 * @param <T>
	 *            Paramerized class model.
	 * @param modelClass
	 *            Model's class.
	 * @param instance
	 *            Instance to update.
	 * @return Weather or not the request has been a success.
	 */
	public <T> boolean update(Class<T> modelClass, T instance) {
		BindableTable bindableTable = getTable(modelClass);
		List<BindableColumn> bindableColumns = new ArrayList<>(bindableTable.getBindableColumns());
		
		BindableColumn idBindableColumn = BindableColumn.findIdColumn(bindableColumns);
		bindableColumns.remove(idBindableColumn);
		
		try {
			/* SQL statement generation */
			StringBuilder stringBuilder = new StringBuilder(String.format("UPDATE %s SET ", bindableTable.getTableName()));
			
			Iterator<BindableColumn> columnIterator = bindableColumns.iterator();
			while (columnIterator.hasNext()) {
				BindableColumn bindableColumn = columnIterator.next();
				
				stringBuilder.append(String.format("%s = ?", bindableColumn.getColumnName()));
				
				if (columnIterator.hasNext()) {
					stringBuilder.append(", ");
				}
			}
			
			stringBuilder.append(String.format(" WHERE %s = ?", DatabaseTableColumn.COLUMN_ID));
			
			/* SQL statement value binding */
			PreparedStatement preparedStatement = databaseConnection.prepareStatement(stringBuilder.toString());
			
			int index = 0;
			for (; index < bindableColumns.size(); index++) {
				BindableColumn bindableColumn = bindableColumns.get(index);
				Field field = bindableColumn.getField();
				
				Object value = field.get(instance);
				if (bindableColumn.isReference()) {
					value = getReferenceIdOf(value);
				}
				
				preparedStatement.setObject(index + 1, value);
			}
			preparedStatement.setObject(index + 1, idBindableColumn.getField().get(instance));
			
			return preparedStatement.executeUpdate() != -1;
		} catch (Exception exception) {
			LOGGER.error("Failed to update row.", exception);
		}
		
		return false;
	}
	
	/**
	 * Insert a new model class instance to the database.
	 * 
	 * @param modelClass
	 *            Model's class.
	 * @param instance
	 *            Instance to insert.
	 * @return Database insert row, or -1 if an error has append.
	 */
	public int insert(Class<?> modelClass, Object instance) {
		BindableTable bindableTable = getTable(modelClass);
		List<BindableColumn> bindableColumns = new ArrayList<>(bindableTable.getBindableColumns());
		
		BindableColumn idBindableColumn = BindableColumn.findIdColumn(bindableColumns);
		bindableColumns.remove(idBindableColumn);
		
		try {
			/* SQL statement generation */
			StringBuilder stringBuilder = new StringBuilder(String.format("INSERT INTO %s (", bindableTable.getTableName()));
			
			Iterator<BindableColumn> columnIterator = bindableColumns.iterator();
			while (columnIterator.hasNext()) {
				BindableColumn bindableColumn = columnIterator.next();
				
				stringBuilder.append(bindableColumn.getColumnName());
				
				if (columnIterator.hasNext()) {
					stringBuilder.append(", ");
				}
			}
			
			stringBuilder.append(") VALUES (");
			
			int size = bindableColumns.size();
			for (int index = 0; index < bindableColumns.size(); index++) {
				stringBuilder.append("?");
				
				if (index != size - 1) {
					stringBuilder.append(", ");
				}
			}
			
			stringBuilder.append(");");
			
			/* SQL statement value binding */
			PreparedStatement preparedStatement = databaseConnection.prepareStatement(stringBuilder.toString());
			
			for (int index = 0; index < bindableColumns.size(); index++) {
				BindableColumn bindableColumn = bindableColumns.get(index);
				Field field = bindableColumn.getField();
				
				Object value = field.get(instance);
				if (bindableColumn.isReference()) {
					value = getReferenceIdOf(value);
				}
				
				preparedStatement.setObject(index + 1, value);
			}
			
			return preparedStatement.executeUpdate();
		} catch (Exception exception) {
			LOGGER.error("Failed to insert row.", exception);
		}
		
		return -1;
	}
	
	private int getReferenceIdOf(Object value) throws Exception {
		if (value == null) {
			return -1;
		} else {
			return (int) BindableColumn.findIdColumn(TableAnalizer.get().analizeColumns(value.getClass())).getField().get(value);
		}
	}
	
	/** @return Executor's {@link AbstractDatabaseConnection database connection}. */
	public AbstractDatabaseConnection getDatabaseConnection() {
		return databaseConnection;
	}
	
}