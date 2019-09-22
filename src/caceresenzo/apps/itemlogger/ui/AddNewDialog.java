package caceresenzo.apps.itemlogger.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

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
		
		KeyListener keyListener = new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				super.keyPressed(keyEvent);
				
				if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
					// fireCallback();
				}
			}
		};
		
		bindableColumns.forEach((bindableColumn) -> fieldListPanel.add(new FieldPartPanel(modelClass, bindableColumn, keyListener)));
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
	
	public void fireCallback() {
		if (callback != null) {
			Object instance = null;
			
			try {
				instance = Objects.requireNonNull(createInstanceFromField(), "Instanced model can't be null.");
				
				setVisible(false);
			} catch (Exception exception) {
				LOGGER.info("Failed to create model instance from field.", exception);
			}
			
			callback.onCreatedItem(modelClass, instance);
		}
	}
	
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
					JOptionPane.showMessageDialog(this, "Failed");
					return null;
				}
				
				field.set(instance, value);
			}
		}
		
		return instance;
	}
	
	public static void open(JFrame parent, Class<?> modelClass, Callback callback) {
		AddNewDialog dialog = new AddNewDialog(parent, modelClass, callback);
		
		dialog.setVisible(true);
	}
	
	public interface Callback {
		
		public void onCreatedItem(Class<?> modelClass, Object instance);
		
	}
	
	public JPanel getPanel() {
		return fieldListPanel;
	}
}