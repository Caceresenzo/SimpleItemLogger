package caceresenzo.apps.itemlogger.ui.models;

import java.lang.reflect.Modifier;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.apps.itemlogger.ui.models.table.ActionCellEditor;
import caceresenzo.apps.itemlogger.ui.models.table.ActionCellPanel;
import caceresenzo.apps.itemlogger.ui.models.table.ActionCellRenderer;
import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.setup.TableAnalizer;
import caceresenzo.frameworks.database.synchronization.DatabaseSynchronizer;
import caceresenzo.libs.internationalization.i18n;

public class DatabaseEntryTableModel<T extends IDatabaseEntry> extends AbstractTableModel {
	
	/* UI */
	private final JTable table;
	
	/* Variables */
	private final Class<T> modelClass;
	private final List<T> entries;
	private final List<JButton> rowActionButtons;
	private Callback<T> actionCallback;
	
	/* Update */
	private DatabaseSynchronizer synchronizer;
	
	/* Table Model */
	List<BindableColumn> columns;
	private String[] columnNames;
	private Class<?>[] columnClass;
	
	/* Constructor */
	public DatabaseEntryTableModel(JTable table, Class<T> modelClass, List<T> databaseEntries) {
		this(table, modelClass, databaseEntries, null);
	}
	
	/* Constructor */
	public DatabaseEntryTableModel(JTable table, Class<T> modelClass, List<T> databaseEntries, List<JButton> rowActionButtons) {
		this.table = table;
		this.modelClass = modelClass;
		this.entries = databaseEntries;
		this.rowActionButtons = rowActionButtons;
		
		this.synchronizer = DataManager.get().getDatabaseSynchronizer();
		
		initializeColumns(modelClass);
		initilizeRenderer();
	}
	
	/**
	 * Initialize the columns with the {@link TableAnalizer#analizeColumns(Class) analized} {@link BindableColumn columns}.
	 * 
	 * @param clazz
	 *            Target class.
	 */
	private void initializeColumns(Class<T> clazz) {
		columns = TableAnalizer.get().analizeColumns(clazz);
		columns.remove(BindableColumn.findIdColumn(columns));
		
		int actionColumnSize = rowActionButtons != null ? 1 : 0;
		
		columnNames = new String[columns.size() + actionColumnSize];
		columnClass = new Class[columns.size() + actionColumnSize];
		
		int index = 0;
		for (; index < columns.size(); index++) {
			BindableColumn bindableColumn = columns.get(index);
			
			columnNames[index] = i18n.string("logger.table.column." + bindableColumn.getColumnName());
			columnClass[index] = bindableColumn.getField().getType();
			
			bindableColumn.getField().setAccessible(true);
		}
		
		if (actionColumnSize != 0) {
			columnNames[index] = "ACTIONS";
			columnClass[index] = Object.class;
		}
	}
	
	/** Initialize the render system of the action column only if the <code>useActionColumn</code> was set to <code>true</code> in the constructor. */
	private void initilizeRenderer() {
		if (rowActionButtons == null) {
			return;
		}
		
		SwingUtilities.invokeLater(() -> {
			int actionColumnIndex = getActionColumnIndex();
			ActionCellPanel actionCellPanel = new ActionCellPanel(rowActionButtons);
			ActionCellRenderer renderer = new ActionCellRenderer(actionCellPanel.copy());
			
			table.getColumnModel().getColumn(actionColumnIndex).setCellRenderer(renderer);
			table.getColumnModel().getColumn(actionColumnIndex).setCellEditor(new ActionCellEditor(actionCellPanel));
			table.setRowHeight(renderer.getTableCellRendererComponent(table, null, true, true, 0, 0).getPreferredSize().height);
		});
	}
	
	/** Synchronize the model with the database values. */
	public void synchronize() {
		this.entries.clear();
		this.entries.addAll(synchronizer.load(modelClass));
		
		fireTableDataChanged();
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
				
				if (synchronizer != null) {
					synchronizer.update(modelClass, row);
				}
			} catch (IllegalArgumentException | IllegalAccessException exception) {
				throw new RuntimeException(exception);
			}
		}
	}
	
	/** @return The index of the action's column. */
	public int getActionColumnIndex() {
		return columns.size();
	}
	
	/**
	 * Check if an index is the action column.
	 * 
	 * @param index
	 *            Target index.
	 * @return Weather or not the index correspond to the action column.
	 */
	public boolean isActionColumn(int index) {
		return index == getActionColumnIndex();
	}
	
	/**
	 * Set the action callback.
	 * 
	 * @param actionCallback
	 *            Callback instance.
	 */
	public void setActionCallback(Callback<T> actionCallback) {
		this.actionCallback = actionCallback;
	}
	
	/** @return Source {@link JTable}. */
	public JTable getTable() {
		return table;
	}
	
	/** @return The entries {@link List list}. */
	public List<T> getEntries() {
		return entries;
	}
	
	public interface Callback<T extends IDatabaseEntry> {
		
		/**
		 * Called when a {@link JButton button} of the action column's action event is trigger.
		 * 
		 * @param table
		 *            Source {@link JTable}.
		 * @param model
		 *            Source model.
		 * @param entries
		 *            The entries {@link List list}.
		 * @param row
		 *            Current row.
		 * @param action
		 *            {@link JButton} action command.
		 */
		void openActionClick(JTable table, DatabaseEntryTableModel<T> model, List<T> entries, int row, String action);
		
	}
	
}