package caceresenzo.apps.itemlogger.models;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;

@DatabaseTable("construction_sites")
public class ConstructionSite implements IDatabaseEntry {
	
	/* Database Fields */
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_ADDRESS = "address";
	
	/* Variables */
	@DatabaseTableColumn(DatabaseTableColumn.COLUMN_ID)
	private final int id;
	@DatabaseTableColumn(COLUMN_NAME)
	private String name;
	@DatabaseTableColumn(COLUMN_ADDRESS)
	private String address;
	
	/* Constructor */
	public ConstructionSite() {
		this(0, null, null);
	}
	
	/* Constructor */
	public ConstructionSite(final int id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	/** @return Construction site's name. */
	public String getName() {
		return name;
	}
	
	/** @return Construction site's address. */
	public String getAddress() {
		return address;
	}
	
	@Override
	public String describe() {
		return String.format("%s (%s)", name, address);
	}

	@Override
	public String describeSimply() {
		return name;
	}
	
	@Override
	public String toString() {
		return "ConstructionSite[id=" + id + ", name=" + name + ", address=" + address + "]";
	}
	
}