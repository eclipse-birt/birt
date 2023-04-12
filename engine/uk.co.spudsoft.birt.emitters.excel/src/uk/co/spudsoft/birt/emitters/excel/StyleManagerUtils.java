/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.text.AttributedString;
import java.text.DateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.w3c.dom.css.CSSValue;

import uk.co.spudsoft.birt.emitters.excel.framework.Logger;

/**
 * <p>
 * StyleManagerUtils contains methods implementing the details of converting
 * BIRT styles to POI styles.
 * </p>
 * <p>
 * StyleManagerUtils is abstract to support a small number of methods that
 * require HSSF/XSSF specific implementations.
 *
 * @author Jim Talbut
 *
 */
public abstract class StyleManagerUtils {

	protected Logger log;

	protected static final FontRenderContext frc = new FontRenderContext(null, true, true);

	public interface Factory {
		StyleManagerUtils create(Logger log);
	}

	/**
	 * @param log The Logger to use for any information reports to be made.
	 */
	public StyleManagerUtils(Logger log) {
		this.log = log;
	}

	/**
	 * Create a RichTextString representing a given string.
	 *
	 * @param value The string to represent in the RichTextString.
	 * @return A RichTextString representing value.
	 */
	public abstract RichTextString createRichTextString(String value);

	/**
	 * Compare two objects in a null-safe manner.
	 *
	 * @param lhs The first object to compare.
	 * @param rhs The second object to compare.
	 * @return true is both objects are null or lhs.equals(rhs), otherwise false.
	 */
	public static boolean objectsEqual(Object lhs, Object rhs) {
		return (lhs == null) ? (rhs == null) : lhs.equals(rhs);
	}

	public static boolean dataFormatsEquivalent(DataFormatValue dataFormat1, DataFormatValue dataFormat2) {
		if (dataFormat1 == null) {
			return (dataFormat2 == null);
		}
		if (dataFormat2 == null) {
			return false;
		}
		if (!objectsEqual(dataFormat1.getNumberPattern(), dataFormat2.getNumberPattern())
				|| !objectsEqual(dataFormat1.getDatePattern(), dataFormat2.getDatePattern())
				|| !objectsEqual(dataFormat1.getDateTimePattern(), dataFormat2.getDateTimePattern())
				|| !objectsEqual(dataFormat1.getTimePattern(), dataFormat2.getTimePattern())) {
			return false;
		}
		return true;
	}

	/**
	 * Convert a BIRT text alignment string into a POI CellStyle constant.
	 *
	 * @param alignment The BIRT alignment string.
	 * @return One of the CellStyle.ALIGN* constants.
	 */
	public HorizontalAlignment poiAlignmentFromBirtAlignment(String alignment) {
		if (CSSConstants.CSS_LEFT_VALUE.equals(alignment)) {
			return HorizontalAlignment.LEFT; // CellStyle.ALIGN_LEFT;
		}
		if (CSSConstants.CSS_RIGHT_VALUE.equals(alignment)) {
			return HorizontalAlignment.RIGHT; // CellStyle.ALIGN_RIGHT;
		}
		if (CSSConstants.CSS_CENTER_VALUE.equals(alignment)) {
			return HorizontalAlignment.CENTER; // CellStyle.ALIGN_CENTER;
		}
		return HorizontalAlignment.GENERAL; // CellStyle.ALIGN_GENERAL;
	}

	/**
	 * Convert a BIRT font size string (either a dimensioned string or "xx-small" -
	 * "xx-large") to a point size.
	 *
	 * @param fontSize The BIRT font size.
	 * @return An appropriate size in points.
	 */
	public short fontSizeInPoints(String fontSize) {
		if (fontSize == null) {
			return 11;
		}
		if ("xx-small".equals(fontSize)) {
			return 6;
		} else if ("x-small".equals(fontSize)) {
			return 8;
		} else if ("small".equals(fontSize)) {
			return 10;
		} else if ("medium".equals(fontSize)) {
			return 11;
		} else if ("large".equals(fontSize)) {
			return 14;
		} else if ("x-large".equals(fontSize)) {
			return 18;
		} else if ("xx-large".equals(fontSize)) {
			return 24;
		} else if ("smaller".equals(fontSize)) {
			return 10;
		} else if ("larger".equals(fontSize)) {
			return 14;
		}

		DimensionType dim = DimensionType.parserUnit(fontSize, "pt");
		// log.debug( "fontSize: \"", fontSize, "\", parses as: \"", dim.toString(), "\"
		// (", dim.getMeasure(), " ", dim.getUnits(), ")");
		if (DimensionType.UNITS_PX.equals(dim.getUnits())) {
			double px = dim.getMeasure();
			double inches = px / 96;
			double points = 72 * inches;
			return (short) points;
		} else if (DimensionType.UNITS_EM.equals(dim.getUnits())) {
			return (short) (12 * dim.getMeasure());
		} else if (DimensionType.UNITS_PERCENTAGE.equals(dim.getUnits())) {
			return (short) (12 * dim.getMeasure() / 100.0);
		} else {
			double points = dim.convertTo(DimensionType.UNITS_PT);
			return (short) points;
		}
	}

	/**
	 * Obtain a POI column width from a BIRT DimensionType.
	 *
	 * @param dim The BIRT dimension, which must be in absolute units.
	 * @return The column with in width units, or zero if a suitable conversion
	 *         could not be performed.
	 */
	public int poiColumnWidthFromDimension(DimensionType dim) {
		if (dim != null) {
			double mmWidth = dim.getMeasure();
			if ((DimensionType.UNITS_CM.equals(dim.getUnits())) || (DimensionType.UNITS_IN.equals(dim.getUnits()))
					|| (DimensionType.UNITS_PT.equals(dim.getUnits()))
					|| (DimensionType.UNITS_PC.equals(dim.getUnits()))) {
				mmWidth = dim.convertTo("mm");
			}
			int result = ClientAnchorConversions.millimetres2WidthUnits(mmWidth);
			// log.debug( "Column width in mm: ", mmWidth, "; converted result: ", result );
			return result;
		} else {
			return 0;
		}
	}

	/**
	 * Object a POI font weight from a BIRT string.
	 *
	 * @param fontWeight The font weight as understood by BIRT.
	 * @return One of the Font.BOLDWEIGHT_* constants.
	 */
	public boolean poiFontWeightFromBirt(String fontWeight) {
		if (fontWeight == null) {
			return false; // 0;
		}
		if ("bold".equals(fontWeight)) {
			return true; // Font.BOLDWEIGHT_BOLD;
		}
		return false; // Font.BOLDWEIGHT_NORMAL;
	}

	/**
	 * Convert a BIRT font name into a system font name. <br>
	 * Just returns the passed in name unless that is a known family name ("serif"
	 * or "sans-serif").
	 *
	 * @param fontName The font name from BIRT.
	 * @return A real font name.
	 */
	public String poiFontNameFromBirt(String fontName) {
		if ("serif".equals(fontName)) {
			return "Times New Roman";
		} else if ("sans-serif".equals(fontName)) {
			return "Arial";
		} else if ("monospace".equals(fontName)) {
			return "Courier New";
		}
		return fontName;
	}

	/**
	 * <p>
	 * Add a colour (specified as "rgb(<i>r</i>, <i>g</i>, <i>b</i>)") to a Font.
	 * </p>
	 * <p>
	 * In the current implementations the XSSF implementation will always produce
	 * exactly the right colour, whilst the HSSF implementation takes the best
	 * approximation from the current palette.
	 *
	 * @param workbook The workbook in which the Font is to be used, needed to
	 *                 obtain the colour palette.
	 * @param font     The font to which the colour is to be added.
	 * @param colour   The colour to add.
	 */
	public abstract void addColourToFont(Workbook workbook, Font font, String colour);

	/**
	 * <p>
	 * Add a colour (specified as "rgb(<i>r</i>, <i>g</i>, <i>b</i>)") as the
	 * background colour of a CellStyle.
	 * </p>
	 * <p>
	 * In the current implementations the XSSF implementation will always produce
	 * exactly the right colour, whilst the HSSF implementation takes the best
	 * approximation from the current palette.
	 *
	 * @param workbook The workbook in which the Font is to be used, needed to
	 *                 obtain the colour palette.
	 * @param style    The style to which the colour is to be added.
	 * @param colour   The colour to add.
	 */
	public abstract void addBackgroundColourToStyle(Workbook workbook, CellStyle style, String colour);

	/**
	 * Check whether a cell is empty and unformatted.
	 *
	 * @param cell The cell to consider.
	 * @return true is the cell is empty and has no style or has no background fill.
	 */
	public static boolean cellIsEmpty(Cell cell) {
		// if (cell.getCellType() != Cell.CELL_TYPE_BLANK) {
		if (!CellType.BLANK.equals(cell.getCellType())) {
			return false;
		}
		CellStyle cellStyle = cell.getCellStyle();
		// if (cellStyle.getFillPattern() == CellStyle.NO_FILL) {
		if ((cellStyle == null) || FillPatternType.NO_FILL.equals(cellStyle.getFillPattern())) {
			return true;
		}
		return false;
	}

	/**
	 * Apply a BIRT border style to one side of a POI CellStyle usage: xls-format /
	 * StyleManagerHUtils
	 *
	 * @param workbook    The workbook that contains the cell being styled.
	 * @param style       The POI CellStyle that is to have the border applied to
	 *                    it.
	 * @param side        The side of the border that is to be applied.<br>
	 *                    Note that although this value is from XSSFCellBorder it is
	 *                    equally valid for HSSFCellStyles.
	 * @param colour      The colour for the new border.
	 * @param borderStyle The BIRT style for the new border.
	 * @param width       The width of the new border.
	 */
	public abstract void applyBorderStyle(Workbook workbook, CellStyle style, BorderSide side, CSSValue colour,
			CSSValue borderStyle, CSSValue width);

	/**
	 * Apply a BIRT border style to one side of a POI CellStyle. usage: xlsx-format
	 * / StyleManagerXUtils
	 *
	 * @param workbook  The workbook that contains the cell being styled.
	 * @param style     The POI CellStyle that is to have the border applied to it.
	 * @param birtStyle birt cell style with all border information
	 * @since 4.13
	 */
	public abstract void applyBorderStyle(Workbook workbook, CellStyle style, BirtStyle birtStyle);

	/**
	 * <p>
	 * Convert a MIME string into a Workbook.PICTURE* constant.
	 * </p>
	 * <p>
	 * In some cases BIRT fails to submit a MIME string, in which case this method
	 * falls back to basic data signatures for JPEG and PNG images.
	 * <p>
	 *
	 * @param mimeType The MIME type.
	 * @param data     The image data to consider if no recognisable MIME type is
	 *                 provided.
	 * @return A Workbook.PICTURE* constant.
	 */
	public int poiImageTypeFromMimeType(String mimeType, byte[] data) {
		if ("image/jpeg".equals(mimeType)) {
			return Workbook.PICTURE_TYPE_JPEG;
		} else if ("image/png".equals(mimeType)) {
			return Workbook.PICTURE_TYPE_PNG;
		} else if ("image/bmp".equals(mimeType)) {
			return Workbook.PICTURE_TYPE_DIB;
		} else {
			if (null != data) {
				log.debug("Data bytes: " + " " + Integer.toHexString(data[0]).toUpperCase() + " "
						+ Integer.toHexString(data[1]).toUpperCase() + " " + Integer.toHexString(data[2]).toUpperCase()
						+ " " + Integer.toHexString(data[3]).toUpperCase());
				if ((data.length > 2) && (data[0] == (byte) 0xFF) && (data[1] == (byte) 0xD8)
						&& (data[2] == (byte) 0xFF)) {
					return Workbook.PICTURE_TYPE_JPEG;
				}
				if ((data.length > 4) && (data[0] == (byte) 0x89) && (data[1] == (byte) 'P') && (data[2] == (byte) 'N')
						&& (data[3] == (byte) 'G')) {
					return Workbook.PICTURE_TYPE_PNG;
				}
			}
			return 0;
		}
	}

	/**
	 * Read an InputStream in full and put the results into a byte[]. <br>
	 * This is needed by the emitter to handle images accessed by URL.
	 *
	 * @param stream The InputStream to read.
	 * @param length The length of the InputStream
	 * @return A byte array containing the contents of the InputStream.
	 * @throws IOException
	 */
	public byte[] streamToByteArray(InputStream stream, int length) throws IOException {
		ByteArrayOutputStream buffer;
		if (length > 0) {
			buffer = new ByteArrayOutputStream(length);
		} else {
			buffer = new ByteArrayOutputStream();
		}

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = stream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}

	/**
	 * Read an image from a URLConnection into a byte array.
	 *
	 * @param conn The URLConnection to provide the data.
	 * @return A byte array containing the data downloaded from the URL.
	 */
	public byte[] downloadImage(URLConnection conn) {
		try {
			int contentLength = conn.getContentLength();
			InputStream imageStream = conn.getInputStream();
			try (imageStream) {
				return streamToByteArray(imageStream, contentLength);
			}
		} catch (IOException ex) {
			log.debug(ex.getClass(), ": ", ex.getMessage());
			return null;
		}

	}

	/**
	 * Convert a BIRT paper size string into a POI PrintSetup.*PAPERSIZE constant.
	 *
	 * @param name The paper size as a BIRT string.
	 * @return A POI PrintSetup.*PAPERSIZE constant.
	 */
	public short getPaperSizeFromString(String name) {
		if ("a4".equals(name)) {
			return PrintSetup.A4_PAPERSIZE;
		} else if ("a3".equals(name)) {
			return PrintSetup.A3_PAPERSIZE;
		} else if ("us-letter".equals(name)) {
			return PrintSetup.LETTER_PAPERSIZE;
		}

		return PrintSetup.A4_PAPERSIZE;
	}

	/**
	 * Check whether a DimensionType represents an absolute (physical) dimension.
	 *
	 * @param dim The DimensionType to consider.
	 * @return true if dim represents an absolute measurement.
	 */
	public boolean isAbsolute(DimensionType dim) {
		if (dim == null) {
			return false;
		}
		String units = dim.getUnits();
		return DimensionType.UNITS_CM.equals(units) || DimensionType.UNITS_IN.equals(units)
				|| DimensionType.UNITS_MM.equals(units) || DimensionType.UNITS_PT.equals(units)
				|| DimensionType.UNITS_PC.equals(units);
	}

	/**
	 * Check whether a DimensionType represents pixels.
	 *
	 * @param dim The DimensionType to consider.
	 * @return true if dim represents pixels.
	 */
	public boolean isPixels(DimensionType dim) {
		return (dim != null) && DimensionType.UNITS_PX.equals(dim.getUnits());
	}

	/**
	 * <p>
	 * Convert a BIRT number format to a POI data format.
	 * </p>
	 * <p>
	 * There is no way this function is complete! More special cases will be added
	 * as they are found.
	 * </p>
	 *
	 * @param birtFormat A string representing a number format in BIRT.
	 * @return A string representing a data format in Excel.
	 */
	private String poiNumberFormatFromBirt(String birtFormat) {
		if ("General Number".equalsIgnoreCase(birtFormat)) {
			return null;
		}
		if (birtFormat.startsWith(ExcelEmitter.CUSTOM_NUMBER_FORMAT)) {
			return birtFormat.substring(ExcelEmitter.CUSTOM_NUMBER_FORMAT.length());
		}

		birtFormat = birtFormat.replace("E00", "E+00");
		birtFormat = birtFormat.replaceAll("^([^0#.\\-,E;%\u2030\u00A4']*)", "\"$1\"");
		int brace = birtFormat.indexOf('{');
		if (brace >= 0) {
			birtFormat = birtFormat.substring(0, brace);
		}
		return birtFormat;
	}

	/**
	 * <p>
	 * Convert a BIRT date/time format to a POI data format.
	 * </p>
	 * <p>
	 * This function is likely to be more complete than poiNumberFormatFromBirt, but
	 * it is still likely to have issues. More special cases will be added as they
	 * are found.
	 * </p>
	 *
	 * @param birtFormat A string representing a date/time format in BIRT.
	 * @return A string representing a data format in Excel.
	 */
	private String poiDateTimeFormatFromBirt(String birtFormat, Locale locale) {
		if ("General Date".equalsIgnoreCase(birtFormat)) {
			birtFormat = DateFormatConverter.getJavaDateTimePattern(DateFormat.LONG, locale);
		}
		if ("Long Date".equalsIgnoreCase(birtFormat)) {
			birtFormat = DateFormatConverter.getJavaDatePattern(DateFormat.LONG, locale);
		}
		if ("Medium Date".equalsIgnoreCase(birtFormat)) {
			birtFormat = DateFormatConverter.getJavaDatePattern(DateFormat.MEDIUM, locale);
		}
		if ("Short Date".equalsIgnoreCase(birtFormat)) {
			birtFormat = DateFormatConverter.getJavaDatePattern(DateFormat.SHORT, locale);
		}
		if ("Long Time".equalsIgnoreCase(birtFormat)) {
			birtFormat = DateFormatConverter.getJavaTimePattern(DateFormat.LONG, locale);
		}
		if ("Medium Time".equalsIgnoreCase(birtFormat)) {
			birtFormat = DateFormatConverter.getJavaTimePattern(DateFormat.MEDIUM, locale);
		}
		if ("Short Time".equalsIgnoreCase(birtFormat)) {
			birtFormat = "kk:mm"; // DateFormatConverter.getJavaTimePattern(DateFormat.SHORT, locale);
		}
		return DateFormatConverter.convert(locale, birtFormat);
	}

	public static String getNumberFormat(BirtStyle style) {
		CSSValue dataFormat = style.getProperty(StyleConstants.STYLE_DATA_FORMAT);
		if (dataFormat instanceof DataFormatValue) {
			DataFormatValue dataFormatValue = (DataFormatValue) dataFormat;
			return dataFormatValue.getNumberPattern();
		}
		return null;
	}

	public static String getDateFormat(BirtStyle style) {
		CSSValue dataFormat = style.getProperty(StyleConstants.STYLE_DATA_FORMAT);
		if (dataFormat instanceof DataFormatValue) {
			DataFormatValue dataFormatValue = (DataFormatValue) dataFormat;
			return dataFormatValue.getDatePattern();
		}
		return null;
	}

	public static String getDateTimeFormat(BirtStyle style) {
		CSSValue dataFormat = style.getProperty(StyleConstants.STYLE_DATA_FORMAT);
		if (dataFormat instanceof DataFormatValue) {
			DataFormatValue dataFormatValue = (DataFormatValue) dataFormat;
			return dataFormatValue.getDateTimePattern();
		}
		return null;
	}

	public static String getTimeFormat(BirtStyle style) {
		CSSValue dataFormat = style.getProperty(StyleConstants.STYLE_DATA_FORMAT);
		if (dataFormat instanceof DataFormatValue) {
			DataFormatValue dataFormatValue = (DataFormatValue) dataFormat;
			return dataFormatValue.getTimePattern();
		}
		return null;
	}

	public static DataFormatValue cloneDataFormatValue(DataFormatValue dataValue) {
		DataFormatValue newValue = new DataFormatValue();
		newValue.setDateFormat(dataValue.getDatePattern(), dataValue.getDateLocale());
		newValue.setDateTimeFormat(dataValue.getDateTimePattern(), dataValue.getDateTimeLocale());
		newValue.setTimeFormat(dataValue.getTimePattern(), dataValue.getTimeLocale());
		newValue.setNumberFormat(dataValue.getNumberPattern(), dataValue.getNumberLocale());
		newValue.setStringFormat(dataValue.getStringPattern(), dataValue.getStringLocale());
		return newValue;
	}

	public static void setNumberFormat(BirtStyle style, String pattern, String locale) {
		DataFormatValue dfv = (DataFormatValue) style.getProperty(StyleConstants.STYLE_DATA_FORMAT);
		if (dfv == null) {
			dfv = new DataFormatValue();
		} else {
			dfv = cloneDataFormatValue(dfv);
		}
		dfv.setNumberFormat(pattern, locale);
		style.setProperty(StyleConstants.STYLE_DATA_FORMAT, dfv);
	}

	public static void setDateFormat(BirtStyle style, String pattern, String locale) {
		DataFormatValue dfv = (DataFormatValue) style.getProperty(StyleConstants.STYLE_DATA_FORMAT);
		if (dfv == null) {
			dfv = new DataFormatValue();
		} else {
			dfv = cloneDataFormatValue(dfv);
		}
		dfv.setDateFormat(pattern, locale);
		style.setProperty(StyleConstants.STYLE_DATA_FORMAT, dfv);
	}

	public static void setDateTimeFormat(BirtStyle style, String pattern, String locale) {
		DataFormatValue dfv = (DataFormatValue) style.getProperty(StyleConstants.STYLE_DATA_FORMAT);
		if (dfv == null) {
			dfv = new DataFormatValue();
		} else {
			dfv = cloneDataFormatValue(dfv);
		}
		dfv.setDateTimeFormat(pattern, locale);
		style.setProperty(StyleConstants.STYLE_DATA_FORMAT, dfv);
	}

	public static void setTimeFormat(BirtStyle style, String pattern, String locale) {
		DataFormatValue dfv = (DataFormatValue) style.getProperty(StyleConstants.STYLE_DATA_FORMAT);
		if (dfv == null) {
			dfv = new DataFormatValue();
		} else {
			dfv = cloneDataFormatValue(dfv);
		}
		dfv.setTimeFormat(pattern, locale);
		style.setProperty(StyleConstants.STYLE_DATA_FORMAT, dfv);
	}

	/**
	 * Apply a BIRT number/date/time format to a POI CellStyle.
	 *
	 * @param workbook  The workbook containing the CellStyle (needed to create a
	 *                  new DataFormat).
	 * @param birtStyle The BIRT style which may contain a number format.
	 * @param poiStyle  The CellStyle that is to receive the number format.
	 */
	public void applyNumberFormat(Workbook workbook, BirtStyle birtStyle, CellStyle poiStyle, Locale locale) {
		String dataFormat = null;
		String format = getNumberFormat(birtStyle);
		if (format != null) {
			log.debug("BIRT number format == ", format);
			dataFormat = poiNumberFormatFromBirt(format);
		} else {
			format = getDateTimeFormat(birtStyle);
			if (format != null) {
				log.debug("BIRT date/time format == ", format);
				dataFormat = poiDateTimeFormatFromBirt(format, locale);
			} else {
				format = getTimeFormat(birtStyle);
				if (format != null) {
					log.debug("BIRT time format == ", format);
					dataFormat = poiDateTimeFormatFromBirt(format, locale);
				} else {
					format = getDateFormat(birtStyle);
					if (format != null) {
						log.debug("BIRT date format == ", format);
						dataFormat = poiDateTimeFormatFromBirt(format, locale);
					}
				}
			}
		}
		if (dataFormat != null) {
			DataFormat poiFormat = workbook.createDataFormat();
			log.debug("Setting POI data format to ", dataFormat);
			poiStyle.setDataFormat(poiFormat.getFormat(dataFormat));
		}
	}

	/**
	 * Add font details to an AttributedString.
	 *
	 * @param attrString The AttributedString to modify.
	 * @param font       The font to take attributes from.
	 * @param startIdx   The index of the first character to be attributed
	 *                   (inclusive).
	 * @param endIdx     The index of the last character to be attributed
	 *                   (inclusive).
	 */
	protected void addFontAttributes(AttributedString attrString, Font font, int startIdx, int endIdx) {
		attrString.addAttribute(TextAttribute.FAMILY, font.getFontName(), startIdx, endIdx);
		attrString.addAttribute(TextAttribute.SIZE, (float) font.getFontHeightInPoints(), startIdx, endIdx);
		// if (font.getBoldweight() == Font.BOLDWEIGHT_BOLD)
		if (font.getBold()) {
			attrString.addAttribute(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD, startIdx, endIdx);
		}
		if (font.getItalic()) {
			attrString.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE, startIdx, endIdx);
		}
		if (font.getUnderline() == Font.U_SINGLE) {
			attrString.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, startIdx, endIdx);
		}
	}

	/**
	 * Find a RichTextRun that includes a specific index.
	 *
	 * @param richTextRuns The list of RichTextRuns to search.
	 * @param startIndex   The character index being sought.
	 * @return The index into richTextRuns such that
	 *         richTextRuns.get(index).startIndex has the largest value less that
	 *         startIndex.
	 */
	protected int getRichTextRunIndexForStart(List<RichTextRun> richTextRuns, int startIndex) {
		if (richTextRuns.isEmpty()) {
			return -1;
		}
		for (int i = 0; i < richTextRuns.size(); ++i) {
			if (richTextRuns.get(i).startIndex >= startIndex) {
				return i - 1;
			}
		}
		return richTextRuns.size() - 1;
	}

	/**
	 * Calculate the height of a string formatted according to a set of RichTextRuns
	 * and fitted within a give width.
	 *
	 * @param sourceText   The string to be measured.
	 * @param defaultFont  The font to be used prior to the first RichTextRun.
	 * @param widthMM      The width of the output.
	 * @param richTextRuns The list of RichTextRuns to be applied to the string
	 * @return The heigh, in points, of a box big enough to contain the formatted
	 *         sourceText.
	 */
	public float calculateTextHeightPoints(String sourceText, Font defaultFont, double widthMM,
			List<RichTextRun> richTextRuns) {
		log.debug("Calculating height for ", sourceText);

		final float widthPt = (float) (72 * Math.max(0, widthMM - 6) / 25.4);

		float totalHeight = 0;
		String[] textLines = sourceText.split("\n");
		int lineStartIndex = 0;
		String lastLine = null;
		Font font = defaultFont;
		for (String textLine : textLines) {
			if (lastLine != null) {
				lineStartIndex += lastLine.length() + 1;
			}
			lastLine = textLine;

			AttributedString attrString = new AttributedString(textLine.isEmpty() ? " " : textLine);
			int runEnd = textLine.length();

			int richTextRunIndex = getRichTextRunIndexForStart(richTextRuns, lineStartIndex);
			if (richTextRunIndex >= 0) {
				font = richTextRuns.get(richTextRunIndex).font;
				if ((richTextRunIndex < richTextRuns.size() - 1)
						&& (richTextRuns.get(richTextRunIndex + 1).startIndex < runEnd)) {
					runEnd = richTextRuns.get(richTextRunIndex + 1).startIndex;
				}
			}

			log.debug("Adding attribute - [", 0, " - ", runEnd, "] = ", defaultFont.getFontName(), " ",
					defaultFont.getFontHeightInPoints(), "pt");
			addFontAttributes(attrString, font, 0, textLine.isEmpty() ? 1 : runEnd);

			for (++richTextRunIndex; (richTextRunIndex < richTextRuns.size())
					&& (richTextRuns.get(richTextRunIndex).startIndex < lineStartIndex
							+ textLine.length()); ++richTextRunIndex) {
				RichTextRun run = richTextRuns.get(richTextRunIndex);
				RichTextRun nextRun = richTextRunIndex < richTextRuns.size() - 1
						? richTextRuns.get(richTextRunIndex + 1)
						: null;
				if ((run.startIndex >= lineStartIndex) && (run.startIndex < lineStartIndex + textLine.length() + 1)) {
					int startIdx = run.startIndex - lineStartIndex;
					int endIdx = (nextRun == null ? sourceText.length() : nextRun.startIndex) - lineStartIndex;
					if (endIdx > textLine.length()) {
						endIdx = textLine.length();
					}
					if (startIdx < endIdx) {
						log.debug("Adding attribute: [", startIdx, " - ", endIdx, "] = ", run.font.getFontName(), " ",
								run.font.getFontHeightInPoints(), "pt");
						addFontAttributes(attrString, run.font, startIdx, endIdx);
					}
				}
			}

			LineBreakMeasurer measurer = new LineBreakMeasurer(attrString.getIterator(), frc);

			float heightAdjustment = 0.0F;
			int lineLength = textLine.isEmpty() ? 1 : textLine.length();
			while (measurer.getPosition() < lineLength) {
				TextLayout layout = measurer.nextLayout(widthPt);
				float lineHeight = layout.getAscent() + layout.getDescent() + layout.getLeading();
				if (layout.getDescent() + layout.getLeading() > heightAdjustment) {
					heightAdjustment = layout.getDescent() + layout.getLeading();
				}
				log.debug("Line: ", textLine, " gives height ", lineHeight, "(", layout.getAscent(), "/",
						layout.getDescent(), "/", layout.getLeading(), ")");
				totalHeight += lineHeight;
			}
			totalHeight += heightAdjustment;

		}
		log.debug("Height calculated as ", totalHeight);
		return totalHeight;
	}

	protected String contrastColour(int colour[]) {
		if ((colour[0] == 0) && (colour[1] == 0) && (colour[2] == 0)) {
			return "white";
		} else {
			return "black";
		}
	}

	protected int[] rgbOnly(int rgb[]) {
		if (rgb == null) {
			return new int[] { 0, 0, 0 };
		} else if (rgb.length == 3) {
			return rgb;
		} else if (rgb.length > 3) {
			return new int[] { rgb[rgb.length - 3], rgb[rgb.length - 2], rgb[rgb.length - 1] };
		} else if (rgb.length == 2) {
			return new int[] { rgb[0], rgb[1], 0 };
		} else if (rgb.length == 2) {
			return new int[] { rgb[0], 0, 0 };
		} else {
			return new int[] { 0, 0, 0 };
		}
	}

	protected int[] rgbOnly(byte rgb[]) {
		if (rgb == null) {
			return new int[] { 0, 0, 0 };
		} else if (rgb.length >= 3) {
			return new int[] { (int) rgb[rgb.length - 3] & 0xFF, (int) rgb[rgb.length - 2] & 0xFF,
					(int) rgb[rgb.length - 1] & 0xFF };
		} else if (rgb.length == 2) {
			return new int[] { (int) rgb[0] & 0xFF, (int) rgb[1] & 0xFF, 0 };
		} else if (rgb.length == 2) {
			return new int[] { (int) rgb[0] & 0xFF, 0, 0 };
		} else {
			return new int[] { 0, 0, 0 };
		}
	}

	int[] parseColour(String colour, String defaultColour) {
		if ((colour == null) || (CSSConstants.CSS_TRANSPARENT_VALUE.equals(colour))
				|| (CSSConstants.CSS_AUTO_VALUE.equals(colour))) {
			return rgbOnly(ColorUtil.getRGBs(defaultColour));
		} else {
			return rgbOnly(ColorUtil.getRGBs(colour));
		}
	}

	public abstract Font correctFontColorIfBackground(FontManager fm, Workbook wb, BirtStyle birtStyle, Font font);

	public void correctFontColorIfBackground(BirtStyle birtStyle) {
		CSSValue bgColour = birtStyle.getProperty(StyleConstants.STYLE_BACKGROUND_COLOR);
		CSSValue fgColour = birtStyle.getProperty(StyleConstants.STYLE_COLOR);

		int bgRgb[] = parseColour(bgColour == null ? null : bgColour.getCssText(), "white");
		int fgRgb[] = parseColour(fgColour == null ? null : fgColour.getCssText(), "black");

		if ((bgRgb[0] == fgRgb[0]) && (bgRgb[1] == fgRgb[1]) && (bgRgb[2] == fgRgb[2])) {
			CSSValue newColour = new StringValue(StringValue.CSS_STRING, contrastColour(bgRgb));
			birtStyle.setProperty(StyleConstants.STYLE_COLOR, newColour);
		}
	}

	/**
	 * Convert a horizontal position in a column (in mm) to a ClientAnchor DX
	 * position.
	 *
	 * @param width    The position within the column.
	 * @param colWidth The width of the column.
	 * @return A value suitable for use as an argument to setDx2() on ClientAnchor.
	 */
	public abstract int anchorDxFromMM(double width, double colWidth);

	/**
	 * Convert a vertical position in a row (in points) to a ClientAnchor DY
	 * position.
	 *
	 * @param height    The position within the row.
	 * @param rowHeight The height of the row.
	 * @return A value suitable for use as an argument to setDy2() on ClientAnchor.
	 *         *
	 */
	public abstract int anchorDyFromPoints(float height, float rowHeight);

	/**
	 * Prepare the margin dimensions on the sheet as per the BIRT page.
	 *
	 * @param page The BIRT page.
	 */
	public abstract void prepareMarginDimensions(Sheet sheet, IPageContent page);

	/**
	 * Place a border around a region on the current sheet. This is used to apply
	 * borders to entire rows or entire tables.
	 *
	 * @param sm
	 * @param sheet
	 *
	 * @param colStart    The column marking the left-side boundary of the region.
	 * @param colEnd      The column marking the right-side boundary of the region.
	 * @param rowStart    The row marking the top boundary of the region.
	 * @param rowEnd      The row marking the bottom boundary of the region.
	 * @param borderStyle The BIRT border style to apply to the region.
	 */
	public void applyBordersToArea(StyleManager sm, Sheet sheet, int colStart, int colEnd, int rowStart, int rowEnd,
			BirtStyle borderStyle) {
		StringBuilder borderMsg = new StringBuilder();
		borderMsg.append("applyBordersToArea [").append(colStart).append(",").append(rowStart).append("]-[")
				.append(colEnd).append(",").append(rowEnd).append("]");

		CSSValue borderStyleBottom = borderStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_STYLE);
		CSSValue borderWidthBottom = borderStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH);
		CSSValue borderColourBottom = borderStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_COLOR);
		CSSValue borderStyleLeft = borderStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_STYLE);
		CSSValue borderWidthLeft = borderStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH);
		CSSValue borderColourLeft = borderStyle.getProperty(StyleConstants.STYLE_BORDER_LEFT_COLOR);
		CSSValue borderStyleRight = borderStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_STYLE);
		CSSValue borderWidthRight = borderStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH);
		CSSValue borderColourRight = borderStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_COLOR);
		CSSValue borderStyleTop = borderStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_STYLE);
		CSSValue borderWidthTop = borderStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH);
		CSSValue borderColourTop = borderStyle.getProperty(StyleConstants.STYLE_BORDER_TOP_COLOR);

		CSSValue borderStyleDiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_DIAGONAL_STYLE);
		CSSValue borderWidthDiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_DIAGONAL_WIDTH);
		CSSValue borderColourDiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_DIAGONAL_COLOR);
		CSSValue borderStyleAntidiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_STYLE);
		CSSValue borderWidthAntidiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_WIDTH);
		CSSValue borderColourAntidiagonal = borderStyle.getProperty(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_COLOR);

		/*
		 * borderMsg.append( ", Bottom:" ).append( borderStyleBottom ).append( "/"
		 * ).append( borderWidthBottom ).append( "/" + borderColourBottom );
		 * borderMsg.append( ", Left:" ).append( borderStyleLeft ).append( "/" ).append(
		 * borderWidthLeft ).append( "/" + borderColourLeft ); borderMsg.append(
		 * ", Right:" ).append( borderStyleRight ).append( "/" ).append(
		 * borderWidthRight ).append( "/" ).append( borderColourRight );
		 * borderMsg.append( ", Top:" ).append( borderStyleTop ).append( "/" ).append(
		 * borderWidthTop ).append( "/" ).append( borderColourTop ); log.debug(
		 * borderMsg.toString() );
		 */
		if ((borderStyleBottom == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleBottom))
				|| (borderWidthBottom == null) || ("0".equals(borderWidthBottom)) || (borderColourBottom == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourBottom.getCssText()))) {
			borderStyleBottom = null;
			borderWidthBottom = null;
			borderColourBottom = null;
		}

		if ((borderStyleLeft == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleLeft))
				|| (borderWidthLeft == null) || ("0".equals(borderWidthLeft)) || (borderColourLeft == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourLeft.getCssText()))) {
			borderStyleLeft = null;
			borderWidthLeft = null;
			borderColourLeft = null;
		}

		if ((borderStyleRight == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleRight))
				|| (borderWidthRight == null) || ("0".equals(borderWidthRight)) || (borderColourRight == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourRight.getCssText()))) {
			borderStyleRight = null;
			borderWidthRight = null;
			borderColourRight = null;
		}

		if ((borderStyleTop == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleTop)) || (borderWidthTop == null)
				|| ("0".equals(borderWidthTop)) || (borderColourTop == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourTop.getCssText()))) {
			borderStyleTop = null;
			borderWidthTop = null;
			borderColourTop = null;
		}

		if ((borderStyleDiagonal == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleDiagonal))
				|| (borderWidthDiagonal == null) || ("0".equals(borderWidthDiagonal)) || (borderColourDiagonal == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourDiagonal.getCssText()))) {
			borderStyleDiagonal = null;
			borderWidthDiagonal = null;
			borderColourDiagonal = null;
		}

		if ((borderStyleAntidiagonal == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleAntidiagonal))
				|| (borderWidthAntidiagonal == null) || ("0".equals(borderWidthDiagonal))
				|| (borderColourDiagonal == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourAntidiagonal.getCssText()))) {
			borderStyleAntidiagonal = null;
			borderWidthAntidiagonal = null;
			borderColourAntidiagonal = null;
		}

		if ((borderStyleBottom != null) || (borderWidthBottom != null) || (borderColourBottom != null)
				|| (borderStyleLeft != null) || (borderWidthLeft != null) || (borderColourLeft != null)
				|| (borderStyleRight != null) || (borderWidthRight != null) || (borderColourRight != null)
				|| (borderStyleTop != null) || (borderWidthTop != null) || (borderColourTop != null)
				|| (borderStyleDiagonal != null) || (borderWidthDiagonal != null) || (borderColourDiagonal != null)
				|| (borderStyleAntidiagonal != null) || (borderWidthAntidiagonal != null)
				|| (borderColourAntidiagonal != null)
		) {
			for (int row = rowStart; row <= rowEnd; ++row) {
				Row styleRow = sheet.getRow(row);
				if (styleRow != null) {
					for (int col = colStart; col <= colEnd; ++col) {
						if ((col == colStart) || (col == colEnd) || (row == rowStart) || (row == rowEnd)) {
							Cell styleCell = styleRow.getCell(col);
							if (styleCell == null) {
								log.debug("Creating cell[", row, ",", col, "]");
								styleCell = styleRow.createCell(col);
							}
							if (styleCell != null) {
								// log.debug( "Applying border to cell [R" + styleCell.getRowIndex() + "C" +
								// styleCell.getColumnIndex() + "]");
								CellStyle newStyle = sm.getStyleWithBorders(styleCell.getCellStyle(),
										((row == rowEnd) ? borderStyleBottom : null),
										((row == rowEnd) ? borderWidthBottom : null),
										((row == rowEnd) ? borderColourBottom : null),
										((col == colStart) ? borderStyleLeft : null),
										((col == colStart) ? borderWidthLeft : null),
										((col == colStart) ? borderColourLeft : null),
										((col == colEnd) ? borderStyleRight : null),
										((col == colEnd) ? borderWidthRight : null),
										((col == colEnd) ? borderColourRight : null),
										((row == rowStart) ? borderStyleTop : null),
										((row == rowStart) ? borderWidthTop : null),
										((row == rowStart) ? borderColourTop : null),
										((row == rowStart) ? borderStyleDiagonal : null),
										((row == rowStart) ? borderWidthDiagonal : null),
										((row == rowStart) ? borderColourDiagonal : null),
										((row == rowStart) ? borderStyleAntidiagonal : null),
										((row == rowStart) ? borderWidthAntidiagonal : null),
										((row == rowStart) ? borderColourAntidiagonal : null)

								);
								styleCell.setCellStyle(newStyle);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Place a border around a region on the current sheet. This is used to apply
	 * borders to entire rows or entire tables.
	 *
	 * @param colStart    The column marking the left-side boundary of the region.
	 * @param colEnd      The column marking the right-side boundary of the region.
	 * @param row         The row to get a bottom border.
	 * @param borderStyle The BIRT border style to apply to the region.
	 */
	public void applyBottomBorderToRow(StyleManager sm, Sheet sheet, int colStart, int colEnd, int row,
			BirtStyle borderStyle) {
		CSSValue borderStyleBottom = borderStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_STYLE);
		CSSValue borderWidthBottom = borderStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH);
		CSSValue borderColourBottom = borderStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_COLOR);

		if ((borderStyleBottom == null) || (CSSConstants.CSS_NONE_VALUE.equals(borderStyleBottom.getCssText()))
				|| (borderWidthBottom == null) || ("0".equals(borderWidthBottom)) || (borderColourBottom == null)
				|| (CSSConstants.CSS_TRANSPARENT_VALUE.equals(borderColourBottom.getCssText()))) {
			borderStyleBottom = null;
			borderWidthBottom = null;
			borderColourBottom = null;
		}

		if ((borderStyleBottom != null) || (borderWidthBottom != null) || (borderColourBottom != null)) {
			Row styleRow = sheet.getRow(row);
			if (styleRow != null) {
				for (int col = colStart; col <= colEnd; ++col) {
					Cell styleCell = styleRow.getCell(col);
					if (styleCell == null) {
						styleCell = styleRow.createCell(col);
					}
					if (styleCell != null) {
						// log.debug( "Applying border to cell [R" + styleCell.getRowIndex() + "C" +
						// styleCell.getColumnIndex() + "]");
						CellStyle newStyle = sm.getStyleWithBorders(styleCell.getCellStyle(), borderStyleBottom,
								borderWidthBottom, borderColourBottom, null, null, null, null, null, null, null, null,
								null, null, null, null, null, null,
								null);
						styleCell.setCellStyle(newStyle);
					}
				}
			}
		}
	}

	public int applyAreaBordersToCell(Collection<AreaBorders> knownAreaBorders, Cell cell, BirtStyle birtCellStyle,
			int rowIndex, int colIndex) {
		for (AreaBorders areaBorders : knownAreaBorders) {
			if ((areaBorders.bottom == rowIndex)
					&& ((areaBorders.left <= colIndex) && (areaBorders.right >= colIndex))) {
				if ((areaBorders.cssStyle[0] != null) && (areaBorders.cssWidth[0] != null)
						&& (areaBorders.cssColour[0] != null)) {
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_BOTTOM_STYLE, areaBorders.cssStyle[0]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH, areaBorders.cssWidth[0]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_BOTTOM_COLOR, areaBorders.cssColour[0]);
				}
			}
			if ((areaBorders.left == colIndex) && ((areaBorders.top <= rowIndex)
					&& ((areaBorders.bottom < 0) || (areaBorders.bottom >= rowIndex)))) {
				if ((areaBorders.cssStyle[1] != null) && (areaBorders.cssWidth[1] != null)
						&& (areaBorders.cssColour[1] != null)) {
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_LEFT_STYLE, areaBorders.cssStyle[1]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH, areaBorders.cssWidth[1]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_LEFT_COLOR, areaBorders.cssColour[1]);
				}
			}
			if ((areaBorders.right == colIndex) && ((areaBorders.top <= rowIndex)
					&& ((areaBorders.bottom < 0) || (areaBorders.bottom >= rowIndex)))) {
				if ((areaBorders.cssStyle[2] != null) && (areaBorders.cssWidth[2] != null)
						&& (areaBorders.cssColour[2] != null)) {
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_RIGHT_STYLE, areaBorders.cssStyle[2]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH, areaBorders.cssWidth[2]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_RIGHT_COLOR, areaBorders.cssColour[2]);
				}
			}
			if ((areaBorders.top == rowIndex) && ((areaBorders.left <= colIndex) && (areaBorders.right >= colIndex))) {
				if ((areaBorders.cssStyle[3] != null) && (areaBorders.cssWidth[3] != null)
						&& (areaBorders.cssColour[3] != null)) {
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_TOP_STYLE, areaBorders.cssStyle[3]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH, areaBorders.cssWidth[3]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_TOP_COLOR, areaBorders.cssColour[3]);
				}
			}
			if ((areaBorders.left == colIndex) && ((areaBorders.top <= rowIndex)
					&& ((areaBorders.bottom < 0) || (areaBorders.bottom >= rowIndex)))) {
				if ((areaBorders.cssStyle[4] != null) && (areaBorders.cssWidth[4] != null)
						&& (areaBorders.cssColour[4] != null)) {
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_DIAGONAL_STYLE, areaBorders.cssStyle[4]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_DIAGONAL_WIDTH, areaBorders.cssWidth[4]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_DIAGONAL_COLOR, areaBorders.cssColour[4]);
				}
			}
			if ((areaBorders.left == colIndex) && ((areaBorders.top <= rowIndex)
					&& ((areaBorders.bottom < 0) || (areaBorders.bottom >= rowIndex)))) {
				if ((areaBorders.cssStyle[5] != null) && (areaBorders.cssWidth[5] != null)
						&& (areaBorders.cssColour[5] != null)) {
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_STYLE, areaBorders.cssStyle[5]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_WIDTH, areaBorders.cssWidth[5]);
					birtCellStyle.setProperty(StyleConstants.STYLE_BORDER_ANTIDIAGONAL_COLOR, areaBorders.cssColour[5]);
				}
			}
		}
		return colIndex;
	}

	public void extendRows(HandlerState state, int startRow, int startCol, int endRow, int endCol) {
		for (int colNum = startCol; colNum < endCol; ++colNum) {
			Cell lastCell = null;
			for (int rowNum = startRow; rowNum < endRow; ++rowNum) {
				Row row = state.currentSheet.getRow(rowNum);
				if (row != null) {
					Cell cell = row.getCell(colNum);
					if (cell != null) {
						lastCell = cell;
					}
				}
			}
			if ((lastCell != null) && (lastCell.getRowIndex() < endRow - 1)) {
				CellRangeAddress range = new CellRangeAddress(lastCell.getRowIndex(), endRow - 1,
						lastCell.getColumnIndex(), lastCell.getColumnIndex());
				log.debug("Extend: merging from [", range.getFirstRow(), ",", range.getFirstColumn(), "] to [",
						range.getLastRow(), ",", range.getLastColumn(), "]");
				state.currentSheet.addMergedRegion(range);
				for (int rowNum = lastCell.getRowIndex() + 1; rowNum < endRow; ++rowNum) {
					Row row = state.currentSheet.getRow(rowNum);
					if (row == null) {
						log.error(0, "Creating a row (for column " + colNum + "), this really shouldn't be necessary",
								null);
						row = state.currentSheet.createRow(rowNum);
					}
					Cell cell = row.createCell(colNum);
					cell.setCellStyle(lastCell.getCellStyle());
				}
			}
		}
	}

}
