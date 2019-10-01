package caceresenzo.apps.itemlogger.managers;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caceresenzo.apps.itemlogger.configuration.Config;
import caceresenzo.apps.itemlogger.models.ConstructionSite;
import caceresenzo.apps.itemlogger.models.HistoryEntry;
import caceresenzo.apps.itemlogger.models.Item;
import caceresenzo.apps.itemlogger.models.Person;
import caceresenzo.frameworks.database.connections.AbstractDatabaseConnection;
import caceresenzo.frameworks.database.connections.implementations.SqliteConnection;
import caceresenzo.frameworks.database.setup.TableCreator;
import caceresenzo.frameworks.database.synchronization.DatabaseSynchronizer;
import caceresenzo.frameworks.managers.AbstractManager;

public class DataManager extends AbstractManager {
	
	/* Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(DataManager.class);
	
	/* Singleton */
	private static DataManager INSTANCE;
	
	/* Database */
	private final AbstractDatabaseConnection databaseConnection;
	private final DatabaseSynchronizer databaseSynchronizer;
	private final TableCreator tableCreator;
	
	/* Private Constructor */
	private DataManager() {
		super();
		
		this.databaseConnection = new SqliteConnection(Config.SQLITE_PATH);
		this.databaseSynchronizer = new DatabaseSynchronizer(databaseConnection);
		this.tableCreator = new TableCreator()
				.with(Person.class)
				.with(Item.class)
				.with(ConstructionSite.class)
				.with(HistoryEntry.class);
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
			tableCreator.autoCreate(databaseConnection);
		} catch (Exception exception) {
			LOGGER.error("Failed to auto-create tables, application can't continue.", exception);
			System.exit(0);
			return;
		}
	}
	
	/** @return Main {@link AbstractDatabaseConnection database connection}. */
	public AbstractDatabaseConnection getDatabaseConnection() {
		return databaseConnection;
	}
	
	/** @return Main {@link DatabaseSynchronizer database data synchronizer}. */
	public DatabaseSynchronizer getDatabaseSynchronizer() {
		return databaseSynchronizer;
	}
	
	/** @return {@link TableCreator} used to initialize the database. */
	public TableCreator getTableCreator() {
		return tableCreator;
	}
	
	/** @return DataManager's singleton instance. */
	public static final DataManager get() {
		if (INSTANCE == null) {
			INSTANCE = new DataManager();
		}
		
		return INSTANCE;
	}
	
}