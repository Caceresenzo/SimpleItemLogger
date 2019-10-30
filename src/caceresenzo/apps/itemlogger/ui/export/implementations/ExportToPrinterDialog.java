package caceresenzo.apps.itemlogger.ui.export.implementations;

import java.awt.print.PrinterJob;
import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.print.PrintService;
import javax.swing.JFrame;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import caceresenzo.apps.itemlogger.configuration.Constants;
import caceresenzo.apps.itemlogger.export.DataExporter;
import caceresenzo.apps.itemlogger.export.implementations.pdf.PdfDataExporter;
import caceresenzo.apps.itemlogger.ui.export.AbstractExportToDialog;
import caceresenzo.frameworks.settings.SettingEntry;

public class ExportToPrinterDialog extends AbstractExportToDialog implements Constants {
	
	/* Variables */
	private PrintService printService;
	
	/* Constructor */
	public ExportToPrinterDialog(JFrame parent) {
		super(parent, AbstractExportToDialog.ExportMode.PRINTER);
	}
	
	@Override
	protected void handleBrowse() {
		openPrinterSelector();
	}
	
	@Override
	protected void handleExport(List<SettingEntry<Boolean>> settingEntries) throws Exception {
		File temporaryFile = new File(System.getProperty("java.io.tmpdir"), String.format("%s.%s", UUID.randomUUID().toString(), PDF_EXTENSION));
		
		DataExporter dataExporter = new PdfDataExporter();
		dataExporter.exportToFile(settingEntries, temporaryFile);
		
		PDDocument document = PDDocument.load(temporaryFile);
		
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintService(printService);
		job.setPageable(new PDFPageable(document));
		job.print();
	}
	
	/** Open the printer selector and update the path text field's text with selected printer's name. */
	private void openPrinterSelector() {
		PrinterJob printJob = PrinterJob.getPrinterJob();
		
		if (printJob.printDialog()) {
			printService = printJob.getPrintService();
			
			updatePathTextField(printService.getName());
		}
	}
	
	/**
	 * Open a new {@link ExportToPrinterDialog} instance.
	 * 
	 * @param parent
	 *            Parent {@link JFrame}.
	 */
	public static void open(JFrame parent) {
		AbstractExportToDialog dialog = new ExportToPrinterDialog(parent);
		
		dialog.setVisible(true);
	}
	
}