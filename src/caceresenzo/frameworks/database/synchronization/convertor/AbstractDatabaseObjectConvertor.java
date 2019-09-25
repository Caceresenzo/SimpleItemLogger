package caceresenzo.frameworks.database.synchronization.convertor;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.synchronization.DatabaseSynchronizer;

public abstract class AbstractDatabaseObjectConvertor<T> {
	
	/* Constructor */
	public AbstractDatabaseObjectConvertor() {
		;
	}
	
	/**
	 * Convert from a source to another representation.
	 * 
	 * @param destinationClass
	 *            Destination class.
	 * @param sourceClass
	 *            Source class.
	 * @param object
	 *            Source object.
	 * @return Converted object.
	 */
	public abstract T convert(DatabaseSynchronizer databaseSynchronizer, Class<? extends IDatabaseEntry> destinationClass, Class<?> sourceClass, Object object);
	
	public abstract Class<?> getPrimitiveStoringClass();
	
	public abstract boolean isSupportingClass(Class<?> clazz);
	
	public abstract boolean isAcceptingTargetClass(Class<?> clazz);
	
}