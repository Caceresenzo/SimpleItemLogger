package caceresenzo.frameworks.database.setup;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.binder.BindableTable;
import caceresenzo.frameworks.database.connections.AbstractDatabaseConnection;
import caceresenzo.frameworks.database.setup.sql.SqlTableBuilder;

public class TableCreator {
	
	/* Variables */
	private final Map<Class<?>, BindableTable> bindables;
	
	/* Constructor */
	public TableCreator() {
		this.bindables = new HashMap<>();
	}
	
	/**
	 * Add a table class.
	 * 
	 * @param clazz
	 *            Class with the {@link DatabaseTable @DatabaseTable} annotation.
	 * @return <code>this</code> for method chaining (fluent API).
	 */
	public TableCreator with(Class<?> clazz) {
		Objects.requireNonNull(clazz, "Cannot add a null class.");
		
		this.bindables.put(clazz, TableAnalizer.get().analize(clazz));
		
		return this;
	}
	
	/**
	 * Do the table creation.
	 * 
	 * @param databaseConnection
	 *            Database connection.
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	public void autoCreate(AbstractDatabaseConnection databaseConnection) throws Exception {
		if (!databaseConnection.isConnected()) {
			throw new IllegalStateException("Database not connected.");
		}
		
		List<String> createSql = new ArrayList<>();
		
		for (BindableTable bindableTable : bindables.values()) {
			SqlTableBuilder tableBuilder = databaseConnection.getSqlTableBuilderClass().newInstance();
			
			tableBuilder.setTableName(bindableTable.getTableName())
					.setIfNotExists(true);
			
			for (BindableColumn bindableField : bindableTable.getBindableFields()) {
				String name = bindableField.getColumnName();
				String type = tableBuilder.findTypeFor(bindableField.getField().getType());
				int flags = 0;
				
				if (name.equals(DatabaseTableColumn.COLUMN_ID)) {
					flags |= SqlTableBuilder.FLAG_PRIMARY_KEY;
					flags |= SqlTableBuilder.FLAG_AUTO_INCREMENT;
				} else {
					flags |= bindableField.getAnnotation().flags();
				}
				
				tableBuilder.withColumn(name, type, flags);
			}
			
			createSql.add(tableBuilder.build());
		}

		Statement statement = databaseConnection.getConnection().createStatement();
		
		for (String sql : createSql) {
			statement.addBatch(sql);
		}
		
		statement.executeBatch();
	}
	
}