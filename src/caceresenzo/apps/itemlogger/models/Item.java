package caceresenzo.apps.itemlogger.models;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.automator.AbstractDatabaseColumnValueAutomator;
import caceresenzo.frameworks.database.binder.BindableTable;
import caceresenzo.frameworks.database.setup.TableAnalizer;

@DatabaseTable("items")
public class Item implements IDatabaseEntry {
	
	/* Database Fields */
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_STOCK = "stock";
	
	/* Variables */
	@DatabaseTableColumn(DatabaseTableColumn.COLUMN_ID)
	private final int id;
	@DatabaseTableColumn(COLUMN_NAME)
	private String name;
	@DatabaseTableColumn(COLUMN_QUANTITY)
	private int quantity;
	@DatabaseTableColumn(value = COLUMN_STOCK, automator = ItemInStockDatabaseColumnValueAutomator.class)
	private final int stock;
	
	/* Constructor */
	public Item() {
		this(0, null, 0, 0);
	}
	
	/* Constructor */
	public Item(final int id, String name, int quantity, int stock) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
		this.stock = stock;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	/** @return Item's name. */
	public String getName() {
		return name;
	}
	
	/** @return Item's total quantity. */
	public int getQuantity() {
		return quantity;
	}
	
	/** @return Item's remaining quantity. */
	public int getStock() {
		return stock;
	}
	
	@Override
	public String toSimpleRepresentation() {
		return String.format("%s (%s/%s)", name, stock, quantity);
	}
	
	@Override
	public String toString() {
		return "Item[id=" + id + ", name=" + name + ", quantity=" + quantity + ", stock=" + stock + "]";
	}
	
	public static final class ItemInStockDatabaseColumnValueAutomator extends AbstractDatabaseColumnValueAutomator<Item> {
		
		@Override
		public void automate(Class<?> forModelClass, Class<?> columnClass, Field field, Object instance) {
			if (!Item.class.equals(forModelClass)) {
				return;
			}
			
			Item item = ((Item) instance);
			
			try {
				BindableTable historyEntryBindableTable = TableAnalizer.get().analizeTable(HistoryEntry.class);
				StringBuilder stringBuilder = new StringBuilder();
				
				stringBuilder
						.append("SELECT ").append(HistoryEntry.COLUMN_QUANTITY)
						.append(" FROM ").append(historyEntryBindableTable.getTableName())
						.append(" WHERE ")
						.append(HistoryEntry.COLUMN_ITEM).append(" = ? AND ")
						.append(HistoryEntry.COLUMN_RETURN_DATE).append(" IS NULL;");
				
				PreparedStatement statement = DataManager.get().getDatabaseConnection().prepareStatement(stringBuilder.toString());
				statement.setInt(1, item.getId());
				
				int inStock = item.getQuantity();
				
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						int quantity = resultSet.getInt(HistoryEntry.COLUMN_QUANTITY);
						
						inStock -= quantity;
					}
				}
				
				field.set(instance, inStock);
			} catch (Exception exception) {
				LOGGER.warn("Failed to automate in stock value for the Item class model.", exception);
			}
		}
		
	}
	
}