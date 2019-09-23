package caceresenzo.apps.itemlogger.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
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

import caceresenzo.apps.itemlogger.ui.part.FieldPartPanel;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.setup.TableAnalizer;
import caceresenzo.libs.internationalization.i18n;

public class AddNewDialog extends JDialog implements ActionListener {
	
	/* Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(AddNewDialog.class);
	
	/* Action Commands */
	public static final String ACTION_COMMAND_DONE = "action_done";
	public static final String ACTION_COMMAND_CANCEL = "action_cancel";
	
	/* UI */
	private final JPanel contentPanel = new JPanel();
	
	/* Variables */
	private final Class<?> modelClass;
	private final Callback callback;
	private final List<BindableColumn> bindableColumns;
	private JPanel fieldListPanel;
	
	/* Constructor */
	public AddNewDialog(JFrame parent, Class<?> modelClass, Callback callback) {
		super(parent);
		
		this.modelClass = modelClass;
		this.callback = callback;
		
		this.bindableColumns = TableAnalizer.get().analizeColumns(modelClass);
		this.bindableColumns.remove(BindableColumn.findIdColumn(bindableColumns));
		
		setSize(700, 400);
		setModal(true);
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBorder(new TitledBorder(null, "New", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
		
		bindableColumns.forEach((bindableColumn) -> fieldListPanel.add(new FieldPartPanel(modelClass, bindableColumn, null)));
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
	 * @return Created instance from field, or <code>null</code> if an error append in the {@link FieldPartPanel#getObject()} method.
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	public Object createInstanceFromField() throws Exception {
		Object instance = modelClass.getConstructors()[0].newInstance();
		
		for (Component component : fieldListPanel.getComponents()) {
			if (component instanceof FieldPartPanel) {
				FieldPartPanel fieldPartPanel = (FieldPartPanel) component;
				BindableColumn bindableColumn = fieldPartPanel.getBindableColumn();
				Field field = bindableColumn.getField();
				
				Object value;
				try {
					value = fieldPartPanel.getObject();
				} catch (Exception exception) {
					LOGGER.warn("Failed to convert field to object.", exception);
					JOptionPane.showMessageDialog(this, "Failed\n" + exception.getLocalizedMessage());
					return null;
				}
				
				field.set(instance, value);
			}
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
	 */
	public static void open(JFrame parent, Class<?> modelClass, Callback callback) {
		AddNewDialog dialog = new AddNewDialog(parent, modelClass, callback);
		
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
	
}