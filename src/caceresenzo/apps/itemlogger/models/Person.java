package caceresenzo.apps.itemlogger.models;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;

@DatabaseTable("persons")
public class Person implements IDatabaseEntry {
	
	/* Database Fields */
	public static final String COLUMN_LASTNAME = "lastname";
	public static final String COLUMN_FIRSTNAME = "firstname";
	public static final String COLUMN_PHONE = "phone";
	
	/* Variables */
	@DatabaseTableColumn(DatabaseTableColumn.COLUMN_ID)
	private final int id;
	@DatabaseTableColumn(COLUMN_LASTNAME)
	private String lastname;
	@DatabaseTableColumn(COLUMN_FIRSTNAME)
	private String firstname;
	@DatabaseTableColumn(COLUMN_PHONE)
	private String phone;
	
	/* Constructor */
	public Person() {
		this(0, null, null, null);
	}
	
	/* Constructor */
	public Person(int id, String lastname, String firstname, String phone) {
		this.id = id;
		this.lastname = lastname;
		this.firstname = firstname;
		this.phone = phone;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	/** @return Person's first name. */
	public String getFirstname() {
		return firstname;
	}
	
	/** @return Person's last name. */
	public String getLastname() {
		return lastname;
	}
	
	/** @return Person's phone. */
	public String getPhone() {
		return phone;
	}
	
	@Override
	public String toSimpleRepresentation() {
		return String.format("%S %s (%s)", lastname, firstname, phone);
	}
	
	@Override
	public String toString() {
		return "Person[id=" + id + ", lastname=" + lastname + ", firstname=" + firstname + ", phone=" + phone + "]";
	}
	
}