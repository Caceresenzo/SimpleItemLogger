package caceresenzo.apps.itemlogger.export;

import java.io.File;
import java.util.List;

import caceresenzo.frameworks.settings.SettingEntry;

public interface DataExporter {
	
	public void exportToFile(List<SettingEntry<Boolean>> settingEntries, File file) throws Exception;
	
}