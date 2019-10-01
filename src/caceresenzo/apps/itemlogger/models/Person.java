package caceresenzo.apps.itemlogger.models;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;

@DatabaseTable("persons")
public class Person implements IDatabaseEntry {
	
	/* Database Fields */
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_CORPORATION = "corporation";
	public static final String COLUMN_PHONE = "phone";
	
	/* Variables */
	@DatabaseTableColumn(DatabaseTableColumn.COLUMN_ID)
	private final int id;
	@DatabaseTableColumn(COLUMN_NAME)
	private String name;
	@DatabaseTableColumn(COLUMN_CORPORATION)
	private String corporation;
	@DatabaseTableColumn(COLUMN_PHONE)
	private String phone;
	
	/* Constructor */
	public Person() {
		this(0, null, null, null);
	}
	
	/* Constructor */
	public Person(int id, String name, String corporation, String phone) {
		this.id = id;
		this.name = name;
		this.corporation = corporation;
		this.phone = phone;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	/** @return Person's name. */
	public String getName() {
		return name;
	}
	
	/** @return Person's corporation. */
	public String getCorporation() {
		return corporation;
	}
	
	/** @return Person's phone. */
	public String getPhone() {
		return phone;
	}
	
	@Override
	public String describe() {
		return String.format("%s (%s)", name, corporation);
	}
	
	@Override
	public String describeSimply() {
		return String.format("%s", name);
	}
	
	@Override
	public String toString() {
		return "Person[id=" + id + ", lastname=" + name + ", firstname=" + corporation + ", phone=" + phone + "]";
	}
	
}