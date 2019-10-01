package caceresenzo.frameworks.database.setup.sql;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import caceresenzo.frameworks.utils.IBuilder;
import caceresenzo.libs.string.StringUtils;

public class SqlTableBuilder implements IBuilder<String> {
	
	/* Flags */
	public static final int FLAG_PRIMARY_KEY = 1 << 1;
	public static final int FLAG_NOT_NULL = 1 << 2;
	public static final int FLAG_NULL = 1 << 3;
	public static final int FLAG_AUTO_INCREMENT = 1 << 4;
	
	/* Variables */
	private final List<String> columns;
	private String tableName;
	private boolean ifNotExists;
	
	/* Constructor */
	public SqlTableBuilder() {
		this.columns = new ArrayList<>();
	}
	
	/**
	 * Set the table's name.
	 * 
	 * @param tableName
	 *            Table's name.
	 * @return <code>this</code> for method chaining (fluent API).
	 */
	public SqlTableBuilder setTableName(String tableName) {
		this.tableName = tableName;
		
		return this;
	}
	
	/**
	 * Add the "IF NOT EXISTS" part in the table SQL.
	 * 
	 * @param ifNotExists
	 *            Weather or not the part should be added.
	 * @return <code>this</code> for method chaining (fluent API).
	 */
	public SqlTableBuilder setIfNotExists(boolean ifNotExists) {
		this.ifNotExists = ifNotExists;
		
		return this;
	}
	
	/**
	 * Add a column to the table.
	 * 
	 * @param name
	 *            Column's name.
	 * @param type
	 *            Column's type.
	 * @param flags
	 *            Flag for the column.
	 * @return <code>this</code> for method chaining (fluent API).
	 * @see #FLAG_PRIMARY_KEY Primary key flag
	 * @see #FLAG_NOT_NULL Not <code>null</code> flag
	 * @see #FLAG_NULL <code>null</code> flag
	 */
	public SqlTableBuilder withColumn(String name, String type, int flags) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(name)
				.append(" ")
				.append(type);
		
		if (flags != 0) {
			if ((flags & FLAG_PRIMARY_KEY) == FLAG_PRIMARY_KEY) {
				stringBuilder.append(" ")
						.append(getKeywordForFlag(FLAG_PRIMARY_KEY));
			}
			
			if ((flags & FLAG_NOT_NULL) == FLAG_NOT_NULL) {
				stringBuilder.append(" ")
						.append(getKeywordForFlag(FLAG_NOT_NULL));
			}
			
			if ((flags & FLAG_NULL) == FLAG_NULL) {
				stringBuilder.append(" ")
						.append(getKeywordForFlag(FLAG_NULL));
			}
			
			if ((flags & FLAG_AUTO_INCREMENT) == FLAG_AUTO_INCREMENT) {
				stringBuilder.append(" ")
						.append(getKeywordForFlag(FLAG_AUTO_INCREMENT));
			}
		}
		
		columns.add(stringBuilder.toString());
		
		return this;
	}
	
	/**
	 * Get the corresponding keyword for a <code>flag</code>.
	 * 
	 * @param flag
	 *            Target flag.
	 * @return Corresponding keyword.
	 * @throws IllegalStateException
	 *             If the flag is unknown.
	 */
	public String getKeywordForFlag(int flag) {
		switch (flag) {
			case FLAG_PRIMARY_KEY: {
				return "PRIMARY KEY";
			}
			
			case FLAG_NOT_NULL: {
				return "NOT NULL";
			}
			
			case FLAG_NULL: {
				return "NULL";
			}
			
			case FLAG_AUTO_INCREMENT: {
				return "AUTO_INCREMENT";
			}
			
			default: {
				throw new IllegalStateException("Unknown flag: " + flag);
			}
		}
	}
	
	/**
	 * Find a good correspondance between Java's types and SQL syntax.
	 * 
	 * @param type
	 *            Class that need to find an equivalent.
	 * @return Corresponding SQL type. Or <code>null</code> if not supported.
	 */
	public String findTypeFor(Class<?> type) {
		if (type.equals(String.class)) {
			return "TEXT";
		} else if (type.equals(int.class) || type.equals(Integer.class)) {
			return "INTEGER";
		} else if (type.equals(Date.class)) {
			return "INTEGER";
		}
		
		return "INTEGER";
	}
	
	@Override
	public String build() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("CREATE TABLE ");
		
		if (ifNotExists) {
			stringBuilder.append("IF NOT EXISTS ");
		}
		
		stringBuilder
				.append(tableName)
				.append(" (")
				.append(StringUtils.join(columns, ", "))
				.append(");");
		
		return stringBuilder.toString();
	}
	
}