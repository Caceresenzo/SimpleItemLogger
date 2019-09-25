package caceresenzo.apps.itemlogger.ui.models.combobox;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 * TODO: Fix white header.
 * 
 * @author Enzo CACERES
 */
public class DatabaseEntryComboBoxCellEditor<T> extends DefaultCellEditor {
	
	/* Serialization */
	private static final long serialVersionUID = 5122814824629571506L;
	
	/* Constructor */
	public DatabaseEntryComboBoxCellEditor(JComboBox<T> comboBox) {
		super(comboBox);
	}
	
}