package caceresenzo.apps.itemlogger.export;

import java.util.List;

import caceresenzo.apps.itemlogger.export.generic.GenericExporter;
import caceresenzo.frameworks.settings.SettingEntry;

public interface DataExporter extends GenericExporter<List<SettingEntry<Boolean>>> {
	
}