package caceresenzo.frameworks.database.automator;

import java.lang.reflect.Field;

import caceresenzo.frameworks.automate.OperationAutomator;

public abstract class AbstractDatabaseColumnValueAutomator<T> extends OperationAutomator {
	
	public abstract void automate(Class<?> forModelClass, Class<?> columnClass, Field field, Object instance);
	
}