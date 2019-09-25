package caceresenzo.frameworks.database.synchronization.convertor.implementations;

import java.util.List;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.synchronization.DatabaseSynchronizer;
import caceresenzo.frameworks.database.synchronization.convertor.AbstractDatabaseObjectConvertor;

public class GenericModelDatabaseObjectConvertor extends AbstractDatabaseObjectConvertor<IDatabaseEntry> {

	@Override
	@SuppressWarnings("unchecked")
	public IDatabaseEntry convert(DatabaseSynchronizer databaseSynchronizer, Class<? extends IDatabaseEntry> destinationClass, Class<?> sourceClass, Object object) {
		List<IDatabaseEntry> destinationItems = (List<IDatabaseEntry>) databaseSynchronizer.load(destinationClass);
		
		for (IDatabaseEntry entry : destinationItems) {
			if (entry.getId() == (int) object) {
				return entry;
			}
		}
		
		return null;
	}

	@Override
	public Class<?> getPrimitiveStoringClass() {
		return int.class;
	}

	@Override
	public boolean isSupportingClass(Class<?> clazz) {
		return clazz == int.class || clazz == Integer.class;
	}
	
	@Override
	public boolean isAcceptingTargetClass(Class<?> clazz) {
		return IDatabaseEntry.class.isAssignableFrom(clazz);
	}
	
}