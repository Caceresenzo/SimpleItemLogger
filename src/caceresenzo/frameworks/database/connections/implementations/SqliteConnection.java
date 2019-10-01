package caceresenzo.frameworks.database.connections.implementations;

import caceresenzo.frameworks.database.connections.AbstractDatabaseConnection;
import caceresenzo.frameworks.database.setup.sql.SqlTableBuilder;
import caceresenzo.frameworks.database.setup.sql.SqliteTableBuilder;

public class SqliteConnection extends AbstractDatabaseConnection {
	
	/* Constants */
	public static final String JDBC_URL_BASE = "jdbc:sqlite:";
	
	/* Variables */
	private final String path;
	
	/* Constructor */
	public SqliteConnection(String path) {
		super();
		
		this.path = path;
	}
	
	@Override
	public String getJdbcUrl() {
		return JDBC_URL_BASE + path;
	}

	@Override
	public Class<? extends SqlTableBuilder> getSqlTableBuilderClass() {
		return SqliteTableBuilder.class;
	}
	
}