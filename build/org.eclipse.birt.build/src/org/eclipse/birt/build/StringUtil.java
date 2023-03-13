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

package org.eclipse.birt.build;

/**
 * Collection of string utilities.
 *
 */

public class StringUtil {

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
		if (value.length() == 0) {
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
		if (value == null || value.length() == 0) {
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

		if (fNumber < 0) {
			fNumber = 0;
		}

		String value = Double.toString(d);
		int ePos = value.indexOf('E');
		int dotPos = value.indexOf('.');

		// Convert the double like "1234567890000000.56789" to string, which
		// will be converted to
		// "1.23456789000E15" by Double.toString().

		if (ePos != -1) {
			// Get the exponent of this double

			String e = value.substring(ePos + 1);
			int exp = Integer.parseInt(e);

			// Move the dot position according to the exponent

			StringBuilder sb = new StringBuilder(value.substring(0, dotPos));
			if (ePos - dotPos - 1 > exp) {
				// "1.23456789000E4"

				sb.append(value.substring(dotPos + 1, dotPos + 1 + exp));
				sb.append('.');
				sb.append(value.substring(dotPos + 1 + exp, ePos));

				// "12345.6789000"
			} else {
				// "1.23400E8"

				sb.append(value.substring(dotPos + 1, ePos));
				for (int i = 0; i < exp - (ePos - dotPos - 1); i++) {
					sb.append('0');
				}

				// "123400000"
			}

			// "1234567890000000" will be the final one

			value = sb.toString();
		}

		// Limit the fractional number to maximum fractional number

		int pos = value.indexOf('.');
		if (pos != -1) {
			if (value.length() - pos - 1 > fNumber) {
				value = value.substring(0, pos + fNumber + 1);
			}

			// Remove the ending '0'.

			int i = 0;
			for (; i < fNumber; i++) {
				if (value.charAt(value.length() - i - 1) != '0') {
					break;
				}
			}
			value = value.substring(0, value.length() - i);

			// Remove the last dot

			if (value.charAt(value.length() - 1) == '.') {
				value = value.substring(0, value.length() - 1);
			}
		}

		return value;
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
		if (slashPos == -1) {
			slashPos = filePathName.lastIndexOf('/');
		}

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
		if (slashPos == -1) {
			slashPos = filePathName.lastIndexOf('/');
		}
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

}
