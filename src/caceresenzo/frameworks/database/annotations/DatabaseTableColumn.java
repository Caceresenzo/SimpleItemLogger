package caceresenzo.frameworks.database.annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import caceresenzo.frameworks.database.setup.sql.SqlTableBuilder;

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
	
}