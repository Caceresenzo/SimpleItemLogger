package caceresenzo.apps.itemlogger.ui;

import java.awt.BorderLayout;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import caceresenzo.apps.itemlogger.configuration.Constants;
import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.apps.itemlogger.ui.part.ExportSettingPanel;
import caceresenzo.apps.itemlogger.utils.Utils;
import caceresenzo.frameworks.database.binder.BindableTable;
import caceresenzo.frameworks.database.setup.TableCreator;
import caceresenzo.libs.internationalization.i18n;

public class ExportToPdfDialog extends JDialog implements Constants, ActionListener {
	
	/* Action Commands */
	public static final String ACTION_COMMAND_BROWSE_FILE = "action_browse";
	public static final String ACTION_COMMAND_DO_EXPORT = "action_export";
	public static final String ACTION_COMMAND_CANCEL = "action_cancel";
	
	/* UI */
	private final JPanel contentPanel;
	private JPanel settingListPanel, filePanel, buttonPane;
	private JScrollPane settingListScrollPane;
	private JTextField filePathTextField;
	private JButton browseButton, doExportButton, cancelButton;
	
	/* Variables */
	private File targetFile;
	
	/* Constructor */
	public ExportToPdfDialog(JFrame parent) {
		super(parent);
		
		this.contentPanel = new JPanel();
		
		setSize(700, 500);
		setModal(true);
		setTitle(i18n.string("export-dialog.window.title"));
		setMinimumSize(getSize());
		setLocationRelativeTo(null);
		setResizable(true);
		getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		filePanel = new JPanel();
		filePanel.setBorder(new TitledBorder(null, i18n.string("export-dialog.panel.file.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		settingListScrollPane = new JScrollPane();
		settingListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		settingListScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		settingListScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		settingListScrollPane.setBorder(new TitledBorder(null, i18n.string("export-dialog.panel.settings.title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_contentPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(settingListScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE)
										.addComponent(filePanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 654, Short.MAX_VALUE))
								.addContainerGap()));
		gl_contentPanel.setVerticalGroup(
				gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(settingListScrollPane, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(filePanel, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		
		settingListPanel = new JPanel();
		settingListScrollPane.setViewportView(settingListPanel);
		settingListPanel.setLayout(new BoxLayout(settingListPanel, BoxLayout.Y_AXIS));
		
		filePathTextField = new JTextField();
		filePathTextField.setEditable(false);
		
		browseButton = new JButton(i18n.string("export-dialog.button.browse"));
		browseButton.setActionCommand(ACTION_COMMAND_BROWSE_FILE);
		GroupLayout gl_filePanel = new GroupLayout(filePanel);
		gl_filePanel.setHorizontalGroup(
				gl_filePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_filePanel.createSequentialGroup()
								.addContainerGap()
								.addComponent(filePathTextField, GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(browseButton, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		gl_filePanel.setVerticalGroup(
				gl_filePanel.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_filePanel.createSequentialGroup()
								.addContainerGap()
								.addGroup(gl_filePanel.createParallelGroup(Alignment.BASELINE)
										.addComponent(filePathTextField, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
										.addComponent(browseButton, GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
								.addContainerGap()));
		filePanel.setLayout(gl_filePanel);
		contentPanel.setLayout(gl_contentPanel);
		
		buttonPane = new JPanel();
		buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		doExportButton = new JButton(i18n.string("export-dialog.button.do-export"));
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
	}
	
	/** Initialize the {@link ExportSettingPanel} list with the {@link TableCreator}'s loaded {@link BindableTable bindables}. */
	private void initializeSettings() {
		DataManager.get().getTableCreator().getBindables().values().forEach((bindableTable) -> {
			Class<?> modelClass = bindableTable.getModelClass();
			
			String key = Utils.formatModelClassSettingEntryKey(modelClass);
			String display = i18n.string("logger.panel.data.title.with.part." + modelClass.getSimpleName().toLowerCase());
			
			settingListPanel.add(new ExportSettingPanel(key, display, true));
		});
	}
	
	/** Open the file selector and update the file path text field's text with the selected file's path. */
	private void openFileSelector() {
		FileDialog fileDialog = new FileDialog(this, i18n.string("export-dialog.file-chooser.title"), FileDialog.SAVE);
		fileDialog.setLocationRelativeTo(null);
		fileDialog.setFile("*." + PDF_EXTENSION);
		fileDialog.setVisible(true);
		
		String directory = fileDialog.getDirectory();
		String filename = fileDialog.getFile();
		if (directory != null && filename != null) {
			if (!filename.endsWith("." + PDF_EXTENSION)) {
				filename += "." + PDF_EXTENSION;
			}
			
			targetFile = new File(directory, filename);
			filePathTextField.setText(targetFile.getAbsolutePath());
			doExportButton.setEnabled(true);
			browseButton.transferFocus();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		String actionCommand = event.getActionCommand();
		
		switch (actionCommand) {
			case ACTION_COMMAND_BROWSE_FILE: {
				openFileSelector();
				break;
			}
			
			case ACTION_COMMAND_DO_EXPORT: {
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
	
	/**
	 * Open a new {@link ExportToPdfDialog} instance.
	 * 
	 * @param parent
	 *            Parent {@link JFrame}.
	 */
	public static void open(JFrame parent) {
		ExportToPdfDialog dialog = new ExportToPdfDialog(parent);
		
		dialog.setVisible(true);
	}
	
}