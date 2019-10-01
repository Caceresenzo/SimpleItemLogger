package caceresenzo.apps.itemlogger.ui.part.implementations;

import java.awt.event.KeyListener;
import java.util.List;

import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.apps.itemlogger.ui.part.AbstractSelectionFieldPartPanel;
import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.binder.BindableColumn;

public class ModelSelectionFieldPartPanel extends AbstractSelectionFieldPartPanel<IDatabaseEntry> {

	/* Constructor */
	public ModelSelectionFieldPartPanel(Class<?> modelClass, BindableColumn bindableColumn, KeyListener keyListener) {
		super(modelClass, bindableColumn, keyListener);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IDatabaseEntry> getItems() {
		return (List<IDatabaseEntry>) DataManager.get().getDatabaseSynchronizer().load(bindableColumn.getField().getType());
	}
	
}