package caceresenzo.apps.itemlogger.ui.part.implementations;

import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import caceresenzo.apps.itemlogger.ui.part.AbstractFormattedFieldPartPanel;
import caceresenzo.frameworks.database.binder.BindableColumn;

public class NumberFieldPartPanel extends AbstractFormattedFieldPartPanel<Integer> {
	
	/* Constructor */
	public NumberFieldPartPanel(Class<?> modelClass, BindableColumn bindableColumn, KeyListener keyListener) {
		super(modelClass, bindableColumn, keyListener);
	}
	
	@Override
	protected AbstractFormatterFactory createFormatterFactory() {
		NumberFormat numberFormat = NumberFormat.getIntegerInstance();
		numberFormat.setParseIntegerOnly(true);
		
		NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
		numberFormatter.setMaximum(Integer.MAX_VALUE);
		numberFormatter.setMinimum(0);
		
		return new DefaultFormatterFactory(numberFormatter);
	}
	
	@Override
	public Integer getObject() {
		return Integer.parseInt(String.valueOf(getFormattedFieldComponent().getValue()));
	}
	
}