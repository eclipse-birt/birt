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

import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Logger;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.util.ULocale;

/**
 * 
 * 
 * Defines a string formatting class. Notice that unlike numeric or Date
 * formatting, locale is irrelevant in string formatting
 * 
 */
public class StringFormatter implements IFormatter {

	/**
	 * logger used to log syntax errors.
	 */
	static protected Logger logger = Logger.getLogger(StringFormatter.class.getName());

	// original format string
	protected String formatPattern;
	// Locale
	private ULocale locale = ULocale.getDefault();

	// uppercase or lowercase;
	private char chcase;

	// number of & in format string;
	private int nand;

	// number of @ in format string;
	private int natt;

	// from left to right.
	private boolean dir;

	// should we trim the space.
	private boolean trim;

	/**
	 * resets all the member variable to initial value;
	 */
	private void init() {
		formatPattern = ""; //$NON-NLS-1$
		chcase = ' ';
		nand = 0;
		natt = 0;
		dir = false;
		trim = true;
	}

	/**
	 * constructor with no argument
	 */
	public StringFormatter() {
		applyPattern(null);
	}

	/**
	 * constructor with no formatting string
	 */
	public StringFormatter(ULocale locale) {
		this.locale = locale;
		applyPattern(null);
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	public StringFormatter(Locale locale) {
		this(ULocale.forLocale(locale));
	}

	/**
	 * constructor with a format string argument
	 * 
	 * @param format the format string
	 */
	public StringFormatter(String format) {
		applyPattern(format);
	}

	/**
	 * Constructor with the format string and locale
	 * 
	 * @param format the format string
	 * @param locale the locale
	 */
	public StringFormatter(String format, ULocale locale) {
		this.locale = locale;
		applyPattern(format);
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	public StringFormatter(String format, Locale locale) {
		this(format, ULocale.forLocale(locale));
	}

	/**
	 * @param format the format pattern
	 */
	public void applyPattern(String format) {
		init();
		if (format == null) {
			return;
		}
		char c = ' ';
		StringBuffer scan = new StringBuffer(format);
		int len = scan.length();

		for (int i = 0; i < len; i++) {
			c = scan.charAt(i);
			switch (c) {
			case ('@'):
				natt++;
				break;

			case ('&'):
				nand++;
				break;

			case ('<'):
			case ('>'):
				chcase = c;
				break;

			case ('!'):
				dir = true;
				break;

			case ('^'):
				trim = false;
				break;
			}
		}
		if ("Zip Code + 4".equalsIgnoreCase(format)) {
			applyPattern("@@@@@-@@@@");
			return;
		}
		if ("Phone Number".equalsIgnoreCase(format)) {
			applyPattern("(@@@)@@@-@@@@");
			return;
		}
		if ("Social Security Number".equalsIgnoreCase(format)) {
			applyPattern("@@@-@@-@@@@");
			return;
		}
		formatPattern = format;
	}

	/**
	 * returns the original format string.
	 */
	public String getPattern() {
		return this.formatPattern;
	}

	/**
	 * 
	 * getLoacle() method, return the locale value.
	 * 
	 */
	public ULocale getULocale() {
		return this.locale;
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	public Locale getLocale() {
		return getULocale().toLocale();
	}

	/**
	 * 
	 * setLoacle() method, set the locale value.
	 * 
	 */
	public void setLocale(ULocale theLocale) {
		locale = theLocale;
	}

	/**
	 * @deprecated since 2.1
	 * @return
	 */
	public void setLocale(Locale theLocale) {
		setLocale(ULocale.forLocale(theLocale));
	}

	/**
	 * @param val    string to be handled
	 * @param option to upper case or to lower case
	 * @return
	 */
	private String handleCase(String val, char option) {
		if (option == '<')
			return UCharacter.toLowerCase(locale, val);
		else if (option == '>')
			return UCharacter.toUpperCase(locale, val);
		else
			return val;

	}

	/**
	 * 
	 * returns the formated string for the string parameter.
	 * <li>'@' - character or space
	 * <li>'&' - character or empty
	 * <li>'&lt;' - tolower
	 ** <li>'>' - toupper
	 * <li>'!' - left to right
	 * 
	 * @param str format string
	 * 
	 */
	public String format(String str) {
		if (trim && str != null) {
			str = str.trim();
		}

		if (formatPattern == null || formatPattern.length() == 0 || formatPattern.equals("Unformatted")) //$NON-NLS-1$
			return str;

		int len = str.length();
		int col = natt + nand;
		int ext = 0;
		StringBuffer orig = new StringBuffer(str);
		StringBuffer fstr = new StringBuffer(this.formatPattern);
		StringBuffer ret = new StringBuffer(""); //$NON-NLS-1$
		int i = 0;
		// offset of the process position.
		int pos = 0;

		// length of the format string;
		int len2 = 0;

		char fc = ' ';

		String sc = null;

		if (!dir) {
			if (len > col) {
				ret.append(handleCase(orig.substring(0, len - col), chcase));
				pos = len - col;
				len = col;

			}
			ext = col - len;
		}
		len2 = this.formatPattern.length();
		for (i = 0; i < len2; i++) {

			fc = fstr.charAt(i);
			switch (fc) {
			case ('@'):
			case ('&'):
				// character or space
				if (ext > 0 || len == 0) {
					if (fc == '@')
						ret.append(' ');
					ext--;
				} else {
					sc = orig.substring(pos, pos + 1);
					ret.append(handleCase(sc, chcase));
					pos++;
					len--;
				}
				break;

			case ('<'):
			case ('>'):
			case ('!'):
			case ('^'):
				// ignore
				break;

			default:
				ret.append(fc);
				break;
			}
		}

		while (--len >= 0) {
			sc = orig.substring(pos, pos + 1);
			ret.append(handleCase(sc, chcase));
			pos++;
		}

		return ret.toString();
	}

	/**
	 * Parses the input string into a unformatted string type.
	 * 
	 * @param str the input string to parse
	 * @return the string
	 * @throws ParseException if the specified string cannot be parsed according to
	 *                        specified pattern.
	 */
	public String parser(String str) throws ParseException {
		if (formatPattern == null || "".equals(formatPattern) //$NON-NLS-1$
				|| formatPattern.indexOf(">") > -1 || formatPattern.indexOf("<") > -1) //$NON-NLS-1$ //$NON-NLS-2$
		{
			return str;
		}
		StringBuffer orig = new StringBuffer(str);
		StringBuffer fstr = new StringBuffer(""); //$NON-NLS-1$
		StringBuffer ret = new StringBuffer(""); //$NON-NLS-1$

		for (int i = 0; i < formatPattern.length(); i++) {
			if (formatPattern.charAt(i) != '!' && formatPattern.charAt(i) != '>' && formatPattern.charAt(i) != '<'
					&& formatPattern.charAt(i) != '^') {
				fstr.append(formatPattern.charAt(i));
			}
		}
		char fc = ' ';
		int lenPattern = fstr.length();
		int lenFormatStr = orig.length();
		if (lenPattern > lenFormatStr) {
			if (dir) {

				for (int k = lenFormatStr; k < lenPattern; k++) {
					if (fstr.charAt(k) != '&') {
						throw new ParseException(CoreMessages.getFormattedString(ResourceConstants.UNPARSEABLE_STRING,
								new Object[] { orig.toString() }), k);
					}
					orig.append(" "); //$NON-NLS-1$
				}
			} else {
				for (int k = 0; k < lenPattern - lenFormatStr; k++) {
					if (fstr.charAt(lenPattern - lenFormatStr - k - 1) != '&') {
						throw new ParseException(CoreMessages.getFormattedString(ResourceConstants.UNPARSEABLE_STRING,
								new Object[] { orig.toString() }), 0);
					}
					orig.insert(0, " "); //$NON-NLS-1$
				}
			}
		} else if (lenPattern < lenFormatStr) {
			if (dir) {
				// fstr.append(orig.subSequence(lenPattern, lenFormatStr));
				for (int k = lenPattern; k < lenFormatStr; k++) {
					fstr.append('&');
				}

			} else {
				// fstr.insert(0, orig.substring(0, lenFormatStr-lenPattern));
				for (int k = lenPattern; k < lenFormatStr; k++) {
					fstr.insert(0, '&');
				}
			}
		}
		int index = 0;
		int count = lenPattern > lenFormatStr ? lenPattern : lenFormatStr;
		for (int i = 0; i < count; i++) {
			fc = fstr.charAt(i);
			switch (fc) {
			case ('@'):
				if (orig.charAt(index) != ' ') {
					ret.append(orig.charAt(index));
				}
				index++;

				break;
			case ('&'):
				ret.append(orig.charAt(index));
				index++;
				break;

			case ('<'):
			case ('>'):
				return str;

			default:
				if (orig.charAt(index) != fstr.charAt(i)) {
					throw new ParseException(CoreMessages.getFormattedString(ResourceConstants.UNPARSEABLE_STRING,
							new Object[] { orig.toString() }), index);
				}
				index++;
				break;
			}
		}
		return ret.toString();
	}

	/**
	 * @param trim the trim to set
	 */
	public void setTrim(boolean trim) {
		this.trim = trim;
	}

	public String formatValue(Object value) {
		assert value instanceof String;
		return format((String) value);
	}
}
