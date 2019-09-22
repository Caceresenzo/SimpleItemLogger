package caceresenzo.frameworks.database.connections;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import caceresenzo.frameworks.database.setup.sql.SqlTableBuilder;

public abstract class AbstractDatabaseConnection {
	
	/* Variables */
	private Connection connection;
	
	/* Constructor */
	public AbstractDatabaseConnection() {
		;
	}
	
	/** @return Get the JDBC url. */
	public abstract String getJdbcUrl();
	
	/** @return The class for easely create an SQL table. */
	public Class<? extends SqlTableBuilder> getSqlTableBuilderClass() {
		return SqlTableBuilder.class;
	}
	
	/**
	 * Open a connection to the database.
	 * 
	 * @return Database's open connection.
	 * @throws SQLException
	 *             If anything goes wrong.
	 * @see DriverManager#getConnection(String)
	 */
	public Connection connect() throws SQLException {
		return connection = DriverManager.getConnection(getJdbcUrl());
	}
	
	/**
	 * Close the currently open connection to the database.
	 * 
	 * @return If the connection has been close, otherwise the connection was <code>null</code> and could not be close.
	 * @throws SQLException
	 *             If anything goes wrong.
	 * @see Connection#close()
	 */
	public boolean close() throws SQLException {
		if (connection != null) {
			connection.close();
			return true;
		}
		
		return false;
	}
	
	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}
	
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return connection.prepareStatement(sql);
	}
	
	/** @return Weather or not the database {@link Connection connection} is not <code>null</code>. */
	public boolean isConnected() {
		return connection != null;
	}
	
	/** @return Database's direct connection. */
	public Connection getConnection() {
		return connection;
	}
	
}