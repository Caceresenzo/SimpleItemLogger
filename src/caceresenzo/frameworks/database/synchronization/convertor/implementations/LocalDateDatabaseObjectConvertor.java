package caceresenzo.frameworks.database.synchronization.convertor.implementations;

import java.time.LocalDate;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.synchronization.DatabaseSynchronizer;
import caceresenzo.frameworks.database.synchronization.convertor.AbstractDatabaseObjectConvertor;

public class LocalDateDatabaseObjectConvertor extends AbstractDatabaseObjectConvertor<LocalDate> {

	@Override
	public LocalDate convert(DatabaseSynchronizer databaseSynchronizer, Class<? extends IDatabaseEntry> destinationClass, Class<?> clazz, Object object) {
		return LocalDate.parse(String.valueOf(object));
	}

	@Override
	public Class<?> getPrimitiveStoringClass() {
		return String.class;
	}

	@Override
	public boolean isSupportingClass(Class<?> clazz) {
		return clazz.equals(String.class) || clazz.equals(int.class) || clazz.equals(Integer.class);
	}

	@Override
	public boolean isAcceptingTargetClass(Class<?> clazz) {
		return clazz == LocalDate.class;
	}
	
}