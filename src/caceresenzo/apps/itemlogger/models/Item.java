package caceresenzo.apps.itemlogger.models;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.search.ISearchable;
import caceresenzo.frameworks.search.filters.IFilter;

@DatabaseTable("items")
public class Item implements IDatabaseEntry, ISearchable<Item> {

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
	
	/** @return Item's database row id. */
	public int getId() {
		return id;
	}
	
	/** @return Item's name. */
	public String getName() {
		return name;
	}
	
	/**
	 * Change item's name.
	 * 
	 * @param name
	 *            New item's name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/** @return Item's total quantity. */
	public int getQuantity() {
		return quantity;
	}
	
	/**
	 * Change item's total quantity.
	 * 
	 * @param quantity
	 *            New item's quantity.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public boolean search(IFilter<Item> filters) {
		return false;
	}
	
}