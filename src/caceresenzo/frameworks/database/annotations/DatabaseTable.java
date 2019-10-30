package caceresenzo.frameworks.database.annotations;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface DatabaseTable {
	
	/** @return Table's name. */
	public String value();
	
	/** @return Weather or not the table can has one of its row remove from the database. */
	public boolean isRemovable() default false;
	
	/** @return Weather or not the table row can only exists if a row in another table is linked to it. */
	public boolean isSubData() default false;
	
}