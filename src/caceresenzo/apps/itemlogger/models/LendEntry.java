package caceresenzo.apps.itemlogger.models;

import java.time.LocalDate;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.setup.sql.SqlTableBuilder;

@DatabaseTable("history_entries")
public class LendEntry implements IDatabaseEntry {
	
	/* Database Fields */
	public static final String COLUMN_PERSON = "person";
	public static final String COLUMN_ITEM = "item";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_CONSTRUCTION_SITE = "construction_site";
	public static final String COLUMN_LEND_DATE = "lend";
	public static final String COLUMN_RETURN_DATE = "return";
	public static final String COLUMN_EXTRA = "extra";
	
	/* Variables */
	@DatabaseTableColumn(DatabaseTableColumn.COLUMN_ID)
	private final int id;
	@DatabaseTableColumn(value = COLUMN_ITEM, isReference = true)
	private Item item;
	@DatabaseTableColumn(value = COLUMN_PERSON, isReference = true)
	private Person person;
	@DatabaseTableColumn(COLUMN_QUANTITY)
	private int quantity;
	@DatabaseTableColumn(value = COLUMN_CONSTRUCTION_SITE, isReference = true)
	private ConstructionSite constructionSite;
	@DatabaseTableColumn(value = COLUMN_LEND_DATE, isVisible = false)
	private LocalDate lendDate;
	@DatabaseTableColumn(value = COLUMN_RETURN_DATE, flags = SqlTableBuilder.FLAG_NULL, isVisible = false)
	private LocalDate returnDate;
	@DatabaseTableColumn(value = COLUMN_EXTRA, flags = SqlTableBuilder.FLAG_NULL, isVisible = false)
	private String extra;
	
	/* Constructor */
	public LendEntry() {
		this(0, null, null, 0, null, null, null, null);
	}
	
	/* Constructor */
	public LendEntry(int id, Item item, Person person, int quantity, ConstructionSite constructionSite, LocalDate lendDate, LocalDate returnDate, String extra) {
		this.id = id;
		this.item = item;
		this.person = person;
		this.quantity = quantity;
		this.constructionSite = constructionSite;
		this.lendDate = lendDate;
		this.returnDate = returnDate;
		this.extra = extra;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	/** @return History entry's target item. */
	public Item getItem() {
		return item;
	}
	
	/** @return History entry's target person. */
	public Person getPerson() {
		return person;
	}
	
	/** @return How much of {@link #getItem() item} has been borrow in this history entry. */
	public int getQuantity() {
		return quantity;
	}
	
	/** @return History entry's target construction site. */
	public ConstructionSite getConstructionSite() {
		return constructionSite;
	}
	
	/** @return History entry's lend date. */
	public LocalDate getLendDate() {
		return lendDate;
	}
	
	/** @return History entry's return date. */
	public LocalDate getReturnDate() {
		return returnDate;
	}
	
	/** @return History entry's extra user data. */
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
	
	/**
	 * "Split" ths history entry to two instance if necessary.<br>
	 * It means that if the quantity to remove is not equals to the {@link LendEntry}'s quantity, then another {@link LendEntry} will be created with the difference quantity between the current quantity and the quantity to remove (also call the remaining).<br>
	 * Otherwise, a <code>null</code> object will be returned.
	 * 
	 * @param quantityToRemove
	 *            Quantity to remove to this {@link LendEntry}.
	 * @return Splited {@link LendEntry} or <code>null</code> if there is no need to split.
	 */
	public LendEntry split(int quantityToRemove) {
		quantityToRemove = Math.min(Math.max(quantityToRemove, 0), quantity);
		
		if (quantity != quantityToRemove) {
			int remaining = quantity - quantityToRemove;
			quantity = quantityToRemove;
			
			return new LendEntry(id, item, person, remaining, constructionSite, lendDate, returnDate, extra);
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return "HistoryEntry[id=" + id + ", item=" + item + ", person=" + person + ", quantity=" + quantity + ", constructionSite=" + constructionSite + ", lendDate=" + lendDate + ", returnDate=" + returnDate + ", extra=" + extra + "]";
	}
	
}