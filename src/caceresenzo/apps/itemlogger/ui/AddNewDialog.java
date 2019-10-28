package caceresenzo.apps.itemlogger.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caceresenzo.apps.itemlogger.ui.part.AbstractFieldPartPanel;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.setup.TableAnalizer;
import caceresenzo.libs.internationalization.i18n;

public class AddNewDialog extends JDialog implements ActionListener {
	
	/* Logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(AddNewDialog.class);
	
	/* Action Commands */
	public static final String ACTION_COMMAND_DONE = "action_done";
	public static final String ACTION_COMMAND_CANCEL = "action_cancel";
	
	/* UI */
	private final JPanel contentPanel = new JPanel();
	private JPanel fieldListPanel;
	
	/* Variables */
	private final Class<?> modelClass;
	private final transient Callback callback;
	private final transient List<BindableColumn> bindableColumns;
	private final transient List<FullfilledEntry> fullfilledEntries;
	
	/* Constructor */
	public AddNewDialog(JFrame parent, Class<?> modelClass, Callback callback) {
		this(parent, modelClass, callback, null);
	}
	
	/* Constructor */
	public AddNewDialog(JFrame parent, Class<?> modelClass, Callback callback, List<FullfilledEntry> fullfilledEntries) {
		super(parent);
		
		this.modelClass = modelClass;
		this.callback = callback;
		this.fullfilledEntries = fullfilledEntries == null ? new ArrayList<>() : fullfilledEntries;
		
		this.bindableColumns = TableAnalizer.get().analizeColumns(modelClass);
		this.bindableColumns.remove(BindableColumn.findIdColumn(bindableColumns));
		BindableColumn.removeAutomatable(bindableColumns);
		
		setSize(700, 400);
		setModal(true);
		setTitle(i18n.string("create-dialog.window.title"));
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(8);
		scrollPane.setBorder(new TitledBorder(null, i18n.string("create-dialog.frame.new-line-part", i18n.string("model." + modelClass.getSimpleName().toLowerCase())), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE));
		gl_contentPanel.setVerticalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE));
		
		fieldListPanel = new JPanel();
		fieldListPanel.setBorder(null);
		scrollPane.setViewportView(fieldListPanel);
		fieldListPanel.setLayout(new BoxLayout(fieldListPanel, BoxLayout.Y_AXIS));
		contentPanel.setLayout(gl_contentPanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		JButton doneButton = new JButton(i18n.string("create-dialog.button.done"));
		doneButton.setActionCommand(ACTION_COMMAND_DONE);
		doneButton.addActionListener(this);
		buttonPanel.add(doneButton);
		
		JButton cancelButton = new JButton(i18n.string("create-dialog.button.cancel"));
		cancelButton.setActionCommand(ACTION_COMMAND_CANCEL);
		cancelButton.addActionListener(this);
		buttonPanel.add(cancelButton);
		
		getRootPane().setDefaultButton(doneButton);
		
		initializePanels();
	}
	
	/** Fill the main panel with all {@link AbstractFieldPartPanel}. */
	private void initializePanels() {
		for (BindableColumn bindableColumn : bindableColumns) {
			FullfilledEntry fullfilledEntry = FullfilledEntry.find(fullfilledEntries, bindableColumn);
			
			if (fullfilledEntry == null) {
				fieldListPanel.add(AbstractFieldPartPanel.find(modelClass, bindableColumn, null));
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();
		
		switch (actionCommand) {
			case ACTION_COMMAND_DONE: {
				fireCallback();
				break;
			}
			
			case ACTION_COMMAND_CANCEL: {
				setVisible(false);
				break;
			}
			
			default: {
				throw new IllegalStateException("Unknown action command: " + actionCommand);
			}
		}
	}
	
	/** Create the item model if the callback is not <code>null</code> and return it. */
	public void fireCallback() {
		if (callback != null) {
			Object instance = null;
			
			try {
				instance = createInstanceFromField();
				
				if (instance == null) {
					return;
				}
				
				setVisible(false);
			} catch (Exception exception) {
				LOGGER.info("Failed to create model instance from field.", exception);
			}
			
			callback.onCreatedItem(modelClass, instance);
		}
	}
	
	/**
	 * Create an model instance from the fields.
	 * 
	 * @return Created instance from field, or <code>null</code> if an error append in the {@link AbstractFieldPartPanel#getObject()} method.
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	public Object createInstanceFromField() throws Exception {
		Object instance = modelClass.getConstructors()[0].newInstance();
		
		for (Component component : fieldListPanel.getComponents()) {
			if (component instanceof AbstractFieldPartPanel) {
				AbstractFieldPartPanel<?> fieldPartPanel = (AbstractFieldPartPanel<?>) component;
				BindableColumn bindableColumn = fieldPartPanel.getBindableColumn();
				Field field = bindableColumn.getField();
				
				Object value;
				
				try {
					value = fieldPartPanel.getObject();
				} catch (Exception exception) {
					LOGGER.warn("Failed to convert field to object.", exception);
					
					String errorMessage = i18n.string("create-dialog.error.dialog.message", i18n.string("logger.table.column." + bindableColumn.getColumnName()), exception.getLocalizedMessage());
					JOptionPane.showMessageDialog(this, errorMessage, i18n.string("create-dialog.error.dialog.title"), JOptionPane.ERROR_MESSAGE);
					
					return null;
				}
				
				field.set(instance, value);
			}
		}
		
		for (FullfilledEntry fullfilledEntry : fullfilledEntries) {
			BindableColumn bindableColumn = fullfilledEntry.getBindableColumn();
			Object value = fullfilledEntry.getValue();
			Field field = bindableColumn.getField();
			
			field.set(instance, value);
		}
		
		return instance;
	}
	
	/**
	 * Open a new {@link AddNewDialog} instance.
	 * 
	 * @param parent
	 *            Parent {@link JFrame}.
	 * @param modelClass
	 *            Model class of the item.
	 * @param callback
	 *            Callback for knowing when an item has been created.
	 * @see AddNewDialog#open(JFrame, Class, Callback, List)
	 */
	public static void open(JFrame parent, Class<?> modelClass, Callback callback) {
		open(parent, modelClass, callback, null);
	}
	
	/**
	 * Open a new {@link AddNewDialog} instance.
	 * 
	 * @param parent
	 *            Parent {@link JFrame}.
	 * @param modelClass
	 *            Model class of the item.
	 * @param callback
	 *            Callback for knowing when an item has been created.
	 * @param fullfilledEntries
	 *            Already full-filled entries {@link List list}.
	 * @see AddNewDialog#open(JFrame, Class, Callback)
	 */
	public static void open(JFrame parent, Class<?> modelClass, Callback callback, List<FullfilledEntry> fullfilledEntries) {
		AddNewDialog dialog = new AddNewDialog(parent, modelClass, callback, fullfilledEntries);
		
		dialog.setVisible(true);
	}
	
	/**
	 * {@link AddNewDialog}'s callback.
	 *
	 * @author Enzo CACERES
	 */
	public interface Callback {
		
		/**
		 * Called when an item has been created and is valid.
		 * 
		 * @param modelClass
		 *            Item model class.
		 * @param instance
		 *            Created instance.
		 */
		public void onCreatedItem(Class<?> modelClass, Object instance);
		
	}
	
	public static class FullfilledEntry {
		
		/* Variables */
		private final BindableColumn bindableColumn;
		private final Object value;
		
		/* Constructor */
		public FullfilledEntry(BindableColumn bindableColumn, Object value) {
			this.bindableColumn = bindableColumn;
			this.value = value;
		}
		
		/** @return {@link FullfilledEntry}'s {@link BindableColumn}. */
		public BindableColumn getBindableColumn() {
			return bindableColumn;
		}
		
		/** @return {@link FullfilledEntry}'s filled value. */
		public Object getValue() {
			return value;
		}
		
		/**
		 * Find a {@link FullfilledEntry} instance in a {@link List list} if the {@link BindableColumn} matches.
		 * 
		 * @param fullfilledEntries
		 *            {@link List} of {@link FullfilledEntry} to search into.
		 * @param bindableColumn
		 *            {@link BindableColumn} to match.
		 * @return Matched {@link FullfilledEntry} or <code>null</code> if not found.
		 */
		public static FullfilledEntry find(List<FullfilledEntry> fullfilledEntries, BindableColumn bindableColumn) {
			for (FullfilledEntry fullfilledEntry : fullfilledEntries) {
				if (fullfilledEntry.getBindableColumn().equals(bindableColumn)) {
					return fullfilledEntry;
				}
			}
			
			return null;
		}
		
	}
	
}