package caceresenzo.apps.itemlogger.configuration;

import caceresenzo.libs.config.Configuration;
import caceresenzo.libs.config.annotations.ConfigFile;
import caceresenzo.libs.config.annotations.ConfigProperty;
import caceresenzo.libs.config.processor.implementations.PropertiesConfigProcessor;

public class Config extends Configuration {
	
	/* Singleton */
	private static Config INSTANCE;
	
	/* Files */
	@ConfigFile(name = "config", processor = PropertiesConfigProcessor.class)
	public static String CONFIG_FILE = "config.properties";
	
	/* Entries */
	@ConfigProperty(defaultValue = "itemlogger.db", type = ConfigProperty.PropertyType.STRING, file = "config", key = "database.file")
	public static String SQLITE_PATH;
	
	@ConfigProperty(defaultValue = "true", type = ConfigProperty.PropertyType.BOOLEAN, file = "config", key = "table.data.reverse")
	public static boolean TABLE_DATE_REVERSE;
	
	/** @return Config's singleton instance. */
	public static final Config get() {
		if (INSTANCE == null) {
			INSTANCE = Configuration.initialize(Config.class);
		}
		
		return INSTANCE;
	}
	
}