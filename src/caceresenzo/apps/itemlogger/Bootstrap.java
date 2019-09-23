package caceresenzo.apps.itemlogger;

import caceresenzo.apps.itemlogger.configuration.Config;
import caceresenzo.apps.itemlogger.configuration.Language;
import caceresenzo.apps.itemlogger.managers.ItemLoggerManager;
import caceresenzo.apps.itemlogger.ui.MainLoggerWindow;

public class Bootstrap {
	
	public static void main(String[] args) {
		Config.get();
		Language.get().initialize();
		ItemLoggerManager.get().initialize();
		
		MainLoggerWindow.open();
	}
	
}