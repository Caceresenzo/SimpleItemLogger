package caceresenzo.frameworks.pdf;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import caceresenzo.frameworks.assets.FrameworkAssets;
import caceresenzo.libs.internationalization.i18n;

public class PdfBuilder {
	
	/* Constants */
	public static final int PAGE_MARGIN_HORIZONTAL = 30;
	public static final int PAGE_MARGIN_VERTICAL = 30;
	
	public static final int FONT_SIZE = 12;
	
	public static final int CARD_MARGIN = 8;
	public static final int CARD_TITLE_MARGIN = 8;
	public static final int CARD_CONTENT_MARGIN = 10;
	public static final int CARD_TITLE_FONT_SIZE = (int) (FONT_SIZE * 0.8f);
	public static final int CARD_CONTENT_FONT_SIZE = FONT_SIZE;
	
	/* Variables */
	protected PDDocument document;
	protected PDFont font;
	protected PDPage lastestPage;
	
	/**
	 * Create a {@link PDDocument} and store it in protected class variable.
	 * 
	 * @return The instance just created.
	 * @throws IllegalStateException
	 *             If a document has already been created.
	 */
	protected PDDocument createDocument() {
		if (document != null) {
			throw new IllegalStateException("Cannot create multiple document.");
		}
		
		return document = new PDDocument();
	}
	
	/**
	 * Load the <code>Consola</code> font and store it in a protected class variable.
	 * 
	 * @throws IOException
	 *             If there is an error reading the font stream.
	 */
	protected void loadFont() throws IOException {
		font = PDType0Font.load(document, PdfBuilder.class.getResourceAsStream(FrameworkAssets.FRAMEWORK_FONT_CONSOLA));
	}
	
	/**
	 * Prepare the document.<br>
	 * This mean calling {@link #createDocument()} and {@link #loadFont()}.
	 * 
	 * @throws IOException
	 *             If there is an error reading the font stream.
	 */
	protected void prepareNewDocument() throws IOException {
		createDocument();
		loadFont();
	}
	
	/**
	 * Create a new {@link PDPage page} and automatically add it to the current {@link PDDocument document}.
	 * 
	 * @return The {@link PDPage page} just created.
	 */
	protected PDPage createPage(boolean landscape) {
		PDPage page = lastestPage = !landscape ? new PDPage() : new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
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
	protected float computeStringWidth(String string) {
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
	protected float computeStringWidth(String string, float fontSize) {
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
	protected void printSimpleHorizontalLine(PDPageContentStream contentStream, float x1, float x2, float y) throws IOException {
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
	protected void printSimpleHorizontalLine(PDPageContentStream contentStream, float x1, float x2, float y, float thickness) throws IOException {
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
	protected void printSimpleVerticalLine(PDPageContentStream contentStream, float x, float y1, float y2) throws IOException {
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
	protected void printSimpleVerticalLine(PDPageContentStream contentStream, float x, float y1, float y2, float thickness) throws IOException {
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
	 * @return How much Y unit has been consummed.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 */
	protected int printSimpleText(PDPageContentStream contentStream, float x, float y, float fontSize, String text) throws IOException {
		int usedY = 0;
		
		contentStream.beginText();
		contentStream.setFont(font, fontSize);
		contentStream.setLeading(fontSize);
		contentStream.newLineAtOffset(x, y);
		
		String[] lines = text.split("\n");
		for (int index = 0; index < lines.length; index++) {
			contentStream.showText(lines[index]);
			
			if (index != lines.length) {
				contentStream.newLine();
				usedY += FONT_SIZE;
			}
		}
		
		contentStream.endText();
		
		return usedY;
	}
	
	/**
	 * Print a logo at the top right of the page.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param mediaBox
	 *            {@link PDPage Page}'s {@link PDRectangle bound}.
	 * @param y
	 *            Y position to draw the logo.
	 * @param logo
	 *            Logo to display at the top right of the page.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 */
	protected void printLogo(PDPageContentStream contentStream, PDRectangle mediaBox, float y, PDImageXObject logo) throws IOException {
		float xEnd = mediaBox.getWidth() - PAGE_MARGIN_HORIZONTAL;
		
		float newHeight = FONT_SIZE * 2.5f;
		float newWidth = logo.getWidth() * (newHeight / logo.getHeight());
		
		contentStream.drawImage(logo, xEnd - newWidth, y, newWidth, newHeight);
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
	protected void printFooter(PDPageContentStream contentStream, PDRectangle mediaBox) throws IOException {
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
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 */
	protected void printCurrentPageNumber(PDPageContentStream contentStream, PDRectangle mediaBox, int pageCount, int currentPageNumber) throws IOException {
		float baseY = PAGE_MARGIN_VERTICAL / 1.5f;
		float fontSize = FONT_SIZE * 1.0f;
		
		String pageNumber = String.format("%s/%s", currentPageNumber, pageCount);
		
		float textWidth = computeStringWidth(pageNumber, fontSize);
		float x = (mediaBox.getWidth() - textWidth) - PAGE_MARGIN_HORIZONTAL;
		float y = baseY;
		
		printSimpleText(contentStream, x, y, fontSize, pageNumber);
	}
	
	/**
	 * Compute a {@link List list} of line which has been correctly separated to ensure the fitting of the text according to some constraint.
	 * 
	 * @param text
	 *            Source text to fit.
	 * @param fontSize
	 *            Font size to compute text width with.
	 * @param availableWidth
	 *            Available width to work with.
	 * @param padding
	 *            Right padding of the text.
	 * @return A {@link List list} of {@link String} containing the original source text but correctly split to fit the constraint.
	 */
	protected List<String> fitLines(String text, int fontSize, float availableWidth, float padding) {
		String[] lines = text.split("\n");
		List<String> fitLines = new ArrayList<>();
		
		for (String line : lines) {
			String[] words = line.split(" ");
			String currentLine = null;
			
			for (String word : words) {
				/* Short the word if it is too long for the line */
				boolean hasBeenCut = false;
				while (computeStringWidth(word, fontSize) >= availableWidth) {
					word = word.substring(0, word.length() - 2);
					hasBeenCut = true;
				}
				
				if (hasBeenCut) {
					word = word.substring(0, Math.max(0, word.length() - 6));
					word += "[...]";
				}
				
				/* Store line in a list line-based */
				if (currentLine == null) {
					currentLine = word;
				} else {
					String concat = currentLine + " " + word;
					
					if (computeStringWidth(concat, fontSize) < availableWidth - padding) {
						currentLine = concat;
					} else {
						fitLines.add(currentLine);
						currentLine = word;
					}
				}
			}
			
			if (currentLine != null) {
				fitLines.add(currentLine);
			}
		}
		
		return fitLines;
	}
	
	/**
	 * Print a card on the PDF. The card height will automatically be augmented if the content text need more space to be print. <br>
	 * It will look like this:<br>
	 * 
	 * <pre>
	 * + - - title - - - - - +
	 * | content             |
	 * + - - - - - - - - - - +
	 * </pre>
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.<br>
	 *            Can be <code>null</code>, if it is the case, nothing will be printed and this method will only return the computed used Y pixel value.
	 * @param title
	 *            Card's title.
	 * @param content
	 *            Card's content.
	 * @param minX
	 *            Card's minimal X value. (where to start)
	 * @param maxX
	 *            Card's maxmial X value. (where to end)
	 * @param currentY
	 *            Current Y position to print the card.
	 * @return Used Y pixel.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 * @see {@link #fitLines(String, int, float, float)} Compute text height.
	 */
	protected float printCard(PDPageContentStream contentStream, String title, String content, float minX, float maxX, float currentY) throws IOException {
		float consumedY = 0f;
		
		final float cardMinX = minX + CARD_MARGIN;
		final float cardBeforeTitleMaxX = cardMinX + PAGE_MARGIN_HORIZONTAL;
		final float cardMaxX = maxX - CARD_MARGIN;
		final List<String> fitLines = fitLines(content, CARD_CONTENT_FONT_SIZE, cardMaxX - cardMinX - CARD_CONTENT_MARGIN, CARD_CONTENT_MARGIN);
		final float cardHeight = (CARD_CONTENT_FONT_SIZE * fitLines.size()) + (CARD_CONTENT_MARGIN * 2f);
		final float titleTextWidth = computeStringWidth(title, CARD_TITLE_FONT_SIZE);
		final float endCurrentY = currentY - cardHeight;
		
		if (contentStream != null) {
			printSimpleHorizontalLine(contentStream, cardMinX, cardBeforeTitleMaxX, currentY);
			printSimpleText(contentStream, cardBeforeTitleMaxX + CARD_TITLE_MARGIN, currentY - (CARD_TITLE_FONT_SIZE * 0.3f), CARD_TITLE_FONT_SIZE, title);
			printSimpleHorizontalLine(contentStream, cardBeforeTitleMaxX + titleTextWidth + (CARD_TITLE_MARGIN * 2), cardMaxX, currentY);
			
			printSimpleVerticalLine(contentStream, cardMinX, currentY, endCurrentY);
			printSimpleVerticalLine(contentStream, cardMaxX, currentY, endCurrentY);
			
			printSimpleHorizontalLine(contentStream, cardMinX, cardMaxX, endCurrentY);
			
			contentStream.setNonStrokingColor(Color.BLUE);
			for (int index = 0; index < fitLines.size(); index++) {
				String line = fitLines.get(index);
				
				printSimpleText(contentStream, cardMinX + CARD_CONTENT_MARGIN, currentY - (CARD_CONTENT_FONT_SIZE * (index + 1)) - CARD_CONTENT_MARGIN, CARD_CONTENT_FONT_SIZE, line);
			}
			contentStream.setNonStrokingColor(Color.BLACK);
		}
		
		consumedY += CARD_TITLE_FONT_SIZE;
		consumedY += cardHeight;
		
		return consumedY;
	}
	
	/**
	 * Save the {@link PDDocument} to a {@link File} and close the object.
	 * 
	 * @param file
	 *            Destination file to save to.
	 * @throws IOException
	 *             If the output could not be written.
	 */
	protected void finishDocument(File file) throws IOException {
		document.save(file);
		document.close();
	}
	
}