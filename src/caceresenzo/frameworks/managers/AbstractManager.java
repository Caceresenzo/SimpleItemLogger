package caceresenzo.frameworks.managers;

public abstract class AbstractManager {
	
	/** Called when the {@link AbstractManager manager} should initialize. */
	public void initialize() {
		;
	}
	
	/** Called when the {@link AbstractManager manager} should save its progress. */
	public void step() {
		;
	}
	
	/** Called when the {@link AbstractManager manager} will be destroyed. */
	public void destroy() {
		;
	}
	
}