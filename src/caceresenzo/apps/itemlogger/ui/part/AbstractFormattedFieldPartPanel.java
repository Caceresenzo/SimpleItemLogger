package caceresenzo.apps.itemlogger.ui.part;

import java.awt.Component;
import java.awt.event.KeyListener;

import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;

import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.libs.string.StringUtils;

public abstract class AbstractFormattedFieldPartPanel<T> extends AbstractFieldPartPanel<T> {
	
	/* Constructor */
	public AbstractFormattedFieldPartPanel(Class<?> modelClass, BindableColumn bindableColumn, KeyListener keyListener) {
		super(modelClass, bindableColumn, keyListener);
		
		applyCorrectFormatter();
	}
	
	@Override
	protected Component createComponent() {
		return new JFormattedTextField();
	}
	
	/** @return The {@link #getFieldComponent() field component} but casted to a {@link JFormattedTextField}. */
	public JFormattedTextField getFormattedFieldComponent() {
		return (JFormattedTextField) super.getFieldComponent();
	}
	
	/** Apply the best {@link AbstractFormatter formatter} for this field. */
	protected void applyCorrectFormatter() {
		((JFormattedTextField) getFieldComponent()).setFormatterFactory(createFormatterFactory());
	}
	
	/** @return A {@link AbstractFormatterFactory} for the {@link JFormattedTextField} field component. */
	protected abstract AbstractFormatterFactory createFormatterFactory();
	
	/**
	 * Validate the {@link JFormattedTextField}'s text and throws an exception if the text is <code>null</code> or empty.
	 * 
	 * @throws EmptyFieldException
	 *             If the field's text (after trim) is <code>null</code> or empty.
	 * @see StringUtils#validate(String...)
	 */
	public void validateInput() {
		String text = getFormattedFieldComponent().getText().trim();
		
		if (!StringUtils.validate(text) && !canBeNull()) {
			throw new EmptyFieldException();
		}
	}
	
}