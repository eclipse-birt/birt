/*******************************************************************************
 * Copyright (c) 2025 Thomas Gutmann
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Thomas Gutmann  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.layout.pdf.font;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.openpdf.text.Document;
import org.openpdf.text.DocumentException;
import org.openpdf.text.Font;
import org.openpdf.text.FontFactory;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.BaseFont;
import org.openpdf.text.pdf.LayoutProcessor;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfWriter;

/**
 * This is a test class to create and execute manual tests for the combination
 * of font handling and pdf configuration based on the layout processor which is
 * used by openPDF. The primary target is to test the different options of
 * kerning and ligatures. The class is not established to instantiate JUnits
 * tests. This class support the creation of further test examples and the
 * results must be validated manually.
 *
 * @since 3.3
 *
 */
public class FontHandlingPdfTest {

	/**
	 * Main method to start the creation of the demo pdf documents
	 *
	 * @param args argument
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] args) throws DocumentException, IOException {

		createPDFLigaturePara("pflicht - wo spacing", "C:/temp/pdf_para_ligatures_enabled.pdf");

		createPdfLigatureCB("pflicht", "C:/temp/pdf_cb_ligatures_enabled.pdf", "enableKernAndLig");

		createPdfLigatureCB("pflicht", "C:/temp/pdf_cb_ligatures_disabled.pdf", "disableKernAndLig");

		createPdfLigatureCB("pflicht", "C:/temp/pdf_cb_layout_processor_disabled.pdf", "disableLayoutProcessor");

		System.out.println("PDF generated successfully!");
	}

	private static String baseFontName = "C:/temp/Fonts/calibri.ttf";

	/**
	 * Set font name, which can be name or full font path
	 *
	 * @param font font name or full font path
	 */
	public static void setFont(String font) {
		baseFontName = font;
	}

	/**
	 * Get the font name
	 *
	 * @return font name
	 */
	public static String getFont() {
		return baseFontName;
	}

	/**
	 * Create a base font from the main class BaseFont
	 */
	private static BaseFont getBaseFontCreated() throws IOException {
		return BaseFont.createFont(getFont(), BaseFont.IDENTITY_H, true);
	}

	/**
	 * Fetch the base font from the font factory
	 */
	private static BaseFont getBaseFontFontFactory() {
		return FontFactory.getFont(getFont(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, 0).getBaseFont();
	}

	/**
	 * Create demo pdf to test ligature handling based on paragraph
	 *
	 * @param docText    demo text for the document
	 * @param exportPath export path of the pdf file
	 * @throws IOException
	 * @throws DocumentException
	 *
	 */
	public static void createPDFLigaturePara(String docText, String exportPath) throws IOException, DocumentException {

		// Output PDF file
		Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream(exportPath));
		} catch (DocumentException | FileNotFoundException e) {
			e.printStackTrace();
		}

		document.open();

		BaseFont bf = getBaseFontCreated();
		Font font = new Font(bf, 14);
		if (!LayoutProcessor.isEnabled()) {
			LayoutProcessor.enableKernLiga();
		}
		document.add(new Paragraph(docText, font));

		document.close();
	}

	/**
	 * Create demo pdf to test ligature handling based on content byte
	 *
	 * @param docText      demo text for the document
	 * @param exportPath   export path of the pdf file
	 * @param handlingMode mode of layout processor configuration
	 *
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static void createPdfLigatureCB(String docText, String exportPath, String handlingMode)
			throws IOException, DocumentException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(exportPath));
		document.open();
		PdfContentByte cb = writer.getDirectContent();

		BaseFont bf = getBaseFontFontFactory();
		bf.setIncludeCidSet(true);
		float x = 36;
		float y = 750;

		// Set character spacing to 5 points
		cb.saveState();
		cb.setCharacterSpacing(5);
		cb.setColorFill(Color.BLACK);
		cb.concatCTM(1, 0, 0, 1, 20, 20);
		cb.beginText();
		cb.setFontAndSize(bf, 20);
		cb.setTextMatrix(x, y + 20);

		if (!LayoutProcessor.isEnabled()) {
			if (handlingMode.equals("enableKernAndLig")) {
				LayoutProcessor.enableKernLiga();

			} else if (handlingMode.equals("disableKernAndLig")) {
				LayoutProcessor.enable(0);

			} else if (handlingMode.equals("disableLayoutProcessor")) {
				LayoutProcessor.enableKernLiga();
			}
		}
		cb.showText(docText);
		cb.endText();
		cb.restoreState();

		document.close();
	}
}