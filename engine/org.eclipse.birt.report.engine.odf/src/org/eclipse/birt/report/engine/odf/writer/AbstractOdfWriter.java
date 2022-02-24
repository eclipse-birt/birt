/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.odf.writer;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.odf.IOdfWriter;
import org.eclipse.birt.report.engine.odf.SpanInfo;
import org.eclipse.birt.report.engine.odf.style.HyperlinkInfo;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;

@SuppressWarnings("nls")
/**
 * Base class for ODF format writers.
 */
public class AbstractOdfWriter implements IOdfWriter {
	public XMLWriter writer;
	protected int bookmarkId = 0;
	protected int imageId = 75;

	public void startTable(String name, StyleEntry style) {
		writer.openTag("table:table");

		if (name != null) {
			writer.attribute("table:name", name);
		}

		if (style != null) {
			writer.attribute("table:style-name", style.getName());
		}
	}

	public void endTable() {
		writer.closeTag("table:table");
	}

	public void startTableHeader() {
		writer.openTag("table:table-header-rows");
	}

	public void endTableHeader() {
		writer.closeTag("table:table-header-rows");
	}

	/**
	 * 
	 * @param style  style of the row
	 * @param height height of current row, if heigh equals 1 then ignore height
	 * @param type   header or normal
	 */
	public void startTableRow(StyleEntry rowStyle) {
		writer.openTag("table:table-row");
		if (rowStyle != null) {
			writer.attribute("table:style-name", rowStyle.getName());
		}
	}

	public void endTableRow() {
		writer.closeTag("table:table-row");
	}

	public void startTableRowGroup() {
		writer.openTag("table:table-row-group");
	}

	public void endTableRowGroup() {
		writer.closeTag("table:table-row-group");
	}

	public void startTableCell(StyleEntry style, SpanInfo spanInfo) {
		writer.openTag("table:table-cell");
		if (style != null) {
			writer.attribute("table:style-name", style.getName());
		}

		if (spanInfo != null) {
			writeGridSpan(spanInfo);
		}
	}

	public void endTableCell() {
		writer.closeTag("table:table-cell");
	}

	public void writeAutoText(int type) {
		if (type == IAutoTextContent.PAGE_NUMBER) {
			writer.openTag("text:page-number");
			writer.closeTag("text:page-number");
		} else if (type == IAutoTextContent.TOTAL_PAGE) {
			writer.openTag("text:page-count");
			writer.closeTag("text:page-count");
		}
	}

	public void writeColumn(StyleEntry[] colStyles) {
		int i = 0;
		while (i < colStyles.length) {
			StyleEntry colStyle = colStyles[i];
			writer.openTag("table:table-column");
			if (colStyle != null) {
				writer.attribute("table:style-name", colStyle.getName());
				int count = 1;
				// group columns with same style together
				while (i < colStyles.length - 1 && colStyles[i + 1] != null
						&& colStyle.getName().equals(colStyles[i + 1].getName())) {
					count++;
					i++;
				}
				if (count > 1) {
					writer.attribute("table:number-columns-repeated", count);
				}
			} else {
				int count = 1;
				// if the next one is null as well, group
				while (i < colStyles.length - 1 && colStyles[i + 1] == null) {
					count++;
					i++;
				}
				if (count > 1) {
					writer.attribute("table:number-columns-repeated", count);
				}
			}
			writer.closeTag("table:table-column");
			i++;
		}
	}

	public void writeSpanCell(SpanInfo info) {
		writer.openTag("table:covered-table-cell");
		StyleEntry style = info.getStyle();
		if (style != null) {
			writer.attribute("table:style-name", style.getName());
		}

		writeGridSpan(info);

		insertHiddenParagraph();
		writer.closeTag("table:covered-table-cell");
	}

	public void writeEmptyCell() {
		writer.openTag("table:table-cell");
		insertHiddenParagraph();
		writer.closeTag("table:table-cell");
	}

	public void insertHiddenParagraph() {
		writer.openTag("text:p");
		writer.attribute("text:style-name", StyleConstant.HIDDEN_STYLE_NAME);
		writer.closeTag("text:p");
	}

	public void writeGridSpan(SpanInfo spanInfo) {
		int columnSpan = spanInfo.getColumnSpan();
		int rowSpan = spanInfo.getRowSpan();
		if (columnSpan > 1) {
			writer.attribute("table:number-columns-spanned", columnSpan);
		}
		if (rowSpan > 1) {
			writer.attribute("table:number-rows-spanned", rowSpan);
		}
	}

	protected void openHyperlink(HyperlinkInfo info) {
		openHyperlink(info, "text");
	}

	protected void openHyperlink(HyperlinkInfo info, String baseType) {
		if (info == null) {
			return;
		}

		writer.openTag(baseType + ":a");

		writer.attribute("xlink:type", "simple");
		if (HyperlinkInfo.BOOKMARK == info.getType()) {
			writer.attribute("xlink:href", "#" + info.getUrl());
		} else if (HyperlinkInfo.HYPERLINK == info.getType() || HyperlinkInfo.DRILL == info.getType()) {
			writer.attribute("xlink:href", info.getUrl());
		}
		if (info.getTooltip() != null) {
			writer.attribute("xlink:title", info.getTooltip());
		}
	}

	protected void closeHyperlink(HyperlinkInfo info) {
		closeHyperlink(info, "text");
	}

	protected void closeHyperlink(HyperlinkInfo info, String baseType) {
		if ((info == null)) {
			return;
		}
		writer.closeTag(baseType + ":a");
	}

	public void close() throws IOException {
		writer.close();
	}

	public void writeParagraph(String txt, StyleEntry style) {
		writer.openTag("text:p");
		if (style != null) {
			writer.attribute("text:style-name", style.getName());
		}
		writeString(txt);
		writer.closeTag("text:p");
	}

	public void writeString(String txt) {
		if (txt == null) {
			return;
		}

		boolean notFirst = false;

		for (String st : txt.split("\n")) {
			String row = "<![CDATA[" + st + "]]>";
			if (notFirst) {
				row = "<text:line-break />" + row;
			} else {
				notFirst = true;
			}
			writer.cdata(row);
		}
	}

	public void writeBookmark(String bm) {
		if (bm == null) {
			return;
		}
		writer.openTag("text:bookmark");
		writer.attribute("text:name", bm);
		writer.closeTag("text:bookmark");

		bookmarkId++;
	}

	/**
	 * @param data
	 * @param height
	 * @param width
	 * @param style
	 * @param altText
	 * @param imageId
	 */
	protected void drawImage(String imageUrl, byte[] imageData, Double positionX, Double positionY, double height,
			double width, StyleEntry style, String altText, String layer, int imageId) {
		writer.openTag("draw:frame");
		if (style != null) {
			writer.attribute("draw:style-name", style.getName());
		}

		if (layer != null) {
			writer.attribute("draw:layer", layer);
		}

		writer.attribute("draw:name", "Image" + imageId);
		writer.attribute("text:anchor-type", "paragraph");
		writer.attribute("svg:width", width + "in");
		writer.attribute("svg:height", height + "in");

		if (positionX != null) {
			writer.attribute("svg:x", positionX.doubleValue() + "in");
		}
		if (positionY != null) {
			writer.attribute("svg:y", positionY.doubleValue() + "in");
		}

		writer.attribute("draw:z-index", "0");

		writer.openTag("draw:image");
		if (imageData != null) {
			drawImageData(imageData);
		} else {
			drawImageData(imageUrl);
		}
		writer.closeTag("draw:image");

		writer.openTag("svg:title");
		writer.text(altText);
		writer.closeTag("svg:title");

		writer.closeTag("draw:frame");
	}

	protected void drawImageData(String imageUrl) {
		if (imageUrl != null && imageUrl.length() > 0) {
			writer.attribute("xlink:href", imageUrl);
		}
	}

	protected void drawImageData(byte[] data) {
		String pic2Text = null;
		if (data != null && data.length != 0) {
			Base64 base = new Base64();
			pic2Text = new String(base.encode(data));
		}
		if (pic2Text != null) {
			writer.openTag("office:binary-data");
			writer.text(pic2Text);
			writer.closeTag("office:binary-data");
		}
	}

	/**
	 * @param txt
	 * @param style
	 * @param info
	 * @param fieldType
	 * @param isField
	 */
	protected void writeSpan(String txt, StyleEntry style, HyperlinkInfo info, int fieldType, boolean isField) {
		writer.openTag("text:span");
		if (style != null && style.getType() == StyleConstant.TYPE_TEXT) {
			writer.attribute("text:style-name", style.getName());
		}
		// inline style for hyperlinks
		else if (info != null && info.getStyle() != null) {
			writer.attribute("text:style-name", info.getStyle().getName());
		}

		if (isField) {
			writeAutoText(fieldType);
		} else {
			writeString(txt);
		}
		writer.closeTag("text:span");
	}

	protected int getImageID() {
		return imageId++;
	}

}
