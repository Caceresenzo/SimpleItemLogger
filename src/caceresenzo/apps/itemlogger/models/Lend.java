package caceresenzo.apps.itemlogger.models;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.automator.AbstractDatabaseColumnValueAutomator;
import caceresenzo.frameworks.database.setup.TableAnalizer;
import caceresenzo.frameworks.database.setup.sql.SqlTableBuilder;

@DatabaseTable(value = "lends", hasSubData = true, isRemovable = true)
public class Lend implements IDatabaseEntry {
	
	/* Database Fields */
	public static final String COLUMN_ITEM = "item";
	public static final String COLUMN_PERSON = "person";
	public static final String COLUMN_CONSTRUCTION_SITE = "construction_site";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_WAITING_QUANTITY = "waiting_quantity";
	public static final String COLUMN_LEND_DATE = "lend";
	public static final String COLUMN_EXTRA = "extra";
	
	/* Variables */
	@DatabaseTableColumn(DatabaseTableColumn.COLUMN_ID)
	private final int id;
	@DatabaseTableColumn(value = COLUMN_ITEM, isReference = true, isEditable = false)
	private Item item;
	@DatabaseTableColumn(value = COLUMN_PERSON, isReference = true, isEditable = false)
	private Person person;
	@DatabaseTableColumn(value = COLUMN_CONSTRUCTION_SITE, isReference = true, isEditable = false)
	private ConstructionSite constructionSite;
	@DatabaseTableColumn(value = COLUMN_QUANTITY, isEditable = false)
	private int quantity;
	@DatabaseTableColumn(value = COLUMN_WAITING_QUANTITY, isEditable = false, automator = LendRemainingToReturnDatabaseColumnValueAutomator.class)
	private int waitingQuantity;
	@DatabaseTableColumn(value = COLUMN_LEND_DATE, isVisible = false, isEditable = false)
	private LocalDate lendDate;
	@DatabaseTableColumn(value = COLUMN_EXTRA, flags = SqlTableBuilder.FLAG_NULL, isVisible = false, isEditable = false)
	private String extra;
	
	/* Constructor */
	public Lend() {
		this(0, null, null, 0, null, null, null);
	}
	
	/* Constructor */
	public Lend(int id, Item item, Person person, int quantity, ConstructionSite constructionSite, LocalDate lendDate, String extra) {
		this.id = id;
		this.item = item;
		this.person = person;
		this.quantity = quantity;
		this.constructionSite = constructionSite;
		this.lendDate = lendDate;
		this.extra = extra;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	/** @return {@link Lend}'s target item. */
	public Item getItem() {
		return item;
	}
	
	/** @return {@link Lend}'s target person. */
	public Person getPerson() {
		return person;
	}
	
	/** @return {@link Lends}'s target construction site. */
	public ConstructionSite getConstructionSite() {
		return constructionSite;
	}
	
	/** @return How much of {@link #getItem() item} has been borrow for this {@link Lend}. */
	public int getQuantity() {
		return quantity;
	}
	
	/** @return How much of {@link #getItem() item} has been returned for this {@link Lend}. */
	public int getWaitingQuantity() {
		return waitingQuantity;
	}
	
	/** @return {@link Lend}'s lend date. */
	public LocalDate getLendDate() {
		return lendDate;
	}
	
	/** @return {@link Lend}'s extra user data. */
	public String getExtra() {
		return extra;
	}
	
	@Override
	public String describe() {
		return item.getName();
	}
	
	@Override
	public String describeSimply() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		return "Lend[id=" + id + ", item=" + item + ", person=" + person + ", constructionSite=" + constructionSite + ", quantity=" + quantity + ", waitingQuantity=" + waitingQuantity + ", lendDate=" + lendDate + ", extra=" + extra + "]";
	}
	
	public static final class LendRemainingToReturnDatabaseColumnValueAutomator extends AbstractDatabaseColumnValueAutomator {
		
		/* Variables */
		private final String returnEntryTableName;
		
		/* Constructor */
		public LendRemainingToReturnDatabaseColumnValueAutomator() {
			super();
			
			this.returnEntryTableName = TableAnalizer.get().analizeTable(ReturnEntry.class).getTableName();
		}
		
		@Override
		public void automate(Class<?> forModelClass, Class<?> columnClass, Field field, Object instance) {
			if (!Lend.class.equals(forModelClass)) {
				return;
			}
			
			Lend lend = ((Lend) instance);
			
			try {
				StringBuilder stringBuilder = new StringBuilder();
				
				stringBuilder
						.append("SELECT ").append(ReturnEntry.COLUMN_QUANTITY)
						.append(" FROM ").append(returnEntryTableName)
						.append(" WHERE ")
						.append(ReturnEntry.COLUMN_LEND).append(" = ?;");
				
				PreparedStatement statement = DataManager.get().getDatabaseConnection().prepareStatement(stringBuilder.toString());
				statement.setInt(1, lend.getId());
				
				int waiting = lend.getQuantity();
				
				try (ResultSet resultSet = statement.executeQuery()) {
					while (resultSet.next()) {
						int quantity = resultSet.getInt(Lend.COLUMN_QUANTITY);
						
						waiting -= quantity;
					}
				}
				
				field.set(instance, waiting);
			} catch (Exception exception) {
				LOGGER.warn("Failed to automate in stock value for the Lend class model.", exception);
			}
		}
		
	}
	
}