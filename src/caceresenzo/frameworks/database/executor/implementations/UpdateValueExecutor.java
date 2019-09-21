package caceresenzo.frameworks.database.executor.implementations;

import caceresenzo.frameworks.database.connections.AbstractDatabaseConnection;
import caceresenzo.frameworks.database.executor.AbstractValueExecutor;

public class UpdateValueExecutor extends AbstractValueExecutor {

	public UpdateValueExecutor(AbstractDatabaseConnection databaseConnection) {
		super(databaseConnection);
	}
	
}