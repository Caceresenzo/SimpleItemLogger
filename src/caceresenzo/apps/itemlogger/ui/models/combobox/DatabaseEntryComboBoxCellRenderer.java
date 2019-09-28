package caceresenzo.apps.itemlogger.ui.models.combobox;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import caceresenzo.frameworks.database.IDatabaseEntry;

public class DatabaseEntryComboBoxCellRenderer extends DefaultTableCellRenderer {
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		IDatabaseEntry entry = (IDatabaseEntry) value;
		
		setIcon(null);
		setText(entry.describe());
		
		return this;
	}
	
}