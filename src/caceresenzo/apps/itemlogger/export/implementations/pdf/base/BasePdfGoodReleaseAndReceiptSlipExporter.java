package caceresenzo.apps.itemlogger.export.implementations.pdf.base;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import caceresenzo.apps.itemlogger.builder.NegroPdfBuilder;
import caceresenzo.libs.internationalization.i18n;
import caceresenzo.libs.string.StringUtils;

public abstract class BasePdfGoodReleaseAndReceiptSlipExporter<T> extends NegroPdfBuilder {
	
	/* Constants */
	public static final int FONT_SIZE_TITLE = (FONT_SIZE * 3);
	
	/* Variables */
	private final Map<String, String> valueMap;
	
	/* Constructor */
	public BasePdfGoodReleaseAndReceiptSlipExporter() {
		this.valueMap = new LinkedHashMap<>();
	}
	
	@Override
	protected void prepareNewDocument() throws IOException {
		super.prepareNewDocument();
		
		valueMap.clear();
	}
	
	/**
	 * Do the value binding for the card.
	 * 
	 * @param data
	 *            Original data.
	 */
	public abstract void bindValues(T data);
	
	/**
	 * Bind some data with a key and a value.
	 * 
	 * @param key
	 *            Key to bind.
	 * @param value
	 *            Value to bind.
	 */
	protected void bind(String key, String value) {
		valueMap.put(key, value);
	}
	
	/**
	 * Create the PDF file.
	 * 
	 * @param data
	 *            Data to export.
	 * @param file
	 *            Target file destination.
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	protected void createPdf(T data, File file) throws Exception {
		prepareNewDocument();
		
		bindValues(data);
		
		PDPage page = createPage(false);
		PDRectangle mediaBox = page.getMediaBox();
		
		final float minX = PAGE_MARGIN_HORIZONTAL;
		final float maxX = mediaBox.getWidth() - PAGE_MARGIN_HORIZONTAL;
		
		float currentY = (mediaBox.getHeight() - PAGE_MARGIN_VERTICAL);
		
		try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {
			currentY -= printHeader(contentStream, mediaBox, minX, maxX, currentY);
			
			currentY -= FONT_SIZE;
			
			for (Entry<String, String> entry : valueMap.entrySet()) {
				String i18nKey = "pdf.card.title." + entry.getKey();
				String value = entry.getValue();
				
				currentY -= printCard(contentStream, i18n.string(i18nKey), value, minX, maxX, currentY);
			}
			
			printDateAndSignAndExtraCards(contentStream, minX, maxX);
		}
		
		finishDocument(file);
	}
	
	/**
	 * Print a header containing the Negro logo and a title.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param mediaBox
	 *            {@link PDPage Page}'s {@link PDRectangle bound}.
	 * @param minX
	 *            Minimum X value used to print the text and draw the line.
	 * @param maxX
	 *            Maximum X value used to draw the line.
	 * @param currentY
	 *            Current Y position.
	 * @return Used Y pixel.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 * @see #getHeaderTitle() Getting the title.
	 */
	protected float printHeader(PDPageContentStream contentStream, PDRectangle mediaBox, float minX, float maxX, float currentY) throws IOException {
		float startCurrentY = currentY;
		
		currentY -= FONT_SIZE_TITLE;
		printSimpleText(contentStream, minX, currentY, FONT_SIZE_TITLE, getHeaderTitle());
		printLogo(contentStream, mediaBox, currentY);
		
		currentY -= FONT_SIZE;
		printSimpleHorizontalLine(contentStream, minX, maxX, currentY);
		
		return startCurrentY - currentY;
	}
	
	/**
	 * Draw a "date and sign" and a "extra" card at the bottom of the page.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param minX
	 *            Minimum X value used to draw the cards.
	 * @param maxX
	 *            Maximum X value used to draw the cards.
	 * @return
	 *         Current Y position after drawing cards.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 */
	protected float printDateAndSignAndExtraCards(PDPageContentStream contentStream, float minX, float maxX) throws IOException {
		String empty = StringUtils.multiplySequence("·\n", 3);
		final float cardHeight = printCard(null, "", empty, minX, maxX, 0);
		float currentY = PAGE_MARGIN_VERTICAL + (cardHeight * 2);
		
		currentY -= printCard(contentStream, i18n.string("pdf.card.title.negro.date-and-sign"), empty, minX, maxX, currentY);
		currentY -= printCard(contentStream, i18n.string("pdf.card.title.negro.extra"), empty, minX, maxX, currentY);
		
		return currentY;
	}
	
	/** @return Page's heading title. */
	protected abstract String getHeaderTitle();
	
}