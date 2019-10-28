package caceresenzo.apps.itemlogger.ui.lend;

import java.awt.Dimension;
import java.lang.reflect.Field;
import java.time.DayOfWeek;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ReturnedLendDetailPanel extends JPanel {
	private DatePicker datePicker;
	private JTextField textField;
	private JTextField textField_1;
	
	/**
	 * Create the panel.
	 */
	public ReturnedLendDetailPanel() {
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Returned", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		setMinimumSize(new Dimension(0, 115));
		setSize(new Dimension(200, 115));
		setMaximumSize(new Dimension(32767, 115));
		
		DatePickerSettings dateSettings = new DatePickerSettings();
		dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
		dateSettings.setAllowEmptyDates(false);
		
		datePicker = new DatePicker(dateSettings);
		datePicker.setEnabled(false);
		
		textField = new JTextField();
		textField.setEnabled(false);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setText("No comment.");
		textField_1.setEnabled(false);
		textField_1.setColumns(10);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textField_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
						.addComponent(textField, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE)
						.addComponent(datePicker, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 168, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(datePicker, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(9))
		);
		
		setLayout(groupLayout);
	}
}
