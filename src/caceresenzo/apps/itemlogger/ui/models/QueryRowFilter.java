package caceresenzo.apps.itemlogger.ui.models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import javax.swing.RowFilter;

import caceresenzo.frameworks.database.IDatabaseEntry;

public class QueryRowFilter extends RowFilter<Object, Object> {
	
	/* Variables */
	private final String lowerQuery;
	private final String[] splitedQuery;
	
	/* Constructor */
	public QueryRowFilter(String query) {
		this.lowerQuery = query.toLowerCase();
		this.splitedQuery = lowerQuery.split(" ");
	}
	
	@Override
	public boolean include(RowFilter.Entry<?, ?> entry) {
		for (int index = 0; index < entry.getValueCount(); index++) {
			Object object = entry.getValue(index);
			String stringValue = null;
			
			if (object instanceof IDatabaseEntry) {
				stringValue = ((IDatabaseEntry) object).describe();
			} else if (object instanceof LocalDate) {
				stringValue = ((LocalDate) object).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
			} else {
				stringValue = String.valueOf(object);
			}
			
			for (String word : splitedQuery) {
				if (stringValue.toLowerCase().contains(word)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}