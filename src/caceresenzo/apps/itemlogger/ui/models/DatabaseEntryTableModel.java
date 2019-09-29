package caceresenzo.apps.itemlogger.ui.models;

import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.apps.itemlogger.ui.models.combobox.DatabaseEntryComboBoxCellEditor;
import caceresenzo.apps.itemlogger.ui.models.combobox.DatabaseEntryComboBoxCellRenderer;
import caceresenzo.apps.itemlogger.ui.models.combobox.DatabaseEntryComboBoxRenderer;
import caceresenzo.apps.itemlogger.ui.models.table.ActionCellEditor;
import caceresenzo.apps.itemlogger.ui.models.table.ActionCellPanel;
import caceresenzo.apps.itemlogger.ui.models.table.ActionCellRenderer;
import caceresenzo.frameworks.database.IDatabaseEntry;
import caceresenzo.frameworks.database.annotations.DatabaseTable;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.binder.BindableTable;
import caceresenzo.frameworks.database.setup.TableAnalizer;
import caceresenzo.frameworks.database.synchronization.DatabaseSynchronizer;
import caceresenzo.libs.internationalization.i18n;

public class DatabaseEntryTableModel<T extends IDatabaseEntry> extends AbstractTableModel {
	
	/* Serialization */
	private static final long serialVersionUID = 1082999014657444212L;
	
	/* Action Commands */
	private static final String ACTION_COMMAND_REMOVE_ROW = "action_remove_row";
	
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
	private BindableTable bindableTable;
	private List<BindableColumn> columns;
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
		this.rowActionButtons = rowActionButtons == null ? new ArrayList<>() : rowActionButtons;
		
		this.synchronizer = DataManager.get().getDatabaseSynchronizer();
		
		this.bindableTable = TableAnalizer.get().analizeTable(modelClass);
		
		checkActionForRemovableRows();
		initializeColumns();
		initializeComboBoxRenderer();
		initilizeActionButtonsRenderer();
	}
	
	/** Check if the {@link BindableTable} has its row "{@link DatabaseTable#removable() removable}" and add the action column (if not already enabled) to add a remove button. */
	private void checkActionForRemovableRows() {
		if (bindableTable.isRowRemovable()) {
			JButton removeRowButton = new JButton(i18n.string("logger.table.column.actions.button.remove"));
			removeRowButton.setActionCommand(ACTION_COMMAND_REMOVE_ROW);
			
			rowActionButtons.add(removeRowButton);
			
			final Callback<T> oldActionCallback = actionCallback;
			actionCallback = new Callback<T>() {
				@Override
				public void openActionClick(JTable table, DatabaseEntryTableModel<T> tableModel, List<T> entries, int row, String action) {
					switch (action) {
						case ACTION_COMMAND_REMOVE_ROW: {
							int reply = JOptionPane.showConfirmDialog(table, i18n.string("logger.table.column.actions.button.remove.confirm-dialog.message"), i18n.string("logger.table.column.actions.button.remove.confirm-dialog.title"), JOptionPane.YES_NO_OPTION);
							
							if (reply == JOptionPane.YES_OPTION) {
								Object instance = entries.get(row);
								Class<?> modelClass = instance.getClass();
								
								synchronizer.delete(modelClass, instance);
								
								synchronize();
							}
							break;
						}
						
						default: {
							if (oldActionCallback != null) {
								oldActionCallback.openActionClick(table, tableModel, entries, row, action);
							}
						}
					}
				}
			};
		}
	}
	
	/** Initialize the columns with the {@link TableAnalizer#analizeColumns(Class) analized} {@link BindableColumn columns}. */
	private void initializeColumns() {
		columns = bindableTable.getBindableColumns();
		columns.remove(BindableColumn.findIdColumn(columns));
		
		int actionColumnSize = rowActionButtons.isEmpty() ? 0 : 1;
		
		columnNames = new String[columns.size() + actionColumnSize];
		columnClass = new Class[columns.size() + actionColumnSize];
		
		int index = 0;
		for (; index < columns.size(); index++) {
			BindableColumn bindableColumn = columns.get(index);
			
			columnNames[index] = i18n.string("logger.table.column." + bindableColumn.getColumnName());
			
			Class<?> type = bindableColumn.getField().getType();
			if (type == int.class) {
				type = Integer.class;
			}
			columnClass[index] = type;
			
			bindableColumn.getField().setAccessible(true);
		}
		
		if (actionColumnSize != 0) {
			columnNames[index] = i18n.string("logger.table.column.actions");
			columnClass[index] = Object.class;
		}
		
		SwingUtilities.invokeLater(() -> {
			for (int jndex = 0; jndex < columns.size(); jndex++) {
				if (columnClass[jndex] == Integer.class) {
					TableColumn tableColumn = table.getColumnModel().getColumn(jndex);
					
					DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
					cellRenderer.setHorizontalAlignment(SwingConstants.LEFT);
					
					DefaultCellEditor cellEditor = (DefaultCellEditor) table.getDefaultEditor(Integer.class);
					((JTextField) cellEditor.getComponent()).setHorizontalAlignment(SwingConstants.LEFT);
					
					tableColumn.setCellRenderer(cellRenderer);
					tableColumn.setCellEditor(cellEditor);
				}
			}
		});
	}
	
	/** Initialize the {@link JComboBox} for columns with references. */
	@SuppressWarnings("unchecked")
	private void initializeComboBoxRenderer() {
		SwingUtilities.invokeLater(() -> {
			for (int index = 0; index < columns.size(); index++) {
				BindableColumn bindableColumn = columns.get(index);
				
				if (bindableColumn.isReference()) {
					JComboBox<?> comboBox = new JComboBox<>(synchronizer.load(bindableColumn.getField().getType()).toArray());
					comboBox.setRenderer(new DatabaseEntryComboBoxRenderer());
					
					table.getColumnModel().getColumn(index).setCellRenderer(new DatabaseEntryComboBoxCellRenderer());
					table.getColumnModel().getColumn(index).setCellEditor(new DatabaseEntryComboBoxCellEditor<>(comboBox));
				}
			}
		});
	}
	
	/** Initialize the render system of the action column only if the <code>useActionColumn</code> was set to <code>true</code> in the constructor. */
	private void initilizeActionButtonsRenderer() {
		if (rowActionButtons.isEmpty()) {
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
		filter(null);
		
		SwingUtilities.invokeLater(() -> {
			this.entries.clear();
			this.entries.addAll(synchronizer.load(modelClass));
			
			fireTableDataChanged();
		});
	}
	
	/**
	 * Filter the {@link JTable table} with a query.<br>
	 * A row will only be accepted if at least one of her column contains one of the words of the query.
	 * 
	 * @param query
	 *            Target query to filter with. Or <code>null</code> to disable the filtering.
	 */
	public void filter(final String query) {
		TableRowSorter<TableModel> sorter = null;
		
		if (query != null) {
			String lowerQuery = query.toLowerCase();
			String[] splitedQuery = lowerQuery.split(" ");
			
			sorter = new TableRowSorter<>(this);
			sorter.setRowFilter(new RowFilter<Object, Object>() {
				public boolean include(RowFilter.Entry<?, ?> entry) {
					for (int index = 0; index < entry.getValueCount(); index++) {
						Object object = entry.getValue(index);
						String stringValue = null;
						
						if (object instanceof IDatabaseEntry) {
							stringValue = ((IDatabaseEntry) object).describe();
						} else if (object instanceof LocalDate) {
							stringValue = ((LocalDate) object).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
						} else {
							stringValue = String.valueOf(object);
						}
						
						for (String word : splitedQuery) {
							if (stringValue.toLowerCase().contains(word)) {
								return true;
							}
						}
					}
					
					return false;
				}
			});
		}
		
		table.setRowSorter(sorter);
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
		
		BindableColumn bindableColumn = columns.get(columnIndex);
		
		if (bindableColumn.isAutomatable()) {
			return false;
		}
		
		return !Modifier.isFinal(bindableColumn.getField().getModifiers());
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
					if (!synchronizer.update(modelClass, row)) {
						synchronize();
					}
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
		 * @param tableClass
		 *            Source model.
		 * @param entries
		 *            The entries {@link List list}.
		 * @param row
		 *            Current row.
		 * @param action
		 *            {@link JButton} action command.
		 */
		void openActionClick(JTable table, DatabaseEntryTableModel<T> tableClass, List<T> entries, int row, String action);
		
	}
	
}