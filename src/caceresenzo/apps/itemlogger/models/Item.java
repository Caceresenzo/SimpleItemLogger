package caceresenzo.apps.itemlogger.models;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.automator.AbstractDatabaseColumnValueAutomator;
import caceresenzo.frameworks.database.setup.TableAnalizer;
import caceresenzo.libs.list.ListUtils;

@DatabaseTable("items")
public class Item implements IDatabaseEntry {
	
	/* Database Fields */
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_INITIAL_STOCK = "initial_stock";
	public static final String COLUMN_CURRENT_STOCK = "current_stock";
	
	/* Variables */
	@DatabaseTableColumn(DatabaseTableColumn.COLUMN_ID)
	private final int id;
	@DatabaseTableColumn(COLUMN_NAME)
	private String name;
	@DatabaseTableColumn(COLUMN_INITIAL_STOCK)
	private int initialStock;
	@DatabaseTableColumn(value = COLUMN_CURRENT_STOCK, automator = ItemCurrentStockDatabaseColumnValueAutomator.class)
	private final int currentStock;
	
	/* Constructor */
	public Item() {
		this(0, null, 0, 0);
	}
	
	/* Constructor */
	public Item(final int id, String name, int initialStock, int currentStock) {
		this.id = id;
		this.name = name;
		this.initialStock = initialStock;
		this.currentStock = currentStock;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	/** @return Item's name. */
	public String getName() {
		return name;
	}
	
	/** @return Item's initial stock. */
	public int getInitialStock() {
		return initialStock;
	}
	
	/** @return Item's current (remaining) stock. */
	public int getCurrentStock() {
		return currentStock;
	}
	
	@Override
	public String describe() {
		return String.format("%s (%s/%s)", name, currentStock, initialStock);
	}
	
	@Override
	public String describeSimply() {
		return name;
	}
	
	@Override
	public String toString() {
		return "Item[id=" + id + ", name=" + name + ", initialStock=" + initialStock + ", currentStock=" + currentStock + "]";
	}
	
	public static final class ItemCurrentStockDatabaseColumnValueAutomator extends AbstractDatabaseColumnValueAutomator {
		
		/* Variables */
		private final String lendTableName, returnEntryTableName;
		
		/* Constructor */
		public ItemCurrentStockDatabaseColumnValueAutomator() {
			super();
			
			this.lendTableName = TableAnalizer.get().analizeTable(Lend.class).getTableName();
			this.returnEntryTableName = TableAnalizer.get().analizeTable(ReturnEntry.class).getTableName();
		}
		
		@Override
		public void automate(Class<?> forModelClass, Class<?> columnClass, Field field, Object instance) {
			if (!Item.class.equals(forModelClass)) {
				return;
			}
			
			Item item = ((Item) instance);
			int inStock = item.getInitialStock();
			
			try {
				StringBuilder stringBuilder = new StringBuilder();
				
				stringBuilder
						.append("SELECT ").append(String.join(", ", DatabaseTableColumn.COLUMN_ID, Lend.COLUMN_QUANTITY))
						.append(" FROM ").append(lendTableName)
						.append(" WHERE ").append(Lend.COLUMN_ITEM).append(" = ?;");
				
				PreparedStatement statement = DataManager.get().getDatabaseConnection().prepareStatement(stringBuilder.toString());
				statement.setInt(1, item.getId());
				
				List<Integer> lendIds = new ArrayList<>();
				
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						int id = resultSet.getInt(DatabaseTableColumn.COLUMN_ID);
						int quantity = resultSet.getInt(Lend.COLUMN_QUANTITY);
						
						lendIds.add(id);
						inStock -= quantity;
					}
				}
				
				if (!lendIds.isEmpty()) {
					stringBuilder.setLength(0);
					stringBuilder
							.append("SELECT ").append(ReturnEntry.COLUMN_QUANTITY)
							.append(" FROM ").append(returnEntryTableName)
							.append(" WHERE ").append(ReturnEntry.COLUMN_LEND).append(" IN (").append(ListUtils.separate(lendIds, ", ", " ")).append(");");
					
					statement = DataManager.get().getDatabaseConnection().prepareStatement(stringBuilder.toString());
					
					try (ResultSet resultSet = statement.executeQuery()) {
						while (resultSet.next()) {
							int quantity = resultSet.getInt(ReturnEntry.COLUMN_QUANTITY);
							
							inStock += quantity;
						}
					}
				}
				
				field.set(instance, inStock);
			} catch (Exception exception) {
				LOGGER.warn("Failed to automate in stock value for the Item class model.", exception);
			}
		}
		
	}
	
}