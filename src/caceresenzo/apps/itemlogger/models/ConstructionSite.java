package caceresenzo.apps.itemlogger.models;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.search.ISearchable;
import caceresenzo.frameworks.search.filters.IFilter;

@DatabaseTable("construction_sites")
public class ConstructionSite implements IDatabaseEntry, ISearchable<ConstructionSite> {
	
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
	
	/** @return Construction site's database row id. */
	public int getId() {
		return id;
	}
	
	/** @return Construction site's name. */
	public String getName() {
		return name;
	}
	
	/**
	 * Change construction site's name.
	 * 
	 * @param name
	 *            New construction site's name.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/** @return Construction site's address. */
	public String getAddress() {
		return address;
	}
	
	/**
	 * Change construction site's address.
	 * 
	 * @param address
	 *            New construction site's address.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toSimpleRepresentation() {
		return String.format("%s (%s)", name, address);
	}
	
	@Override
	public boolean search(IFilter<ConstructionSite> filters) {
		return false;
	}
	
}