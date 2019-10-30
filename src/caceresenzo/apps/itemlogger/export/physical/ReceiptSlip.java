package caceresenzo.apps.itemlogger.export.physical;

import java.time.LocalDate;

import caceresenzo.apps.itemlogger.models.Lend;
import caceresenzo.apps.itemlogger.models.ReturnEntry;

public class ReceiptSlip {
	
	/* Variables */
	private final ReturnEntry returnEntry;
	
	/* Constructor */
	public ReceiptSlip(ReturnEntry returnEntry) {
		this.returnEntry = returnEntry;
	}
	
	/** @return Delegated {@link ReturnEntry#getLend()}. */
	public Lend getLend() {
		return getReturnEntry().getLend();
	}
	
	/** @return Delegated {@link ReturnEntry#getQuantity()}). */
	public int getQuantity() {
		return getReturnEntry().getQuantity();
	}
	
	/** @return Delegated {@link ReturnEntry#getReturnDate()}. */
	public LocalDate getReturnDate() {
		return getReturnEntry().getReturnDate();
	}
	
	/** @return Original {@link ReturnEntry}. */
	public ReturnEntry getReturnEntry() {
		return returnEntry;
	}
	
	@Override
	public String toString() {
		return "ReceiptSlip[returnEntry=" + returnEntry + "]";
	}
	
}