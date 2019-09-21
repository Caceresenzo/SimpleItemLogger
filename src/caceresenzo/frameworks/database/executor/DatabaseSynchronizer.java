package caceresenzo.frameworks.database.executor;

import java.util.List;

import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.connections.AbstractDatabaseConnection;
import caceresenzo.frameworks.database.setup.TableAnalizer;

public class DatabaseSynchronizer<T> {
	
	/* Connection */
	private final AbstractDatabaseConnection databaseConnection;
	
	/* Model */
	private final Class<T> modelClass;
	private final List<BindableColumn> columns;
	
	/* Constructor */
	public DatabaseSynchronizer(AbstractDatabaseConnection databaseConnection, Class<T> modelClass) {
		this.databaseConnection = databaseConnection;
		this.modelClass = modelClass;
		

		this.columns = TableAnalizer.get().analizeColumns(modelClass);
	}
	
	public boolean update(T instance) {
		for (BindableColumn bindableColumn : columns) {
			try {
				System.out.println(String.format("%s : %s", bindableColumn.getColumnName(), bindableColumn.getField().get(instance)));
			} catch (IllegalArgumentException | IllegalAccessException exception) {
				exception.printStackTrace();
			}
		}
		
		return false;
	}
	
	/** @return Executor's {@link AbstractDatabaseConnection database connection}. */
	public AbstractDatabaseConnection getDatabaseConnection() {
		return databaseConnection;
	}
	
}