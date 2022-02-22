/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.css.engine.value;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.eclipse.birt.core.util.IOUtil;
import org.w3c.dom.css.CSSValue;

public class DataFormatValue extends Value implements Serializable, Cloneable {

	final static long serialVersionUID = 0x35ab0c3879c21567L;

	private static int ENDING_TAG = -1;
	private static int VERSION_0 = 0;

	private static int FORMATS = 5;
	private static int STRING_FORMAT = 0;
	private static int NUMBER_FORMAT = 1;
	private static int DATE_FORMAT = 2;
	private static int TIME_FORMAT = 3;
	private static int DATETIME_FORMAT = 4;

	protected FormatValue[] values;

	public DataFormatValue() {
		values = new FormatValue[FORMATS];
	}

	@Override
	public short getCssValueType() {
		return CSSValue.CSS_CUSTOM;
	}

	public FormatValue getStringFormat() {
		return values[STRING_FORMAT];
	}

	public void setStringFormat(String pattern, String locale) {
		values[STRING_FORMAT] = new FormatValue(pattern, locale);
	}

	public String getStringPattern() {
		if (values[STRING_FORMAT] == null) {
			return null;
		}
		return values[STRING_FORMAT].pattern;
	}

	public String getStringLocale() {
		if (values[STRING_FORMAT] == null) {
			return null;
		}
		return values[STRING_FORMAT].locale;
	}

	public FormatValue getNumberFormat() {
		return values[NUMBER_FORMAT];
	}

	public void setNumberFormat(String pattern, String locale) {
		values[NUMBER_FORMAT] = new FormatValue(pattern, locale);
	}

	public String getNumberPattern() {
		if (values[NUMBER_FORMAT] == null) {
			return null;
		}
		return values[NUMBER_FORMAT].pattern;
	}

	public String getNumberLocale() {
		if (values[NUMBER_FORMAT] == null) {
			return null;
		}
		return values[NUMBER_FORMAT].locale;
	}

	public FormatValue getDateFormat() {
		return values[DATE_FORMAT];
	}

	public void setDateFormat(String pattern, String locale) {
		values[DATE_FORMAT] = new FormatValue(pattern, locale);
	}

	public String getDatePattern() {
		if (values[DATE_FORMAT] == null) {
			return null;
		}
		return values[DATE_FORMAT].pattern;
	}

	public String getDateLocale() {
		if (values[DATE_FORMAT] == null) {
			return null;
		}
		return values[DATE_FORMAT].locale;
	}

	public FormatValue getTimeFormat() {
		return values[TIME_FORMAT];
	}

	public void setTimeFormat(String pattern, String locale) {
		values[TIME_FORMAT] = new FormatValue(pattern, locale);
	}

	public String getTimePattern() {
		if (values[TIME_FORMAT] == null) {
			return null;
		}
		return values[TIME_FORMAT].pattern;
	}

	public String getTimeLocale() {
		if (values[TIME_FORMAT] == null) {
			return null;
		}
		return values[TIME_FORMAT].locale;
	}

	public FormatValue getDateTimeFormat() {
		return values[DATETIME_FORMAT];
	}

	public void setDateTimeFormat(String pattern, String locale) {
		values[DATETIME_FORMAT] = new FormatValue(pattern, locale);
	}

	public String getDateTimePattern() {
		if (values[DATETIME_FORMAT] == null) {
			return null;
		}
		return values[DATETIME_FORMAT].pattern;
	}

	public String getDateTimeLocale() {
		if (values[DATETIME_FORMAT] == null) {
			return null;
		}
		return values[DATETIME_FORMAT].locale;
	}

	@Override
	public DataFormatValue clone() {
		try {
			return (DataFormatValue) super.clone();
		} catch (CloneNotSupportedException ex) {

		}
		return null;
	}

	// utility to serialize/deserialize a DataFormatValue
	public static void write(DataOutputStream out, DataFormatValue value) throws IOException {
		IOUtil.writeInt(out, VERSION_0);
		if (value != null) {
			for (int i = 0; i < value.values.length; i++) {
				FormatValue format = value.values[i];
				if (format != null) {
					IOUtil.writeInt(out, i);
					IOUtil.writeString(out, format.pattern);
					IOUtil.writeString(out, format.locale);
				}
			}
		}
		IOUtil.writeInt(out, ENDING_TAG);
	}

	public static DataFormatValue read(DataInputStream in) throws IOException {
		int version = IOUtil.readInt(in);
		if (version == VERSION_0) {
			DataFormatValue value = new DataFormatValue();
			int field = IOUtil.readInt(in);
			while (field != ENDING_TAG) {
				String pattern = IOUtil.readString(in);
				String locale = IOUtil.readString(in);
				value.values[field] = new FormatValue(pattern, locale);
				field = IOUtil.readInt(in);
			}
			return value;
		}
		return null;
	}

	public static DataFormatValue createDataFormatValue(DataFormatValue oldValue) {
		if (oldValue == null) {
			return new DataFormatValue();
		}
		return oldValue.clone();
	}

	// FormatValue class
	private static class FormatValue {

		String pattern;
		String locale;

		public FormatValue(String p, String l) {
			pattern = p;
			locale = l;
		}
	}
}
