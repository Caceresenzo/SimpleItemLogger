package caceresenzo.apps.itemlogger.ui.part.implementations;

import java.awt.event.KeyListener;

import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.text.DefaultFormatterFactory;

import caceresenzo.apps.itemlogger.ui.part.AbstractFormattedFieldPartPanel;
import caceresenzo.frameworks.database.binder.BindableColumn;

public class TextFieldPartPanel extends AbstractFormattedFieldPartPanel<String> {
	
	/* Constructor */
	public TextFieldPartPanel(Class<?> modelClass, BindableColumn bindableColumn, KeyListener keyListener) {
		super(modelClass, bindableColumn, keyListener);
	}
	
	@Override
	public String getObject() {
		validateInput();
		
		return getFormattedFieldComponent().getText().trim();
	}
	
	@Override
	protected AbstractFormatterFactory createFormatterFactory() {
		return new DefaultFormatterFactory();
	}
	
}