package caceresenzo.frameworks.database.synchronization.convertor;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.synchronization.DatabaseSynchronizer;

public abstract class AbstractDatabaseObjectConvertor<T> {
	
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
	
	/** @return For this convertor, what is the best way to store the data in the first place. */
	public abstract Class<?> getPrimitiveStoringClass();
	
	/**
	 * Check if this convertor support a conversion from a class.
	 * 
	 * @param clazz
	 *            Class to check.
	 * @return Weather or not this convertor support conversion from this class.
	 */
	public abstract boolean isSupportingClass(Class<?> clazz);
	
	/**
	 * Check if the convertor accept to convert to the destination {@link Class}.
	 * 
	 * @param clazz
	 *            {@link Class} to check.
	 * @return Weather or not this convertor support convertion to a class.
	 */
	public abstract boolean isAcceptingTargetClass(Class<?> clazz);
	
}