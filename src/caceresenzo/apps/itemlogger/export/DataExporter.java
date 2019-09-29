package caceresenzo.apps.itemlogger.export;

import java.io.File;
import java.util.List;

import caceresenzo.frameworks.settings.SettingEntry;

public interface DataExporter {
	
	/**
	 * Export data to a file.
	 * 
	 * @param settingEntries
	 *            Setting that the exporter should respect.
	 * @param file
	 *            Target file destination.
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	public void exportToFile(List<SettingEntry<Boolean>> settingEntries, File file) throws Exception;
	
}