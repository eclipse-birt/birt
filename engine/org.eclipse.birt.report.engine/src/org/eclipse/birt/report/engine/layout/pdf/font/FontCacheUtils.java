/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.font;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Locale;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * Font cache utils
 *
 * @since 3.3
 *
 */
public class FontCacheUtils {

	/**
	 * Main method of the class
	 *
	 * @param args
	 * @throws Exception
	 */
	static public void main(String[] args) throws Exception {
		createUnicodeText("unicode.txt");
		createUnicodePDF("pdf", Locale.CHINESE, "unicode.pdf");
	}

	// FontFactory.registerDirectory( "e:\\windows\\fonts\\" );
	// createCache( "Courier New", BaseFont.IDENTITY_H );

	static void createUnicodeText(String fileName) throws IOException {
		OutputStream out = new FileOutputStream(fileName);
		Writer writer = new OutputStreamWriter(out, "utf-8");
		for (int seg = 0; seg < 0xFF; seg++) {
			StringBuilder sb = new StringBuilder();
			sb.append(Integer.toHexString(seg * 0xFF)).append('\n');
			writer.write(sb.toString());
			for (int hi = 0; hi < 16; hi++) {
				sb.setLength(0);
				for (int lo = 0; lo < 16; lo++) {
					char ch = (char) (seg * 0xFF + hi * 16 + lo);
					if (Character.isISOControl(ch)) {
						ch = '?';
					}
					sb.append(ch).append(' ');
				}
				sb.append('\n');
				writer.write(sb.toString());
			}
		}
		writer.close();
	}

	static void createUnicodePDF(String format, Locale locale, String fileName) throws Exception {
		FontMappingManager manager = FontMappingManagerFactory.getInstance().getFontMappingManager(format, locale);

		// step 1: creation of a document-object
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fileName));
		document.open();
		for (int seg = 0; seg < 0xFF; seg++) {
			PdfContentByte cb = writer.getDirectContent();
			cb.beginText();
			for (int hi = 0; hi < 16; hi++) {
				for (int lo = 0; lo < 16; lo++) {
					int x = 100 + hi * 32;
					int y = 100 + lo * 32;
					char ch = (char) (seg * 0xFF + hi * 16 + lo);

					String fontFamily = manager.getDefaultPhysicalFont(ch);
					BaseFont bf = manager.createFont(fontFamily, Font.NORMAL);
					cb.setFontAndSize(bf, 16);
					cb.setTextMatrix(x, y);
					cb.showText(new String(new char[] { ch }));
				}
			}
			cb.endText();
		}
		document.close();
	}

	static void createFontIndex(String fontName, String encoding, Writer writer) throws Exception {
		BaseFont font = BaseFont.createFont(fontName, encoding, false);
		ArrayList<CharSegment> charSegs = new ArrayList<CharSegment>();
		int start = 0;
		int end = 0;
		for (char ch = 0; ch < 0xFFFF; ch++) {
			if (font.charExists(ch)) {
				if (start == -1) {
					start = ch;
				}
				end = ch;
			} else if (start != -1) {
				charSegs.add(new CharSegment(start, end, fontName));
				start = -1;
			}
		}
		if (start != -1) {
			charSegs.add(new CharSegment(start, end, fontName));
		}
		for (int i = 0; i < charSegs.size(); i++) {
			StringBuilder sb = new StringBuilder();
			sb.append("<block region-start=\"").append(start).append("\" region-end=\"").append(end).append("\"/>")
					.append('\n');
			writer.write(sb.toString());
		}
	}
}
