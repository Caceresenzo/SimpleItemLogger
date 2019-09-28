package caceresenzo.apps.itemlogger.managers;

import caceresenzo.frameworks.managers.AbstractMainManager;

public class ItemLoggerManager extends AbstractMainManager {
	
	/* Singleton */
	private static ItemLoggerManager INSTANCE;
	
	/* Private Constructor */
	private ItemLoggerManager() {
		super();

		register(DataManager.get());
		register(SearchManager.get());
	}
	
	/** @return ItemLoggerManager's singleton instance. */
	public static final ItemLoggerManager get() {
		if (INSTANCE == null) {
			INSTANCE = new ItemLoggerManager();
		}
		
		return INSTANCE;
	}
	
}