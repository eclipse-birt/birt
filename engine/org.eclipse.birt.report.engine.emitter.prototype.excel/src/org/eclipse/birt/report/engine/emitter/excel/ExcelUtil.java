/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelLayoutEngine;
import org.eclipse.birt.report.engine.ir.DimensionType;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.Currency;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class ExcelUtil {

	public static final int SHEETNAME_LENGTH = 31;
	private static final char EXCEL_DECIMAL_SEPARATOR = '.';
	public final static int maxCellTextLength = 32767;
	private final static String scienticPattern = "0*.*0*E0*";
	private static final double SECONDS_PER_DAY = 86400.0;
	private static final double SECONDS_PER_MINUTE = 60.0;
	private static final double SECONDS_PER_HOUR = 3600.0;
	protected static final BigDecimal MAX_DOUBLE = new BigDecimal(Double.MAX_VALUE);
	protected static final BigDecimal MIN_DOUBLE = MAX_DOUBLE.negate().subtract(BigDecimal.ONE);
	protected static final BigDecimal MIN_POSITIVE_DECIMAL_NUMBER = new BigDecimal("0.000000000000001");
	protected static final BigDecimal MAX_POSITIVE_DECIMAL_NUMBER = new BigDecimal(10e15)
			.subtract(MIN_POSITIVE_DECIMAL_NUMBER);

	protected static final BigDecimal MIN_NEGATIVE_DECIMAL_NUMBER = new BigDecimal(-10e14)
			.add(new BigDecimal("0.000000000000001"));
	protected static final BigDecimal MAX_NEGATIVE_DECIMAL_NUMBER = MIN_POSITIVE_DECIMAL_NUMBER.negate();
	protected static final long MILLISECS_PER_DAY = 24 * 3600 * 1000;
	// TODO: time zone
	private static final long BASE_DATE_TIME;
	private static final String validStr = "#.0<>()%_";
	private static final String specialStr = "mMdDyYhHsSeEbBgGnN/*\"@";

	// for unicode currency symbols : "£¢€￥¥"
	private static final String currencySymbol = "\u00a3\u00a2\u20ac\uffe5\u00a5";
	protected static Logger logger = Logger.getLogger(ExcelUtil.class.getName());

	private static final HashSet<Character> splitChar = new HashSet<Character>();

	private static Pattern pattern = Pattern.compile(scienticPattern,
			Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

	private static Map<String, String> formatCache = new HashMap<String, String>();

	private static Map<String, String> namedPatterns = new HashMap<String, String>();

	static {

		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("GMT-0:00"));
		date.setTime(new Date("1900/01/01"));
		BASE_DATE_TIME = date.getTimeInMillis();

		splitChar.add(Character.valueOf(' '));
		splitChar.add(Character.valueOf('\r'));
		splitChar.add(Character.valueOf('\n'));

		namedPatterns.put("Fixed", "Fixed");
		namedPatterns.put("Percent", "Percent");
		namedPatterns.put("Scientific", "Scientific");
		namedPatterns.put("Standard", "Standard");
		namedPatterns.put("General Number", "General");

	}

	public final static float INCH_PT = 72;

	public final static float PT_TWIPS = 20;

	public final static float INCH_TWIPS = INCH_PT * PT_TWIPS;

	public final static int PAPER_LETTER = 1;// 8.5in*11in
	public final static int PAPER_TABLOID = 3;// 11in*17in
	public final static int PAPER_LEGAL = 5;// 8.5in*14in
	public final static int PAPER_STATEMENT = 6;// 5.5in*8.5in
	public final static int PAPER_EXECUTIVE = 7;// 7.25in*10.5in

	public final static int PAPER_A3 = 8;// 297mm*420mm
	public final static int PAPER_A4 = 9;// 210mm*297mm
	public final static int PAPER_A5 = 11;// 148mm*210mm
	public final static int PAPER_B4 = 12;// 250mm*353mm
	public final static int PAPER_B5 = 13;// 176mm*250mm

	public final static int PAPER_FOLIO = 14;// 8.5in*13in
	public final static int PAPER_10_ENVELOP = 20;// 4.125in*9.5in;
	public final static int PAPER_DL_ENVELOPE = 27;// 110mm*220mm
	public final static int PAPER_C5_ENVELOPE = 28;// 162mm*229mm
	public final static int PAPER_B5_ENVELOPE = 34;// 176mm*250mm
	public final static int PAPER_MONARCH_ENVELOPE = 37;// 3.875in*7.5in
	public final static int PAPER_ISOB4 = 42;// 250mm*353mm;

	public static String ridQuote(String val) {
		if (val.charAt(0) == '"' && val.charAt(val.length() - 1) == '"') {
			return val.substring(1, val.length() - 1);
		}
		return val;
	}

	// For unicode characters: "`~!@#$%^&*()-=+\\|[]{};:'\",./?><
	// \t\n\r\！￥（）：；，"

	private static String invalidBookmarkChars = "`~!@#$%^&*()-=+\\|[]{};:'\",./?>< \t\n\r\uff01\uffe5\uff08\uff09\uff1a\uff1b\uff0c";

	// This check can not cover all cases, cause we do not know exactly the
	// excel range name restraint.
	public static boolean isValidBookmarkName(String name) {
		if (name.equalsIgnoreCase("r")) {
			return false;
		}
		if (name.equalsIgnoreCase("c")) {
			return false;
		}
		if (name.startsWith(ExcelLayoutEngine.AUTO_GENERATED_BOOKMARK)) {
			return false;
		}
		for (int i = 0; i < name.length(); i++) {
			if (invalidBookmarkChars.indexOf(name.charAt(i)) != -1) {
				return false;
			}
		}

		// The bookmark name can not start with a digit.
		if (name.matches("[0-9].*")) {
			return false;
		}
		// columnID<=IV, rowID<=65536 can not be used as bookmark.
		if (name.matches("([A-Za-z]|[A-Ha-h][A-Za-z]|[Ii][A-Va-v])[0-9]{1,5}.*")) {
			String[] strs = name.split("[A-Za-z]");
			if (strs.length > 0) {
				int rowId = 0;
				try {
					rowId = Integer.parseInt(strs[strs.length - 1]);
				} catch (NumberFormatException e) {
					return true;
				}
				if (rowId <= 65536) {
					return false;
				} else {
					return true;
				}
			}
			return true;
		}
		return true;
	}

	/**
	 * format the date with default time zone
	 * 
	 * @param data
	 * @return
	 */
	public static String formatDate(Object data) {
		return formatDate(data, null);
	}

	/**
	 * format the date with defined time zone.
	 * 
	 * @param data
	 * @param timeZone
	 * @return
	 */
	public static String formatDate(Object data, TimeZone timeZone) {
		Date date = getDate(data);
		if (date == null) {
			return null;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ENGLISH);
		if (timeZone != null && needAdjustWithTimeZone(date)) {
			dateFormat.setTimeZone(timeZone);
		}
		return dateFormat.format(date);
	}

	/**
	 * test if the output need adjust with time zone.
	 * 
	 * @param date
	 * @return
	 */
	protected static boolean needAdjustWithTimeZone(Date date) {
		if (date instanceof java.sql.Date) {
			return false;
		}
		return true;
	}

	public static Date getDate(Object data) {
		Date date = null;
		if (data instanceof com.ibm.icu.util.Calendar) {
			date = ((com.ibm.icu.util.Calendar) data).getTime();
		} else if (data instanceof Date) {
			date = (Date) data;
		} else {
			date = null;
		}
		return date;
	}

	public static String formatNumberAsDecimal(Object data) {
		Number number = (Number) data;
		DecimalFormat numberFormat = new DecimalFormat("0.##############");
		numberFormat.setMaximumFractionDigits(15);
		updateExcelDecimalSeparator(numberFormat);
		return numberFormat.format(number);
	}

	private static void updateExcelDecimalSeparator(DecimalFormat numberFormat) {
		DecimalFormatSymbols symbol = numberFormat.getDecimalFormatSymbols();
		if (symbol.getDecimalSeparator() != EXCEL_DECIMAL_SEPARATOR) {
			symbol.setDecimalSeparator(EXCEL_DECIMAL_SEPARATOR);
			numberFormat.setDecimalFormatSymbols(symbol);
		}
	}

	public static String formatNumberAsScienceNotation(Number data) {
		assert data instanceof BigDecimal;
		BigDecimal bigDecimal = (BigDecimal) data;
		int scale = 0;
		if (bigDecimal.compareTo(BigDecimal.ZERO) == 0) {
			return "0";
		}
		String prefix = "";
		if (bigDecimal.compareTo(BigDecimal.ZERO) == -1) {
			prefix = "-";
			bigDecimal = bigDecimal.negate();
		}
		if (bigDecimal.compareTo(BigDecimal.ONE) == -1) {
			while (bigDecimal.compareTo(BigDecimal.ONE) == -1) {
				bigDecimal = bigDecimal.movePointRight(1);
				scale = scale - 1;
			}
		} else {
			while (bigDecimal.compareTo(BigDecimal.TEN) == 1) {
				bigDecimal = bigDecimal.movePointLeft(1);
				scale = scale + 1;
			}
		}
		DecimalFormat decimalFormat = new DecimalFormat("0.##############");
		updateExcelDecimalSeparator(decimalFormat);
		String number = decimalFormat.format(bigDecimal);
		String sign = scale >= 0 ? "+" : "";
		return prefix + number + "E" + sign + scale;
	}

	public static int getType(Object val) {
		if (val instanceof Number) {
			return SheetData.NUMBER;
		} else if (val instanceof Date) {
			return SheetData.DATE;
		} else if (val instanceof Calendar) {
			return SheetData.CALENDAR;
		} else if (val instanceof Boolean) {
			return SheetData.BOOLEAN;
		} else {
			return SheetData.STRING;
		}
	}

	private static String replaceDateFormat(String pattern) {
		if (pattern == null) {
			String rg = "";

			return rg;
		}

		StringBuffer toAppendTo = new StringBuffer();
		boolean inQuote = false;
		char prevCh = 0;
		int count = 0;

		for (int i = 0; i < pattern.length(); ++i) {
			char ch = pattern.charAt(i);

			if (ch != prevCh && count > 0) {
				toAppendTo.append(subReplaceDateFormat(prevCh, count));
				count = 0;
			}

			if (ch == '/') {
				toAppendTo.append('\\');
				toAppendTo.append(ch);
			} else if (ch == '\'') {
				if ((i + 1) < pattern.length() && pattern.charAt(i + 1) == '\'') {
					toAppendTo.append("\"");
					++i;
				} else {
					inQuote = !inQuote;
				}
			} else if (!inQuote) {
				prevCh = ch;
				++count;
			} else {

				toAppendTo.append(ch);
			}
		}

		if (count > 0) {
			toAppendTo.append(subReplaceDateFormat(prevCh, count));
		}

		return toAppendTo.toString();
	}

	/**
	 * only used in the method replaceDataFormat().
	 */
	private static String subReplaceDateFormat(char ch, int count) {
		StringBuffer current = new StringBuffer();
		int patternCharIndex = -1;
		String datePatternChars = "GyMdkHmsSEDFwWahKz";
		if ((patternCharIndex = datePatternChars.indexOf(ch)) == -1) {
			for (int i = 0; i < count; i++) {
				current.append(ch);
			}
			return current.toString();
		}

		switch (patternCharIndex) {
		case 0: // 'G' - ERA
			return "";
		case 1: // 'y' - YEAR
			for (int i = 0; i < count; i++) {
				current.append(ch);
			}
			break;
		case 2: // 'M' - MONTH
			for (int i = 0; i < count; i++) {
				current.append(ch);
			}
			break;
		case 3: // 'd' - Date
			for (int i = 0; i < count; i++) {
				current.append(ch);
			}
			break;
		case 4: // 'k' - HOUR_OF_DAY: 1-based. eg, 23:59 + 1 hour =>>
				// 24:59
			return "h";
		case 5: // case 5: // 'H'-HOUR_OF_DAY:0-based.eg, 23:59+1
				// hour=>>00:59
			for (int i = 0; i < count; i++) {
				current.append(ch);
			}
			break;
		case 6: // case 6: // 'm' - MINUTE
			for (int i = 0; i < count; i++) {
				current.append(ch);
			}
			break;
		case 7: // case 7: // 's' - SECOND
			for (int i = 0; i < count; i++) {
				current.append(ch);
			}
			break;
		case 8: // case 8: // 'S' - MILLISECOND
			for (int i = 0; i < count; i++) {
				current.append(ch);
			}
			break;
		case 9: // 'E' - DAY_OF_WEEK
			for (int i = 0; i < count; i++) {
				current.append("a");
			}
			break;
		case 14: // 'a' - AM_PM
			return "AM/PM";
		case 15: // 'h' - HOUR:1-based. eg, 11PM + 1 hour =>> 12 AM
			for (int i = 0; i < count; i++) {
				current.append(ch);
			}
			break;
		case 17: // 'z' - ZONE_OFFSET
			return "";
		default:
			// case 10: // 'D' - DAY_OF_YEAR
			// case 11: // 'F' - DAY_OF_WEEK_IN_MONTH
			// case 12: // 'w' - WEEK_OF_YEAR
			// case 13: // 'W' - WEEK_OF_MONTH
			// case 16: // 'K' - HOUR: 0-based. eg, 11PM + 1 hour =>> 0 AM
			return "";
		}

		return current.toString();
	}

	public static String getPattern(Object data, String val) {
		if (val != null && data instanceof Date) {
			return replaceDateFormat(val);
		} else if (val == null && data instanceof Time) {
			return "Long Time";
		} else if (val == null && data instanceof java.sql.Date) {
			// According to java SDK 1.4.2-16, sql.Date doesn't have
			// a time component.
			return "mmm d, yyyy";// hh:mm AM/PM";
		} else if (val == null && data instanceof java.util.Date) {
			return "mmm d, yyyy h:mm AM/PM";
		} else if (val != null && data instanceof Number) {

			// if ( val.indexOf( "E" ) >= 0 )
			// {
			// return "Scientific";
			// }
			return new NumberFormatter(val).getPattern();
		} else if (val != null && data instanceof String) {
			return new StringFormatter(val).getPattern();
		}

		return null;
	}

	public static String replaceAll(String str, String old, String news) {
		if (str == null) {
			return str;
		}

		int begin = 0;
		int idx = 0;
		int len = old.length();
		StringBuffer buf = new StringBuffer();

		while ((idx = str.indexOf(old, begin)) >= 0) {
			buf.append(str.substring(begin, idx));
			buf.append(news);
			begin = idx + len;
		}

		return new String(buf.append(str.substring(begin)));
	}

	public static String getValue(String val) {
		if (val == null) {
			return StyleConstant.NULL;
		}
		if (val.charAt(0) == '"' && val.charAt(val.length() - 1) == '"') {
			return val.substring(1, val.length() - 1);
		}

		return val;
	}

	public static int convertToPt(String size) {
		try {
			int s = Integer.valueOf(size.substring(0, size.length() - 2)).intValue();
			if (size.endsWith("in")) {
				return s * 72;
			} else if (size.endsWith("cm")) {
				return (int) (s / 2.54 * 72);
			} else if (size.endsWith("mm")) {
				return (int) (s * 10 / 2.54 * 72);
			} else if (size.endsWith("pc")) {
				return s;
			} else {
				return s;
			}
		} catch (Exception e) {
			logger.log(Level.WARNING, "Unknown unit: " + size);
			return 0;
		}
	}

	// change the columnWidth unit from point to characterNumber
	public static double convertColWidth(double width) {
		return convertColWidth(width, 96);
	}

	public static double convertColWidth(double width, int dpi) {
		float PX_PT = INCH_PT / dpi;
		// TODO: more study about the caculation
		if (width < 0)
			return 0;

		double result = 0;
		// Convert unit from point to pixel.
		double widthInPixel = width / PX_PT;
		double digitalWidth = 7;

		// convert from pixel to number of charaters
		result = (int) ((widthInPixel - 5) / digitalWidth * 100 + 0.5);
		double characterNumber = (double) result / 100;

		// calculate characterNumber
		result = (int) ((characterNumber * digitalWidth + 5) / digitalWidth * 256);
		return result / 256;
	}

	public static boolean isBigNumber(Object number) {
		if (number == null) {
			return false;
		}
		try {
			BigDecimal num = getBigDecimal(number);
			if (num.compareTo(MAX_DOUBLE) == 1 || num.compareTo(MIN_DOUBLE) == -1) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	private static BigDecimal getBigDecimal(Object number) {
		BigDecimal num = null;
		if (number instanceof BigDecimal) {
			num = (BigDecimal) number;
		} else {
			num = new BigDecimal(number.toString());
		}
		return num;
	}

	public static boolean displayedAsScientific(Object number) {
		BigDecimal num = getBigDecimal(number);
		if (num.compareTo(MAX_POSITIVE_DECIMAL_NUMBER) <= 0 && num.compareTo(MIN_POSITIVE_DECIMAL_NUMBER) >= 0) {
			return false;
		}
		if (num.compareTo(MAX_NEGATIVE_DECIMAL_NUMBER) <= 0 && num.compareTo(MIN_NEGATIVE_DECIMAL_NUMBER) >= 0) {
			return false;
		}
		return true;
	}

	public static boolean isInfinity(Object number) {
		if (number == null) {
			return false;
		}
		try {
			return Double.isInfinite((Double) number);
		} catch (Exception e) {
			return false;
		}
	}

	public static String getColumnOfExp(String exp) {
		return exp.substring(exp.indexOf("dataSetRow["), exp.lastIndexOf("]") + 1);
	}

	private static final String reg1 = "Total." + "(count|ave|sum|max|min)" + "\\(", reg2 = "\\)", reg3 = "\\[",
			reg4 = "\\]";

	public static boolean isValidExp(String exp, String[] columnNames) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < columnNames.length; i++) {
			sb.append(columnNames[i] + "|");
		}
		String columnRegExp = "(" + sb.substring(0, sb.length() - 1) + ")";
		columnRegExp = columnRegExp.replaceAll(reg3, "Z");
		columnRegExp = columnRegExp.replaceAll(reg4, "Z");

		String aggregateRegExp = reg1 + columnRegExp + reg2;

		exp = exp.replaceAll(reg3, "Z");
		exp = exp.replaceAll(reg4, "Z");

		Pattern p = Pattern.compile(aggregateRegExp);
		Matcher m = p.matcher(exp);
		boolean agg = m.matches();

		p = Pattern.compile(columnRegExp);
		m = p.matcher(exp);
		return agg || m.matches();
	}

	/**
	 * @param value
	 * @param parent with of parent, the unit is 1/1000 point.
	 * @return
	 */
	public static int convertDimensionType(DimensionType value, float parent, float dpi) {
		float INCH_PX = dpi;
		float PX_PT = INCH_PT / INCH_PX;
		if (value == null) {
			return (int) (parent);
		}
		if (DimensionType.UNITS_PERCENTAGE.equals(value.getUnits())) {
			return (int) (value.getMeasure() / 100 * parent);
		}
		if (DimensionType.UNITS_PX.equalsIgnoreCase(value.getUnits())) {
			return (int) (value.getMeasure() * PX_PT * 1000);
		}

		// FIXME: We should use font size to calculate the EM/EX
		if (DimensionType.UNITS_EM.equalsIgnoreCase(value.getUnits())
				|| DimensionType.UNITS_EX.equalsIgnoreCase(value.getUnits())) {
			return (int) (value.getMeasure() * 12 * 1000);
		} else {
			return (int) (value.convertTo(DimensionType.UNITS_PT) * 1000);
		}
	}

	public static float convertTextIndentToEM(FloatValue indent, float fontSize) {
		if (indent == null || indent.getFloatValue() == 0f) {
			return 0;
		}
		DimensionType size = DimensionType.parserUnit(indent.getCssText());
		try {
			float indentInPt = convertDimensionType(size, 0, 96) / 1000f;
			if (indentInPt != 0f) {
				return indentInPt / fontSize;
			}
		} catch (Exception ignored) {
		}
		return 0f;
	}

	public static String parse(Object txt, String dateTime, ULocale locale) {
		if (dateTime == null) {
			DateFormatter format = new DateFormatter(locale);
			if (txt instanceof java.sql.Date) {
				format.applyPattern(DateFormatter.DATE_UNFORMATTED);
			} else if (txt instanceof java.sql.Time) {
				format.applyPattern(DateFormatter.TIME_UNFORMATTED);
			} else {
				format.applyPattern(DateFormatter.DATETIME_UNFORMATTED);
			}
			dateTime = updateFormat(format.getLocalizedFormatCode());

		}
		if (dateTime.indexOf("Date") != -1 || dateTime.indexOf("Time") != -1) {
			DateFormatter dateFormatter = new DateFormatter(dateTime, locale);
			dateTime = updateFormat(dateFormatter.getLocalizedFormatCode());
		}
		StringBuffer buffer = new StringBuffer();
		boolean inQuto = false;
		int eCount = 0;
		for (int count = 0; count < dateTime.length(); count++) {
			char tempChar = dateTime.charAt(count);
			if (inQuto) {
				if (tempChar == '\'' && nextIsQuto(dateTime, count)) {
					buffer.append(tempChar);
					count++;
				} else {
					if (tempChar == '\'') {
						inQuto = false;
					} else {
						if (specialStr.indexOf(tempChar) != -1) {
							buffer.append("\\" + tempChar);
						} else {
							buffer.append(tempChar);
						}
					}
				}
			} else {
				if (tempChar == '\'') {
					eCount = 0;
					if (nextIsQuto(dateTime, count)) {
						buffer.append(tempChar);
						count++;
					} else {
						inQuto = true;
					}
				} else {
					if ("Ee".indexOf(tempChar) != -1) {
						eCount++;
						if (eCount == 3) {
							buffer.append("ddd");
						}
						if (eCount >= 4) {
							buffer.append("d");
						}
						continue;
					}
					eCount = 0;
					if (tempChar == 'a') {
						buffer.append("AM/PM");
						continue;
					}
					if ("zZFWwG".indexOf(tempChar) != -1) {
						continue;
					}
					if ("kK".indexOf(tempChar) != -1) {
						buffer.append("h");
						continue;
					}
					buffer.append(tempChar);
				}
			}
		}
		return buffer.toString();
	}

	/*
	 * According to icu's change: The original code process 'y', 'yy', 'yyy' in
	 * the same way. and process patterns with 4 or more than 4 'y' characters
	 * in the same way. Now icu's code process 'y' and 'yyyy'in the same way.
	 * But excel displays differently with pattern 'y' and pattern 'yyyy'. So
	 * change 'y' to 'yyyy' for excel date pattern. This change started from
	 * Birt2.6.0.
	 */
	public static String updateFormat(String dateTime) {
		if (dateTime.indexOf('y') == dateTime.lastIndexOf('y')) {
			dateTime = dateTime.replace("y", "yyyy");
		}
		return dateTime;
	}

	public static String formatNumberPattern(String givenValue) {
		return formatNumberPattern(givenValue, ULocale.getDefault());
	}

	public static String formatNumberPattern(String givenValue, ULocale locale) {
		if (givenValue == null) {
			return "";
		}

		String key = givenValue + "-" + locale;
		String format = formatCache.get(key);
		if (format == null) {
			format = localizePattern(givenValue, locale);
			synchronized (formatCache) {
				formatCache.put(key, format);
			}
		}
		return format;
	}

	private static String localizePattern(String givenValue, ULocale locale) {
		if (givenValue.length() == 1) {
			char ch = givenValue.charAt(0);
			switch (ch) {
			case 'G':
			case 'g':
			case 'd':
			case 'D':
				return "###,##0.###";
			case 'C':
			case 'c':
				return getCurrencySymbol(locale) + "###,##0.00";
			case 'f':
			case 'F':
				return "#0.00";
			case 'N':
			case 'n':
				return "###,##0.00";
			case 'p':
			case 'P':
				return "###,##0.00 %";
			case 'e':
			case 'E':
				return "0.000000E00";
			case 'x':
			case 'X':
				return "####";
			}
		}

		if (namedPatterns.containsKey(givenValue)) {
			return namedPatterns.get(givenValue);
		}

		if (validType(givenValue)) {
			return givenValue;
		}

		if (isScientific(givenValue)) {
			givenValue = givenValue.replace("E", "E+");
			return givenValue;
		}
		int count = givenValue.length();
		StringBuffer returnStr = new StringBuffer();
		boolean flag = false;
		for (int num = 0; num < count; num++) {
			char temp = givenValue.charAt(num);
			if (temp == '\'') {
				if (flag) {
					flag = false;
				} else {
					char nextChar = givenValue.charAt(num + 1);
					if (nextChar == '\'') {
						returnStr.append('\'');
						num++;
						flag = false;
					} else {
						flag = true;
					}
				}
			} else {
				if (flag) {
					returnStr.append("\\").append(temp);
				} else {
					if (specialStr.indexOf(temp) != -1) {
						returnStr.append("\\").append(temp);
					} else if (temp == '\u00a4') // this corresponds to symbol
													// '¤'
					{
						String symbol = getCurrencySymbol(locale);
						returnStr.append(symbol);
					} else if (temp == '\u2030') // ‰
					{
						returnStr.append('%');
					} else if (currencySymbol.indexOf(temp) != -1) {
						returnStr.append(temp);
					} else {
						returnStr.append(temp);
					}
				}
			}
		}
		if (returnStr.indexOf("#") == -1 && returnStr.indexOf("0") == -1) {
			returnStr.append("#");
		}
		return returnStr.toString();
	}

	private static boolean isScientific(String givenValue) {
		Matcher matcher = pattern.matcher(givenValue);
		if (matcher.matches()) {
			return true;
		} else
			return false;
	}

	private static String getCurrencySymbol(ULocale locale) {
		NumberFormat format = NumberFormat.getCurrencyInstance(locale);
		Currency currency = format.getCurrency();
		if (currency != null) {
			String symbol = currency.getSymbol(locale);
			if (symbol.equals("EUR")) {
				symbol = "\u20ac"; // "€";
			} else if (symbol.equals("GBP")) {
				symbol = "\u00a3"; // "£";
			} else if (symbol.equals("XXX")) {
				symbol = "\u00a4"; // "¤";
			}
			return symbol;
		}
		return "$";
	}

	protected static boolean validType(String str) {
		for (int count = 0; count < str.length(); count++) {
			char ch = str.charAt(count);
			if (validStr.indexOf(ch) == -1) {
				return false;
			}
		}
		return true;
	}

	private static boolean nextIsQuto(String forPar, int index) {
		if (forPar.length() - 1 == index) {
			return false;
		}
		if (forPar.charAt(index + 1) == '\'') {
			return true;
		}
		return false;
	}

	public static boolean equalsIgnoreCase(String obj1, String obj2) {
		return obj1 == null ? obj2 == null : obj1.equalsIgnoreCase(obj2);
	}

	/**
	 * Excel 2007 using 1900 date base system.Date time is combined by date
	 * component and time component. In the 1900 date base system, the lower
	 * limit of day component is January 1, 1900, which has serial value 1. The
	 * upper-limit is December 31, 9999, which has serial value 2,958,465. The
	 * time component of a serial value ranges in value from 0–0.99999999, and
	 * represents times from 0:00:00 (12:00:00 AM) to 23:59:59 (11:59:59 P.M.),
	 * respectively. Going forward in time, the time component of a serial value
	 * increases by 1/86,400 each second.
	 * 
	 * @param d
	 * @param zone
	 * @return
	 */
	public static String getDay(Date d, TimeZone zone) {
		Calendar currentDay = Calendar.getInstance(zone);
		currentDay.setTime(d);
		int hours = currentDay.get(Calendar.HOUR_OF_DAY);
		int minutes = currentDay.get(Calendar.MINUTE);
		int seconds = currentDay.get(Calendar.SECOND);
		double timeComponent = (hours * SECONDS_PER_HOUR + minutes * SECONDS_PER_MINUTE + seconds) / SECONDS_PER_DAY;
		if (timeComponent < 0 || timeComponent > 1) {
			logger.log(Level.WARNING, "Invalid time!");
			timeComponent = 0;
		}
		long currentTimeInMillis = currentDay.getTimeInMillis();
		int dayComponent = (int) ((currentTimeInMillis - BASE_DATE_TIME) / MILLISECS_PER_DAY);
		if (dayComponent < 0 || dayComponent > 2958463) {
			logger.log(Level.WARNING, "Invaild day");
			dayComponent = 0;
		}
		if (dayComponent <= 59)
			dayComponent = dayComponent + 1;
		else
			dayComponent = dayComponent + 2;
		double dateTime = dayComponent + timeComponent;
		return Double.toString(dateTime);
	}

	public static String convertColor(String value) {
		if (value == null || "transparent".equalsIgnoreCase(value) || "null".equalsIgnoreCase(value)) {
			return null;
		} else
			return value.replace("#", "FF");

	}

	public static String covertBorderStyle(String style) {
		if (style == null)
			return null;
		if (style.equalsIgnoreCase("Dot"))
			return "dotted";
		if (style.equalsIgnoreCase("DashDot"))
			return "dashDot";
		if (style.equalsIgnoreCase("Double"))
			return "double";
		if (style.equalsIgnoreCase("Continuous"))
			return "thin";
		return null;
	}

	public static String capitalize(String text) {
		boolean capitalizeNextChar = true;
		char[] array = text.toCharArray();
		for (int i = 0; i < array.length; i++) {
			Character c = Character.valueOf(text.charAt(i));
			if (splitChar.contains(c))
				capitalizeNextChar = true;
			else if (capitalizeNextChar) {
				array[i] = Character.toUpperCase(array[i]);
				capitalizeNextChar = false;
			}
		}
		return new String(array);
	}

	/**
	 * Get cell Ref from col no. For example: 1-A 27-AA 676-YZ
	 * 
	 * @param row --row no
	 * @param column -- col no
	 * @return the cell refrence in excel
	 */
	public static String getRef(int row, int column) {
		return getCellId(row, getColumnId(column));
	}

	public static String getCellId(int row, String columnId) {
		String cellId = columnId;
		if (row >= 0)
			cellId = columnId + row;
		return cellId;
	}

	public static String getColumnId(int column) {
		Stack<Character> digits = new Stack<Character>();
		int dividant = column;
		while (dividant > 26) {
			int remain = dividant % 26;
			dividant = dividant / 26;
			if (remain == 0) {
				remain = 26;
				dividant--;
			}
			digits.push((char) ('A' + remain - 1));
		}
		digits.push((char) ('A' + dividant - 1));
		StringBuffer buffer = new StringBuffer();
		while (!digits.empty()) {
			buffer.append(digits.pop());
		}
		String columnId = buffer.toString();
		return columnId;
	}

	public static String ZIP = "@@@@@-@@@@";
	public static String PHONE = "(@@@)@@@-@@@@";
	public static String SOCIAL = "@@@-@@-@@@@";
	public static String ZIP_CODE = "00000\\-0000";
	public static String PHONE_CODE = "[<=9999999]###\\-####;\\(###\\)\\ ###\\-####";
	public static String SOCIALNUMBER_CODE = "000\\-00\\-0000";

	public static String convertStringFormat(String property) {
		if (property == null)
			return null;
		if (ZIP.equals(property))
			return ZIP_CODE;
		if (PHONE.equals(property))
			return PHONE_CODE;
		if (SOCIAL.equals(property))
			return SOCIALNUMBER_CODE;
		return property;
	}

	/**
	 * Convert scientific format code such as 00/E00 to 00E+00 so excel 2007 can
	 * output it correctly.
	 * 
	 * @param code
	 * @return
	 */
	public static String convertSciFormat(String code) {
		if (null == code)
			return null;
		int index = code.indexOf('E');
		if (index != -1) {
			return code.substring(0, index - 1) + "E" + "+" + code.substring(index + 1);
		}
		return code;
	}

	public static boolean isNaN(Object number) {
		if (number == null) {
			return false;
		}
		try {
			return Double.isNaN((Double) number);
		} catch (Exception e) {
			return false;
		}
	}

	public static String format(Object value, int dataType) {
		if (value == null) {
			return "";
		} else if (dataType == SheetData.DATE) {
			return formatDate(value);
		} else if (dataType == SheetData.NUMBER) {
			Number number = (Number) value;
			if (isBigNumber(number)) {
				return formatNumberAsScienceNotation(number);
			} else if (number.toString().length() > 31) {
				if (displayedAsScientific(number)) {
					return formatNumberAsScienceNotation(number);
				} else {
					return formatNumberAsDecimal(number);
				}
			}
		}
		return value.toString();
	}

	public static String getValidSheetName(String name) {
		// Make sure the name you entered does not exceed 31 characters.
		// Make sure the name does not contain any of the following characters:
		// \ / ? * : [ or ]
		name = name.replaceAll("[\\\\/?*:\\[\\]]", "_");
		if (name.length() > SHEETNAME_LENGTH) {
			logger.log(Level.WARNING, "The sheetName " + name + " is too long for output.");
			name = name.substring(0, SHEETNAME_LENGTH);
		}
		return name;
	}

	private static final double POINTS_PER_INCH = 72;
	private static final double CM_PER_INCH = 2.54;
	private static final double POINTS_PER_CM = POINTS_PER_INCH / CM_PER_INCH;

	public static int getPageSizeIndex(int pageWidth, int pageHeight) {
		if (pageHeight == 8.5 * ExcelUtil.INCH_PT && pageWidth == 11 * ExcelUtil.INCH_PT)
			return PAPER_LETTER;
		if (pageHeight == 11 * ExcelUtil.INCH_PT && pageWidth == 17 * ExcelUtil.INCH_PT)
			return PAPER_TABLOID;
		if (pageHeight == 8.5 * ExcelUtil.INCH_PT && pageWidth == 14 * ExcelUtil.INCH_PT)
			return PAPER_LEGAL;
		if (pageHeight == 5.5 * ExcelUtil.INCH_PT && pageWidth == 8.5 * ExcelUtil.INCH_PT)
			return PAPER_STATEMENT;
		if (pageHeight == 7.25 * ExcelUtil.INCH_PT && pageWidth == 10.5 * ExcelUtil.INCH_PT)
			return PAPER_EXECUTIVE;

		if (pageHeight == 297 / 10 * POINTS_PER_CM && pageWidth == 420 / 10 * POINTS_PER_CM)
			return PAPER_A3;
		if (pageHeight == 210 / 10 * POINTS_PER_CM && pageWidth == 297 / 10 * POINTS_PER_CM)
			return PAPER_A4;
		if (pageHeight == 148 / 10 * POINTS_PER_CM && pageWidth == 210 / 10 * POINTS_PER_CM)
			return PAPER_A5;
		if (pageHeight == 250 / 10 * POINTS_PER_CM && pageWidth == 353 / 10 * POINTS_PER_CM)
			return PAPER_B4;
		if (pageHeight == 176 / 10 * POINTS_PER_CM && pageWidth == 250 / 10 * POINTS_PER_CM)
			return PAPER_B5;

		if (pageHeight == 8.5 * ExcelUtil.INCH_PT && pageWidth == 13 * ExcelUtil.INCH_PT)
			return PAPER_FOLIO;
		if (pageHeight == 4.125 * ExcelUtil.INCH_PT && pageWidth == 9.5 * ExcelUtil.INCH_PT)
			return PAPER_10_ENVELOP;
		if (pageHeight == 110 / 10 * POINTS_PER_CM && pageWidth == 220 / 10 * POINTS_PER_CM)
			return PAPER_DL_ENVELOPE;
		if (pageHeight == 162 / 10 * POINTS_PER_CM && pageWidth == 229 / 10 * POINTS_PER_CM)
			return PAPER_C5_ENVELOPE;
		if (pageHeight == 176 / 10 * POINTS_PER_CM && pageWidth == 250 / 10 * POINTS_PER_CM)
			return PAPER_B5_ENVELOPE;
		if (pageHeight == 3.875 * ExcelUtil.INCH_PT && pageWidth == 7.5 * ExcelUtil.INCH_PT)
			return PAPER_MONARCH_ENVELOPE;
		if (pageHeight == 250 / 10 * POINTS_PER_CM && pageWidth == 353 / 10 * POINTS_PER_CM)
			return PAPER_ISOB4;
		return PAPER_A4;
	}

	public static String truncateCellText(String txt) {
		if (txt.length() > maxCellTextLength) {
			txt = txt.substring(0, maxCellTextLength);
			logger.log(Level.WARNING, "The text length should not exceed 32767 characters!");
		}
		return txt;
	}

	public static SheetData getRealData(SheetData data) {
		while (data != null && data.isBlank()) {
			data = ((BlankData) data).getData();
		}
		return data;
	}
}
