package caceresenzo.apps.itemlogger.ui.export.implementations;

import java.awt.FileDialog;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import caceresenzo.apps.itemlogger.export.DataExporter;
import caceresenzo.apps.itemlogger.export.implementations.pdf.PdfDataExporter;
import caceresenzo.apps.itemlogger.ui.export.AbstractExportToDialog;
import caceresenzo.frameworks.settings.SettingEntry;
import caceresenzo.libs.internationalization.i18n;
import caceresenzo.libs.os.OS;
import caceresenzo.libs.os.OSUtils;

public class ExportToPdfDialog extends AbstractExportToDialog {
	
	/* Variables */
	private File targetFile;
	
	/* Constructor */
	public ExportToPdfDialog(JFrame parent, String filterText) {
		super(parent, filterText, AbstractExportToDialog.ExportMode.PDF);
	}
	
	@Override
	protected void handleBrowse() {
		openFileSelector();
	}
	
	@Override
	protected void handleExport(List<SettingEntry<Boolean>> settingEntries, String filterText) throws Exception {
		DataExporter dataExporter = new PdfDataExporter();
		dataExporter.setFilter(filterText);
		
		dataExporter.exportToFile(settingEntries, targetFile);
		
		if (OSUtils.checkOSType().equals(OS.WINDOWS)) {
			String absolutePath = targetFile.getAbsolutePath();
			int reply = JOptionPane.showConfirmDialog(this, i18n.string("export-dialog.dialog.open-file.message", absolutePath), i18n.string("export-dialog.dialog.open-file.title"), JOptionPane.YES_NO_OPTION);
			
			if (reply == JOptionPane.YES_OPTION) {
				Runtime.getRuntime().exec("cmd /c \"" + absolutePath + "\"");
			}
		}
	}
	
	/** Open the file selector and update the path text field's text with the selected file's path. */
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
			updatePathTextField(targetFile.getAbsolutePath());
		}
	}
	
	/**
	 * Open a new {@link ExportToPdfDialog} instance.
	 * 
	 * @param parent
	 *            Parent {@link JFrame}.
	 * @param filterText
	 *            Initial filtering text.
	 */
	public static void open(JFrame parent, String filterText) {
		AbstractExportToDialog dialog = new ExportToPdfDialog(parent, filterText);
		
		dialog.setVisible(true);
	}
	
}