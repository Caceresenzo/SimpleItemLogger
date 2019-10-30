package caceresenzo.apps.itemlogger.export.implementations.pdf.builder;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import caceresenzo.apps.itemlogger.assets.Assets;
import caceresenzo.frameworks.pdf.PdfBuilder;

public class NegroPdfBuilder extends PdfBuilder {
	
	/* Variables */
	protected PDImageXObject logoImage;
	
	@Override
	protected void prepareNewDocument() throws IOException {
		super.prepareNewDocument();
		
		loadLogo();
	}
	
	/**
	 * Load the Negro LOGO into memory.
	 * 
	 * @throws IOException
	 *             If anything goes wrong when doing I/O operation.
	 */
	public void loadLogo() throws IOException {
		BufferedImage logoBufferedImage = ImageIO.read(getClass().getResourceAsStream(Assets.LOGO_NEGRO));
		logoImage = LosslessFactory.createFromImage(document, logoBufferedImage);
	}
	
	/**
	 * Same as {@link #printLogo(PDPageContentStream, PDRectangle, float, PDImageXObject)}, but with the logo parameter already been fulfilled.
	 * 
	 * @param contentStream
	 *            The {@link PDPage page}'s writable steam.
	 * @param mediaBox
	 *            {@link PDPage Page}'s {@link PDRectangle bound}.
	 * @param y
	 *            Y position to draw the logo.
	 * @throws IOException
	 *             If there is an error writing to the stream.
	 * @see #printLogo(PDPageContentStream, PDRectangle, float, PDImageXObject) Print any logo.
	 */
	protected void printLogo(PDPageContentStream contentStream, PDRectangle mediaBox, float y) throws IOException {
		printLogo(contentStream, mediaBox, y, logoImage);
	}
	
}