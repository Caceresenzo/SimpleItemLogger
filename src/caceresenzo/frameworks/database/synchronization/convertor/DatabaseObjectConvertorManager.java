package caceresenzo.frameworks.database.synchronization.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caceresenzo.frameworks.database.synchronization.convertor.implementations.GenericModelDatabaseObjectConvertor;
import caceresenzo.frameworks.database.synchronization.convertor.implementations.LocalDateDatabaseObjectConvertor;
import caceresenzo.frameworks.managers.AbstractManager;

public class DatabaseObjectConvertorManager extends AbstractManager {
	
	/* Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(DatabaseObjectConvertorManager.class);
	
	/* Singleton */
	private static DatabaseObjectConvertorManager INSTANCE;
	
	/* Convertors */
	private final List<AbstractDatabaseObjectConvertor<?>> convertors;
	
	/* Private Constructor */
	private DatabaseObjectConvertorManager() {
		super();
		
		this.convertors = new ArrayList<>();
		
		loadDefaults();
	}
	
	/** Load the already implemented {@link AbstractDatabaseObjectConvertor convertor}s. */
	private void loadDefaults() {
		register(new GenericModelDatabaseObjectConvertor());
		register(new LocalDateDatabaseObjectConvertor());
	}
	
	/**
	 * Register a new {@link AbstractDatabaseObjectConvertor convertor} and add it at the top of the {@link List list}.
	 * 
	 * @param convertor
	 *            New {@link AbstractDatabaseObjectConvertor convertor} instance.
	 * @return <code>this</code> for method chaining (fluent API).
	 */
	public DatabaseObjectConvertorManager register(AbstractDatabaseObjectConvertor<?> convertor) {
		Objects.requireNonNull(convertor, "Cannot register a null convertor.");
		
		convertors.add(0, convertor);
		
		LOGGER.info("Registering database object convertor \"{}\".", convertor.getClass().getSimpleName());
		
		return this;
	}
	
	/**
	 * Find the best match to convert an instance from a class to another.
	 * 
	 * @param <T>
	 *            Paramerized target type.
	 * @param targetClazz
	 *            Target class.
	 * @param objectClazz
	 *            Source object class.
	 * @return An {@link AbstractDatabaseObjectConvertor} instance able to convert an instance from an <code>objectClazz</code> to a <code>targetClazz</code>.<br>
	 *         Or <code>null</code> if there is no implementation registered to do this conversion.
	 */
	@SuppressWarnings("unchecked")
	public <T> AbstractDatabaseObjectConvertor<T> find(Class<T> targetClazz, Class<?> objectClazz) {
		for (AbstractDatabaseObjectConvertor<?> convertor : convertors) {
			if (convertor.isAcceptingTargetClass(targetClazz) && convertor.isSupportingClass(objectClazz)) {
				return (AbstractDatabaseObjectConvertor<T>) convertor;
			}
		}
		
		return null;
	}
	
	/** @return DatabaseObjectConvertorManager's singleton instance. */
	public static final DatabaseObjectConvertorManager get() {
		if (INSTANCE == null) {
			INSTANCE = new DatabaseObjectConvertorManager();
		}
		
		return INSTANCE;
	}
	
}