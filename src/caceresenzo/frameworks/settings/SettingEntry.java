package caceresenzo.frameworks.settings;

public class SettingEntry<T> {
	
	/* Variables */
	private final String key;
	private final T value;
	
	/* Constructor */
	public SettingEntry(String key, T value) {
		this.key = key;
		this.value = value;
	}
	
	/** @return Setting's key. */
	public String getKey() {
		return key;
	}
	
	/** @return Setting's value. */
	public T getValue() {
		return value;
	}
	
	public static interface Aware<T> {
		
		/**
		 * Called when the implemented class must return a {@link SettingEntry} instance of his current state.
		 * 
		 * @return A {@link SettingEntry} instance.
		 */
		public SettingEntry<T> toSettingEntry();
		
	}
	
}