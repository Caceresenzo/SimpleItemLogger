package caceresenzo.apps.itemlogger.models;

import java.time.LocalDate;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.setup.sql.SqlTableBuilder;
import caceresenzo.frameworks.search.ISearchable;
import caceresenzo.frameworks.search.filters.IFilter;

@DatabaseTable("history_entries")
public class HistoryEntry implements IDatabaseEntry, ISearchable<HistoryEntry> {
	
	/* Database Fields */
	public static final String COLUMN_PERSON_ID = "person_id";
	public static final String COLUMN_ITEM_ID = "item_id";
	public static final String COLUMN_LEND_DATE = "lend";
	public static final String COLUMN_RETURN_DATE = "return";
	
	/* Variables */
	@DatabaseTableColumn(DatabaseTableColumn.COLUMN_ID)
	private final int id;
	@DatabaseTableColumn(COLUMN_PERSON_ID)
	private final int personId;
	@DatabaseTableColumn(COLUMN_ITEM_ID)
	private final int itemId;
	@DatabaseTableColumn(COLUMN_LEND_DATE)
	private final LocalDate lendDate;
	@DatabaseTableColumn(value = COLUMN_RETURN_DATE, flags = SqlTableBuilder.FLAG_NULL)
	private LocalDate returnDate;
	
	/* Constructor */
	public HistoryEntry() {
		this(0, 0, 0, null, null);
	}
	
	/* Constructor */
	public HistoryEntry(int id, int personId, int itemId, LocalDate lendDate, LocalDate returnDate) {
		this.id = id;
		this.personId = personId;
		this.itemId = itemId;
		this.lendDate = lendDate;
		this.returnDate = returnDate;
	}
	
	/** @return History entry's database row id. */
	public int getId() {
		return id;
	}
	
	/** @return History entry's target person id. */
	public int getPersonId() {
		return personId;
	}
	
	/** @return History entry's target item id. */
	public int getItemId() {
		return itemId;
	}
	
	/** @return History entry's lend date. */
	public LocalDate getLendDate() {
		return lendDate;
	}
	
	/** @return History entry's return date. */
	public LocalDate getReturnDate() {
		return returnDate;
	}

	@Override
	public String toSimpleRepresentation() {
		return null;
	}
	
	@Override
	public boolean search(IFilter<HistoryEntry> filters) {
		return false;
	}
	
	@Override
	public String toString() {
		return "HistoryEntry[id=" + id + ", personId=" + personId + ", itemId=" + itemId + ", lendDate=" + lendDate + ", returnDate=" + returnDate + "]";
	}
	
}