package caceresenzo.apps.itemlogger.ui.part;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyListener;
import java.time.LocalDate;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import caceresenzo.apps.itemlogger.ui.part.implementations.LocalDateFieldPartPanel;
import caceresenzo.apps.itemlogger.ui.part.implementations.ModelSelectionFieldPartPanel;
import caceresenzo.apps.itemlogger.ui.part.implementations.NumberFieldPartPanel;
import caceresenzo.apps.itemlogger.ui.part.implementations.TextFieldPartPanel;
import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTableColumn;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.setup.sql.SqlTableBuilder;
import caceresenzo.libs.internationalization.i18n;

public abstract class AbstractFieldPartPanel<T> extends JPanel {
	
	/* Serialization */
	private static final long serialVersionUID = 7906523043795112645L;
	
	/* UI */
	private Component fieldComponent;
	private JLabel label;
	
	/* Variables */
	protected final Class<?> modelClass;
	protected final BindableColumn bindableColumn;
	
	/* Constructor */
	public AbstractFieldPartPanel(Class<?> modelClass, BindableColumn bindableColumn, KeyListener keyListener) {
		this.modelClass = modelClass;
		this.bindableColumn = bindableColumn;
		
		setMaximumSize(new Dimension(32767, 70));
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		label = new JLabel(i18n.string("logger.table.column." + bindableColumn.getColumnName()));
		if (canBeNull()) {
			label.setText(label.getText() + " " + i18n.string("create-dialog.field.optional"));
		}
		
		fieldComponent = createComponent();
		if (keyListener != null) {
			fieldComponent.addKeyListener(keyListener);
		}
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(fieldComponent, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
										.addComponent(label, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
								.addContainerGap()));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(label)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(fieldComponent, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		
		setLayout(groupLayout);
	}
	
	/** Create the component that will be used to fullfill the field's data correctly. */
	protected abstract Component createComponent();
	
	/**
	 * Recreate an object from field value.
	 * 
	 * @return Created object.
	 * @throws NullPointerException
	 *             If the field is empty (after being {@link String#trim() trim()}).
	 */
	public abstract T getObject();
	
	/** @return Weather or not this field can be <code>null</code>. It is set by with the flag {@link SqlTableBuilder#FLAG_NULL} declared in the {@link DatabaseTableColumn} annotation. */
	public boolean canBeNull() {
		return (bindableColumn.getAnnotation().flags() & SqlTableBuilder.FLAG_NULL) == SqlTableBuilder.FLAG_NULL;
	}
	
	/** @return Field's Swing component. */
	public Component getFieldComponent() {
		return fieldComponent;
	}
	
	/** @return Field's source {@link BindableColumn}. */
	public BindableColumn getBindableColumn() {
		return bindableColumn;
	}
	
	/**
	 * Find the best {@link AbstractFieldPartPanel} implementation for a specific {@link BindableColumn}'s field type.
	 * 
	 * @param <T>
	 *            Paramerized model class type.
	 * @param modelClass
	 *            Model class.
	 * @param bindableColumn
	 *            {@link BindableColumn Column} to match.
	 * @param keyListener
	 *            {@link KeyListener} to listen to custom event (can be <code>null</code>).
	 * @return Best match.
	 * @throws IllegalStateException
	 *             If the {@link BindableColumn}'s field's type is unknown and/or not supported.
	 * @throws IllegalStateException
	 *             If the reflected constructor failed to instanciate.
	 */
	@SuppressWarnings("unchecked")
	public static <T> AbstractFieldPartPanel<T> find(Class<T> modelClass, BindableColumn bindableColumn, KeyListener keyListener) {
		Class<?> columnClass = bindableColumn.getField().getType();
		Class<? extends AbstractFieldPartPanel<?>> clazz;
		
		if (columnClass == String.class) {
			clazz = TextFieldPartPanel.class;
		} else if (columnClass == int.class || columnClass == Integer.class) {
			clazz = NumberFieldPartPanel.class;
		} else if (columnClass == LocalDate.class) {
			clazz = LocalDateFieldPartPanel.class;
		} else if (IDatabaseEntry.class.isAssignableFrom(bindableColumn.getField().getType())) {
			clazz = ModelSelectionFieldPartPanel.class;
		} else {
			throw new IllegalStateException("Unknown field type: " + columnClass);
		}
		
		try {
			return (AbstractFieldPartPanel<T>) clazz.getConstructors()[0].newInstance(modelClass, bindableColumn, keyListener);
		} catch (Exception exception) {
			throw new IllegalStateException("Failed to instanciate the class constructor.", exception);
		}
	}
	
	public static final class EmptyFieldException extends IllegalStateException {
		
		/* Serialization */
		private static final long serialVersionUID = -1003252877471551269L;
		
	}
	
}