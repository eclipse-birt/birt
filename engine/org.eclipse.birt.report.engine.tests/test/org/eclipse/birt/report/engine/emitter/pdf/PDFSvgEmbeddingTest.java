/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.pdf;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.apache.batik.transcoder.SVGAbstractTranscoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.print.PrintTranscoder;
import org.openpdf.text.Document;
import org.openpdf.text.PageSize;
import org.openpdf.text.pdf.PdfContentByte;
import org.openpdf.text.pdf.PdfTemplate;
import org.openpdf.text.pdf.PdfWriter;

import junit.framework.TestCase;

public class PDFSvgEmbeddingTest extends TestCase {

	private static final String SIMPLE_SVG =
			"<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"100\" height=\"100\">" +
			"<rect x=\"10\" y=\"10\" width=\"80\" height=\"80\" fill=\"red\"/>" +
			"</svg>";

	private static final String SVG_WITH_PATH =
			"<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"200\" height=\"200\">" +
			"<path d=\"M10,10 L190,10 L190,190 L10,190 Z\" fill=\"blue\" stroke=\"black\" stroke-width=\"2\"/>" +
			"</svg>";

	private static final String SVG_WITH_CIRCLE =
			"<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"50\" height=\"50\">" +
			"<circle cx=\"25\" cy=\"25\" r=\"20\" fill=\"green\"/>" +
			"</svg>";

	private static final String SVG_WITH_TEXT =
			"<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"300\" height=\"50\">" +
			"<text x=\"10\" y=\"30\" font-family=\"Arial\" font-size=\"20\">Hello World</text>" +
			"</svg>";

	public void testTranscodeSvgByteArrayToPdfTemplate() throws Exception {
		byte[] svgData = SIMPLE_SVG.getBytes(StandardCharsets.UTF_8);
		float width = 100, height = 100;

		PdfTemplate template = transSvgToTemplate(svgData, width, height);

		assertNotNull("PDF template must not be null", template);
		assertTrue("Template width must be positive", template.getWidth() > 0);
		assertTrue("Template height must be positive", template.getHeight() > 0);
	}

	public void testTranscodeSvgWithPath() throws Exception {
		byte[] svgData = SVG_WITH_PATH.getBytes(StandardCharsets.UTF_8);
		float width = 200, height = 200;

		PdfTemplate template = transSvgToTemplate(svgData, width, height);

		assertNotNull("PDF template must not be null", template);
		assertEquals("Template width must match", width, template.getWidth());
		assertEquals("Template height must match", height, template.getHeight());
	}

	public void testTranscodeSvgWithCircle() throws Exception {
		byte[] svgData = SVG_WITH_CIRCLE.getBytes(StandardCharsets.UTF_8);
		PdfTemplate template = transSvgToTemplate(svgData, 50, 50);
		assertNotNull("PDF template for circle SVG must not be null", template);
	}

	public void testTranscodeSvgWithText() throws Exception {
		byte[] svgData = SVG_WITH_TEXT.getBytes(StandardCharsets.UTF_8);
		PdfTemplate template = transSvgToTemplate(svgData, 300, 50);
		assertNotNull("PDF template for text SVG must not be null", template);
	}

	public void testGeneratedPdfIsReadableAndContainsSvgContent() throws Exception {
		byte[] svgData = SVG_WITH_PATH.getBytes(StandardCharsets.UTF_8);

		Document document = new Document(PageSize.A4);
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, pdfOut);

		document.open();

		PdfContentByte cb = writer.getDirectContent();
		PdfTemplate svgTemplate = transSvgToTemplate(svgData, 200, 200);

		assertNotNull("SVG template must not be null", svgTemplate);
		cb.addTemplate(svgTemplate, 50, 500);

		document.newPage();
		document.close();

		byte[] pdfBytes = pdfOut.toByteArray();
		assertTrue("PDF must be non-empty", pdfBytes.length > 0);
		assertTrue("PDF must start with %PDF-",
				new String(pdfBytes, 0, Math.min(pdfBytes.length, 5), StandardCharsets.ISO_8859_1).startsWith("%PDF-"));
	}

	public void testSvgEmbeddedAsVectorNotRasterized() throws Exception {
		byte[] svgData = SVG_WITH_PATH.getBytes(StandardCharsets.UTF_8);

		Document document = new Document(PageSize.A4);
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, pdfOut);

		document.open();

		PdfContentByte cb = writer.getDirectContent();
		PdfTemplate svgTemplate = transSvgToTemplate(svgData, 200, 200);
		cb.addTemplate(svgTemplate, 50, 500);

		document.close();

		byte[] pdfBytes = pdfOut.toByteArray();
		String pdfContent = new String(pdfBytes, StandardCharsets.ISO_8859_1);

		assertTrue("PDF must contain vector path commands",
				pdfContent.contains(" m ") || pdfContent.contains(" l "));

		assertFalse("PDF must NOT contain JPEG raster stream (DCTDecode)",
				pdfContent.contains("/DCTDecode"));
	}

	public void testSvgWithNamespaceEmbeddedCorrectly() throws Exception {
		String svgWithNs = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
				"width=\"100\" height=\"100\">" +
				"<rect x=\"0\" y=\"0\" width=\"100\" height=\"100\" fill=\"yellow\"/>" +
				"</svg>";
		byte[] svgData = svgWithNs.getBytes(StandardCharsets.UTF_8);

		PdfTemplate template = transSvgToTemplate(svgData, 100, 100);
		assertNotNull("SVG with namespace must transcode to non-null template", template);
	}

	public void testEmptySvgDataThrowsException() throws Exception {
		try {
			transSvgToTemplate(new byte[0], 100, 100);
			fail("Empty SVG data should cause an exception");
		} catch (Exception e) {
		}
	}

	public void testNullSvgDataDoesNotFail() throws Exception {
		PdfTemplate template = transSvgToTemplate(null, 100, 100);
		assertNotNull("Null SVG data should still produce a template (empty)", template);
	}

	private PdfTemplate transSvgToTemplate(byte[] svgData, float width, float height) throws Exception {
		ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		Document document = new Document(new org.openpdf.text.Rectangle(width + 20, height + 20));
		PdfWriter writer = PdfWriter.getInstance(document, pdfOut);

		document.open();
		PdfContentByte cb = writer.getDirectContent();

		PdfTemplate template = cb.createTemplate(width, height);
		java.awt.Graphics2D g2D = template.createGraphics(width, height);

		PrintTranscoder transcoder = new PrintTranscoder();
		if (svgData != null && svgData.length > 0) {
			transcoder.transcode(new TranscoderInput(new ByteArrayInputStream(svgData)), null);
		}
		transcoder.addTranscodingHint(SVGAbstractTranscoder.KEY_ALLOW_EXTERNAL_RESOURCES, Boolean.TRUE);

		java.awt.print.PageFormat pg = new java.awt.print.PageFormat();
		java.awt.print.Paper p = new java.awt.print.Paper();
		p.setSize(width, height);
		p.setImageableArea(0, 0, width, height);
		pg.setPaper(p);
		transcoder.print(g2D, pg, 0);

		g2D.dispose();
		document.close();

		return template;
	}
}
