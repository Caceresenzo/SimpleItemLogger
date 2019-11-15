package caceresenzo.apps.itemlogger.export;

import java.util.List;

import caceresenzo.apps.itemlogger.export.generic.GenericExporter;
import caceresenzo.frameworks.settings.SettingEntry;

public interface DataExporter extends GenericExporter<List<SettingEntry<Boolean>>> {
	
	/**
	 * Set the filter that need to be applied to each value for them to validate to be exported.
	 * 
	 * @param filterText
	 *            Filtering text, can be <code>null</code>.
	 */
	public void setFilter(String filterText);
	
}