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

package org.eclipse.birt.report.model.util;

import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * Utility class for DimensionValue.
 */
public class DimensionValueUtil {

	/**
	 * Validates whether the input dimension value just contains digital numbers.
	 * Exception will be thrown out when the letter occurred in the input value is
	 * not "." or ",".
	 * 
	 * @param value dimension value
	 * @throws PropertyValueException if the value input is not valid.
	 */
	public static void validateDecimalValue(String value) throws PropertyValueException {
		assert value != null;
		char separator = new DecimalFormatSymbols(ThreadResources.getLocale().toLocale()).getDecimalSeparator();

		if (separator == '.') {
			if (!DimensionValue.dotSeparatorPattern.matcher(value).matches())
				throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
						IPropertyType.DIMENSION_TYPE);
		}

		else if (separator == ',') {
			if (!DimensionValue.commaSeparatorPattern.matcher(value).matches())
				throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
						IPropertyType.DIMENSION_TYPE);
		} else
			assert false;

	}

	/**
	 * Validates the a dimension string. And return the the unit string of it.
	 * 
	 * @param value the value to be validated.
	 * @return Unit name of the dimension. <CODE>null</CODE> if no unit specified.
	 * @throws PropertyValueException if the unit is not in the list.
	 */
	public static String validateUnit(String value) throws PropertyValueException {
		assert value != null;
		int indexOfFirstLetter = DimensionValue.indexOfUnitLetter(value);
		if (indexOfFirstLetter == -1) {
			// No unit.
			return DimensionValue.DEFAULT_UNIT;
		}

		String suffix = value.substring(indexOfFirstLetter).trim();

		if (suffix.equalsIgnoreCase(DesignChoiceConstants.UNITS_IN))
			return DesignChoiceConstants.UNITS_IN;
		else if (suffix.equalsIgnoreCase(DesignChoiceConstants.UNITS_CM))
			return DesignChoiceConstants.UNITS_CM;
		else if (suffix.equalsIgnoreCase(DesignChoiceConstants.UNITS_MM))
			return DesignChoiceConstants.UNITS_MM;
		else if (suffix.equalsIgnoreCase(DesignChoiceConstants.UNITS_PT))
			return DesignChoiceConstants.UNITS_PT;
		else if (suffix.equalsIgnoreCase(DesignChoiceConstants.UNITS_PC))
			return DesignChoiceConstants.UNITS_PC;
		else if (suffix.equalsIgnoreCase(DesignChoiceConstants.UNITS_EM))
			return DesignChoiceConstants.UNITS_EM;
		else if (suffix.equalsIgnoreCase(DesignChoiceConstants.UNITS_EX))
			return DesignChoiceConstants.UNITS_EX;
		else if (suffix.equalsIgnoreCase(DesignChoiceConstants.UNITS_PX))
			return DesignChoiceConstants.UNITS_PX;
		else if (suffix.equalsIgnoreCase(DesignChoiceConstants.UNITS_PERCENTAGE))
			return DesignChoiceConstants.UNITS_PERCENTAGE;

		throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				IPropertyType.DIMENSION_TYPE);
	}

	/**
	 * Parses a dimension string.
	 * 
	 * @param value           the dimension string to parse
	 * @param localeDependent <code>true</code> means that the string needs to be
	 *                        parsed in locale-dependent way.
	 * @param locale
	 * @return a dimension object representing the dimension string.
	 * @throws PropertyValueException if the string is not valid
	 */

	public static DimensionValue doParse(String value, boolean localeDependent, ULocale locale)
			throws PropertyValueException {
		value = StringUtil.trimString(value);
		if (value == null)
			return null;

		if (locale == null)
			locale = ThreadResources.getLocale();

		String units = validateUnit(value);

		int indexOfFirstLetter = DimensionValue.indexOfUnitLetter(value);
		if (indexOfFirstLetter != -1) {
			value = StringUtil.trimString(value.substring(0, indexOfFirstLetter));
			if (value == null)
				return null;
		}

		double measure = 0;
		try {
			if (localeDependent) {
				// Parse in locale-dependent way.
				// Use the decimal separator from the locale.

				validateDecimalValue(value);

				Number number = NumberFormat.getNumberInstance(locale.toLocale()).parse(value);
				measure = number.doubleValue();

			} else {
				measure = Double.parseDouble(value);
			}
		} catch (ParseException e) {
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					IPropertyType.DIMENSION_TYPE);
		} catch (NumberFormatException e) {
			throw new PropertyValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					IPropertyType.DIMENSION_TYPE);
		}

		return new DimensionValue(measure, units);
	}

	/**
	 * Returns whether the given unit is valid.
	 * 
	 * @param unit the unit to check
	 * @return <code>true</code> if the unit is valid; return <code>false</code>
	 *         otherwise.
	 */

	public static boolean isValidUnit(String unit) {
		if (DesignChoiceConstants.UNITS_IN.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_CM.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_MM.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_PT.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_PC.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_EM.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_EX.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_PX.equalsIgnoreCase(unit)
				|| DesignChoiceConstants.UNITS_PERCENTAGE.equalsIgnoreCase(unit))
			return true;

		return false;
	}

}
