package caceresenzo.apps.itemlogger.export.pdf;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import caceresenzo.apps.itemlogger.configuration.Config;
import caceresenzo.apps.itemlogger.configuration.Language;
import caceresenzo.apps.itemlogger.export.DataExporter;
import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.apps.itemlogger.managers.ItemLoggerManager;
import caceresenzo.apps.itemlogger.utils.Utils;
import caceresenzo.frameworks.assets.FrameworkAssets;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.binder.BindableTable;
import caceresenzo.frameworks.settings.SettingEntry;
import caceresenzo.libs.internationalization.i18n;

public class PdfDataExporter implements DataExporter {
	
	/* Constants */
	public static final int PAGE_MARGIN_HORIZONTAL = 30;
	public static final int PAGE_MARGIN_VERTICAL = 30;
	
	public static final int FONT_SIZE = 12;
	public static final int FONT_SIZE_ROWS = (int) (FONT_SIZE * 0.9);
	
	public static final float PADDING_COLUMNS = 4;
	
	/* Variables */
	private PDDocument document;
	private PDFont font;
	private PDPage lastestPage;
	
	public static void main(String[] args) throws Exception {
		Config.get();
		Language.get().initialize();
		ItemLoggerManager.get().initialize();
		
		// for (int index = 0; index < 50; index++) {
		// Item item = new Item(0, "item-" + ((char) (index + 65)), index, 0);
		//
		// DataManager.get().getDatabaseSynchronizer().insert(Item.class, item);
		// }
		
		File targetFile = new File("test.pdf");
		
		new PdfDataExporter().exportToFile(new ArrayList<>(), targetFile);
		Runtime.getRuntime().exec("cmd /c \"" + targetFile.getAbsolutePath() + "\"");
	}
	
	@Override
	public void exportToFile(List<SettingEntry<Boolean>> settingEntries, File file) throws Exception {
		List<BindableTable> bindableTables = new ArrayList<>(DataManager.get().getTableCreator().getBindables().values());
		
		prepareNewDocument();
		
		Iterator<BindableTable> bindableTableIterator = bindableTables.iterator();
		while (bindableTableIterator.hasNext()) {
			BindableTable bindableTable = bindableTableIterator.next();
			
			String key = Utils.formatModelClassSettingEntryKey(bindableTable.getModelClass());
			boolean modelIsEnabled = true;
			for (SettingEntry<Boolean> entry : settingEntries) {
				if (entry.getKey().equals(key) && Boolean.FALSE.equals(entry.getValue())) {
					modelIsEnabled = false;
					break;
				}
			}
			
			if (!modelIsEnabled) {
				continue;
			}
			
			List<BindableColumn> bindableColumns = bindableTable.getBindableColumns();
			
			BindableColumn idBindableColumn = BindableColumn.findIdColumn(bindableColumns);
			bindableColumns.remove(idBindableColumn);
			
			List<?> modelInstances = DataManager.get().getDatabaseSynchronizer().load(bindableTable.getModelClass());
			ListIterator<?> modelInstanceListIterator = modelInstances.listIterator();
			
			final float minX = PAGE_MARGIN_HORIZONTAL;
			final float minY = PAGE_MARGIN_VERTICAL * 2f;
			float currentY = Float.MAX_VALUE;
			
			while (modelInstanceListIterator.hasNext()) {
				PDPage page = createPage();
				PDRectangle mediaBox = page.getMediaBox();
				
				final float maxX = mediaBox.getWidth() - PAGE_MARGIN_HORIZONTAL;
				currentY = (mediaBox.getHeight() - PAGE_MARGIN_VERTICAL);
				
				try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {
					printHeader(contentStream, mediaBox, bindableTable);
					currentY -= FONT_SIZE * 2.5f;
					
					int size = bindableColumns.size();
					float columnBarThickness = 1.2f;
					float colunmWidth = (mediaBox.getWidth() - PAGE_MARGIN_HORIZONTAL * 2f) / size;
					
					/* Printing columns */
					for (int index = 0; index < size; index++) {
						BindableColumn bindableColumn = bindableColumns.get(index);
						boolean isLast = index == size - 1;
						float xStart = colunmWidth * index + PAGE_MARGIN_HORIZONTAL;
						float xEnd = xStart + colunmWidth;
						
						String translatedColumn = i18n.string("logger.table.column." + bindableColumn.getColumnName());
						printSimpleText(contentStream, xStart + PADDING_COLUMNS, currentY - FONT_SIZE, FONT_SIZE, translatedColumn);
						
						if (!isLast) {
							printSimpleVerticalLine(contentStream, xEnd, currentY, minY, columnBarThickness);
						}
					}
					
					currentY -= FONT_SIZE * 1.5f;
					printSimpleHorizontalLine(contentStream, minX, maxX, currentY, columnBarThickness);
					
					/* Printing values */
					while (modelInstanceListIterator.hasNext()) {
						Object modelInstance = modelInstanceListIterator.next();
						float totalUsedY = 0.0f;
						
						for (int index = 0; index < size; index++) {
							BindableColumn bindableColumn = bindableColumns.get(index);
							float xStart = colunmWidth * index + PAGE_MARGIN_HORIZONTAL;
							float xEnd = xStart + colunmWidth;
							float availableWidth = xEnd - xStart;
							
							float usedY = FONT_SIZE;
							
							String text;
							
							try {
								text = Utils.toAbsolutlySimpleString(bindableColumn.getField().get(modelInstance), true, true);
							} catch (Exception exception) {
								throw new IllegalStateException(exception);
							}
							
							/* Handling long text on multiple lines */
							if (text != null) {
								String[] lines = text.split(" ");
								List<String> fitLines = new ArrayList<>();
								
								String currentLine = null;
								for (String line : lines) {
									if (currentLine == null) {
										currentLine = line;
									} else {
										String concat = currentLine + " " + line;
										
										if (computeStringWidth(concat, FONT_SIZE_ROWS) < availableWidth - PADDING_COLUMNS) {
											currentLine = concat;
										} else {
											fitLines.add(currentLine);
											currentLine = line;
										}
									}
								}
								
								if (currentLine != null) {
									fitLines.add(currentLine);
								}
								
								int lineCount = fitLines.size();
								for (int jndex = 0; jndex < lineCount; jndex++) {
									String line = fitLines.get(jndex);
									boolean isLast = jndex == lineCount - 1;
									
									printSimpleText(contentStream, xStart + PADDING_COLUMNS, currentY - usedY, FONT_SIZE_ROWS, line);
									
									if (!isLast) {
										usedY += FONT_SIZE_ROWS;
										usedY += PADDING_COLUMNS / 2f;
									}
								}
							}
							
							if (usedY > totalUsedY) {
								totalUsedY = usedY;
							}
						}
						
						currentY -= totalUsedY;
						currentY -= PADDING_COLUMNS * 1.5f;
						
						if (currentY - (FONT_SIZE_ROWS + PADDING_COLUMNS) < minY) {
							break;
						}
						
						if (modelInstanceListIterator.hasNext()) {
							printSimpleHorizontalLine(contentStream, minX, maxX, currentY, 0.5f);
						}
					}
					
					printFooter(contentStream, mediaBox);
				}
			}
		}
		
		/* Printing page count */
		PDPageTree pageTree = document.getPages();
		int pageCount = pageTree.getCount();
		
		for (int index = 0; index < pageCount; index++) {
			PDPage page = pageTree.get(index);
			PDRectangle mediaBox = page.getMediaBox();
			
			try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {
				printCurrentPageNumber(contentStream, mediaBox, pageCount, index + 1);
			}
		}
		
		finishDocument(file);
	}
	
	/**
	 * Create a {@link PDDocument} and store it in private class variable.
	 * 
	 * @return The instance just created.
	 */
	private PDDocument createDocument() {
		return document = new PDDocument();
	}
	
	/**
	 * Load the <code>Consola</code> font and store it in a private class variable.
	 * 
	 * @throws IOException
	 *             If there is an error reading the font stream.
	 */
	private void loadFont() throws IOException {
		font = PDType0Font.load(document, PdfDataExporter.class.getResourceAsStream(FrameworkAssets.FRAMEWORK_FONT_CONSOLA));
	}
	
	/**
	 * Prepare the document.<br>
	 * This mean calling {@link #createDocument()} and {@link #loadFont()}.
	 * 
	 * @throws IOException
	 *             If there is an error reading the font stream.
	 */
	private void prepareNewDocument() throws IOException {
		createDocument();
		loadFont();
	}
	
	/**
	 * Create a new {@link PDPage page} and automatically add it to the current {@link PDDocument document}.
	 * 
	 * @return The {@link PDPage page} just created.
	 */
	private PDPage createPage() {
		PDPage page = lastestPage = new PDPage();
		document.addPage(page);
		
		return page;
	}
	
	/**
	 * Compute a string width with the default font size.
	 * 
	 * @param string
	 *            String to compute.
	 * @return A number of "unit" which correspond to the length of the string.
	 * @see #computeStringWidth(String, int) Compute a string width with a custom font size.
	 * @see #FONT_SIZE Default font size.
	 */
	private float computeStringWidth(String string) {
		return computeStringWidth(string, FONT_SIZE);
	}
	
	/**
	 * Compute a string width by a font size.
	 * 
	 * @param string
	 *            String to compute.
	 * @param fontSize
	 *            Font's size to compute with.
	 * @return A number of "unit" which correspond to the length of the string.
	 */
	private float computeStringWidth(String string, float fontSize) {
		return string.length() * (font.getAverageFontWidth() / 1000) * fontSize;
	}
	
	/**
	 * Print a simple horizontal line.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param x1
	 *            X start position.
	 * @param x2
	 *            X end position.
	 * @param y
	 *            Horizontal y.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 * @see #printSimpleHorizontalLine(PDPageContentStream, float, float, float, float) Print a horizontal line with a specified thickness.
	 */
	private void printSimpleHorizontalLine(PDPageContentStream contentStream, float x1, float x2, float y) throws IOException {
		printSimpleHorizontalLine(contentStream, x1, x2, y, -1);
	}
	
	/**
	 * Print a simple horizontal line with a specified <code>thickness</code>.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param x1
	 *            X start position.
	 * @param x2
	 *            X end position.
	 * @param y
	 *            Horizontal y.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 * @see #printSimpleHorizontalLine(PDPageContentStream, float, float, float) Print a horizontal line without specifying a thickness.
	 */
	private void printSimpleHorizontalLine(PDPageContentStream contentStream, float x1, float x2, float y, float thickness) throws IOException {
		if (thickness > 0) {
			contentStream.setLineWidth(thickness);
		}
		
		contentStream.moveTo(x1, y);
		contentStream.lineTo(x2, y);
		contentStream.stroke();
	}
	
	/**
	 * Print a simple vertical line.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param x
	 *            Vertical x.
	 * @param y1
	 *            Y start position.
	 * @param y2
	 *            Y end position.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 * @see #printSimpleVerticalLine(PDPageContentStream, float, float, float, float) Print a vertical line with a specified thickness.
	 */
	private void printSimpleVerticalLine(PDPageContentStream contentStream, float x, float y1, float y2) throws IOException {
		printSimpleVerticalLine(contentStream, x, y1, y2, -1);
	}
	
	/**
	 * Print a simple vertical line with a specified <code>thickness</code>.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param x
	 *            Vertical x.
	 * @param y1
	 *            Y start position.
	 * @param y2
	 *            Y end position.
	 * @param thickness
	 *            Line's thickness.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 * @see #printSimpleVerticalLine(PDPageContentStream, float, float, float) Print a vertical line without specifying a thickness.
	 */
	private void printSimpleVerticalLine(PDPageContentStream contentStream, float x, float y1, float y2, float thickness) throws IOException {
		if (thickness > 0) {
			contentStream.setLineWidth(thickness);
		}
		
		contentStream.moveTo(x, y1);
		contentStream.lineTo(x, y2);
		contentStream.stroke();
	}
	
	/**
	 * Print a simple text at a specified coordinate.<br>
	 * The text will be split in lines that will be each rendered one after the other.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param x
	 *            X position to print the text.
	 * @param y
	 *            Y position to print the text.
	 * @param fontSize
	 *            Specified font size.
	 * @param text
	 *            Text to print.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 */
	private void printSimpleText(PDPageContentStream contentStream, float x, float y, float fontSize, String text) throws IOException {
		contentStream.beginText();
		contentStream.setFont(font, fontSize);
		contentStream.setLeading(fontSize);
		contentStream.newLineAtOffset(x, y);
		
		String[] lines = text.split("\n");
		for (int index = 0; index < lines.length; index++) {
			contentStream.showText(lines[index]);
			
			if (index != lines.length) {
				contentStream.newLine();
			}
		}
		
		contentStream.endText();
	}
	
	/**
	 * Print the current {@link BarReference bar reference} of this page in the header. This function also add a line under the text.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param mediaBox
	 *            {@link PDPage Page}'s {@link PDRectangle bound}.
	 * @param barReference
	 *            Target {@link BarReference bar reference} used to print this page.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 */
	private void printHeader(PDPageContentStream contentStream, PDRectangle mediaBox, BindableTable bindableTable) throws IOException {
		String tableTranslatedName = i18n.string("logger.panel.data.title.with.part." + bindableTable.getModelClass().getSimpleName().toLowerCase());
		
		float xStart = PAGE_MARGIN_HORIZONTAL;
		float xEnd = mediaBox.getWidth() - PAGE_MARGIN_HORIZONTAL;
		float y = mediaBox.getHeight() - PAGE_MARGIN_VERTICAL - FONT_SIZE;
		
		printSimpleText(contentStream, xStart, y, FONT_SIZE * 1.5f, tableTranslatedName);
		printSimpleHorizontalLine(contentStream, xStart, xEnd, (float) (mediaBox.getHeight() - PAGE_MARGIN_VERTICAL - (FONT_SIZE * 1.4f)));
		
		String todayDate = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
		float todayDateWidth = computeStringWidth(todayDate);
		
		printSimpleText(contentStream, xEnd - todayDateWidth, y, FONT_SIZE, todayDate);
	}
	
	/**
	 * Print the copyright in the footer.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param mediaBox
	 *            {@link PDPage Page}'s {@link PDRectangle bound}.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 */
	private void printFooter(PDPageContentStream contentStream, PDRectangle mediaBox) throws IOException {
		float baseY = PAGE_MARGIN_VERTICAL / 1.5f;
		float fontSize = (float) (FONT_SIZE * 0.6);
		
		String[] lines = i18n.string("application.copyright.full").split("\n");
		for (int index = 0; index < lines.length; index++) {
			String line = lines[index];
			
			/* Approximately */
			float textWidth = line.length() * (font.getAverageFontWidth() / 1000) * fontSize;
			float x = (mediaBox.getWidth() - textWidth) / 2f;
			float y = baseY - (fontSize * index * 1.5f);
			
			printSimpleText(contentStream, x, y, fontSize, line);
		}
	}
	
	/**
	 * Print the current page out of the number of page in the footer.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param mediaBox
	 *            {@link PDPage Page}'s {@link PDRectangle bound}.
	 * @param pageCount
	 *            {@link PDDocument Document}'s total page count.
	 * @param currentPageNumber
	 *            Current page number.
	 * @throws IOExceptionIf
	 *             there is an error writing to the stream.
	 */
	private void printCurrentPageNumber(PDPageContentStream contentStream, PDRectangle mediaBox, int pageCount, int currentPageNumber) throws IOException {
		float baseY = PAGE_MARGIN_VERTICAL / 1.5f;
		float fontSize = FONT_SIZE * 1.0f;
		
		String pageNumber = String.format("%s/%s", currentPageNumber, pageCount);
		
		float textWidth = computeStringWidth(pageNumber, fontSize);
		float x = (mediaBox.getWidth() - textWidth) - PAGE_MARGIN_HORIZONTAL;
		float y = baseY;
		
		printSimpleText(contentStream, x, y, fontSize, pageNumber);
	}
	
	/**
	 * Save the {@link PDDocument} to a {@link File} and close the object.
	 * 
	 * @param file
	 *            Destination file to save to.
	 * @throws IOException
	 *             If the output could not be written.
	 */
	private void finishDocument(File file) throws IOException {
		document.save(file);
		document.close();
	}
	
}