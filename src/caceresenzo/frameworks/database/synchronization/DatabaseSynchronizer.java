package caceresenzo.frameworks.database.synchronization;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.binder.BindableTable;
import caceresenzo.frameworks.database.connections.AbstractDatabaseConnection;
import caceresenzo.frameworks.database.setup.TableAnalizer;

public class DatabaseSynchronizer {
	
	/* Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(DatabaseSynchronizer.class);
	
	/* Connection */
	private final AbstractDatabaseConnection databaseConnection;
	
	/* Constructor */
	public DatabaseSynchronizer(AbstractDatabaseConnection databaseConnection) {
		this.databaseConnection = databaseConnection;
	}
	
	private <T> BindableTable getTable(Class<T> modelClass) {
		return TableAnalizer.get().analizeTable(modelClass);
	}
	
	private <T> List<BindableColumn> getColumn(Class<T> modelClass) {
		return TableAnalizer.get().analizeColumns(modelClass);
	}
	
	public <T> List<T> load(Class<T> modelClass) {
		BindableTable bindableTable = getTable(modelClass);
		List<T> items = new ArrayList<>();
		
		try (ResultSet resultSet = databaseConnection.prepareStatement(String.format("SELECT * FROM %s", bindableTable.getTableName())).executeQuery()) {
			while (resultSet.next()) {
				@SuppressWarnings("unchecked")
				T instance = (T) modelClass.getConstructors()[0].newInstance();
				
				bindableTable.getBindableColumns().forEach((bindableColumn) -> {
					Field field = bindableColumn.getField();
					
					try {
						field.set(instance, resultSet.getObject(bindableColumn.getColumnName()));
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
				
				preparedStatement.setObject(index + 1, field.get(instance));
			}
			preparedStatement.setObject(index + 1, idBindableColumn.getField().get(instance));
			
			return preparedStatement.executeUpdate() != -1;
		} catch (Exception exception) {
			LOGGER.error("Failed to update row.", exception);
		}
		
		return false;
	}
	
	/** @return Executor's {@link AbstractDatabaseConnection database connection}. */
	public AbstractDatabaseConnection getDatabaseConnection() {
		return databaseConnection;
	}
	
}