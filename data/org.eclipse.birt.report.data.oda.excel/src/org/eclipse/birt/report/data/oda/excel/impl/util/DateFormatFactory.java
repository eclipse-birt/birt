/*
 *************************************************************************
 * Copyright (c) 2006, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.impl.util;

import java.util.HashMap;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

import com.ibm.icu.util.ULocale;

/**
 * Manages per-thread storage of locale-specific DateFormat instances. Use this
 * factory to obtain DateFormat instances to address two issues: (1) Reduce cost
 * of on-the-fly construction of DateFormat instances. The factory uses cached
 * instances whenever possible. (2) MT-safety issue related to use of shared
 * DateFormat instances across threads
 */
public class DateFormatFactory {

	private static final int NO_TIME_STYLE = -999;

	// TLS HashMap from locale/style key to DateFormat instance
	private static ThreadLocal tlsCache = new ThreadLocal() {

		protected Object initialValue() {
			return new HashMap();
		}
	};

	private static ThreadLocal patternCache = new ThreadLocal() {

		protected Object initialValue() {
			HashMap value = new HashMap();
			String[] dateFormatPattern = { "yyyy-MM-dd HH:mm:ss.S z", //$NON-NLS-1$
					"yyyy-MM-dd HH:mm:ss.Sz", //$NON-NLS-1$
					"yyyy-MM-dd HH:mm:ss.S", //$NON-NLS-1$
					"yyyy-MM-dd HH:mm:ss z", //$NON-NLS-1$
					"yyyy-MM-dd HH:mm:ssz", //$NON-NLS-1$
					"yyyy-MM-dd HH:mm:ss", //$NON-NLS-1$
					"yyyy-MM-dd HH:mm z", //$NON-NLS-1$
					"yyyy-MM-dd HH:mmz", //$NON-NLS-1$
					"yyyy-MM-dd HH:mm", //$NON-NLS-1$
					"yyyy-MM-dd", //$NON-NLS-1$
					"yyyy-MM", //$NON-NLS-1$
					"yyyy", //$NON-NLS-1$
					"HH:mm:ss.S z", //$NON-NLS-1$
					"HH:mm:ss.Sz", //$NON-NLS-1$
					"HH:mm:ss.S", //$NON-NLS-1$
					"HH:mm:ss z", //$NON-NLS-1$
					"HH:mm:ssz", //$NON-NLS-1$
					"HH:mm:ss", //$NON-NLS-1$
					"HH:mm z", //$NON-NLS-1$
					"HH:mmz", //$NON-NLS-1$
					"HH:mm" //$NON-NLS-1$
			};
			SimpleDateFormat simpleDateFormatter = null;
			PatternKey patterKey = null;

			for (int i = 0; i < dateFormatPattern.length; i++) {
				patterKey = PatternKey.getPatterKey(dateFormatPattern[i]);
				simpleDateFormatter = new SimpleDateFormat(dateFormatPattern[i]);
				simpleDateFormatter.setLenient(false);
				value.put(patterKey, simpleDateFormatter);
			}
			return value;
		}
	};

	/**
	 * Gets DateFormat instance allocated to the current thread for the given date
	 * style, timestyle and locale. Returned instance is safe to use
	 * 
	 */
	public static DateFormat getDateTimeInstance(int dateStyle, int timeStyle, ULocale locale) {
		assert locale != null;

		// Create key string for cache lookup
		String keyStr = locale.getName() + "/" + Integer.toString(dateStyle) + "/" //$NON-NLS-1$ //$NON-NLS-2$
				+ Integer.toString(timeStyle);

		HashMap tlsMap = (HashMap) tlsCache.get();
		assert tlsMap != null;

		DateFormat result = (DateFormat) tlsMap.get(keyStr);

		// Create new instance and add to cache if no instance available for
		// current thread/style/locale combination
		if (result == null) {
			if (timeStyle == NO_TIME_STYLE)
				result = DateFormat.getDateInstance(dateStyle, locale.toLocale());
			else
				result = DateFormat.getDateTimeInstance(dateStyle, timeStyle, locale.toLocale());
			result.setLenient(false);
			tlsMap.put(keyStr, result);
		}

		return result;

	}

	/**
	 * Gets DateFormat instance allocated to the current thread for the given date
	 * style, timestyle and locale. Returned instance is safe to use
	 * 
	 */
	public static DateFormat getDateInstance(int dateStyle, ULocale locale) {
		return getDateTimeInstance(dateStyle, NO_TIME_STYLE, locale);
	}

	/**
	 * Gets DateFormat instance allocated to the current thread for the given
	 * pattern. Returned instance is safe to use
	 * 
	 */
	public static SimpleDateFormat getPatternInstance(PatternKey pattern) {

		HashMap patternMap = (HashMap) patternCache.get();
		assert patternMap != null;

		return (SimpleDateFormat) patternMap.get(pattern);
	}

}

/**
 * A class used as hash key of date format pattern.
 *
 */
class PatternKey {
	private int colonNumber;
	private int blankNumber;
	private int hyphenNumber;
	private int dotNumber;
	private int timeZomeNumber;

	/**
	 * 
	 * @param source
	 * @return
	 */
	public static PatternKey getPatterKey(String source) {
		int colonNumber = 0;
		int blankNumber = 0;
		int hyphenNumber = 0;
		int dotNumber = 0;
		int timeZomeNumber = 0;
		boolean beLastBlank = false;

		for (int i = 0; i < source.length(); i++) {
			switch (source.charAt(i)) {
			case ':': {
				beLastBlank = false;
				colonNumber++;
				break;
			}

			case ' ': {
				if (!beLastBlank) {
					blankNumber++;
				}
				beLastBlank = true;
				break;
			}
			case '-': {
				beLastBlank = false;
				if (blankNumber == 0) {
					hyphenNumber++;
				} else {
					timeZomeNumber++;
				}
				break;
			}
			case '.': {
				beLastBlank = false;
				dotNumber++;
				break;
			}
			case '+':
			case 'z':
			case 'Z': {
				beLastBlank = false;
				timeZomeNumber++;
				break;
			}
			}
			if (timeZomeNumber > 0) {
				break;
			}
		}

		if (hyphenNumber == 0 && colonNumber == 0 && source.length() > 4) {
			return null;
		}

		return (new PatternKey(colonNumber, blankNumber, hyphenNumber, dotNumber, timeZomeNumber));
	}

	PatternKey(int colonNumber, int blankNumber, int hyphenNumber, int dotNumber, int timeZomeNumber) {
		this.colonNumber = colonNumber;
		this.blankNumber = blankNumber;
		this.hyphenNumber = hyphenNumber;
		this.dotNumber = dotNumber;
		this.timeZomeNumber = timeZomeNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return colonNumber * 36 + blankNumber * 12 + hyphenNumber * 4 + dotNumber * 2 + timeZomeNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object key) {
		if (key == null)
			return false;
		PatternKey patterKey = (PatternKey) key;
		return patterKey.colonNumber == this.colonNumber || patterKey.blankNumber == this.blankNumber
				|| patterKey.hyphenNumber == this.hyphenNumber || patterKey.dotNumber == this.dotNumber
				|| patterKey.timeZomeNumber == this.timeZomeNumber;
	}
}
