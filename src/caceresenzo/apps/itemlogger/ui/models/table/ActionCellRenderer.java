package caceresenzo.apps.itemlogger.ui.models.table;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ActionCellRenderer extends DefaultTableCellRenderer {
	
	/* UI */
	private final ActionCellPanel actionCellPanel;
	
	/* Constructor */
	public ActionCellRenderer(ActionCellPanel actionCellPanel) {
		this.actionCellPanel = actionCellPanel;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (isSelected) {
			actionCellPanel.setBackground(table.getSelectionBackground());
		} else {
			actionCellPanel.setBackground(table.getBackground());
		}
		
		return actionCellPanel;
	}
	
}