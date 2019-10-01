package caceresenzo.frameworks.database.automator;

import java.lang.reflect.Field;

import caceresenzo.frameworks.automate.OperationAutomator;

public abstract class AbstractDatabaseColumnValueAutomator extends OperationAutomator {
	
	/**
	 * Automate a freshly created model instance.
	 * 
	 * @param forModelClass
	 *            What model class ask the instance.
	 * @param columnClass
	 *            Column's class.
	 * @param field
	 *            Target field (column).
	 * @param instance
	 *            Freshly created instance.
	 */
	public abstract void automate(Class<?> forModelClass, Class<?> columnClass, Field field, Object instance);
	
}