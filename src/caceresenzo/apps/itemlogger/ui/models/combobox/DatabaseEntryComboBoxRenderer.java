package caceresenzo.apps.itemlogger.ui.models.combobox;

import java.awt.Component;

import javax.swing.JList;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import caceresenzo.frameworks.database.IDatabaseEntry;

public class DatabaseEntryComboBoxRenderer extends BasicComboBoxRenderer {
	
	@SuppressWarnings("rawtypes")
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		IDatabaseEntry entry = (IDatabaseEntry) value;
		
		setIcon(null);
		
		if (index == -1) {
			setText(null);
		} else {
			setText(entry.describe());
		}
		
		return this;
	}
	
}