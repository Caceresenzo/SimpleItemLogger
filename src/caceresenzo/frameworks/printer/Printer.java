package caceresenzo.frameworks.printer;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.print.PrintService;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

public class Printer {
	
	/* Variables */
	private PrintService printService;
	
	/** Open the printer selector. */
	public void openPrinterSelector() {
		PrinterJob printJob = PrinterJob.getPrinterJob();
		
		if (printJob.printDialog()) {
			printService = printJob.getPrintService();
		}
	}
	
	/**
	 * Do a print.
	 * 
	 * @param askPrinter
	 *            If a prompt to let the user choose a printer should be open or not.
	 * @param printAction
	 *            The {@link PrintAction} that is going to be print.
	 * @throws IOException
	 *             If anything goes wrong during an I/O operation.
	 * @throws PrinterException
	 *             If there is an error in the print system causing the job to be aborted.
	 */
	public void print(boolean askPrinter, PrintAction printAction) throws IOException, PrinterException {
		File temporaryFile = new File(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
		
		if (temporaryFile.exists()) {
			temporaryFile.delete();
		}
		
		printAction.createDocument(temporaryFile);
		
		if (askPrinter || printService == null) {
			openPrinterSelector();
		}
		
		if (printService != null) {
			PDDocument document = PDDocument.load(temporaryFile);
			
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setPrintService(printService);
			job.setPageable(new PDFPageable(document));
			job.print();
		}
	}
	
	public interface PrintAction {
		
		/**
		 * Write all the data that are going to be printer in the gaven file.
		 * 
		 * @param file
		 *            Target, temporary, file to write data.
		 */
		public void createDocument(File file);
		
	}
	
}