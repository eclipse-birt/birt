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
package org.eclipse.birt.report.engine.emitter.ods;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.emitter.ods.layout.OdsContext;
import org.eclipse.birt.report.engine.odf.OdfUtil;
import org.eclipse.birt.report.engine.odf.SpanInfo;
import org.eclipse.birt.report.engine.odf.style.HyperlinkInfo;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.eclipse.birt.report.engine.odf.writer.AbstractOdfWriter;

@SuppressWarnings("nls")
public class OdsXmlWriter extends AbstractOdfWriter implements IOdsWriter {

	private HashMap<String, BookmarkDef> bookmarkList;

	private int sheetIndex = 1;

	protected static Logger logger = Logger.getLogger(OdsXmlWriter.class.getName());

	OdsContext context = null;

	public OdsXmlWriter(OutputStream out, OdsContext context) {
		this(out, "UTF-8", context);
	}

	public OdsXmlWriter(OutputStream out) {
		this(out, "UTF-8", null);
	}

	public OdsXmlWriter(OutputStream out, String encoding, OdsContext context) {
		this.context = context;
		writer = new XMLWriter();
		// no indent or newlines, because newlines inside paragraphs are
		// considered as white spaces
		writer.setIndent(false);
		writer.open(out, encoding);
	}

	/**
	 * @author bidi_acgc
	 * @param isRTLSheet: represents the direction of the sheet.
	 */
	public OdsXmlWriter(OutputStream out, boolean isRTLSheet) {
		writer.open(out, "UTF-8");
	}

	/**
	 * @author bidi_acgc
	 * @param orientation
	 * @param pageFooter
	 * @param pageHeader
	 * @param isRTLSheet  : represents the direction of the sheet.
	 */
	public OdsXmlWriter(OutputStream out, OdsContext context, boolean isRTLSheet) {
		this(out, "UTF-8", context);
	}

	/**
	 * @author bidi_acgc
	 * @param isRTLSheet: represents the direction of the sheet.
	 */
	public OdsXmlWriter(OutputStream out, String encoding, OdsContext context, boolean isRTLSheet) {
		this.context = context;
		writer.open(out, encoding);
	}

	private String capitalize(String text) {
		boolean capitalizeNextChar = true;
		char[] array = text.toCharArray();
		for (int i = 0; i < array.length; i++) {
			char c = text.charAt(i);
			if (c == ' ' || c == '\n' || c == '\r')
				capitalizeNextChar = true;
			else if (capitalizeNextChar) {
				array[i] = Character.toUpperCase(array[i]);
				capitalizeNextChar = false;
			}
		}
		return new String(array);
	}

	public void startRow(StyleEntry rowStyle) {
		this.startTableRow(rowStyle);
	}

	public void endRow() {
		this.endTableRow();
	}

	private void startCell(int cellIndex, int colspan, int rowspan, StyleEntry cellStyle, HyperlinkInfo hyperLink,
			BookmarkDef linkedBookmark) {
		SpanInfo spanInfo = new SpanInfo(cellIndex, colspan + 1, rowspan + 1, false, cellStyle);
		this.startTableCell(cellStyle, spanInfo);
	}

	public void outputData(SheetData sheetData, StyleEntry style, int column, int colSpan) {
		int rowSpan = sheetData.getRowSpan();
		int type = sheetData.getDataType();
		if (type == SheetData.IMAGE && sheetData instanceof ImageData) {
			outputImageData((ImageData) sheetData, style, column, colSpan);
		} else {
			Data d = (Data) sheetData;

			if (d instanceof BlankData) {
				BlankData blank = (BlankData) d;
				if (blank.getType() != BlankData.Type.NONE) {
					writer.openTag("table:covered-table-cell");
					if (style != null) {
						writer.attribute("table:style-name", style.getName());
					}
					writer.closeTag("table:covered-table-cell");
					return;
				}
			}

			Object value = d.getValue();
			HyperlinkInfo hyperLink = d.getHyperlinkDef();
			BookmarkDef linkedBookmark = d.getLinkedBookmark();
			outputData(type, value, style, column, colSpan, rowSpan, hyperLink, linkedBookmark);
		}
	}

	private void outputImageData(ImageData imageData, StyleEntry style, int column, int colSpan) {
		HyperlinkInfo hyperLink = imageData.getHyperlinkDef();
		BookmarkDef linkedBookmark = imageData.getLinkedBookmark();

		startCell(column, colSpan, imageData.getRowSpan(), style, hyperLink, linkedBookmark);

		openHyperlink(hyperLink, linkedBookmark);
		drawImage(imageData.getImageUrl(), null, null, null, imageData.getImageHeight() / OdfUtil.INCH_PT,
				imageData.getImageWidth() / OdfUtil.INCH_PT, style, imageData.getDescription(), null, getImageID());
		closeHyperlink(hyperLink);

		endCell();
	}

	public void outputData(int col, int row, int type, Object value) {
		outputData(type, value, null, col, 0, 0, null, null);
	}

	private void outputData(int type, Object value, StyleEntry style, int column, int colSpan, int rowSpan,
			HyperlinkInfo hyperLink, BookmarkDef linkedBookmark) {
		startCell(column, colSpan, rowSpan, style, hyperLink, linkedBookmark);

		// TODO: data type and number format styles
		/*
		 * String valueType = null; if ( type == SheetData.NUMBER ) { if (
		 * OdsUtil.isNaN( value ) || OdsUtil.isBigNumber( value ) || OdsUtil.isInfinity(
		 * value ) ) { valueType = "string"; } else { valueType = "number";
		 * //writer.attribute( "office:value", value.toString() ); } } else if ( type ==
		 * SheetData.DATE ) { if ( value instanceof Time ) { valueType = "time";
		 * //writer.attribute( "office:time-value", value.toString() ); } else {
		 * valueType = "date"; // TODO: date/datetime value format //writer.attribute(
		 * "office:date-value", value.toString() ); } } else if ( value instanceof
		 * Boolean ) { valueType = "boolean"; //writer.attribute(
		 * "office:boolean-value", value.toString() ); } else { valueType = "string"; }
		 * 
		 * if ( valueType != null ) { writer.attribute( "office:value-type", valueType
		 * ); }
		 */
		writer.attribute("office:value-type", "string");

		String txt = OdsUtil.format(value, type);
		writer.openTag("text:p");
		if (style != null) {
			writer.attribute("text:style-name", style.getName());
		}

		openHyperlink(hyperLink, linkedBookmark);
		// FIXME: it seems that ODS doesn't support link color
		// writeSpan( txt, style, null, 0, false );

		if (txt.length() > 0) {
			if (style != null) {
				String textTransform = (String) style.getStringProperty(StyleConstant.TEXT_TRANSFORM);
				if (CSSConstants.CSS_CAPITALIZE_VALUE.equalsIgnoreCase(textTransform)) {
					txt = capitalize(txt);
				} else if (CSSConstants.CSS_UPPERCASE_VALUE.equalsIgnoreCase(textTransform)) {
					txt = txt.toUpperCase();
				} else if (CSSConstants.CSS_LOWERCASE_VALUE.equalsIgnoreCase(textTransform)) {
					txt = txt.toLowerCase();
				}
			}

			writeString(txt);
		}
		closeHyperlink(hyperLink);
		writer.closeTag("text:p");

		endCell();
	}

	/**
	 * @param hyperLink
	 * @param linkedBookmark
	 */
	private void openHyperlink(HyperlinkInfo hyperLink, BookmarkDef linkedBookmark) {
		if (hyperLink != null) {
			writer.openTag("text:a");

			String urlAddress = hyperLink.getUrl();
			if (hyperLink.getType() == IHyperlinkAction.ACTION_BOOKMARK) {
				if (linkedBookmark != null)
					urlAddress = "#" + linkedBookmark.getValidName();
				else {
					logger.log(Level.WARNING, "The bookmark: {" + urlAddress + "} is not defined!");
				}
			}

			if (urlAddress != null && urlAddress.length() >= 255) {
				logger.log(Level.WARNING, "The URL: {" + urlAddress + "} is too long!");
				urlAddress = urlAddress.substring(0, 254);
			}
			writer.attribute("xlink:href", urlAddress);
			if (hyperLink.getTooltip() != null) {
				writer.attribute("xlink:title", hyperLink.getTooltip());
			}
		}
	}

	private void endCell() {
		this.endTableCell();
	}

	private void defineNames(Entry<String, BookmarkDef> bookmarkEntry) {
		BookmarkDef bookmark = bookmarkEntry.getValue();
		String name = bookmark.getValidName();
		String refer = getRefer(bookmark.getSheetName(), bookmark);
		defineName(name, refer);
	}

	private String getRefer(String sheetName, BookmarkDef bookmark) {
		StringBuffer sb = new StringBuffer("$");
		sb.append(sheetName);
		sb.append(".$");
		sb.append(OdsUtil.getColumnName(bookmark.getColumnNo() - 1));
		sb.append("$");
		sb.append(bookmark.getRowNo());
		return sb.toString();
	}

	private void defineName(String name, String refer) {
		writer.openTag("table:named-range");
		writer.attribute("table:name", name);
		writer.attribute("table:base-cell-address", refer);
		writer.attribute("table:cell-range-address", refer);
		writer.closeTag("table:named-range");
	}

	public void startSheet(String name) {
		startSheet(name, null, null);
	}

	public void startSheet(String name, StyleEntry tableStyle, StyleEntry[] colStyles) {
		startTable(name, tableStyle);
		writeColumn(colStyles);
	}

	public void closeSheet() {
		endTable();
	}

	public void startSheet(StyleEntry tableStyle, StyleEntry[] colStyles, String name) {
		startSheet(name, tableStyle, colStyles);
		sheetIndex += 1;
	}

	public void endSheet() {
		closeSheet();
	}

	public void start(IReportContent report, HashMap<String, BookmarkDef> bookmarkList) {
		this.bookmarkList = bookmarkList;
		writer.openTag("office:body");
		writer.openTag("office:spreadsheet");
	}

	private void outputBookmarks(HashMap<String, BookmarkDef> bookmarkList) {
		if (!bookmarkList.isEmpty()) {
			writer.openTag("table:named-expressions");
			Set<Entry<String, BookmarkDef>> bookmarkEntry = bookmarkList.entrySet();
			for (Entry<String, BookmarkDef> bookmark : bookmarkEntry)
				defineNames(bookmark);
			writer.closeTag("table:named-expressions");
		}
	}

	public void end() {
		outputBookmarks(bookmarkList);
		writer.closeTag("office:spreadsheet");
		writer.closeTag("office:body");
		close();
	}

	public void close() {
		writer.endWriter();
		writer.close();
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public void startRow() {
		startRow(null);
	}

	public String defineName(String cells) {
		return null;
	}

	public XMLWriter getWriter() {
		return writer;
	}
}
