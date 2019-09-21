package caceresenzo.apps.itemlogger.ui.models;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import caceresenzo.apps.itemlogger.ui.models.table.ActionCellEditor;
import caceresenzo.apps.itemlogger.ui.models.table.ActionCellPanel;
import caceresenzo.apps.itemlogger.ui.models.table.ActionCellRenderer;
import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.setup.TableAnalizer;

public class DatabaseEntryTableModel<T extends IDatabaseEntry> extends AbstractTableModel {
	
	/* UI */
	private final JTable table;
	
	/* Variables */
	private final List<T> entries;
	private Callback<T> actionCallback;
	
	/* Update */
	
	/* Table Model */
	List<BindableColumn> columns;
	private String[] columnNames;
	private Class<?>[] columnClass;
	
	/* Constructor */
	public DatabaseEntryTableModel(JTable table, Class<T> clazz, List<T> databaseEntries) {
		this.table = table;
		this.entries = databaseEntries;
		
		initializeColumns(clazz);
		initilizeRenderer();
	}
	
	private void initializeColumns(Class<T> clazz) {
		columns = TableAnalizer.get().analizeColumns(clazz, true);
		
		columnNames = new String[columns.size() + 1];
		columnClass = new Class[columns.size() + 1];
		
		int index = 0;
		for (; index < columns.size(); index++) {
			BindableColumn bindableColumn = columns.get(index);
			
			columnNames[index] = bindableColumn.getColumnName();
			columnClass[index] = bindableColumn.getField().getType();
			
			bindableColumn.getField().setAccessible(true);
		}
		
		columnNames[index] = "ACTIONS";
		columnClass[index] = Object.class;
	}
	
	private void initilizeRenderer() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				int actionColumnIndex = getActionColumnIndex();
				ActionCellPanel actionCellPanel = new ActionCellPanel(Arrays.asList(new JButton("Hello"), new JButton("Hello1"), new JButton("Hell2o")));
				ActionCellRenderer renderer = new ActionCellRenderer(actionCellPanel.copy());
				
				table.getColumnModel().getColumn(actionColumnIndex).setCellRenderer(renderer);
				table.getColumnModel().getColumn(actionColumnIndex).setCellEditor(new ActionCellEditor(actionCellPanel));
				table.setRowHeight(renderer.getTableCellRendererComponent(table, null, true, true, 0, 0).getPreferredSize().height);
			}
		});
	}
	
	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClass[columnIndex];
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public int getRowCount() {
		return entries.size();
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		T row = entries.get(rowIndex);
		
		if (isActionColumn(columnIndex)) {
			return null;
		}
		
		try {
			return columns.get(columnIndex).getField().get(row);
		} catch (IllegalArgumentException | IllegalAccessException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (isActionColumn(columnIndex)) {
			return true;
		}
		
		return !Modifier.isFinal(columns.get(columnIndex).getField().getModifiers());
	}
	
	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (isActionColumn(columnIndex)) {
			if (actionCallback != null) {
				actionCallback.openActionClick(table, this, entries, rowIndex, (String) aValue);
			}
		} else {
			T row = entries.get(rowIndex);
			
			try {
				columns.get(columnIndex).getField().set(row, aValue);
				
				
			} catch (IllegalArgumentException | IllegalAccessException exception) {
				throw new RuntimeException(exception);
			}
		}
	}
	
	public int getActionColumnIndex() {
		return columns.size();
	}
	
	public boolean isActionColumn(int index) {
		return index == getActionColumnIndex();
	}
	
	public JTable getTable() {
		return table;
	}
	
	public List<T> getEntries() {
		return entries;
	}
	
	public interface Callback<T extends IDatabaseEntry> {
		
		void openActionClick(JTable table, DatabaseEntryTableModel<T> model, List<T> entries, int row, String action);
		
	}
	
}