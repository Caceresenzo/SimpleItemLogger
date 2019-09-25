package caceresenzo.apps.itemlogger.models;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;

@DatabaseTable("items")
public class Item implements IDatabaseEntry {
	
	/* Database Fields */
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_QUANTITY = "quantity";
	
	/* Variables */
	@DatabaseTableColumn(DatabaseTableColumn.COLUMN_ID)
	private final int id;
	@DatabaseTableColumn(COLUMN_NAME)
	private String name;
	@DatabaseTableColumn(COLUMN_QUANTITY)
	private int quantity;
	
	/* Constructor */
	public Item() {
		this(0, null, 0);
	}
	
	/* Constructor */
	public Item(final int id, String name, int quantity) {
		this.id = id;
		this.name = name;
		this.quantity = quantity;
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
	
	@Override
	public String toSimpleRepresentation() {
		return String.format("%s (%s)", name, quantity);
	}
	
	@Override
	public String toString() {
		return "Item[id=" + id + ", name=" + name + ", quantity=" + quantity + "]";
	}
	
}