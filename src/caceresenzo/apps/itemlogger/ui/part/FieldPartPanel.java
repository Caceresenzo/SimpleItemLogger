package caceresenzo.apps.itemlogger.ui.part;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;
import java.text.ParseException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFormattedTextField;
import javax.swing.JFormattedTextField.AbstractFormatterFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.libs.internationalization.i18n;
import caceresenzo.libs.string.StringUtils;
import caceresenzo.libs.thread.ThreadUtils;

public class FieldPartPanel extends JPanel {
	
	/* Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(FieldPartPanel.class);
	
	/* UI */
	private JFormattedTextField formattedTextField;
	private JLabel label;
	
	/* Variables */
	private final Class<?> modelClass;
	private final BindableColumn bindableColumn;
	
	/* Constructor */
	public FieldPartPanel(Class<?> modelClass, BindableColumn bindableColumn, KeyListener keyListener) {
		this.modelClass = modelClass;
		this.bindableColumn = bindableColumn;
		
		setMaximumSize(new Dimension(32767, 70));
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		label = new JLabel(i18n.string("logger.table.column." + bindableColumn.getColumnName()));
		
		formattedTextField = new JFormattedTextField();
		if (keyListener != null) {
			formattedTextField.addKeyListener(keyListener);
		}
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
										.addComponent(formattedTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
										.addComponent(label, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE))
								.addContainerGap()));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(label)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(formattedTextField, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		
		setLayout(groupLayout);
		
		applyCorrectFormatter();
	}
	
	private void applyCorrectFormatter() {
		Class<?> type = bindableColumn.getField().getType();
		AbstractFormatterFactory formatterFactory;
		
		if (type == Integer.class || type == int.class) {
			NumberFormat numberFormat = NumberFormat.getIntegerInstance();
			numberFormat.setParseIntegerOnly(true);
			
			NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
			numberFormatter.setMaximum(Integer.MAX_VALUE);
			numberFormatter.setMinimum(0);
			
			formatterFactory = new DefaultFormatterFactory(numberFormatter);
		} else {
			formatterFactory = new DefaultFormatterFactory();
		}
		
		formattedTextField.setFormatterFactory(formatterFactory);
	}
	
	public Object getObject() {
		Class<?> type = bindableColumn.getField().getType();
		
		if (type == Integer.class || type == int.class) {
			return Integer.parseInt(String.valueOf(formattedTextField.getValue()));
		} else {
			return formattedTextField.getText().trim();
		}
	}
	
	/** @return Field's new value. */
	public JTextField getTextField() {
		return formattedTextField;
	}
	
	/** @return Field's source {@link BindableColumn}. */
	public BindableColumn getBindableColumn() {
		return bindableColumn;
	}
	
}