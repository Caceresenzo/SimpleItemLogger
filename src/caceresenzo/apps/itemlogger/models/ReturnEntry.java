package caceresenzo.apps.itemlogger.models;

import java.time.LocalDate;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.setup.sql.SqlTableBuilder;

@DatabaseTable(value = "return_entries", isSubData = true)
public class ReturnEntry implements IDatabaseEntry {
	
	/* Database Fields */
	public static final String COLUMN_LEND = "lend";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_RETURN_DATE = "return";
	public static final String COLUMN_EXTRA = "extra";
	
	/* Variables */
	@DatabaseTableColumn(DatabaseTableColumn.COLUMN_ID)
	private final int id;
	@DatabaseTableColumn(value = COLUMN_LEND, isReference = true, isVisible = false, isEditable = false)
	private Lend lend;
	@DatabaseTableColumn(value = COLUMN_QUANTITY, isEditable = false)
	private int quantity;
	@DatabaseTableColumn(value = COLUMN_RETURN_DATE, isEditable = false)
	private LocalDate returnDate;
	@DatabaseTableColumn(value = COLUMN_EXTRA, flags = SqlTableBuilder.FLAG_NULL)
	private String extra;
	
	/* Constructor */
	public ReturnEntry() {
		this(0, null, 0, null, null);
	}
	
	/* Constructor */
	public ReturnEntry(int id, Lend lend, int quantity, LocalDate returnDate, String extra) {
		this.id = id;
		this.lend = lend;
		this.quantity = quantity;
		this.returnDate = returnDate;
		this.extra = extra;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	/** @return {@link ReturnEntry}'s parent {@link Lend}. */
	public Lend getLend() {
		return lend;
	}
	
	/** @return {@link ReturnEntry}'s returned quantity. */
	public int getQuantity() {
		return quantity;
	}
	
	/** @return {@link ReturnEntry}'s return date. */
	public LocalDate getReturnDate() {
		return returnDate;
	}
	
	/** @return {@link ReturnEntry}'s extra user data. */
	public String getExtra() {
		return extra;
	}
	
	@Override
	public String describe() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String describeSimply() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return "ReturnEntry[id=" + id + ", lend=" + lend + ", returnDate=" + returnDate + ", extra=" + extra + "]";
	}
	
}