/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.core.format;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ibm.icu.text.DecimalFormat;
import com.ibm.icu.text.DecimalFormatSymbols;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 *
 *
 * Defines a number formatting class. It does the following: 1. In constructor,
 * convert format string to Java format string. 2. Expose a format function,
 * which does the following: a. Format number using Java format string b. Do
 * some post-processing, i.e., e or E, minus sign handling, etc.
 */
public class NumberFormatter implements IFormatter {

	private static final String DIGIT_SUBSTITUTION = "DigitSubstitution";
	private static final String ROUNDING_MODE = "RoundingMode";

	/**
	 * logger used to log syntax errors.
	 */
	static protected Logger logger = Logger.getLogger(NumberFormatter.class.getName());

	/**
	 * the format pattern
	 */
	protected String formatPattern;

	/**
	 * Flag whether to parse numbers and return BigDecimal values.
	 */
	protected boolean parseBigDecimal;

	/**
	 * the locale used for formatting
	 */
	protected ULocale locale = ULocale.getDefault();

	/**
	 * a java.text.NumberFormat format object. We want to use the
	 * createNumberFormat() and format() methods
	 */
	protected NumberFormat numberFormat;
	protected DecimalFormat decimalFormat;

	/**
	 * The default format of Double is Double.toString(); need to localize the
	 * result of Double.toString() to get the final result.
	 *
	 * decimalSeparator is the localized decimal separator.
	 *
	 * currently the exponential character isnt exposed by JDK, so just leave it for
	 * future
	 *
	 * @see definition of java.text.DecimalFormatSymbols#exponential
	 */
	protected char decimalSeparator;

	/**
	 * Do we use hex pattern?
	 */
	private boolean hexFlag;

	private int roundPrecision;

	private String realPattern;

	/**
	 * used to indicate whether to use ICU symbols
	 */
	private boolean digitSubstitution;

	private RoundingMode roundingMode = RoundingMode.UNNECESSARY;

	/**
	 * constructor with no argument
	 */
	public NumberFormatter() {
		applyPattern(null);
	}

	/**
	 * constructor with a format string as parameter
	 *
	 * @param format format string
	 */
	public NumberFormatter(String format) {
		applyPattern(format);
	}

	/**
	 * @param locale the locale used for numer format
	 */
	public NumberFormatter(ULocale locale) {
		this.locale = locale;
		applyPattern(null);
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	@Deprecated
	public NumberFormatter(Locale locale) {
		this(ULocale.forLocale(locale));
	}

	/**
	 * constructor that takes a format pattern and a locale
	 *
	 * @param pattern numeric format pattern
	 * @param locale  locale used to format the number
	 */
	public NumberFormatter(String pattern, ULocale locale) {
		this.locale = locale;
		this.parseBigDecimal = false;
		applyPattern(pattern);
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	@Deprecated
	public NumberFormatter(String pattern, Locale locale) {
		this(pattern, ULocale.forLocale(locale));
	}

	/**
	 * returns the original format string.
	 */
	public String getPattern() {
		return this.formatPattern;
	}

	public String getFormatCode() {
		return realPattern;
	}

	/**
	 * initializes numeric format pattern
	 *
	 * @param patternStr ths string used for formatting numeric data
	 */
	public void applyPattern(String patternStr) {
		try {
			patternStr = processPatternAttributes(patternStr);
			this.formatPattern = patternStr;
			hexFlag = false;
			roundPrecision = -1;
			realPattern = formatPattern;

			// null format String
			if (this.formatPattern == null) {
				numberFormat = NumberFormat.getInstance(locale.toLocale());
				numberFormat.setGroupingUsed(false);
				DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale.toLocale());
				decimalSeparator = symbols.getDecimalSeparator();
				decimalFormat = new DecimalFormat("", //$NON-NLS-1$
						new DecimalFormatSymbols(locale.toLocale()));
				decimalFormat.setMinimumIntegerDigits(1);
				decimalFormat.setGroupingUsed(false);
				roundPrecision = getRoundPrecision(numberFormat);
				applyPatternAttributes();
				return;
			}

			// Single character format string
			if (patternStr.length() == 1) {
				handleSingleCharFormatString(patternStr.charAt(0));
				roundPrecision = getRoundPrecision(numberFormat);
				applyPatternAttributes();
				return;
			}

			// Named formats and arbitrary format string
			handleNamedFormats(patternStr);
			roundPrecision = getRoundPrecision(numberFormat);
			applyPatternAttributes();
		} catch (Exception illeagueE) {
			logger.log(Level.WARNING, illeagueE.getMessage(), illeagueE);
		}
	}

	private String processPatternAttributes(String pattern) {
		if (pattern == null || pattern.length() <= 3) { // pattern must have {?}
			return pattern;
		}

		int length = pattern.length();
		if (pattern.charAt(length - 1) == '}') {
			// end up with '}'
			int begin = pattern.lastIndexOf('{');
			if (begin >= 0) {
				ArrayList<String> names = new ArrayList<>();
				ArrayList<String> values = new ArrayList<>();
				String properties = pattern.substring(begin + 1, length - 1);
				String[] attributes = properties.split(";");
				boolean wellForm = true;
				for (String attribute : attributes) {
					int delimit = attribute.indexOf('=');
					if (delimit == -1) {
						wellForm = false;
						break;
					}
					names.add(attribute.substring(0, delimit));
					values.add(attribute.substring(delimit + 1));
				}
				if (wellForm) {
					// process attributes
					int size = names.size();
					for (int index = 0; index < size; index++) {
						if (DIGIT_SUBSTITUTION.equalsIgnoreCase(names.get(index).trim())) {
							String value = values.get(index).trim();
							digitSubstitution = Boolean.parseBoolean(value);
						}
						if (ROUNDING_MODE.equalsIgnoreCase(names.get(index).trim())) {
							String value = values.get(index).trim();
							if (value.equalsIgnoreCase("HALF_EVEN")) {
								roundingMode = RoundingMode.HALF_EVEN;
							} else if (value.equalsIgnoreCase("HALF_UP")) {
								roundingMode = RoundingMode.HALF_UP;
							} else if (value.equalsIgnoreCase("HALF_DOWN")) {
								roundingMode = RoundingMode.HALF_DOWN;
							} else if (value.equalsIgnoreCase("UP")) {
								roundingMode = RoundingMode.UP;
							} else if (value.equalsIgnoreCase("DOWN")) {
								roundingMode = RoundingMode.DOWN;
							} else if (value.equalsIgnoreCase("FLOOR")) {
								roundingMode = RoundingMode.FLOOR;
							} else if (value.equalsIgnoreCase("CEILING")) {
								roundingMode = RoundingMode.CEILING;
							} else if (value.equalsIgnoreCase("UNNECESSARY")) {
								roundingMode = RoundingMode.UNNECESSARY;
							}
						}
					}
					if (begin == 0) {
						return null;
					}
					return pattern.substring(0, begin);
				}
			}
		}
		return pattern;
	}

	/**
	 * @param num the number to be formatted
	 * @return the formatted string
	 */
	public String format(double num) {
		try {
			if (Double.isNaN(num)) {
				return "NaN"; //$NON-NLS-1$
			}

			if (Double.isInfinite(num)) {
				return "Infinity";
			}

			if (hexFlag) {
				return Long.toHexString(new Double(num).longValue());
			}

			if (num == 0) {
				num = 0;
			}

			if (this.formatPattern == null) {
				long longValue = Math.round(num);
				if (longValue == num) {
					return Long.toString(longValue);
				}
				String result = Double.toString(num);
				return result.replace('.', decimalSeparator);
			}

			num = roundValue(num);
			return numberFormat.format(num);

		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e); // $NON-NLS-1$
			return null;
		}
	}

	/**
	 * format(BigDecimal) method, return the format string for the BigDecimal
	 * parameter.
	 */
	/**
	 * formats a BigDecimal value into a string
	 *
	 * @param big decimal value
	 * @return formatted string
	 */
	public String format(BigDecimal bigDecimal) {
		try {
			if (hexFlag) {
				return Long.toHexString(bigDecimal.longValue());
			}

			if (this.formatPattern == null) {
				return decimalFormat.format(bigDecimal);
			}

			bigDecimal = roundValue(bigDecimal);
			return numberFormat.format(bigDecimal);
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e); // $NON-NLS-1$
			return null;
		}
	}

	public String format(Number number) {
		try {
			if (Double.isNaN(number.doubleValue())) {
				return "NaN";
			}
			if (hexFlag) {
				return Long.toHexString(number.longValue());
			}

			if (number instanceof Double || number instanceof Float) {
				return format(number.doubleValue());
			}

			if (number instanceof BigDecimal) {
				return format((BigDecimal) number);
			}

			return numberFormat.format(number);

		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e); // $NON-NLS-1$
			return null;
		}
	}

	/**
	 * formats a long integer
	 *
	 * @param num the number to be formatted
	 * @return the formatted string
	 */
	public String format(long num) {
		if (hexFlag) {
			return Long.toHexString(num);
		}
		return numberFormat.format(num);
	}

	private void handleSingleCharFormatString(char c) {
		switch (c) {
		case 'G':
		case 'g':
		case 'D':
		case 'd':
			numberFormat = NumberFormat.getInstance(locale.toLocale());
			return;
		case 'C':
		case 'c':
			numberFormat = NumberFormat.getCurrencyInstance(locale.toLocale());
			return;
		case 'F':
		case 'f':
			realPattern = "#0.00"; //$NON-NLS-1$
			numberFormat = new DecimalFormat(realPattern, new DecimalFormatSymbols(locale.toLocale()));
			return;
		case 'N':
		case 'n':
			realPattern = "###,##0.00"; //$NON-NLS-1$
			numberFormat = new DecimalFormat(realPattern, new DecimalFormatSymbols(locale.toLocale()));
			return;
		case 'P':
		case 'p':
			realPattern = "###,##0.00 %"; //$NON-NLS-1$
			numberFormat = new DecimalFormat(realPattern, new DecimalFormatSymbols(locale.toLocale()));
			return;
		case 'E':
		case 'e':
			realPattern = "0.000000E00"; //$NON-NLS-1$
			numberFormat = new DecimalFormat(realPattern, new DecimalFormatSymbols(locale.toLocale()));
			roundPrecision = -2;
			return;
		case 'X':
		case 'x':
			hexFlag = true;
			return;
		default: {
			char data[] = new char[1];
			data[0] = c;
			String str = new String(data);

			numberFormat = new DecimalFormat(str, new DecimalFormatSymbols(locale.toLocale()));
		}
		}
	}

	private DecimalFormatSymbols getICUDecimalSymbols(Locale locale) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
		com.ibm.icu.text.DecimalFormatSymbols icuSymbols = new com.ibm.icu.text.DecimalFormatSymbols(locale);
		symbols.setCurrencySymbol(icuSymbols.getCurrencySymbol());
		symbols.setDecimalSeparator(icuSymbols.getDecimalSeparator());
		symbols.setDigit(icuSymbols.getDigit());
		symbols.setGroupingSeparator(icuSymbols.getGroupingSeparator());
		symbols.setInfinity(icuSymbols.getInfinity());
		symbols.setInternationalCurrencySymbol(icuSymbols.getInternationalCurrencySymbol());
		symbols.setMinusSign(icuSymbols.getMinusSign());
		symbols.setMonetaryDecimalSeparator(icuSymbols.getMonetaryDecimalSeparator());
		symbols.setNaN(icuSymbols.getNaN());
		symbols.setPatternSeparator(icuSymbols.getPatternSeparator());
		symbols.setPercent(icuSymbols.getPercent());
		symbols.setPerMill(icuSymbols.getPerMill());
		symbols.setZeroDigit(icuSymbols.getZeroDigit());
		return symbols;
	}

	private void applyPatternAttributes() {
		if (digitSubstitution) {
			DecimalFormatSymbols symbols = getICUDecimalSymbols(locale.toLocale());
			if (decimalFormat != null) {
				((DecimalFormat) decimalFormat).setDecimalFormatSymbols(symbols);
			}
			if (numberFormat instanceof DecimalFormat) {
				((DecimalFormat) numberFormat).setDecimalFormatSymbols(symbols);
			}
		}

	}

	private void handleNamedFormats(String patternStr) {
		if (patternStr.equals("General Number") || patternStr.equals("Unformatted")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			numberFormat = NumberFormat.getInstance(locale.toLocale());
			numberFormat.setGroupingUsed(false);
			return;
		}
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale.toLocale());
		if (patternStr.equals("Fixed")) //$NON-NLS-1$
		{
			realPattern = "#0.00"; //$NON-NLS-1$
			numberFormat = new DecimalFormat(realPattern, symbols);
			return;

		}
		if (patternStr.equals("Percent")) //$NON-NLS-1$
		{
			realPattern = "0.00%"; //$NON-NLS-1$
			numberFormat = new DecimalFormat(realPattern, symbols);
			return;
		}
		if (patternStr.equals("Scientific")) //$NON-NLS-1$
		{
			realPattern = "0.00E00"; //$NON-NLS-1$
			numberFormat = new DecimalFormat(realPattern, symbols);
			roundPrecision = -2;
			return;

		}
		if (patternStr.equals("Standard")) //$NON-NLS-1$
		{
			realPattern = "###,##0.00"; //$NON-NLS-1$
			numberFormat = new DecimalFormat(realPattern, symbols);
			return;

		}
		try {
			numberFormat = new DecimalFormat(patternStr, symbols);
		} catch (java.lang.IllegalArgumentException e) {
			// if the pattern is invalid, create a default decimal
			numberFormat = new DecimalFormat("", symbols);//$NON-NLS-1$
			logger.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	/**
	 * Returns whether decimal numbers are returned as BigDecimal instances.
	 *
	 * @return the parseBigDecimal
	 */
	public boolean isParseBigDecimal() {
		return parseBigDecimal;
	}

	/**
	 * Sets whether decimal numbers must be returned as BigDecimal instances.
	 *
	 * @param parseBigDecimal the parseBigDecimal to set
	 */
	public void setParseBigDecimal(boolean parseBigDecimal) {
		this.parseBigDecimal = parseBigDecimal;
	}

	/**
	 * Parses the input string into a formatted date type.
	 *
	 * @param number the input string to parse
	 * @return the formatted date
	 * @throws ParseException if the beginning of the specified string cannot be
	 *                        parsed.
	 */

	public Number parse(String number) throws ParseException {
		if (numberFormat instanceof DecimalFormat) {
			((DecimalFormat) numberFormat).setParseBigDecimal(this.parseBigDecimal);
		}
		return numberFormat.parse(number);
	}

	BigDecimal roundValue(BigDecimal bd) {
		if (roundingMode == RoundingMode.UNNECESSARY) {
			return bd;
		}
		if (roundPrecision >= 0) {
			int scale = bd.scale();
			try {
				if (scale > roundPrecision) {
					bd = bd.setScale(roundPrecision, roundingMode);
				}
			} catch (ArithmeticException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
		}
		return bd;
	}

	double roundValue(double value) {
		if (roundingMode == RoundingMode.UNNECESSARY) {
			return value;
		}
		if (roundPrecision >= 0) {
			BigDecimal bd = BigDecimal.valueOf(value);
			int scale = bd.scale();
			try {
				if (scale > roundPrecision) {
					bd = bd.setScale(roundPrecision, roundingMode);
					return bd.doubleValue();
				}
			} catch (ArithmeticException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage(), e);
			}
		}
		return value;
	}

	int getRoundPrecision(NumberFormat format) {
		if (realPattern != null && realPattern.indexOf('E') != -1) {
			return -1;
		}

		int precision = numberFormat.getMaximumFractionDigits();
		if (numberFormat instanceof DecimalFormat) {
			int formatMultiplier = ((DecimalFormat) numberFormat).getMultiplier();
			precision += (int) Math.log10(formatMultiplier);
		}

		return precision;
	}

	@Override
	public String formatValue(Object value) {
		assert value instanceof Number;
		return format((Number) value);
	}
}
