/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.awt.Color;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.emitter.XMLEncodeUtil;
import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelContext;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;

public class ExcelXmlWriter implements IExcelWriter {

	public static final int rightToLeftisTrue = 1; // bidi_acgc added
	private final XMLWriterXLS writer = new XMLWriterXLS();

	public XMLWriterXLS getWriter() {
		return writer;
	}

	private String pageHeader, pageFooter;
	private int sheetIndex = 1;

	static class XLSEncodeUtil extends XMLEncodeUtil {

		protected static final char[] XLS_TEXT_ENCODE = new char[] { '&', '<', '\r', '\n' };

		static String encodeXLSText(String s) {
			char[] chars = s.toCharArray();
			int length = chars.length;
			int index = testEscape(chars, XLS_TEXT_ENCODE);
			if (index >= length) {
				return s;
			}

			StringBuilder sb = new StringBuilder(2 * length);
			sb.append(chars, 0, index);

			while (index < length) {
				char c = chars[index++];
				if (Character.isHighSurrogate(c)) {
					index += decodeSurrogate(c, chars, index, sb);
				} else if (isValidCodePoint(c)) {
					if (c == '&') {
						sb.append("&amp;");
					} else if (c == '<') {
						sb.append("&lt;");
					} else if (c == '\r') {
						if (index < length) {
							char nc = chars[index];
							if (nc == '\n') {
								index++;
							}
						}
						sb.append("&#10;");
					} else if (c == '\n') {
						sb.append("&#10;");
					} else {
						sb.append(c);
					}
				} else {
					logger.log(Level.WARNING, MESSAGE_INVALID_CHARACTER, Integer.valueOf(c));
				}
			}
			return sb.toString();
		}
	}

	public static class XMLWriterXLS extends XMLWriter {
		protected String encodeText(String text) {
			return XLSEncodeUtil.encodeXLSText(text);
		}
	}

	protected static Logger logger = Logger.getLogger(ExcelXmlWriter.class.getName());

	ExcelContext context = null;

	public ExcelXmlWriter(ExcelContext context) {
		this("UTF-8", context);
	}

	public ExcelXmlWriter(OutputStream out) {
		writer.open(out, "UTF-8");
	}

	public ExcelXmlWriter(OutputStream out, ExcelContext context) {
		this.context = context;
		writer.open(out, "UTF-8");
	}

	public ExcelXmlWriter(String encoding, ExcelContext context) {
		this(context.getOutputSteam(), context);
	}

	private void writeDocumentProperties(IReportContent reportContent) {
		if (reportContent == null) {
			return;
		}
		ReportDesignHandle reportDesign = reportContent.getDesign().getReportDesign();
		writer.openTag("DocumentProperties");
		writer.attribute("xmlns", "urn:schemas-microsoft-com:office:office");
		writer.openTag("Author");
		writer.text(reportDesign.getStringProperty(IModuleModel.AUTHOR_PROP));
		writer.closeTag("Author");
		writer.openTag("Title");
		writer.text(reportContent.getTitle());
		writer.closeTag("Title");
		writer.openTag("Description");
		writer.text(reportDesign.getComments());
		writer.closeTag("Description");
		writer.openTag("Subject");
		writer.text(reportDesign.getSubject());
		writer.closeTag("Subject");
		writer.closeTag("DocumentProperties");
	}

	/**
	 * excel doesn't support time zone, so always output the date time in user's
	 * time zone
	 * 
	 * @param value
	 * @param dataType
	 * @return
	 */
	private String format(Object value, int dataType) {
		if (value == null) {
			return "";
		}
		if (dataType == SheetData.DATE) {
			return ExcelUtil.formatDate(value, context.getTimeZone());
		}
		return ExcelUtil.format(value, dataType);
	}

	private void writeText(int type, Object value, StyleEntry style) {
		String txt = format(value, type);
		writer.openTag("Data");
		if (type == SheetData.NUMBER) {
			if (ExcelUtil.isNaN(value) || ExcelUtil.isBigNumber(value) || ExcelUtil.isInfinity(value)) {
				writer.attribute("ss:Type", "String");
			} else {
				writer.attribute("ss:Type", "Number");
			}
		} else if (type == SheetData.DATE) {
			writer.attribute("ss:Type", "DateTime");
		} else {
			writer.attribute("ss:Type", "String");
		}

		if (style != null) {
			String textTransform = (String) style.getProperty(StyleConstant.TEXT_TRANSFORM);
			if (CSSConstants.CSS_CAPITALIZE_VALUE.equalsIgnoreCase(textTransform)) {
				txt = ExcelUtil.capitalize(txt);
			} else if (CSSConstants.CSS_UPPERCASE_VALUE.equalsIgnoreCase(textTransform)) {
				txt = txt.toUpperCase();
			} else if (CSSConstants.CSS_LOWERCASE_VALUE.equalsIgnoreCase(textTransform)) {
				txt = txt.toLowerCase();
			}
		}

		writer.text(ExcelUtil.truncateCellText(txt));

		writer.closeTag("Data");
	}

	public void startRow(double rowHeight) {
		writer.openTag("Row");
		if (rowHeight > 0) {
			writer.attribute("ss:AutoFitHeight", 0);
			writer.attribute("ss:Height", rowHeight);
		} else {
			writer.attribute("ss:AutoFitHeight", 1);
		}
	}

	public void endRow() {
		writer.closeTag("Row");
	}

	private void startCell(int cellIndex, int colspan, int rowspan, int styleId, HyperlinkDef hyperLink,
			BookmarkDef linkedBookmark) {
		writer.openTag("Cell");
		writer.attribute("ss:Index", cellIndex);
		if (styleId > 0) {
			writer.attribute("ss:StyleID", styleId);
		}

		if (hyperLink != null) {
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
			writer.attribute("ss:HRef", urlAddress);
			if (hyperLink.getToolTip() != null) {
				writer.attribute("x:HRefScreenTip", hyperLink.getToolTip());
			}
		}
		if (colspan > 0) {
			writer.attribute("ss:MergeAcross", colspan);
		}
		if (rowspan > 0) {
			writer.attribute("ss:MergeDown", rowspan);
		}
	}

	public void outputData(String sheet, SheetData sheetData, StyleEntry style, int column, int colSpan) {
		// TODO: ignore sheet here. If this function is needed, need to
		// implement.
		outputData(sheetData, style, column, colSpan);
	}

	public void outputData(SheetData sheetData, StyleEntry style, int column, int colSpan) {
		int rowSpan = sheetData.getRowSpan();
		int styleId = sheetData.getStyleId();
		int type = sheetData.getDataType();
		if (type == SheetData.IMAGE) {
			outputData(Data.STRING, null, style, column, colSpan, sheetData.getRowSpan(), sheetData.getStyleId(), null,
					null);
		} else {
			Data d = (Data) sheetData;
			Object value = d.getValue();
			HyperlinkDef hyperLink = d.getHyperlinkDef();
			BookmarkDef linkedBookmark = d.getLinkedBookmark();
			outputData(type, value, style, column, colSpan, rowSpan, styleId, hyperLink, linkedBookmark);
		}
	}

	public void outputData(int col, int row, int type, Object value) {
		outputData(type, value, null, col, 0, 0, -1, null, null);
	}

	private void outputData(int type, Object value, StyleEntry style, int column, int colSpan, int rowSpan, int styleId,
			HyperlinkDef hyperLink, BookmarkDef linkedBookmark) {
		startCell(column, colSpan, rowSpan, styleId, hyperLink, linkedBookmark);
		if (value != null) {
			writeText(type, value, style);
		}
		endCell();
	}

	protected void writeComments(HyperlinkDef linkDef) {
		String toolTip = linkDef.getToolTip();
		writer.openTag("Comment");
		writer.openTag("ss:Data");
		writer.attribute("xmlns", "http://www.w3.org/TR/REC-html40");
		writer.openTag("Font");
		// writer.attribute( "html:Face", "Tahoma" );
		// writer.attribute( "x:CharSet", "1" );
		// writer.attribute( "html:Size", "8" );
		// writer.attribute( "html:Color", "#000000" );
		writer.text(toolTip);
		writer.closeTag("Font");
		writer.closeTag("ss:Data");
		writer.closeTag("Comment");
	}

	private void endCell() {
		writer.closeTag("Cell");
	}

	private void writeAlignment(String horizontal, String vertical, float indent, String direction, boolean wrapText) {
		writer.openTag("Alignment");

		if (isValid(horizontal)) {
			writer.attribute("ss:Horizontal", horizontal);
		}

		if (isValid(vertical)) {
			writer.attribute("ss:Vertical", vertical);
		}
		if (indent != 0f) {
			writer.attribute("ss:Indent", indent);
		}
		if (isValid(direction)) {
			if (CSSConstants.CSS_RTL_VALUE.equals(direction))
				writer.attribute("ss:ReadingOrder", "RightToLeft");
			else
				writer.attribute("ss:ReadingOrder", "LeftToRight");
		}
		if (wrapText) {
			writer.attribute("ss:WrapText", "1");
		}

		writer.closeTag("Alignment");
	}

	private void writeBorder(String position, String lineStyle, Integer weight, Color color) {
		writer.openTag("Border");
		writer.attribute("ss:Position", position);
		if (isValid(lineStyle)) {
			writer.attribute("ss:LineStyle", lineStyle);
		}

		if (weight != null && weight > 0) {
			writer.attribute("ss:Weight", weight);
		}

		if (color != null) {
			writer.attribute("ss:Color", toString(color));
		}

		writer.closeTag("Border");
	}

	private void writeFont(String fontName, Float size, Boolean bold, Boolean italic, Boolean strikeThrough,
			Boolean underline, Color color) {
		writer.openTag("Font");

		if (isValid(fontName)) {
			fontName = getFirstFont(fontName);
			writer.attribute("ss:FontName", fontName);
		}

		if (size != null) {
			writer.attribute("ss:Size", size);
		}

		if (bold != null && bold) {
			writer.attribute("ss:Bold", 1);
		}

		if (italic != null && italic) {
			writer.attribute("ss:Italic", 1);
		}

		if (strikeThrough != null && strikeThrough) {
			writer.attribute("ss:StrikeThrough", 1);
		}

		if (underline != null && underline) {
			writer.attribute("ss:Underline", "Single");
		}

		if (color != null) {
			writer.attribute("ss:Color", toString(color));
		}

		writer.closeTag("Font");
	}

	private void writeBackGroudColor(StyleEntry style) {
		Color bgColor = (Color) style.getProperty(StyleConstant.BACKGROUND_COLOR_PROP);
		if (bgColor != null) {
			writer.openTag("Interior");
			writer.attribute("ss:Color", toString(bgColor));
			writer.attribute("ss:Pattern", "Solid");
			writer.closeTag("Interior");
		}
	}

	private boolean isValid(String value) {
		return !StyleEntry.isNull(value);
	}

	private String getFirstFont(String fontName) {
		int firstSeperatorIndex = fontName.indexOf(',');
		if (firstSeperatorIndex != -1) {
			return fontName.substring(0, firstSeperatorIndex);
		} else {
			return fontName;
		}
	}

	private void declareStyle(StyleEntry style, int id) {
		boolean wrapText = context.getWrappingText();
		String whiteSpace = (String) style.getProperty(StyleConstant.WHITE_SPACE);
		if (CSSConstants.CSS_NOWRAP_VALUE.equals(whiteSpace)) {
			wrapText = false;
		}

		writer.openTag("Style");
		writer.attribute("ss:ID", id);
		if (style.isHyperlink()) {
			writer.attribute("ss:Parent", "HyperlinkId");
		}

		if (id >= StyleEngine.RESERVE_STYLE_ID) {
			String direction = (String) style.getProperty(StyleConstant.DIRECTION_PROP); // bidi_hcg
			String horizontalAlign = (String) style.getProperty(StyleConstant.H_ALIGN_PROP);
			String verticalAlign = (String) style.getProperty(StyleConstant.V_ALIGN_PROP);
			float indent = ExcelUtil.convertTextIndentToEM((FloatValue) style.getProperty(StyleConstant.TEXT_INDENT),
					(Float) style.getProperty(StyleConstant.FONT_SIZE_PROP));
			writeAlignment(horizontalAlign, verticalAlign, indent, direction, wrapText);
			writer.openTag("Borders");
			Color bottomColor = (Color) style.getProperty(StyleConstant.BORDER_BOTTOM_COLOR_PROP);
			String bottomLineStyle = (String) style.getProperty(StyleConstant.BORDER_BOTTOM_STYLE_PROP);
			Integer bottomWeight = (Integer) style.getProperty(StyleConstant.BORDER_BOTTOM_WIDTH_PROP);
			writeBorder("Bottom", bottomLineStyle, bottomWeight, bottomColor);

			Color topColor = (Color) style.getProperty(StyleConstant.BORDER_TOP_COLOR_PROP);
			String topLineStyle = (String) style.getProperty(StyleConstant.BORDER_TOP_STYLE_PROP);
			Integer topWeight = (Integer) style.getProperty(StyleConstant.BORDER_TOP_WIDTH_PROP);
			writeBorder("Top", topLineStyle, topWeight, topColor);

			Color leftColor = (Color) style.getProperty(StyleConstant.BORDER_LEFT_COLOR_PROP);
			String leftLineStyle = (String) style.getProperty(StyleConstant.BORDER_LEFT_STYLE_PROP);
			Integer leftWeight = (Integer) style.getProperty(StyleConstant.BORDER_LEFT_WIDTH_PROP);
			writeBorder("Left", leftLineStyle, leftWeight, leftColor);

			Color rightColor = (Color) style.getProperty(StyleConstant.BORDER_RIGHT_COLOR_PROP);
			String rightLineStyle = (String) style.getProperty(StyleConstant.BORDER_RIGHT_STYLE_PROP);
			Integer rightWeight = (Integer) style.getProperty(StyleConstant.BORDER_RIGHT_WIDTH_PROP);
			writeBorder("Right", rightLineStyle, rightWeight, rightColor);

			Color diagonalColor = (Color) style.getProperty(StyleConstant.BORDER_DIAGONAL_COLOR_PROP);
			String diagonalStyle = (String) style.getProperty(StyleConstant.BORDER_DIAGONAL_STYLE_PROP);
			Integer diagonalWidth = (Integer) style.getProperty(StyleConstant.BORDER_DIAGONAL_WIDTH_PROP);
			writeBorder("DiagonalLeft", diagonalStyle, diagonalWidth, diagonalColor);

			writer.closeTag("Borders");

			String fontName = (String) style.getProperty(StyleConstant.FONT_FAMILY_PROP);
			Float size = (Float) style.getProperty(StyleConstant.FONT_SIZE_PROP);
			Boolean fontStyle = (Boolean) style.getProperty(StyleConstant.FONT_STYLE_PROP);
			Boolean fontWeight = (Boolean) style.getProperty(StyleConstant.FONT_WEIGHT_PROP);
			Boolean strikeThrough = (Boolean) style.getProperty(StyleConstant.TEXT_LINE_THROUGH_PROP);
			Boolean underline = (Boolean) style.getProperty(StyleConstant.TEXT_UNDERLINE_PROP);
			Color color = (Color) style.getProperty(StyleConstant.COLOR_PROP);
			writeFont(fontName, size, fontWeight, fontStyle, strikeThrough, underline, color);
			writeBackGroudColor(style);
		}

		writeDataFormat(style);

		writer.closeTag("Style");
	}

	private String toString(Color color) {
		if (color == null)
			return null;
		return "#" + toHexString(color.getRed()) + toHexString(color.getGreen()) + toHexString(color.getBlue());
	}

	private static String toHexString(int c) {
		String result = Integer.toHexString(c);
		if (result.length() < 2) {
			result = "0" + result;
		}
		return result;
	}

	private void writeDataFormat(StyleEntry style) {
		Integer type = (Integer) style.getProperty(StyleConstant.DATA_TYPE_PROP);
		if (type == null)
			return;
		if (type == SheetData.DATE && style.getProperty(StyleConstant.DATE_FORMAT_PROP) != null) {
			writer.openTag("NumberFormat");
			writer.attribute("ss:Format", style.getProperty(StyleConstant.DATE_FORMAT_PROP));
			writer.closeTag("NumberFormat");

		} else if (type == Data.NUMBER && style.getProperty(StyleConstant.NUMBER_FORMAT_PROP) != null) {
			NumberFormatValue numberFormat = (NumberFormatValue) style.getProperty(StyleConstant.NUMBER_FORMAT_PROP);
			String format = numberFormat.getFormat();
			if (format != null) {
				writer.openTag("NumberFormat");
				writer.attribute("ss:Format", format);
				writer.closeTag("NumberFormat");
			}
		}
	}

	// here the user input can be divided into two cases :
	// the case in the birt input like G and the Currency
	// the case in excel format : like 0.00E00

	private void writeDeclarations() {
		writer.startWriter();
		writer.println();
		writer.println("<?mso-application progid=\"Excel.Sheet\"?>");

		writer.openTag("Workbook");

		writer.attribute("xmlns", "urn:schemas-microsoft-com:office:spreadsheet");
		writer.attribute("xmlns:o", "urn:schemas-microsoft-com:office:office");
		writer.attribute("xmlns:x", "urn:schemas-microsoft-com:office:excel");
		writer.attribute("xmlns:ss", "urn:schemas-microsoft-com:office:spreadsheet");
		writer.attribute("xmlns:html", "http://www.w3.org/TR/REC-html40");
	}

	private void declareStyles(Map<StyleEntry, Integer> style2id) {
		writer.openTag("Styles");
		declareHyperlinkStyle();
		Set<Entry<StyleEntry, Integer>> entrySet = style2id.entrySet();
		for (Map.Entry<StyleEntry, Integer> entry : entrySet) {
			declareStyle(entry.getKey(), entry.getValue());
		}

		writer.closeTag("Styles");
	}

	private void declareHyperlinkStyle() {
		writer.openTag("Style");
		writer.attribute("ss:ID", "HyperlinkId");
		writer.attribute("ss:Name", "Hyperlink");
		writer.openTag("Font");
		writer.attribute("ss:Color", "#0000ff");
		writer.closeTag("Font");
		writer.closeTag("Style");
	}

	private void defineName(String name, String refer) {
		writer.openTag("NamedRange");
		writer.attribute("ss:Name", name);
		writer.attribute("ss:RefersTo", refer);
		writer.closeTag("NamedRange");
	}

	public void startSheet(String name) {
		startSheet(name, null);
	}

	public void startSheet(String name, double[] coordinates) {
		writer.openTag("Worksheet");
		writer.attribute("ss:Name", name);

		// Set the Excel Sheet RightToLeft attribute according to Report
		// if Report Bidi-Orientation is RTL, then Sheet is RTL.
		if (context.isRTL())
			writer.attribute("ss:RightToLeft", rightToLeftisTrue);
		// else : do nothing i.e. LTR
		outputColumns(coordinates);
	}

	public void closeSheet() {
		writer.closeTag("Worksheet");
		writer.endWriter();
	}

	public void outputColumns(double[] width) {
		writer.openTag("ss:Table");

		if (width == null) {
			// logger.log( Level.SEVERE, "Invalid columns width" );
			return;
		}

		for (int i = 0; i < width.length; i++) {
			writer.openTag("ss:Column");
			writer.attribute("ss:Width", width[i] / 1000);
			writer.attribute("ss:AutoFitWidth", 0);
			writer.closeTag("ss:Column");
		}
	}

	public void endTable() {
		writer.closeTag("ss:Table");
	}

	public void insertHorizontalMargin(int height, int span) {
		writer.openTag("Row");
		writer.attribute("ss:AutoFitHeight", 0);
		writer.attribute("ss:Height", height);

		writer.openTag("Cell");
		writer.attribute(" ss:MergeAcross", span);
		writer.closeTag("Cell");

		writer.closeTag("Row");
	}

	public void insertVerticalMargin(int start, int end, int length) {
		writer.openTag("Row");
		writer.attribute("ss:AutoFitHeight", 0);
		writer.attribute("ss:Height", 1);

		writer.openTag("Cell");
		writer.attribute("ss:Index", start);
		writer.attribute(" ss:MergeDown", length);
		writer.closeTag("Cell");

		writer.openTag("Cell");
		writer.attribute("ss:Index", end);
		writer.attribute(" ss:MergeDown", length);
		writer.closeTag("Cell");

		writer.closeTag("Row");
	}

	private void declareWorkSheetOptions(String orientation, int pageWidth, int pageHeight, float leftMargin,
			float rightMargin, float topMargin, float bottomMargin) {
		writer.openTag("WorksheetOptions");
		writer.attribute("xmlns", "urn:schemas-microsoft-com:office:excel");

		if (context.getHideGridlines()) {
			writer.openTag("DoNotDisplayGridlines");
			writer.closeTag("DoNotDisplayGridlines");
		}
		writer.openTag("PageSetup");

		writer.openTag("PageMargins");
		writer.attribute("x:Bottom", bottomMargin / ExcelUtil.INCH_PT);
		writer.attribute("x:Left", leftMargin / ExcelUtil.INCH_PT);
		writer.attribute("x:Right", rightMargin / ExcelUtil.INCH_PT);
		writer.attribute("x:Top", topMargin / ExcelUtil.INCH_PT);
		writer.closeTag("PageMargins");

		if (orientation != null) {
			writer.openTag("Layout");
			writer.attribute("x:Orientation", orientation);
			writer.closeTag("Layout");
		}

		if (pageHeader != null) {
			writer.openTag("Header");
			writer.attribute("x:Data", pageHeader);
			writer.closeTag("Header");
		}

		if (pageFooter != null) {
			writer.openTag("Footer");
			writer.attribute("x:Data", pageFooter);
			writer.closeTag("Footer");
		}
		writer.closeTag("PageSetup");
		writer.openTag("Print");
		writer.openTag("PaperSizeIndex");
		int index = ExcelUtil.getPageSizeIndex(pageWidth / 1000, pageHeight / 1000);
		writer.text(String.valueOf(index));
		writer.closeTag("PaperSizeIndex");
		writer.closeTag("Print");

		writer.closeTag("WorksheetOptions");
	}

	public void startSheet(double[] coordinates, String pageHeader, String pageFooter, String name) {
		this.pageHeader = pageHeader;
		this.pageFooter = pageFooter;
		startSheet(name, coordinates);
		sheetIndex += 1;
	}

	public void endSheet(double[] coordinates, String orientation, int pageWidth, int pageHeight, float leftMargin,
			float rightMargin, float topMargin, float bottomMargin) {
		endTable();
		declareWorkSheetOptions(orientation, pageWidth, pageHeight, leftMargin, rightMargin, topMargin, bottomMargin);
		closeSheet();
	}

	public void start(IReportContent report, Map<StyleEntry, Integer> styles,
			// TODO: style ranges.
			// List<ExcelRange> styleRanges,
			HashMap<String, BookmarkDef> bookmarkList) {
		writeDeclarations();
		writeDocumentProperties(report);
		declareStyles(styles);
		outputBookmarks(bookmarkList);
	}

	private void outputBookmarks(HashMap<String, BookmarkDef> bookmarkList) {
		if (!bookmarkList.isEmpty()) {
			writer.openTag("Names");
			for (Entry<String, BookmarkDef> entry : bookmarkList.entrySet()) {
				BookmarkDef bookmark = entry.getValue();
				defineName(bookmark.getValidName(), getRefer(bookmark));
			}
			writer.closeTag("Names");
		}
	}

	private String getRefer(BookmarkDef bookmark) {
		StringBuffer buffer = new StringBuffer('=');
		buffer.append(bookmark.getSheetName());
		buffer.append("!");
		int startColumn = bookmark.getStartColumn();
		int startRow = bookmark.getStartRow();
		addCellPosition(buffer, startColumn, startRow);
		int endColumn = bookmark.getEndColumn();
		int endRow = bookmark.getEndRow();
		if (endRow != -1 && endColumn != -1 && startRow != endRow && startColumn != endColumn) {
			buffer.append(':');
			addCellPosition(buffer, endColumn, endRow);
		}
		return buffer.toString();
	}

	private void addCellPosition(StringBuffer buffer, int column, int row) {
		buffer.append("R");
		buffer.append(String.valueOf(row));
		buffer.append("C");
		buffer.append(String.valueOf(column));
	}

	public void end() {
		writer.closeTag("Workbook");
		close();
	}

	public void close() {
		writer.endWriter();
		writer.close();
	}

	public void setSheetIndex(int sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	public void endSheet() {
		endSheet(null, null, 0, 0, 0, 0, 0, 0);
	}

	public void startRow() {
		startRow(-1);
	}

	public String defineName(String cells) {
		return null;
	}
}
