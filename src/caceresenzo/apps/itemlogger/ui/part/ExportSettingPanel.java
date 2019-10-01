package caceresenzo.apps.itemlogger.ui.part;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import caceresenzo.frameworks.settings.SettingEntry;
import caceresenzo.libs.internationalization.i18n;

public class ExportSettingPanel extends JPanel implements ActionListener, SettingEntry.Aware<Boolean> {
	
	/* Serialization */
	private static final long serialVersionUID = -6002514162938940714L;

	/* Action Commands */
	public static final String ACTION_COMMAND_TOGGLE = "action_toggle";
	
	/* Colors */
	public static final Color COLOR_ENABLED = Color.GREEN;
	public static final Color COLOR_DISABLE = Color.RED;
	
	/* UI */
	private JTextField textField;
	private JButton toggleButton;
	private JPanel currentStatePanel;
	
	/* Variables */
	private final String key;
	private boolean enabledState;
	
	/* Constructor */
	public ExportSettingPanel(String key, String display, boolean defaultEnabledState) {
		this.key = key;
		this.enabledState = !defaultEnabledState; /* Will be re-inversed with the toggle */
		
		setMaximumSize(new Dimension(32767, 50));
		setMinimumSize(new Dimension(10, 50));
		setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		textField = new JTextField();
		textField.setText(String.valueOf(display).toUpperCase());
		textField.setEditable(false);
		textField.setColumns(10);
		
		toggleButton = new JButton("E/D");
		toggleButton.setActionCommand(ACTION_COMMAND_TOGGLE);
		toggleButton.addActionListener(this);
		
		currentStatePanel = new JPanel();
		currentStatePanel.setBorder(new LineBorder(UIManager.getColor("scrollbar")));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(textField, GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(toggleButton, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(currentStatePanel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(currentStatePanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
										.addComponent(textField, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
										.addComponent(toggleButton, GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
								.addContainerGap()));
		GroupLayout gl_currentStatePanel = new GroupLayout(currentStatePanel);
		gl_currentStatePanel.setHorizontalGroup(
				gl_currentStatePanel.createParallelGroup(Alignment.LEADING)
						.addGap(0, 10, Short.MAX_VALUE));
		gl_currentStatePanel.setVerticalGroup(
				gl_currentStatePanel.createParallelGroup(Alignment.LEADING)
						.addGap(0, 10, Short.MAX_VALUE));
		currentStatePanel.setLayout(gl_currentStatePanel);
		setLayout(groupLayout);
		
		toggleEnabledState();
	}
	
	/** Toggle the enabled state, and so, update the current state panel's color and the button's text. */
	public void toggleEnabledState() {
		enabledState = !enabledState;
		
		toggleButton.setText(i18n.string("export-dialog.button.toggle." + (enabledState ? "enabled" : "disabled")));
		currentStatePanel.setBackground(enabledState ? COLOR_ENABLED : COLOR_DISABLE);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();
		
		switch (actionCommand) {
			case ACTION_COMMAND_TOGGLE: {
				toggleEnabledState();
				break;
			}
			
			default: {
				throw new IllegalStateException("Unknown action command: " + actionCommand);
			}
		}
	}
	
	@Override
	public SettingEntry<Boolean> toSettingEntry() {
		return new SettingEntry<>(key, enabledState);
	}
	
}