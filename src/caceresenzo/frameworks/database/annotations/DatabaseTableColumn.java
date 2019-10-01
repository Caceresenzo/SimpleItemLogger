package caceresenzo.frameworks.database.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import caceresenzo.frameworks.database.automator.AbstractDatabaseColumnValueAutomator;
import caceresenzo.frameworks.database.setup.sql.SqlTableBuilder;
import caceresenzo.frameworks.database.synchronization.DatabaseSynchronizer;

@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface DatabaseTableColumn {
	
	/* Constants */
	public static final String COLUMN_ID = "id";
	
	/** @return Column's name. */
	public String value();
	
	/** @return Column's SQL custom flags. */
	public int flags() default SqlTableBuilder.FLAG_NOT_NULL;
	
	/** @return Weather or not the column is considered as a reference to another model. */
	public boolean isReference() default false;
	
	/** @return Weather or not the column can be edited. */
	public boolean editable() default true;
	
	/** @return An {@link AbstractDatabaseColumnValueAutomator} class to do more code when {@link DatabaseSynchronizer#load(Class) database synchronization loading} on an instance. */
	public Class<? extends AbstractDatabaseColumnValueAutomator> automator() default AbstractDatabaseColumnValueAutomator.class;
	
}