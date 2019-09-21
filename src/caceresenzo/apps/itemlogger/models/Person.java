package caceresenzo.apps.itemlogger.models;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.search.ISearchable;
import caceresenzo.frameworks.search.filters.IFilter;

@DatabaseTable("persons")
public class Person implements IDatabaseEntry, ISearchable<Person> {

	/* Database Fields */
	public static final String COLUMN_FIRSTNAME = "firstname";
	public static final String COLUMN_LASTNAME = "lastname";
	public static final String COLUMN_EMAIL = "email";
	
	/* Variables */
	@DatabaseTableColumn(DatabaseTableColumn.COLUMN_ID)
	private final int id;
	@DatabaseTableColumn(COLUMN_FIRSTNAME)
	private String firstname;
	@DatabaseTableColumn(COLUMN_LASTNAME)
	private String lastname;
	@DatabaseTableColumn(COLUMN_EMAIL)
	private String email;
	
	/* Constructor */
	public Person(int id, String firstname, String lastname, String email) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
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
	
	/** @return Person's email. */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Change person's email.
	 * 
	 * @param email
	 *            New person's email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean search(IFilter<Person> filters) {
		return false;
	}
	
}