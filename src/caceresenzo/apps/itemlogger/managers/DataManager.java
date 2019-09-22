package caceresenzo.apps.itemlogger.managers;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caceresenzo.apps.itemlogger.configuration.Config;
import caceresenzo.apps.itemlogger.models.HistoryEntry;
import caceresenzo.apps.itemlogger.models.Item;
import caceresenzo.apps.itemlogger.models.Person;
import caceresenzo.frameworks.database.connections.AbstractDatabaseConnection;
import caceresenzo.frameworks.database.connections.implementations.SqliteConnection;
import caceresenzo.frameworks.database.setup.TableCreator;
import caceresenzo.frameworks.managers.AbstractManager;

public class DataManager extends AbstractManager {
	
	/* Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(DataManager.class);
	
	/* Singleton */
	private static DataManager INSTANCE;
	
	/* Database */
	private final AbstractDatabaseConnection databaseConnection;
	
	/* Private Constructor */
	private DataManager() {
		super();

		databaseConnection = new SqliteConnection(Config.SQLITE_PATH);
	}
	
	@Override
	public void initialize() {
		super.initialize();
		
		try {
			LOGGER.info("Connecting to SQLite database: " + Config.SQLITE_PATH);
			databaseConnection.connect();
		} catch (SQLException exception) {
			LOGGER.error("Failed to connect to the database, application can't continue.", exception);
			System.exit(0);
			return;
		}
		
		try {
			new TableCreator()
					.with(Person.class)
					.with(Item.class)
					.with(HistoryEntry.class)
					.autoCreate(databaseConnection);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			exception.printStackTrace();
		}
	}
	
	/** @return DataManager's singleton instance. */
	public static final DataManager get() {
		if (INSTANCE == null) {
			INSTANCE = new DataManager();
		}
		
		return INSTANCE;
	}
	
}