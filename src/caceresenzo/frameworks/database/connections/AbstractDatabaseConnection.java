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
	 * Same as {@link #connect()} but it will return the {@link AbstractDatabaseConnection} instance instead.
	 * 
	 * @return <code>this</code> for method chaining (fluent API).
	 * @throws SQLException
	 *             If anything goes wrong.
	 * @see {@link #connect()}
	 */
	public AbstractDatabaseConnection connectAndGet() throws SQLException {
		connect();
		
		return this;
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
	
	/**
	 * Create an empty {@link Statement}.
	 * 
	 * @return The empty statement.
	 * @throws SQLException
	 *             If anything goes wrong.
	 * @see Connection#createStatement()
	 */
	public Statement createStatement() throws SQLException {
		return connection.createStatement();
	}
	
	/**
	 * Prepare a statement.
	 * 
	 * @param sql
	 *            Target SQL to prepare.
	 * @return A prepared statement.
	 * @throws SQLException
	 *             If anything goes wrong.
	 * @see Connection#prepareStatement(String)
	 */
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