package caceresenzo.apps.itemlogger.utils;

import java.nio.channels.IllegalSelectorException;

import caceresenzo.apps.itemlogger.configuration.Constants;
import caceresenzo.frameworks.settings.SettingEntry;

public class Utils implements Constants {
	
	/* Constructor */
	private Utils() {
		throw new IllegalSelectorException();
	}
	
	/**
	 * Format a {@link SettingEntry}'s key with a model class.
	 * 
	 * @param clazz
	 *            Model's class.
	 * @return A formatted key.
	 */
	public static final String formatModelClassSettingEntryKey(Class<?> clazz) {
		return String.format(SETTING_KEY_MODEL_FORMAT, clazz.getSimpleName());
	}
	
}