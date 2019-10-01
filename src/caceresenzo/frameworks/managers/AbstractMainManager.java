package caceresenzo.frameworks.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMainManager extends AbstractManager {
	
	/* Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(AbstractMainManager.class);
	
	/* Managers */
	private final List<AbstractManager> managers;
	
	/* Variables */
	private boolean hasInitialized;
	
	/* Constructor */
	public AbstractMainManager() {
		this.managers = new ArrayList<>();
	}
	
	/**
	 * Register a {@link AbstractManager manager}.<br>
	 * This will also {@link AbstractManager#initialize() initialize} it if the {@link AbstractMainManager main manager} has already be initialized.
	 * 
	 * @param manager
	 *            Target {@link AbstractManager manager} to register.
	 * @throws NullPointerException
	 *             If the <code>manager</code> is <code>null</code>.
	 */
	public void register(AbstractManager manager) {
		Objects.requireNonNull(manager, "Cannot register a null manager.");
		
		LOGGER.info("Registering manager \"{}\".", manager.getClass().getSimpleName());
		
		managers.add(manager);
		
		if (hasInitialized) {
			manager.initialize();
		}
	}
	
	@Override
	public void initialize() {
		if (hasInitialized) {
			return;
		}
		
		LOGGER.info("Initializing all managers...");
		
		managers.forEach(AbstractManager::initialize);
	}
	
	@Override
	public void step() {
		managers.forEach(AbstractManager::step);
	}
	
	@Override
	public void destroy() {
		managers.forEach(AbstractManager::destroy);
	}
	
}