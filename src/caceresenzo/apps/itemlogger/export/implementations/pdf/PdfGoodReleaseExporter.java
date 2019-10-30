package caceresenzo.apps.itemlogger.export.implementations.pdf;

import java.io.File;
import java.util.List;

import caceresenzo.apps.itemlogger.configuration.Config;
import caceresenzo.apps.itemlogger.configuration.Language;
import caceresenzo.apps.itemlogger.export.GoodReleaseExporter;
import caceresenzo.apps.itemlogger.export.implementations.pdf.base.BasePdfGoodReleaseAndReceiptSlipExporter;
import caceresenzo.apps.itemlogger.export.physical.GoodRelease;
import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.apps.itemlogger.managers.ItemLoggerManager;
import caceresenzo.apps.itemlogger.models.Lend;
import caceresenzo.apps.itemlogger.utils.Utils;
import caceresenzo.libs.internationalization.i18n;

public class PdfGoodReleaseExporter extends BasePdfGoodReleaseAndReceiptSlipExporter<GoodRelease> implements GoodReleaseExporter {
	
	@Override
	public void exportToFile(GoodRelease goodRelease, File file) throws Exception {
		createPdf(goodRelease, file);
	}
	
	@Override
	public void bindValues(GoodRelease goodRelease) {
		bind("item", goodRelease.getItem().describeSimply());
		bind("person", goodRelease.getPerson().describe());
		bind("construction-site", goodRelease.getConstructionSite().describe());
		bind("quantity", String.valueOf(goodRelease.getQuantity()));
		bind("date", Utils.localDateToPickerFormat(goodRelease.getLendDate()));
		bind("extra", goodRelease.getLend().getExtra());
	}

	@Override
	protected String getHeaderTitle() {
		return i18n.string("pdf.header.title.good-release");
	}
	
	public static void main(String[] args) throws Exception {
		Config.get();
		Language.get().initialize();
		ItemLoggerManager.get().initialize();
		
		List<Lend> lends = DataManager.get().getDatabaseSynchronizer().load(Lend.class);
		File targetFile = new File("test.pdf");
		
		new PdfGoodReleaseExporter().exportToFile(new GoodRelease(lends.get(3)), targetFile);
		Runtime.getRuntime().exec("cmd /c \"" + targetFile.getAbsolutePath() + "\"");
	}
	
}