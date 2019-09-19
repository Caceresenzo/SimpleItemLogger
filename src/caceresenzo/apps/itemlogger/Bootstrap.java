package caceresenzo.apps.itemlogger;

import caceresenzo.apps.itemlogger.models.HistoryEntry;
import caceresenzo.apps.itemlogger.models.Item;
import caceresenzo.apps.itemlogger.models.Person;
import caceresenzo.frameworks.database.connections.SqliteConnection;
import caceresenzo.frameworks.database.setup.TableCreator;

public class Bootstrap {
	
	public static void main(String[] args) throws Exception {
		SqliteConnection sqliteConnection = new SqliteConnection("hello.db");
		sqliteConnection.connect();
		
		new TableCreator()
				.with(Person.class)
				.with(Item.class)
				.with(HistoryEntry.class)
				.autoCreate(sqliteConnection);
	}
	
}