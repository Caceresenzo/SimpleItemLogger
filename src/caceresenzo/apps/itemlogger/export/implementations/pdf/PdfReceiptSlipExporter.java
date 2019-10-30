package caceresenzo.apps.itemlogger.export.implementations.pdf;

import java.io.File;
import java.util.List;

import caceresenzo.apps.itemlogger.configuration.Config;
import caceresenzo.apps.itemlogger.configuration.Language;
import caceresenzo.apps.itemlogger.export.ReceiptSlipExporter;
import caceresenzo.apps.itemlogger.export.implementations.pdf.base.BasePdfGoodReleaseAndReceiptSlipExporter;
import caceresenzo.apps.itemlogger.export.physical.ReceiptSlip;
import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.apps.itemlogger.managers.ItemLoggerManager;
import caceresenzo.apps.itemlogger.models.ReturnEntry;
import caceresenzo.apps.itemlogger.utils.Utils;
import caceresenzo.libs.internationalization.i18n;

public class PdfReceiptSlipExporter extends BasePdfGoodReleaseAndReceiptSlipExporter<ReceiptSlip> implements ReceiptSlipExporter {
	
	@Override
	public void exportToFile(ReceiptSlip receiptSlip, File file) throws Exception {
		createPdf(receiptSlip, file);
	}
	
	@Override
	public void bindValues(ReceiptSlip receiptSlip) {
		bind("item", receiptSlip.getLend().getItem().describeSimply());
		bind("person", receiptSlip.getLend().getPerson().describe());
		bind("construction-site", receiptSlip.getLend().getConstructionSite().describe());
		bind("quantity", String.valueOf(receiptSlip.getQuantity()));
		bind("date", Utils.localDateToPickerFormat(receiptSlip.getReturnDate()));
		bind("extra", receiptSlip.getReturnEntry().getExtra());
	}
	
	@Override
	protected String getHeaderTitle() {
		return i18n.string("pdf.header.title.receipt-slip");
	}
	
	public static void main(String[] args) throws Exception {
		Config.get();
		Language.get().initialize();
		ItemLoggerManager.get().initialize();
		
		List<ReturnEntry> returnEntries = DataManager.get().getDatabaseSynchronizer().load(ReturnEntry.class);
		File targetFile = new File("test.pdf");
		
		new PdfReceiptSlipExporter().exportToFile(new ReceiptSlip(returnEntries.get(0)), targetFile);
		Runtime.getRuntime().exec("cmd /c \"" + targetFile.getAbsolutePath() + "\"");
	}
	
}