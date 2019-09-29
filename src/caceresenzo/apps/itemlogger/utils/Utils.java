package caceresenzo.apps.itemlogger.utils;

import java.nio.channels.IllegalSelectorException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;

import caceresenzo.apps.itemlogger.configuration.Constants;
import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.settings.SettingEntry;
import caceresenzo.libs.string.StringUtils;

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
	
	/**
	 * Try to extract relevant data from an object.
	 * 
	 * @param object
	 *            Object to convert to string.
	 * @param useShort
	 *            If it is a {@link LocalDate}, weather or not the {@link FormatStyle} used should be short or not.
	 * @param makeItBeautiful
	 *            If it is a {@link LocalDate}, weather or not the day and/or month should be "aligned" with spaces.
	 * @return A relevant string describing the <code>object</code>. Or <code>null</code> if the <code>object</code> was <code>null</code> at first.
	 */
	public static final String toAbsolutlySimpleString(Object object, boolean useShort, boolean makeItBeautiful) {
		if (object == null) {
			return null;
		}
		
		if (object instanceof IDatabaseEntry) {
			return ((IDatabaseEntry) object).describeSimply();
		}
		
		if (object instanceof LocalDate) {
			String string = ((LocalDate) object).format(DateTimeFormatter.ofLocalizedDate(useShort ? FormatStyle.MEDIUM : FormatStyle.LONG));
			
			if (makeItBeautiful) {
				String[] split = string.split(" ");
				
				if (split[0].length() != 2) {
					split[0] = " " + split[0];
				}
				
				if (split[1].length() != 5) {
					split[1] = " " + split[1];
				}
				
				string = StringUtils.join(Arrays.asList(split), " ");
			}
			
			return string;
		}
		
		return String.valueOf(object);
	}
	
}