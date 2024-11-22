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

package org.eclipse.birt.report.model.api.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.DimensionValueUtil;
import org.eclipse.birt.report.model.util.ModelUtil;

import com.ibm.icu.util.ULocale;

/**
 * Collection of string utilities.
 *
 */

public class StringUtil {

	/**
	 *
	 */
	public static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private final static Map<Integer, NumberFormatter> formatters;

	static {
		formatters = new HashMap<>(ModelUtil.MAP_CAPACITY_LOW);

		// put the most common locale into the map first.

		formatters.put(ULocale.ENGLISH.toString().hashCode(), new NumberFormatter(ULocale.US));

		formatters.put(ULocale.ENGLISH.toString().hashCode(), new NumberFormatter(ULocale.ENGLISH));

		formatters.put(ULocale.SIMPLIFIED_CHINESE.toString().hashCode(),
				new NumberFormatter(ULocale.SIMPLIFIED_CHINESE));
	}

	/**
	 * Trim a string. Removes leading and trailing blanks. If the resulting string
	 * is empty, normalizes the string to an null string.
	 *
	 * @param value the string to trim
	 * @return the trimmed string, or null if the string is empty
	 */

	public static String trimString(String value) {
		if (value == null) {
			return null;
		}
		value = value.trim();
		if (value.isEmpty()) {
			return null;
		}
		return value;
	}

	/**
	 * Convert an integer to an HTML RGB value. The result is of the form #hhhhhh.
	 * The input rgb integer value will be clipped into the range 0 ~ 0xFFFFFF
	 *
	 * @param rgb the integer RGB value
	 * @return the value as an HTML RGB string
	 */

	public static String toRgbText(int rgb) {
		// clip input value.

		if (rgb > 0xFFFFFF) {
			rgb = 0xFFFFFF;
		}
		if (rgb < 0) {
			rgb = 0;
		}

		String str = "000000" + Integer.toHexString(rgb); //$NON-NLS-1$
		return "#" + str.substring(str.length() - 6); //$NON-NLS-1$
	}

	/**
	 * Check if the locale string is a valid locale format, with the language,
	 * country and variant separated by underbars.
	 * <p>
	 * The language argument is a valid <STRONG>ISO Language Code. </STRONG>. These
	 * codes are the lower-case, two-letter codes.
	 * <p>
	 * The country argument is a valid <STRONG>ISO Country Code. </STRONG> These
	 * codes are the upper-case, two-letter codes.
	 * <p>
	 * If the language is missing, the string should begin with an underbar. (Can't
	 * have a locale with just a variant -- the variant must accompany a valid
	 * language or country code). Examples: "en", "de_DE", "_GB", "en_US_WIN",
	 * "de__POSIX", "fr__MAC"
	 *
	 * @param locale string representing a locale
	 * @return true if the locale is a valid locale, false if the locale is not
	 *         valid.
	 */

	public static boolean isValidLocale(String locale) {
		// TODO: needs to confirm if BIRT support limited collection of locale.

		return true;
	}

	/**
	 * Reports if a string is blank. A string is considered blank either if it is
	 * null, is an empty string, of consists entirely of white space.
	 * <p>
	 * For example,
	 * <ul>
	 * <li>null, "" and " " are blank strings
	 * </ul>
	 *
	 * @param str the string to check
	 * @return true if the string is blank, false otherwise.
	 */

	public static boolean isBlank(String str) {
		// FIXME This could be optimized for performance.
		return trimString(str) == null;
	}

	/**
	 * Reports if a string is empty. A string is considered empty either if it is
	 * null, is an empty string.
	 * <p>
	 * For example,
	 * <ul>
	 * <li>Both null and "" are empty strings
	 * <li>" " is not empty string.
	 * </ul>
	 *
	 * @param value the string to check
	 * @return true if the string is empty, false otherwise.
	 */

	public static boolean isEmpty(String value) {
		if (value == null || value.isEmpty()) {
			return true;
		}

		return false;
	}

	/**
	 * Returns if the two string are null or equal. The
	 * {@link java.lang.String#equals(String)}is used to compare two strings.
	 *
	 * @param str1 the string to compare
	 * @param str2 the string to compare
	 * @return true, if the two string are null, or the two string are equal with
	 *         case sensitive.
	 */

	public static boolean isEqual(String str1, String str2) {
		return str1 == str2 || (str1 != null && str1.equals(str2));
	}

	/**
	 * Returns if the two string are null or equal. The
	 * {@link java.lang.String#equalsIgnoreCase(String)}is used to compare two
	 * strings.
	 *
	 * @param str1 the string to compare
	 * @param str2 the string to compare
	 * @return true, if the two string are null, or the two string are equal with
	 *         case sensitive.
	 */

	public static boolean isEqualIgnoreCase(String str1, String str2) {
		return str1 == str2 || (str1 != null && str1.equalsIgnoreCase(str2));
	}

	/**
	 * Converts the double value to locale-independent string representation. This
	 * method works like <code>Double.toString( double )</code>, and can also handle
	 * very large number like 1.234567890E16 to "12345678900000000".
	 *
	 * @param d       the double value to convert
	 * @param fNumber the positive maximum fractional number
	 * @return the locale-independent string representation.
	 */

	public static String doubleToString(double d, int fNumber) {
		return doubleToString(d, fNumber, ULocale.ENGLISH);
	}

	/**
	 * Converts the double value to locale-dependent string representation.
	 *
	 * @param d       the double value to convert
	 * @param fNumber the positive maximum fractional number
	 * @param locale
	 * @return the locale-dependent string representation.
	 */

	public static String doubleToString(double d, int fNumber, ULocale locale) {
		if (fNumber < 0) {
			fNumber = 0;
		}

		String pattern = null;
		switch (fNumber) {
		case 0:
			pattern = "#0"; //$NON-NLS-1$
			break;
		default:
			pattern = "#0."; //$NON-NLS-1$
			StringBuilder b = new StringBuilder(pattern);
			for (int i = 0; i < fNumber; i++) {
				b.append('#');
			}
			pattern = b.toString();
			break;

		}

		if (locale == null) {
			locale = ULocale.getDefault();
		}

		Integer localeCode = locale.toString().hashCode();
		NumberFormatter formatter = formatters.get(localeCode);
		if (formatter == null) {
			// synchronize to get the formatter

			synchronized (formatters) {
				// check again since another thread may save the formatter
				// already.

				formatter = formatters.get(localeCode);
				if (formatter == null) {
					formatter = new NumberFormatter(locale);
					formatters.put(localeCode, formatter);
				}
			}
		}

		formatter.applyPattern(pattern);
		String value = formatter.format(d);

		return value;
	}

	/**
	 * Parses a dimension string in locale-independent way. The input string must
	 * match the following:
	 * <ul>
	 * <li>null</li>
	 * <li>[1-9][0-9]*[.[0-9]*[ ]*[in|cm|mm|pt|pc|em|ex|px|%]]</li>
	 * </ul>
	 *
	 * @param value the dimension string to parse
	 * @return a dimension object representing the dimension string.
	 * @throws PropertyValueException if the string is not valid
	 */

	public static DimensionValue parse(String value) throws PropertyValueException {
		return DimensionValueUtil.doParse(value, false, null);
	}

	/**
	 * Parses a dimension string in locale-dependent way. The input can be in
	 * localized value. The measure part use the decimal separator from the locale.
	 * e,g. "123,456.78" for English ; "123.456,78" for German.
	 * <p>
	 * The string must match the following:
	 * <ul>
	 * <li>null</li>
	 * <li>[1-9][0-9]*[.[0-9]*[ ]*[u]], u is the one of the allowed units</li>
	 * </ul>
	 * <p>
	 *
	 * @param value  the string to parse
	 * @param locale the locale where the input string resides
	 * @return a dimension object
	 * @throws PropertyValueException if the string is not valid
	 */
	public static DimensionValue parseInput(String value, ULocale locale) throws PropertyValueException {
		return DimensionValueUtil.doParse(value, true, locale);
	}

	/**
	 * Extract file name (without path and suffix) from file name with path and
	 * suffix.
	 * <p>
	 * For example:
	 * <p>
	 * <ul>
	 * <li>"c:\home\abc.xml" => "abc"
	 * <li>"c:\home\abc" => "abc"
	 * <li>"/home/user/abc.xml" => "abc"
	 * <li>"/home/user/abc" => "abc"
	 * </ul>
	 *
	 * @param filePathName the file name with path and suffix
	 * @return the file name without path and suffix
	 */

	public static String extractFileName(String filePathName) {
		if (filePathName == null) {
			return null;
		}

		int dotPos = filePathName.lastIndexOf('.');
		int slashPos = filePathName.lastIndexOf('\\');

		int backSlashPos = filePathName.lastIndexOf('/');
		slashPos = slashPos > backSlashPos ? slashPos : backSlashPos;

		if (dotPos > slashPos) {
			return filePathName.substring(slashPos > 0 ? slashPos + 1 : 0, dotPos);
		}

		return filePathName.substring(slashPos > 0 ? slashPos + 1 : 0);
	}

	/**
	 * Extract file name (without path but with suffix) from file name with path and
	 * suffix.
	 * <p>
	 * For example:
	 * <p>
	 * <ul>
	 * <li>"c:\home\abc.xml" => "abc.xml"
	 * <li>"c:\home\abc" => "abc"
	 * <li>"/home/user/abc.xml" => "abc.xml"
	 * <li>"/home/user/abc" => "abc"
	 * </ul>
	 *
	 * @param filePathName the file name with path and suffix
	 * @return the file name without path but with suffix
	 */

	public static String extractFileNameWithSuffix(String filePathName) {
		if (filePathName == null) {
			return null;
		}

		int slashPos = filePathName.lastIndexOf('\\');
		int backSlashPos = filePathName.lastIndexOf('/');

		slashPos = slashPos > backSlashPos ? slashPos : backSlashPos;
		return filePathName.substring(slashPos > 0 ? slashPos + 1 : 0);
	}

	/**
	 * Extracts the libaray namespace from the given qualified reference value.
	 * <p>
	 * For example,
	 * <ul>
	 * <li>"LibA" is extracted from "LibA.style1"
	 * <li>null is returned from "style1"
	 * </ul>
	 *
	 * @param qualifiedName the qualified reference value
	 * @return the library namespace
	 */

	public static String extractNamespace(String qualifiedName) {
		if (qualifiedName == null) {
			return null;
		}

		int pos = qualifiedName.indexOf('.');
		if (pos == -1) {
			return null;
		}

		return StringUtil.trimString(qualifiedName.substring(0, pos));
	}

	/**
	 * Extracts the name from the given qualified reference value.
	 *
	 * <p>
	 * For example,
	 * <ul>
	 * <li>"style1" is extracted from "LibA.style1"
	 * <li>"style1" is returned from "style1"
	 * </ul>
	 *
	 * @param qualifiedName the qualified reference value
	 * @return the name
	 */
	public static String extractName(String qualifiedName) {
		if (qualifiedName == null) {
			return null;
		}

		int pos = qualifiedName.indexOf('.');
		if (pos == -1) {
			return qualifiedName;
		}

		return StringUtil.trimString(qualifiedName.substring(pos + 1));
	}

	/**
	 * Builds the qualified reference value.
	 * <p>
	 * For example,
	 * <ul>
	 * <li>("LibA", "style1") => "LibA.style1"
	 * <li>(" ", "style1) => "style1"
	 * </ul>
	 *
	 * @param namespace the library namespace to indicate which library the
	 *                  reference is using.
	 * @param value     the actual reference value
	 * @return the qualified reference value
	 */

	public static String buildQualifiedReference(String namespace, String value) {
		if (StringUtil.isBlank(namespace)) {
			return value;
		}

		return namespace + "." + value; //$NON-NLS-1$
	}

	/**
	 * Trims the quotes.
	 * <p>
	 * For example,
	 * <ul>
	 * <li>("a.b") => a.b
	 * <li>("a.b) => "a.b
	 * <li>(a.b") => a.b"
	 * </ul>
	 *
	 * @param value the string may have quotes
	 * @return the string without quotes
	 */

	public static String trimQuotes(String value) {
		if (value == null) {
			return value;
		}

		value = value.trim();
		if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) { //$NON-NLS-1$ //$NON-NLS-2$
			return value.substring(1, value.length() - 1);
		}

		return value;
	}
}
