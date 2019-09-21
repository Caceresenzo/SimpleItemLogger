package caceresenzo.apps.itemlogger.ui.models.table;

import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

public class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
	
	/* UI */
	private final ActionCellPanel actionCellPanel;
	
	/* Constructor */
	public ActionCellEditor(ActionCellPanel actionCellPane) {
		this.actionCellPanel = actionCellPane;
		
		initialize();
	}
	
	/** Initialize the cell editor. */
	private void initialize() {
		actionCellPanel.addActionListener((event) -> SwingUtilities.invokeLater(() -> stopCellEditing()));
	}
	
	@Override
	public Object getCellEditorValue() {
		return actionCellPanel.getState();
	}
	
	@Override
	public boolean isCellEditable(EventObject eventObject) {
		return true;
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (isSelected) {
			actionCellPanel.setBackground(table.getSelectionBackground());
		} else {
			actionCellPanel.setBackground(table.getBackground());
		}
		
		return actionCellPanel;
	}
	
}