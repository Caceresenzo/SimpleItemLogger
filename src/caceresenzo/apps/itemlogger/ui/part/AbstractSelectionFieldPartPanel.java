package caceresenzo.apps.itemlogger.ui.part;

import java.awt.Component;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;

import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.libs.internationalization.i18n;

public abstract class AbstractSelectionFieldPartPanel<T extends IDatabaseEntry> extends AbstractFieldPartPanel<T> {
	
	/* Variables */
	protected List<T> items;
	
	/* Constructor */
	public AbstractSelectionFieldPartPanel(Class<?> modelClass, BindableColumn bindableColumn, KeyListener keyListener) {
		super(modelClass, bindableColumn, keyListener);
	}
	
	/** @return An final {@link List list} of item that are able to be {@link IModel selected}. */
	public abstract List<T> getItems();
	
	@Override
	protected Component createComponent() {
		List<String> stringItems = new ArrayList<>();
		
		items = getItems();
		items.sort((item1, item2) -> item1.describe().compareTo(item2.describe()));
		items.forEach((item) -> stringItems.add(item.describe()));
		
		stringItems.add(0, i18n.string("create-dialog.combobox.default.select-an-item"));
		
		return new JComboBox<>(stringItems.toArray());
	}
	
	/** @return The {@link #getFieldComponent() field component} but casted to a {@link JComboBox}. */
	public JComboBox<?> getComboBoxFieldComponent() {
		return (JComboBox<?>) super.getFieldComponent();
	}
	
	/**
	 * Validate the {@link JFormattedTextField}'s selection and throws an exception if the selected item index is <code>0</code> (which correspond to the "select an item" text).
	 * 
	 * @throws EmptyFieldException
	 *             If the selected item index is <code>0</code>.
	 */
	public void validateInput() {
		if (canBeNull()) {
			return;
		}
		
		if (getComboBoxFieldComponent().getSelectedIndex() == 0) {
			throw new EmptyFieldException();
		}
	}
	
	@Override
	public T getObject() {
		validateInput();
		
		return items.get(getComboBoxFieldComponent().getSelectedIndex() - 1);
	}
	
}