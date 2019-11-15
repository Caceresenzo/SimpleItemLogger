package caceresenzo.apps.itemlogger.ui.export;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caceresenzo.apps.itemlogger.configuration.Constants;
import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.apps.itemlogger.ui.part.ExportSettingPanel;
import caceresenzo.apps.itemlogger.utils.Utils;
import caceresenzo.frameworks.database.binder.BindableTable;
import caceresenzo.frameworks.database.setup.TableCreator;
import caceresenzo.frameworks.settings.SettingEntry;
import caceresenzo.libs.internationalization.i18n;
import caceresenzo.libs.string.StringUtils;

import javax.swing.JCheckBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public abstract class AbstractExportToDialog extends JDialog implements Constants, ActionListener, ItemListener {
	
	/* Logger */
	private static Logger LOGGER = LoggerFactory.getLogger(AbstractExportToDialog.class);
	
	/* Action Commands */
	public static final String ACTION_COMMAND_BROWSE_FILE = "action_browse";
	public static final String ACTION_COMMAND_DO_EXPORT = "action_export";
	public static final String ACTION_COMMAND_CANCEL = "action_cancel";
	public static final String ACTION_COMMAND_CLOSE = "action_close";
	
	/* UI */
	protected final JPanel contentPanel = new JPanel();
	protected JPanel settingListPanel, filePanel, buttonPane;
	protected JScrollPane settingListScrollPane;
	protected JTextField exportPathTextField;
	protected JButton browseButton, doExportButton, cancelButton;
	private JTextField filterTextField;
	private JCheckBox filterEnableCheckBox;
	
	/* Constructor */
	public AbstractExportToDialog(JFrame parent, String filterText, AbstractExportToDialog.ExportMode exportMode) {
		super(parent);
		
		setSize(700, 500);
		setModal(true);
		setTitle(i18n.string(exportMode.getWindowTitleKey()));
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setResizable(true);
		getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		filePanel = new JPanel();
		filePanel.setBorder(new TitledBorder(null, i18n.string(exportMode.getPathPanelKey()), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		settingListScrollPane = new JScrollPane();
		settingListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		settingListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		settingListScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		settingListScrollPane.setBorder(new TitledBorder(null, i18n.string("export-dialog.panel.settings.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, i18n.string("export-dialog.panel.filter.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(settingListScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
										.addComponent(filePanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
										.addComponent(panel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE))
								.addContainerGap()));
		gl_contentPanel.setVerticalGroup(
				gl_contentPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(settingListScrollPane, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(panel, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(filePanel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		
		filterTextField = new JTextField();
		filterTextField.setColumns(10);
		
		filterEnableCheckBox = new JCheckBox(i18n.string("export-dialog.checkbox.filter.text"));
		filterEnableCheckBox.addItemListener(this);
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
								.addContainerGap()
								.addComponent(filterTextField, GroupLayout.DEFAULT_SIZE, 553, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(filterEnableCheckBox)
								.addContainerGap()));
		gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(filterTextField, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
										.addComponent(filterEnableCheckBox))
								.addContainerGap(25, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		
		settingListPanel = new JPanel();
		settingListScrollPane.setViewportView(settingListPanel);
		settingListPanel.setLayout(new BoxLayout(settingListPanel, BoxLayout.Y_AXIS));
		
		exportPathTextField = new JTextField();
		exportPathTextField.setEditable(false);
		
		browseButton = new JButton(i18n.string(exportMode.getBrowseKey()));
		browseButton.setActionCommand(ACTION_COMMAND_BROWSE_FILE);
		GroupLayout gl_filePanel = new GroupLayout(filePanel);
		gl_filePanel.setHorizontalGroup(
				gl_filePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_filePanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(exportPathTextField, GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(browseButton, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		gl_filePanel.setVerticalGroup(
				gl_filePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_filePanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_filePanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(exportPathTextField, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
										.addComponent(browseButton, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
								.addContainerGap()));
		filePanel.setLayout(gl_filePanel);
		contentPanel.setLayout(gl_contentPanel);
		
		buttonPane = new JPanel();
		buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		doExportButton = new JButton(i18n.string(exportMode.getDoExportKey()));
		doExportButton.setEnabled(false);
		doExportButton.setActionCommand(ACTION_COMMAND_DO_EXPORT);
		
		cancelButton = new JButton(i18n.string("export-dialog.button.cancel"));
		cancelButton.setActionCommand(ACTION_COMMAND_CANCEL);
		
		buttonPane.add(doExportButton);
		buttonPane.add(cancelButton);
		
		getRootPane().setDefaultButton(browseButton);
		
		for (JButton button : Arrays.asList(browseButton, doExportButton, cancelButton)) {
			button.addActionListener(this);
		}
		
		initializeSettings();
		
		filterTextField.setText(filterText);
		
		filterEnableCheckBox.setSelected(StringUtils.validate(filterText));
		filterTextField.setEnabled(filterEnableCheckBox.isSelected());
	}
	
	/** Initialize the {@link ExportSettingPanel} list with the {@link TableCreator}'s loaded {@link BindableTable bindables}. */
	private void initializeSettings() {
		for (BindableTable bindableTable : DataManager.get().getTableCreator().getBindables().values()) {
			if (!bindableTable.isExportable()) {
				continue;
			}
			
			Class<?> modelClass = bindableTable.getModelClass();
			
			String key = Utils.formatModelClassSettingEntryKey(modelClass);
			String display = i18n.string("logger.panel.data.title.with.part." + modelClass.getSimpleName().toLowerCase());
			
			settingListPanel.add(new ExportSettingPanel(key, display, true));
		}
	}
	
	/** Called when the "browse" button has been clicked. */
	protected abstract void handleBrowse();
	
	/**
	 * Called when the user want to do the export.
	 * 
	 * @param settingEntries
	 *            Settings for the export.
	 * @param filterText
	 *            Filter text to apply to the exported content.
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	protected abstract void handleExport(List<SettingEntry<Boolean>> settingEntries, String filterText) throws Exception;
	
	/**
	 * Update the path text field and transfer the focus to the do export button.
	 * 
	 * @param text
	 *            Text to set to the path text field.
	 */
	protected void updatePathTextField(String text) {
		exportPathTextField.setText(text);
		doExportButton.setEnabled(true);
		browseButton.transferFocus();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();
		
		switch (actionCommand) {
			case ACTION_COMMAND_BROWSE_FILE: {
				handleBrowse();
				break;
			}
			
			case ACTION_COMMAND_DO_EXPORT: {
				List<SettingEntry<Boolean>> settingEntries = new ArrayList<>();
				int enabledCount = 0;
				
				for (Component component : settingListPanel.getComponents()) {
					if (component instanceof SettingEntry.Aware) {
						SettingEntry.Aware<Boolean> aware = (SettingEntry.Aware<Boolean>) component;
						SettingEntry<Boolean> settingEntry = aware.toSettingEntry();
						
						settingEntries.add(settingEntry);
						
						if (Boolean.TRUE.equals(settingEntry.getValue())) {
							enabledCount++;
						}
					}
				}
				
				if (enabledCount == 0) {
					JOptionPane.showMessageDialog(this, i18n.string("export-dialog.dialog.error.nothing-enabled.message"), i18n.string("export-dialog.dialog.error.nothing-enabled.title"), JOptionPane.ERROR_MESSAGE);
				} else {
					try {
						String filterText = filterTextField.getText();
						
						if (!filterEnableCheckBox.isEnabled() || !StringUtils.validate(filterText)) {
							filterText = null;
						}
						
						handleExport(settingEntries, filterText);
						
						actionPerformed(new ActionEvent(this, 0, ACTION_COMMAND_CLOSE));
					} catch (Exception exception) {
						LOGGER.warn("Failed to export.", exception);
						
						JOptionPane.showMessageDialog(this, i18n.string("export-dialog.dialog.error.export-error.message", exception.getLocalizedMessage()), i18n.string("export-dialog.dialog.error.export-error.title"), JOptionPane.ERROR_MESSAGE);
					}
				}
				
				break;
			}
			
			case ACTION_COMMAND_CANCEL:
			case ACTION_COMMAND_CLOSE: {
				setVisible(false);
				break;
			}
			
			default: {
				throw new IllegalStateException("Unknown action command: " + actionCommand);
			}
		}
	}
	
	@Override
	public void itemStateChanged(ItemEvent event) {
		Component source = (Component) event.getSource();
		
		if (source == filterEnableCheckBox) {
			filterTextField.setEnabled(filterEnableCheckBox.isSelected());
		} else {
			throw new IllegalStateException("Unknown item state changed source: " + source);
		}
	}
	
	public enum ExportMode {
		
		PDF("export-dialog.pdf.window.title", "export-dialog.panel.file.title", "export-dialog.button.browse", "export-dialog.pdf.button.do-export"), //
		PRINTER("export-dialog.print.window.title", "export-dialog.panel.print.title", "export-dialog.button.choose", "export-dialog.print.button.do-export");
		
		/* Translations */
		private final String windowTitleKey, pathPanelKey, browseKey, doExportKey;
		
		/* Constructor */
		private ExportMode(String windowTitleKey, String pathPanelKey, String browseKey, String doExportKey) {
			this.windowTitleKey = windowTitleKey;
			this.pathPanelKey = pathPanelKey;
			this.browseKey = browseKey;
			this.doExportKey = doExportKey;
		}
		
		/** @return Translation's key for the window's title of this {@link ExportMode}. */
		public String getWindowTitleKey() {
			return windowTitleKey;
		}
		
		/** @return Translation's key for the path panel of this {@link ExportMode}. */
		public String getPathPanelKey() {
			return pathPanelKey;
		}
		
		/** @return Translation's key for the browse button of this {@link ExportMode}. */
		public String getBrowseKey() {
			return browseKey;
		}
		
		/** @return Translation's key for the do export button of this {@link ExportMode}. */
		public String getDoExportKey() {
			return doExportKey;
		}
		
	}
	
}