package caceresenzo.apps.itemlogger.export.physical;

import java.time.LocalDate;

import caceresenzo.apps.itemlogger.models.ConstructionSite;
import caceresenzo.apps.itemlogger.models.Item;
import caceresenzo.apps.itemlogger.models.Lend;
import caceresenzo.apps.itemlogger.models.Person;

public class GoodRelease {
	
	/* Variables */
	private final Lend lend;
	
	/* Constructor */
	public GoodRelease(Lend lend) {
		this.lend = lend;
	}
	
	/** @return Delegated {@link Lend#getItem()}. */
	public Item getItem() {
		return getLend().getItem();
	}
	
	/** @return Delegated {@link Lend#getPerson()}. */
	public Person getPerson() {
		return getLend().getPerson();
	}
	
	/** @return Delegated {@link Lend#getConstructionSite()}. */
	public ConstructionSite getConstructionSite() {
		return getLend().getConstructionSite();
	}
	
	/** @return Delegated {@link Lend#getQuantity()}. */
	public int getQuantity() {
		return getLend().getQuantity();
	}
	
	/** @return Delegated {@link Lend#getLendDate()}. */
	public LocalDate getLendDate() {
		return getLend().getLendDate();
	}
	
	/** @return Original {@link Lend}. */
	public Lend getLend() {
		return lend;
	}
	
	@Override
	public String toString() {
		return "GoodRelease[lend=" + lend + "]";
	}
	
}