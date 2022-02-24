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

package org.eclipse.birt.report.model.api.metadata;

import java.text.NumberFormat;
import java.util.regex.Pattern;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.DimensionValueUtil;

import com.ibm.icu.util.ULocale;

/**
 * Representation of a dimension property value. A dimension has two parts: the
 * measure and the optional units. If the units are <code>DEFAULT_UNIT</code>,
 * then the units are assumed to be those set on the design as a whole.
 * <p>
 * The following units are supported:
 * <ul>
 * <li>in (inch)</li>
 * <li>cm (centimeter)</li>
 * <li>mm (millimeter)</li>
 * <li>pt (point)</li>
 * <li>pc (pica)</li>
 * <li>px (pixel)</li>
 * <li>em (the height of the element's font)</li>
 * <li>ex (x-height)</li>
 * <li>% (percentage)</li>
 * </ul>
 * 
 * 
 * @see org.eclipse.birt.report.model.api.util.DimensionUtil
 */

public class DimensionValue {

	/**
	 * The numeric measure part of the dimension.
	 */

	protected final double measure;

	/**
	 * The units part of the dimension.
	 */

	protected final String units;

	/**
	 * Default unit for the dimension.
	 */

	public static final String DEFAULT_UNIT = ""; //$NON-NLS-1$

	/**
	 * Regular expression for "000,000.000,000".
	 */
	private static final String DOT_SEPARATOR_EXPRESSION = "[\\s]*[-]?" //$NON-NLS-1$
			+ "[\\d]*([\\d]+[,]?[\\d]+)*" + //$NON-NLS-1$
			"[./]?" + //$NON-NLS-1$
			"[\\d]*([\\d]+[,]?[\\d]+)*[\\s]*";//$NON-NLS-1$

	/**
	 * Regular expression for "000.000,000.000".
	 */

	private static final String COMMA_SEPARATOR_EXPRESSION = "[\\s]*[-]?" //$NON-NLS-1$
			+ "[\\d]*([\\d]+[.]?[\\d]+)*" + //$NON-NLS-1$
			"[,]?" + //$NON-NLS-1$
			"[\\d]*([\\d]+[.]?[\\d]+)*[\\s]*";//$NON-NLS-1$

	/**
	 * The maximum number for the NumberFormat of double type. In Java, the double
	 * precision is 17 digits for decimal fraction.
	 */

	private static final int MAX_FORMAT_NUMBER = 18;

	/**
	 * For multiple thread safety, we should initialize the format with the
	 * specified pattern and locale when class is loaded.
	 */

	private static final NumberFormatter formatter;

	static {
		formatter = new NumberFormatter(ULocale.ENGLISH);
		String pattern = null;
		int fNumber = MAX_FORMAT_NUMBER;
		switch (fNumber) {
		case 0:
			pattern = "#0"; //$NON-NLS-1$
			break;
		default:
			pattern = "#0."; //$NON-NLS-1$
			StringBuffer b = new StringBuffer(pattern);
			for (int i = 0; i < fNumber; i++) {
				b.append('#');
			}
			pattern = b.toString();
			break;

		}
		formatter.applyPattern(pattern);
	}

	/**
	 * Constructs a DimensionValue given its measure and unit.
	 * 
	 * @param theMeasure numeric measure
	 * @param theUnits   units part for the dimension.
	 * @throws IllegalArgumentException if the unit is not supported.
	 */

	public DimensionValue(double theMeasure, String theUnits) {
		measure = theMeasure;

		if (StringUtil.isBlank(theUnits))
			units = DEFAULT_UNIT;
		else if (DimensionValueUtil.isValidUnit(theUnits))
			units = theUnits;
		else
			throw new IllegalArgumentException("The unit " + theUnits + " is not supported."); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Compiled pattern for CSS absolute pattern: "000,000.000,000"
	 */

	public final static Pattern dotSeparatorPattern = Pattern.compile(DOT_SEPARATOR_EXPRESSION);

	/**
	 * Compiled pattern for CSS absolute pattern: "000.000,000.000"
	 */

	public final static Pattern commaSeparatorPattern = Pattern.compile(COMMA_SEPARATOR_EXPRESSION);

	/**
	 * Returns the measure portion of the dimension.
	 * 
	 * @return the measure
	 */

	public double getMeasure() {
		return measure;
	}

	/**
	 * Returns the units portion of the dimension.
	 * 
	 * @return the units.
	 */

	public String getUnits() {
		return units;
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
	 * @deprecated replaced by {@link StringUtil#parse(String)}
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
	 * @param value the string to parse
	 * @return a dimension object
	 * @throws PropertyValueException if the string is not valid
	 * @deprecated replaced by
	 *             {@link StringUtil#parseInput(String, com.ibm.icu.util.ULocale)}
	 */
	public static DimensionValue parseInput(String value) throws PropertyValueException {
		return DimensionValueUtil.doParse(value, true, ThreadResources.getLocale());
	}

	/**
	 * Converts the dimension value to a locale-independent string. The string will
	 * be converted into a format like "#.###", there is no group separator and
	 * remains at most 3 digits after the decimal separator. e.g:
	 * "12,000,000.12345cm" will be converted into "12000000.123"
	 * 
	 * @return The string presentation of this dimension value.
	 */

	public String toString() {
		// ".0", ".00" or ".000" that tacks onto the end of integers is
		// eliminate.

		String value = formatter.format(measure);

		return value + units;
	}

	/**
	 * Returns the dimension value in localized format.
	 * <p>
	 * 
	 * @return localized format for this instance.
	 */

	public String toDisplayString() {
		NumberFormat nf = NumberFormat.getNumberInstance(ThreadResources.getLocale().toLocale());
		String value = nf.format(measure);

		return value + units;
	}

	/**
	 * Finds index of the first unit character( pt, %, pc... ) in the String.
	 * 
	 * @param value an input string
	 * @return index of the first letter. Return -1 if no letter found in the String
	 *         value.
	 */

	public static int indexOfUnitLetter(String value) {
		char[] ch = value.toCharArray();

		for (int i = 0; i < ch.length; i++) {
			if (Character.isLetter(ch[i]) || ch[i] == '%') {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Compares this dimension value to the given object. The result is <CODE>
	 * true</CODE> if and only if the argument is not null and is a dimension value
	 * object with the same measure and the same type of unit. The two dimension
	 * values with different units are not equal, although they can be converted to
	 * same measure
	 * 
	 * @param obj the object to compare this dimension value against.
	 * 
	 * @return <CODE>true</CODE> if this dimension value is equal to the given one;
	 *         <CODE>false</CODE> otherwise.
	 */

	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		DimensionValue dv = (DimensionValue) obj;

		// Double.doubleToLongBits() required to cope with Double.Nan and -0.0d
		// (see also Double.equals())

		return Double.doubleToLongBits(measure) == Double.doubleToLongBits(dv.measure)
				&& (units == dv.units || (units != null && units.equalsIgnoreCase(dv.units)));
	}

	/**
	 * Returns a hash code for this <CODE>DimensionValue</CODE> object. The result
	 * is computed with the exclusive OR of the two halves of the <CODE>
	 * long</CODE> integer bit representation of the measure, and the hash code of
	 * unit string. The measure bit representation is exactly produced by the method
	 * Double.doubleToLongBits(double), of the primitive <code>double</code> value
	 * represented by the measure of this <code>DimensionValue</code> object. That
	 * is, the hash code is the value of the expression: <blockquote>
	 * 
	 * <pre>
	 * int result = 17 + 37 * (int) (m &circ; (m &gt;&gt;&gt; 32));
	 * result = 37 * result + getUnits().toLowerCase().hashCode();
	 * </pre>
	 * 
	 * </blockquote> where <code>m</code> is defined by: <blockquote>
	 * 
	 * <pre>
	 * 
	 * long m = Double.doubleToLongBits(this.getMeasure());
	 * </pre>
	 * 
	 * </blockquote>
	 * 
	 * @return the hash code value for this object.
	 */

	public int hashCode() {
		int result = 17;

		long m = Double.doubleToLongBits(getMeasure());
		result = 37 * result + (int) (m ^ (m >>> 32));

		if (getUnits() != null) {
			int u = getUnits().toLowerCase().hashCode();
			result = 37 * result + u;
		}

		return result;
	}

}
