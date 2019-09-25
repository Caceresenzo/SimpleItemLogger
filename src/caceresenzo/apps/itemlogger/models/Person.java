package caceresenzo.apps.itemlogger.models;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.search.ISearchable;
import caceresenzo.frameworks.search.filters.IFilter;

@DatabaseTable("persons")
public class Person implements IDatabaseEntry, ISearchable<Person> {
	
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
	
	/** @return Person's database row id. */
	public int getId() {
		return id;
	}
	
	/** @return Person's first name. */
	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * Change person's first name.
	 * 
	 * @param firstname
	 *            New person's first name.
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/** @return Person's last name. */
	public String getLastname() {
		return lastname;
	}
	
	/**
	 * Change person's last name.
	 * 
	 * @param lastname
	 *            New person's last name.
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	/** @return Person's phone. */
	public String getPhone() {
		return phone;
	}
	
	/**
	 * Change person's phone.
	 * 
	 * @param phone
	 *            New person's phone.
	 */
	public void setPhone(String email) {
		this.phone = email;
	}

	@Override
	public String toSimpleRepresentation() {
		return String.format("%S %s (%s)", lastname, firstname, phone);
	}
	
	@Override
	public boolean search(IFilter<Person> filters) {
		return false;
	}
	
	@Override
	public String toString() {
		return "Person[id=" + id + ", lastname=" + lastname + ", firstname=" + firstname + ", phone=" + phone + "]";
	}
	
}