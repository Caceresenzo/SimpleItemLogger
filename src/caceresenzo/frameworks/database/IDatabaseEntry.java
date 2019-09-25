package caceresenzo.frameworks.database;

public interface IDatabaseEntry {

	/** @return Entry's database row id. */
	public int getId();
	
	public String toSimpleRepresentation();
	
}