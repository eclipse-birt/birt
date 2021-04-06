/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.math.RoundingMode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */

public class NumberFormatValue {

	private int fractionDigits;

	private String format;
	private RoundingMode roundingMode;
	private static Pattern pattern = Pattern.compile("^(.*?)\\{RoundingMode=(.*?)\\}", Pattern.CASE_INSENSITIVE);

	private NumberFormatValue() {
	}

	public static NumberFormatValue getInstance(String numberFormat) {
		if (numberFormat != null) {
			NumberFormatValue value = new NumberFormatValue();
			Matcher matcher = pattern.matcher(numberFormat);
			if (matcher.matches()) {
				String f = matcher.group(1);
				if (f != null && f.length() > 0) {
					value.format = f;
					int index = f.lastIndexOf('.');
					if (index > 0) {
						int end = f.length();
						for (int i = index + 1; i < f.length(); i++) {
							if (f.charAt(i) != '0') {
								end = i;
								break;
							}
						}
						value.fractionDigits = end - 1 - index;
					}
					char lastChar = f.charAt(f.length() - 1);
					switch (lastChar) {
					case '%':
						value.fractionDigits += 2;
						break;
					case '‰':
						value.fractionDigits += 3;
						break;
					case '‱':
						value.fractionDigits += 4;
						break;
					}
				}
				String m = matcher.group(2);
				if (m != null) {
					value.roundingMode = RoundingMode.valueOf(m);
				}
			} else {
				value.format = numberFormat;
			}
			return value;
		}
		return null;
	}

	public int getFractionDigits() {
		return fractionDigits;
	}

	public void setFractionDigits(int fractionDigits) {
		this.fractionDigits = fractionDigits;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public RoundingMode getRoundingMode() {
		return roundingMode;
	}

	public void setRoundingMode(RoundingMode roundingMode) {
		this.roundingMode = roundingMode;
	}

	public int hashCode() {
		return (format == null ? 0 : format.hashCode()) + (roundingMode == null ? 0 : roundingMode.hashCode());
	}

	public boolean equals(Object o) {
		NumberFormatValue v = (NumberFormatValue) o;
		boolean formatEqual = true;
		boolean roundingModeEqual = true;
		if (v == null) {
			return false;
		}
		if (format != null) {
			formatEqual = format.equals(v.format);
		} else if (v.format != null) {
			return false;
		}

		if (roundingMode != null) {
			roundingModeEqual = roundingMode.equals(v.roundingMode);
		} else if (v.roundingMode != null) {
			return false;
		}
		return formatEqual && roundingModeEqual;
	}
}
