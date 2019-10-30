package caceresenzo.apps.itemlogger.export.implementations.pdf;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import caceresenzo.apps.itemlogger.builder.NegroPdfBuilder;
import caceresenzo.apps.itemlogger.export.DataExporter;
import caceresenzo.apps.itemlogger.managers.DataManager;
import caceresenzo.apps.itemlogger.utils.Utils;
import caceresenzo.frameworks.database.binder.BindableColumn;
import caceresenzo.frameworks.database.binder.BindableTable;
import caceresenzo.frameworks.settings.SettingEntry;
import caceresenzo.libs.internationalization.i18n;

public class PdfDataExporter extends NegroPdfBuilder implements DataExporter {
	
	/* Constants */
	public static final int FONT_SIZE_ROWS = (int) (FONT_SIZE * 0.9);
	
	public static final float PADDING_COLUMNS = 4;
	
	/* Settings */
	public static final boolean SHRINK_INTEGER_COLUMN = true;
	
	@Override
	public void exportToFile(List<SettingEntry<Boolean>> settingEntries, File file) throws Exception {
		List<BindableTable> bindableTables = new ArrayList<>(DataManager.get().getTableCreator().getBindables().values());
		
		prepareNewDocument();
		
		Iterator<BindableTable> bindableTableIterator = bindableTables.iterator();
		while (bindableTableIterator.hasNext()) {
			BindableTable bindableTable = bindableTableIterator.next();
			
			String key = Utils.formatModelClassSettingEntryKey(bindableTable.getModelClass());
			boolean modelIsEnabled = false;
			for (SettingEntry<Boolean> entry : settingEntries) {
				if (entry.getKey().equals(key) && Boolean.TRUE.equals(entry.getValue())) {
					modelIsEnabled = true;
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
				PDPage page = createPage(false);
				PDRectangle mediaBox = page.getMediaBox();
				
				final float maxX = mediaBox.getWidth() - PAGE_MARGIN_HORIZONTAL;
				currentY = (mediaBox.getHeight() - PAGE_MARGIN_VERTICAL);
				
				try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {
					printHeader(contentStream, mediaBox, bindableTable, logoImage);
					currentY -= FONT_SIZE * 3.5f;
					
					List<PdfDataExporter.Column> columns = computeColumnsWidths(bindableTable, mediaBox, FONT_SIZE);
					
					int size = columns.size();
					float columnBarThickness = 1.2f;
					
					/* Printing columns */
					float usedX = 0.0f;
					for (int index = 0; index < size; index++) {
						PdfDataExporter.Column column = columns.get(index);
						boolean isLast = index == size - 1;
						float xStart = usedX + PAGE_MARGIN_HORIZONTAL;
						float xEnd = xStart + column.getWidth();
						
						printSimpleText(contentStream, xStart + PADDING_COLUMNS, currentY - FONT_SIZE, FONT_SIZE, column.getTranslation());
						
						if (!isLast) {
							printSimpleVerticalLine(contentStream, xEnd, currentY, minY, columnBarThickness);
						}
						
						usedX += column.getWidth();
					}
					
					currentY -= FONT_SIZE * 1.5f;
					printSimpleHorizontalLine(contentStream, minX, maxX, currentY, columnBarThickness);
					
					/* Printing values */
					while (modelInstanceListIterator.hasNext()) {
						Object modelInstance = modelInstanceListIterator.next();
						float totalUsedY = 0.0f;
						
						usedX = 0.0f;
						for (int index = 0; index < size; index++) {
							PdfDataExporter.Column column = columns.get(index);
							BindableColumn bindableColumn = column.getBindableColumn();
							float xStart = usedX + PAGE_MARGIN_HORIZONTAL;
							float xEnd = xStart + column.getWidth();
							float availableWidth = xEnd - xStart;
							
							float usedY = FONT_SIZE;
							
							Object rawValue;
							String text;
							
							try {
								rawValue = bindableColumn.getField().get(modelInstance);
								
								text = Utils.toAbsolutlySimpleString(rawValue, true, true);
							} catch (Exception exception) {
								throw new IllegalStateException(exception);
							}
							
							/* Handling long text on multiple lines */
							if (text != null) {
								if (rawValue instanceof Integer) { /* Centering int values */
									float valueWidth = computeStringWidth(text, FONT_SIZE_ROWS);
									float x = (availableWidth / 2f) - (valueWidth / 2f);
									
									printSimpleText(contentStream, xStart + x, currentY - usedY, FONT_SIZE_ROWS, text);
								} else {
									List<String> fitLines = fitLines(text, FONT_SIZE_ROWS, availableWidth, PADDING_COLUMNS);
									
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
							}
							
							if (usedY > totalUsedY) {
								totalUsedY = usedY;
							}
							
							usedX += column.getWidth();
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
	 * Print the current {@link BindableTable bindable table} of this page in the header. This function also add today's date, a logo and a line under the text.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param mediaBox
	 *            {@link PDPage Page}'s {@link PDRectangle bound}.
	 * @param bindableTable
	 *            {@link BindableTable}'s instance to get title from.
	 * @param logo
	 *            Logo to display at the top right of the page.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 */
	protected void printHeader(PDPageContentStream contentStream, PDRectangle mediaBox, BindableTable bindableTable, PDImageXObject logo) throws IOException {
		String tableTranslatedName = i18n.string("logger.panel.data.title.with.part." + bindableTable.getModelClass().getSimpleName().toLowerCase());
		String todayDate = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG));
		
		float xStart = PAGE_MARGIN_HORIZONTAL;
		float xEnd = mediaBox.getWidth() - PAGE_MARGIN_HORIZONTAL;
		float y = mediaBox.getHeight() - PAGE_MARGIN_VERTICAL - FONT_SIZE;
		
		y -= printSimpleText(contentStream, xStart, y, FONT_SIZE * 1.5f, tableTranslatedName);
		y -= FONT_SIZE * 0.4f;
		printSimpleText(contentStream, xStart, y, FONT_SIZE, todayDate);
		
		printLogo(contentStream, mediaBox, y, logo);
		
		printSimpleHorizontalLine(contentStream, xStart, xEnd, y - (FONT_SIZE * 0.4f));
	}
	
	/**
	 * Compute all {@link BindableColumn} of a {@link BindableTable} and set custom, fixed, width for some columns.<br>
	 * For exemple, if the setting {@link #SHRINK_INTEGER_COLUMN} is set to <code>true</code>, {@link Integer}'s column will have a width set to the width of the max integer OR the width or the column's translation depending on which one is the longest.
	 * 
	 * @param bindableTable
	 *            Source {@link BindableTable} to compute column width.
	 * @param mediaBox
	 *            {@link PDPage Page}'s {@link PDRectangle bound}.
	 * @param fontSize
	 *            Font size to compute the text width with.
	 * @return A {@link List list} of {@link PdfDataExporter.Column} with optimimal width.
	 */
	private List<PdfDataExporter.Column> computeColumnsWidths(BindableTable bindableTable, PDRectangle mediaBox, float fontSize) {
		List<BindableColumn> bindableColumns = bindableTable.getBindableColumns();
		List<PdfDataExporter.Column> columns = new ArrayList<>();
		
		Map<BindableColumn, PdfDataExporter.Column> alreadyComputedMap = new HashMap<>();
		float usableWidth = mediaBox.getWidth() - PAGE_MARGIN_HORIZONTAL * 2f;
		
		if (SHRINK_INTEGER_COLUMN) {
			float maxIntegerWidth = computeStringWidth(String.valueOf(Integer.MAX_VALUE), fontSize);
			
			for (BindableColumn bindableColumn : bindableColumns) {
				Class<?> fieldType = bindableColumn.getField().getType();
				
				if (Integer.class.equals(fieldType) || int.class.equals(fieldType)) {
					float columnNameWidth = computeStringWidth(PdfDataExporter.Column.translate(bindableColumn), fontSize);
					float columnWidth = Math.max(columnNameWidth, maxIntegerWidth) + (PADDING_COLUMNS * 2f);
					
					alreadyComputedMap.put(bindableColumn, new PdfDataExporter.Column(columnWidth, bindableColumn));
					usableWidth -= columnWidth;
				}
			}
		}
		
		int remainingColumnCount = bindableColumns.size() - alreadyComputedMap.size();
		float columnWidth = usableWidth / remainingColumnCount;
		
		for (BindableColumn bindableColumn : bindableColumns) {
			PdfDataExporter.Column column = alreadyComputedMap.getOrDefault(bindableColumn, new PdfDataExporter.Column(columnWidth, bindableColumn));
			
			columns.add(column);
		}
		
		return columns;
	}
	
	private static final class Column {
		
		/* Variables */
		private float width;
		private BindableColumn bindableColumn;
		
		/* Constructor */
		public Column(float width, BindableColumn bindableColumn) {
			this.width = width;
			this.bindableColumn = bindableColumn;
		}
		
		/** @return Column's width. */
		public float getWidth() {
			return width;
		}
		
		/** @return Column's original {@link BindableColumn}. */
		public BindableColumn getBindableColumn() {
			return bindableColumn;
		}
		
		/**
		 * @return The column translation.
		 * @see PdfDataExporter.Column#translate(BindableColumn) Get a column translation.
		 */
		public String getTranslation() {
			return translate(bindableColumn);
		}
		
		/**
		 * Get a column's translation.
		 * 
		 * @param bindableColumn
		 *            Target {@link BindableColumn} to get translation with.
		 * @return Column's translation.
		 */
		public static final String translate(BindableColumn bindableColumn) {
			return i18n.string("logger.table.column." + bindableColumn.getColumnName());
		}
		
	}
	
}