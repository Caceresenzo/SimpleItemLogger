package caceresenzo.frameworks.database.executor;

import caceresenzo.frameworks.database.connections.AbstractDatabaseConnection;

public abstract class AbstractValueExecutor {
	
	/* Connection */
	private final AbstractDatabaseConnection databaseConnection;
	
	/* Constructor */
	public AbstractValueExecutor(AbstractDatabaseConnection databaseConnection) {
		this.databaseConnection = databaseConnection;
	}
	
	/** @return Executor's {@link AbstractDatabaseConnection database connection}. */
	public AbstractDatabaseConnection getDatabaseConnection() {
		return databaseConnection;
	}
	
}