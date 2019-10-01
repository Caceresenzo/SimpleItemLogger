package caceresenzo.frameworks.database;

public interface IDatabaseEntry {
	
	/** @return Entry's database row id. */
	public int getId();
	
	/** @return Describe the entry with relevant informations. */
	public String describe();
	
	/** @return Describe the entry with only the most critical informations. */
	public String describeSimply();
	
}